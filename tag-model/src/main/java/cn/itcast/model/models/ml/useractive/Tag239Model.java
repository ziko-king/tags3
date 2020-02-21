package cn.itcast.model.models.ml.useractive;

import cn.itcast.model.beans.BasicTagBean;
import cn.itcast.model.beans.MetaDataBean;
import cn.itcast.model.beans.Tag;
import cn.itcast.model.models.AbstractModel;
import cn.itcast.model.models.ModelConfig;
import cn.itcast.model.models.ml.commons.ClusterMapping;
import cn.itcast.model.tools.hbase.HBaseTools;
import cn.itcast.model.tools.hdfs.HdfsTools;
import cn.itcast.model.tools.parser.MetaParser;
import cn.itcast.model.tools.spark.sql.SQLHBase;
import cn.itcast.model.utils.DateUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.MinMaxScaler;
import org.apache.spark.ml.feature.MinMaxScalerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.IOException;
import java.util.*;

/**
 * 用户活跃度模型（RFE模型，即Recency Frequency Engagements）
 * Created by mengyao
 * 2019年6月2日
 */
public class Tag239Model extends AbstractModel {

    private static String appName = Tag239Model.class.getSimpleName();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelConfig modelConfig = new ModelConfig();
    private SparkConf sparkConf;
    private SparkSession session;
    private List<BasicTagBean> tag;
    private String modelPath;


    public Tag239Model() {
        super(appName, "用户价值模型:RFM+KMeans");
        sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        session = SQLHBase.getSession(sparkConf);
        modelPath = modelConfig.getModelBasePath() + appName + "/" + DateUtil.FMT_YMD.get().format(new Date());
        logger.info("==== 初始化模型相关配置 ====");
    }

    public static void main(String[] args) {
        Tag239Model tagModel = new Tag239Model();
        tagModel.execute();
    }

    @Override
    public String getType() {
        return ModelType.ML.toString();
    }

    /**
     * 239 	用户活跃度
     * 240	活跃
     * 241	衰退
     * 242	沉睡
     * 243	 流失
     */
    @Override
    public List<? extends Tag> getTag() {
        if (null == tag) {
            // SQL按照level字段升序，确保第一条数据是4级标签
            Dataset<Row> rowDF = session.read().jdbc(
                    modelConfig.getMySQLUrl(),
                    "(SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE id = 239 UNION SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE pid = 239 ORDER BY `level`,`order` ASC) AS btag",
                    new Properties() {{
                        setProperty("driver", modelConfig.getMySQLDriver());
                    }}
            );
            List<Row> rows = rowDF.collectAsList();
            if (rows.size() > 0) {
                tag = new LinkedList<BasicTagBean>();
                rows.forEach(row -> {
                    BasicTagBean bean = new BasicTagBean();
                    if (!row.isNullAt(0)) {
                        bean.setId(row.getLong(0));
                    }
                    if (!row.isNullAt(1)) {
                        bean.setName(row.getString(1));
                    }
                    if (!row.isNullAt(2)) {
                        bean.setIndustry(row.getString(2));
                    }
                    if (!row.isNullAt(3)) {
                        bean.setRule(row.getString(3));
                    }
                    if (!row.isNullAt(4)) {
                        bean.setBusiness(row.getString(4));
                    }
                    if (!row.isNullAt(5)) {
                        bean.setLevel(row.getInt(5));
                    }
                    if (!row.isNullAt(6)) {
                        bean.setPid(row.getLong(6));
                    }
                    if (!row.isNullAt(7)) {
                        bean.setCtime(row.getTimestamp(7).toString());
                    }
                    if (!row.isNullAt(8)) {
                        bean.setUtime(row.getTimestamp(8).toString());
                    }
                    if (!row.isNullAt(9)) {
                        bean.setState(row.getInt(9));
                    }
                    if (!row.isNullAt(10)) {
                        bean.setRemark(row.getString(10));
                    }
                    tag.add(bean);
                });
                rowDF.unpersist();
            }
        }
        logger.info("==== 模型所需的标签数据为: {} ====", tag);
        return tag;
    }

