package cn.itcast.model.models.ml.customervalue;

import cn.itcast.model.beans.BasicTagBean;
import cn.itcast.model.beans.MetaDataBean;
import cn.itcast.model.beans.Tag;
import cn.itcast.model.models.AbstractModel;
import cn.itcast.model.models.ModelConfig;
import cn.itcast.model.models.match.Tag8Model;
import cn.itcast.model.models.ml.commons.ClusterMapping;
import cn.itcast.model.models.ml.commons.sparkml.MLKMeans;
import cn.itcast.model.tools.hbase.HBaseTools;
import cn.itcast.model.tools.parser.MetaParser;
import cn.itcast.model.tools.spark.sql.SQLHBase;
import cn.itcast.model.utils.DateUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 用户价值模型（RFM模型，即Recency Frequency Monetary）
 * Created by mengyao
 * 2019年6月2日
 */
public class Tag22Model extends AbstractModel {

    private static String appName = Tag8Model.class.getSimpleName();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelConfig modelConfig = new ModelConfig();
    private SparkConf sparkConf;
    private SparkSession session;
    private List<BasicTagBean> tag;
    private String modelPath;


    public Tag22Model() {
        super(appName, "用户价值模型:RFM+KMeans");
        sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        session = SQLHBase.getSession(sparkConf);
        modelPath = modelConfig.getModelBasePath() + appName + "/" + DateUtil.FMT_YMD.get().format(new Date());
        logger.info("==== 初始化模型相关配置 ====");
    }

    public static void main(String[] args) throws Exception {
        Tag22Model tagModel = new Tag22Model();
        tagModel.execute();
    }

    @Override
    public String getType() {
        return ModelType.ML.toString();
    }

    @SuppressWarnings("serial")
    @Override
    public List<? extends Tag> getTag() {
        if (null == tag) {
            // SQL按照level字段升序，确保第一条数据是4级标签
            Dataset<Row> rowDF = session.read().jdbc(
                    modelConfig.getMySQLUrl(),
                    "(SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE id = 22 UNION SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE pid = 22 ORDER BY `level`,`order` ASCSELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE id = 22 UNION SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE pid = 22 ORDER BY `level`,`order` ASC) AS btag",
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
        if (null == tag || tag.size() == 0) {
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

                        String recencyStr = "recency";
                        String frequencyStr = "frequency";
                        String monetaryStr = "monetary";
                        String featureStr = "feature";
                        String predictStr = "predic";
                        //memberId,finishTime,orderSn,orderAmount
                        Column memberId = new Column("memberId");
                        Column finishTime = new Column("finishTime");
                        Column orderSn = new Column("orderSn");
                        Column orderAmount = new Column("orderAmount");
                        Column recency = functions.datediff(functions.current_date(), functions.max(functions.from_unixtime(finishTime))).as(recencyStr);
                        Column frequency = functions.count("orderSn").cast(DataTypes.DoubleType).as(frequencyStr);
                        Column monetary = functions.sum("orderAmount").as(monetaryStr);
                        Column recencyNew = new Column(recencyStr);
                        Column frequencyNew = new Column(frequencyStr);
                        Column monetaryNew = new Column(monetaryStr);
                        Column feature = new Column(featureStr);
                        Column predic = new Column(predictStr);
                        //计算RFM值
                        Dataset<Row> rfmTagDF = cacheDF.select(memberId, finishTime, orderSn, orderAmount).groupBy(memberId).agg(recency, frequency, monetary);

                        //计算RFM评分
                        //R:1-3天=5分，4-6天=4分，7-9天=3分，10-15天=2分，大于16天=1分
                        //F:≥200=5分，150-199=4分，100-149=3分，50-99=2分，1-49=1分
                        //M:≥20=5分，10-19w=4分，5-9w=3分，1-4w=2分，≤1w=1分
                        Dataset<Row> rfmScoreDF = rfmTagDF
                                .select(memberId,
                                        functions
                                                .when(recencyNew.between(1, 3), 5)
                                                .when(recencyNew.between(4, 6), 4)
                                                .when(recencyNew.between(7, 9), 3)
                                                .when(recencyNew.between(10, 15), 2)
                                                .when(recencyNew.geq(16), 1).alias(recencyStr)
                                        ,
                                        functions
                                                .when(frequencyNew.geq(200), 5)
                                                .when(frequencyNew.between(150, 199), 4)
                                                .when(frequencyNew.between(100, 149), 3)
                                                .when(frequencyNew.between(50, 99), 2)
                                                .when(frequencyNew.between(1, 49), 1).alias(frequencyStr)
                                        ,
                                        functions
                                                .when(monetaryNew.geq(200000), 5)
                                                .when(monetaryNew.between(100000, 199999), 4)
                                                .when(monetaryNew.between(50000, 99999), 3)
                                                .when(monetaryNew.between(10000, 49999), 2)
                                                .when(monetaryNew.leq(10000), 0).alias(monetaryStr)
                                ).where(recencyNew.isNotNull().and(frequencyNew.isNotNull()).and(monetaryNew.isNotNull()));
                        // 聚类训练和预测
                        MLKMeans mlkMeans = new MLKMeans(7, new String[]{recencyStr, frequencyStr, monetaryStr}, featureStr, predictStr, rfmScoreDF, modelPath);
                        Dataset<Row> resultDF = mlkMeans.run();
                        // 获取聚类后经过排序的簇编号和簇质心
                        LinkedList<ClusterMapping> clusters = mlkMeans.getClusters();
                        // 获取簇中样本的最大和最小值
                        Dataset<Row> predictGroupDF = resultDF.groupBy(predic).agg(functions.min(recencyNew.plus(frequencyNew).plus(monetaryNew)), functions.max(recencyNew.plus(frequencyNew).plus(monetaryNew))).sort(predic.asc());
                        // 最终预测出簇的最大和最小值
                        List<Row> gbRows = predictGroupDF.collectAsList();
                        // 包装每个簇的最大最小值
                        if (gbRows.size() > 0) {
                            gbRows.forEach(row -> {
                                int cluster = row.getInt(0);
                                int min = row.getInt(1);
                                int max = row.getInt(2);
                                clusters.forEach(b -> {
                                    if (b.getCluster() == cluster) {
                                        b.set(min, max);
                                    }
                                });
                            });
                        }
                        // 因标签是按照ASC查询出的，标签rule最小值为数组最后一个元素，逆序赋值到clusters中，即每个元素的结构为{cluster,min,max,center,tagId}
                        for (int i = tag.size() - 2; i >= 0; i--) {
                            long tagId = tag.get(i).getId();
                            clusters.get(i).setTagId(tagId);
                        }
                        // 将预测结果根据cluster升序排序
                        Dataset<Row> predictDF = resultDF.select(memberId, recencyNew, frequencyNew, monetaryNew, recencyNew.plus(frequencyNew).plus(monetaryNew).alias("score"), feature, predic).orderBy(predic.asc());
                        List<Row> rows = predictDF.collectAsList();
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
                                if (!row.isNullAt(6)) {
                                    //所属簇
                                    int cluster = row.getInt(6);
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

}