    @Override
    public void compute() {
        int subTagSize = tag.size();
        if (null == tag || subTagSize == 0) {
            logger.error("==== 没有该模型的标签数据！  ====");
            throw new RuntimeException();
        } else {
            // 四级标签规则必须存在
            String rule = tag.get(0).getRule();
            if (StringUtils.isEmpty(rule)) {
                logger.error("==== 四级标签规则不存在！ ====");
                throw new RuntimeException();
            } else {
                MetaDataBean meta = MetaParser.getParser(rule).getMeta();
                if (null == meta || StringUtils.isEmpty(meta.getInType())) {
                    logger.error("==== 标签规则解析失败！ ====");
                    throw new RuntimeException();
                } else {
                    if (meta.getInType().toLowerCase().equals("hbase")) {
                        Map<String, String> options = null;
                        // 获取数据源表配置
                        String table = meta.getHbaseTable();
                        Preconditions.checkNotNull(table, "数据源表名必须不为空！");
                        String family = meta.getFamily();
                        Preconditions.checkNotNull(family, "数据源表的列簇必须不为空");
                        String selectFields = meta.getSelectFieldNames();
                        Preconditions.checkNotNull(selectFields, "数据源表的查询展示字段必须不为空！");
                        String whereFields = meta.getWhereFieldNames();
                        // 验证数据源的查询条件
                        if (StringUtils.isEmpty(whereFields)) {
                            //如果没有有查询条件
                            options = modelConfig.getOptions(table, family, selectFields);
                            logger.info("==== 数据源table={},family={},selectFileds={} ====", table, family, selectFields);
                        } else {
                            //如果有查询条件
                            options = modelConfig.getOptions(table, family, selectFields, whereFields);
                            logger.info("==== 数据源table={},family={},selectFileds={},whereFields={} ====", table, family, selectFields, whereFields);
                        }
                        // 获取画像表配置
                        String profileTable = modelConfig.getProfileTableName();
                        Preconditions.checkNotNull(profileTable, "画像表的表名必须不为空！");
                        String userFamily = modelConfig.getProfileTableFamilyUser();
                        Preconditions.checkNotNull(userFamily, "画像表的列簇必须不为空！");
                        String userFamilyAlias = modelConfig.getProfileTableFamilyUserAlias();
                        Preconditions.checkNotNull(userFamilyAlias, "画像表的RowKey前缀必须不为空！");
                        // qualifier=userId
                        String userIdColumn = modelConfig.getProfileFamilyUserCol();
                        Preconditions.checkNotNull(userIdColumn, "画像表的userId列必须不为空！");
                        // qualifier=tagIds
                        String tagIdsColumn = modelConfig.getProfileCommonCol();
                        Preconditions.checkNotNull(tagIdsColumn, "画像表的tagIds列必须不为空！");
                        // 获取HBase数据源插件实现
                        String format = modelConfig.getFormat();
                        Dataset<Row> rowDF = session.read()
                                .format(format)
                                .options(options)
                                .load();
                        Dataset<Row> cacheDF = rowDF.persist(StorageLevel.DISK_ONLY());

                        String userStr = "global_user_id";
                        String urlStr = "loc_url";
                        String timeStr = "log_time";
                        Column userCol = new Column(userStr);
                        Column urlCol = new Column(urlStr);
                        Column timeCol = new Column(timeStr);

                        String recencyStr = "recency";
                        String frequencyStr = "frequency";
                        String engagementsStr = "engagements";
                        Column recencyCol = functions.datediff(functions.current_date(), functions.max(timeCol)).as(recencyStr);
                        Column frequencyCol = functions.count(urlCol).cast(DataTypes.DoubleType).as(frequencyStr);
                        Column engagementsCol = functions.countDistinct(urlCol).as(engagementsStr);
                        Column recencyColNew = new Column(recencyStr);
                        Column frequencyColNew = new Column(frequencyStr);
                        Column engagementsColNew = new Column(engagementsStr);
                        String featureStr = "feature";
                        String scaleFeature = "scaleFeature";
                        String predictStr = "predic";
                        Column predictCol = new Column(predictStr);
                        // 计算RFE值
                        // R:距离当前时间的天数差
                        // F:访问的总页面数
                        // E:访问的独立页面数，一个页面重复浏览4次及以上，页面越多则分值越高
                        //global_user_id|recency|frequency|engagements
                        Dataset<Row> rfeTagDF = cacheDF.select(userCol, timeCol, urlCol).groupBy(userCol).agg(recencyCol, frequencyCol, engagementsCol);
                        //计算RFE评分(put 'tbl_logs','1','detail:log_time','2019-09-09 13:09:21')
                        //R:0-3天=5分，4-6天=4分，7-9天=3分，10-15天=2分，大于16天=1分
                        //F:≥400=5分，300-399=4分，200-299=3分，100-199=2分，≤0=99分
                        //E:≥5=5分，4=4分，3=3分，2=2分，1=1分
                        Dataset<Row> rfeScoreDF = rfeTagDF
                                .select(userCol,
                                        functions
                                                .when(recencyColNew.between(0, 3), 5)
                                                .when(recencyColNew.between(4, 6), 4)
                                                .when(recencyColNew.between(7, 9), 3)
                                                .when(recencyColNew.between(10, 15), 2)
                                                .when(recencyColNew.geq(16), 1).alias(recencyStr)
                                        ,
                                        functions
                                                .when(frequencyColNew.geq(400), 5)
                                                .when(frequencyColNew.between(300, 399), 4)
                                                .when(frequencyColNew.between(200, 299), 3)
                                                .when(frequencyColNew.between(100, 199), 2)
                                                .when(frequencyColNew.between(0, 99), 1).alias(frequencyStr)
                                        ,
                                        functions
                                                .when(engagementsColNew.geq(250), 5)
                                                .when(engagementsColNew.between(230, 249), 4)
                                                .when(engagementsColNew.between(210, 229), 3)
                                                .when(engagementsColNew.between(200, 209), 2)
                                                .when(engagementsColNew.leq(100), 1).alias(engagementsStr)
                                ).where(recencyColNew.isNotNull().and(frequencyColNew.isNotNull()).and(engagementsColNew.isNotNull()));

                        rfeTagDF.printSchema();
                        rfeScoreDF.printSchema();
                        rfeTagDF.show(1000, false);
                        System.out.println("=====######");
                        rfeScoreDF.show(1000);
                        // 将多个列合并为向量列的特征变换器
                        VectorAssembler vectorAssembler = new VectorAssembler()
                                // 将DataFrame中的recencyStr, frequencyStr, engagementsStr列作为特征向量列feature
                                .setInputCols(new String[]{recencyStr, frequencyStr, engagementsStr})
                                // 输出特征向量列feature
                                .setOutputCol(featureStr);
                        Dataset<Row> vector = vectorAssembler.transform(rfeScoreDF);
                        // 使用区间缩放将特征数据单位做无量纲化处理
                        MinMaxScaler scale = new MinMaxScaler()
                                .setInputCol(featureStr)
                                .setOutputCol(scaleFeature);
                        MinMaxScalerModel scaleModel = scale.fit(vector);
                        Dataset<Row> scaleDF = scaleModel.transform(vector);
                        scaleDF.show(1000, false);

                        KMeansModel model = null;
                        //使用KMeans聚类，指定K为4(活跃、衰退、沉睡、流失)
                        if (HdfsTools.build().exist(modelPath)) {
                            model = KMeansModel.load(modelPath);
                        } else {
                            KMeans kmeans = new KMeans()
                                    .setK(4)
                                    .setSeed(20)
                                    .setFeaturesCol(scaleFeature)
                                    .setPredictionCol(predictStr);
                            model = kmeans.fit(scaleDF);
                        }
                        try {
                            model.save(modelPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 预测
                        Dataset<Row> resultDF = model.transform(scaleDF);
                        // 存放分类对应的标签ID
                        LinkedList<ClusterMapping> clusters = new LinkedList<ClusterMapping>();
                        int i = 0;
                        for (Vector center : model.clusterCenters()) {
                            clusters.add(new ClusterMapping(i, Arrays.stream(center.toArray()).sum()));
                            i++;
                        }
                        // 将子标签的ID赋值到分类中
                        for (int j = subTagSize - 2; j >= 0; j--) {
                            long tagId = tag.get(j + 1).getId();
                            clusters.get(j).setTagId(tagId);
                        }
                        // 按分类的类中心排序
                        if (null != clusters) {
                            clusters.sort((i1, i2) -> i1.getCenter().compareTo(i2.getCenter()));
                        }
                        // 取出分类结果
                        List<Row> rows = resultDF.select(userCol, predictCol).collectAsList();
                        // 获取HBase客户端
                        HBaseTools build = HBaseTools.build();
                        // 获取已存在的画像数据
                        Map<String, Map<String, String>> existProfileData = build.scan(profileTable, userFamily);
                        // 封装画像数据
                        if (rows.size() > 0) {
                            Map<String, Map<String, String>> kvs = new HashMap<String, Map<String, String>>();
                            rows.forEach(row -> {
                                Map<String, String> kv = new HashMap<String, String>();
                                String rowKey = null;
                                if (!row.isNullAt(0)) {
                                    //userId
                                    rowKey = row.getString(0);
                                    kv.put(userIdColumn, rowKey);
                                }
                                if (!row.isNullAt(1)) {
                                    //所属簇
                                    int cluster = row.getInt(1);
                                    clusters.forEach(b -> {
                                        if (b.getCluster() == cluster) {
                                            long tagId = b.getTagId();
                                            kv.put(tagIdsColumn, tagId + "");
                                        }
                                    });
                                }
                                if (!StringUtils.isEmpty(rowKey)) {
                                    rowKey = userFamilyAlias + rowKey;
                                }
                                // 取出本次计算完成用户ID和五级标签
                                String newUserId = kv.get(userIdColumn);
                                String newTagId = kv.get(tagIdsColumn);
                                if (StringUtils.isEmpty(newUserId) || StringUtils.isEmpty(newTagId)) {
                                    logger.info("==== 本次计算完成，但是没有结果！ ====");//本次计算完成，但是没有结果
                                } else {
                                    // 说明画像表是空表，直接新增
                                    if (existProfileData.size() == 0) {
                                        kvs.put(rowKey, kv);
                                    } else {// 如果不是空表，则验证
                                        Map<String, String> map = existProfileData.get(rowKey);
                                        // 没有该用户的画像数据，直接新增
                                        if (null == map) {
                                            kvs.put(rowKey, kv);
                                        } else {// 有该用户的画像数据，取出
                                            String existTagIds = map.get(tagIdsColumn);
                                            if (!StringUtils.isEmpty(existTagIds)) {
                                                if (!existTagIds.contains(newTagId)) {// 用户存在，但本次计算的标签不存在
                                                    kv.put(tagIdsColumn, existTagIds + "," + newTagId);//在已有的标签后面追加本次计算的标签
                                                    kvs.put(rowKey, kv);
                                                } else {
                                                    logger.info("==== 该{}用户已存在ID={}的标签！ ====", rowKey, newTagId);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            //写入画像表
                            build.addRows(profileTable, userFamily, kvs);
                            build.close();
                        } else {
                            logger.info("==== 作业计算完成，但计算结果为空！ ====");
                        }

                    }
                }
            }
        }
    }

    @Override
    public void clear() {
        if (null != session) {
            session.close();
            logger.info("==== 释放SparkSQL相关资源 ====");
        }
    }

    /**
     * 计算RFE
     *
     * @param rows
     * @return
     */
    @SuppressWarnings("unused")
    private void rfe(Dataset<Row> rows) {
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(rows.sparkSession().sparkContext());
        jsc.sc().setLogLevel("ERROR");

        JavaRDD<RFEBean> inputRDD = rows.as(Encoders.bean(RFEBean.class)).toJavaRDD();
        //获取每个userId的最近一次浏览时间
        JavaRDD<Tuple2<String, String>> recencyRDD = inputRDD
                .groupBy(bean -> bean.getUserId())
                .map(tp2 -> {
                    ArrayList<RFEBean> list = new ArrayList<RFEBean>();
                    tp2._2.iterator().forEachRemaining(bean -> list.add(bean));
                    RFEBean bean = list.stream()
                            .sorted((o1, o2) -> o1.getRecency().compareTo(o2.getRecency()))
                            .findFirst()
                            .get();
                    return new Tuple2<String, String>(bean.getUserId(), "recency_" + bean.getRecency());
                });

        //获取每个userId的浏览商品次数
        JavaRDD<Tuple2<String, String>> frequencyRDD = inputRDD
                .groupBy(rfe -> rfe.getUserId())
                .map(tup2 -> {
                    ArrayList<RFEBean> list = new ArrayList<RFEBean>();
                    tup2._2.iterator().forEachRemaining(rfe -> list.add(rfe));
                    int count = list.stream()
                            .map(rfe -> rfe.getFrequency())
                            .reduce((o1, o2) -> o1 + o2)
                            .get();
                    RFEBean rfe = list.stream().findFirst().get();
                    return new Tuple2<String, String>(rfe.getUserId(), "frequency_" + count);
                });

        //获取每个userId的参与度
        JavaRDD<Tuple2<String, String>> engagementsRDD = inputRDD
                .groupBy(rfe -> rfe.getUserId())
                .map(tup2 -> {
                    ArrayList<RFEBean> list = new ArrayList<RFEBean>();
                    tup2._2.iterator().forEachRemaining(rfe -> list.add(rfe));
                    int total = list.stream()
                            .map(rfe -> rfe.getEngagements())
                            .reduce((o1, o2) -> o1 + o2)
                            .get();
                    RFEBean rfe = list.stream().findFirst().get();
                    return new Tuple2<String, String>(rfe.getUserId(), "engagements_" + total);
                });

        //计算rfe评分
        JavaRDD<RFEBean> rfeModelRDD = recencyRDD.union(frequencyRDD).union(engagementsRDD)
                .groupBy(tup2 -> tup2._1).map(tup2 -> {
                    RFEBean rfe = new RFEBean();
                    tup2._2.forEach(field -> {
                        String userId = field._1;
                        rfe.setUserId(userId);
                        String uncertainField = field._2;
                        if (uncertainField.contains("recency_")) {
                            rfe.setRecency(Integer.parseInt(uncertainField.split("_")[1]));
                        }
                        if (uncertainField.contains("frequency_")) {
                            rfe.setFrequency(Integer.parseInt(uncertainField.split("_")[1]));
                        }
                        if (uncertainField.contains("engagements_")) {
                            rfe.setEngagements(Integer.parseInt(uncertainField.split("_")[1]));
                        }
                    });
                    return rfe.computeScore();
                });
    }

    @Override
    public List<?> exampleData() {
        return Arrays.asList(
                new RFEBean("1", "20190719", 1, 1),
                new RFEBean("1", "20190809", 1, 2),
                new RFEBean("1", "20190721", 1, 2),
                new RFEBean("1", "20190721", 1, 4),

                new RFEBean("2", "20190723", 1, 3),
                new RFEBean("2", "20190720", 1, 1),
                new RFEBean("2", "20190719", 1, 2),

                new RFEBean("3", "20190719", 1, 5),
                new RFEBean("3", "20190719", 1, 2),
                new RFEBean("3", "20190719", 1, 1),

                new RFEBean("4", "20190719", 1, 4),
                new RFEBean("4", "20190719", 1, 1)
        );
    }

}