package cn.itcast.model.models.ml.pricesensitivity;

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
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 促销敏感度模型（PSM模型，即Price Sensitivity Measurement）
 * Created by mengyao
 * 2019年6月2日
 */
@SuppressWarnings("all")
public class Tag233Model extends AbstractModel {

    private static String appName = Tag8Model.class.getSimpleName();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelConfig modelConfig = new ModelConfig();
    private SparkConf sparkConf;
    private SparkSession session;
    private List<BasicTagBean> tag;
    private String modelPath;

    public Tag233Model() {
        super(appName, "促销敏感度模型:PSM+KMeans");
        sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        session = SQLHBase.getSession(sparkConf);
        modelPath = modelConfig.getModelBasePath() + appName + "/" + DateUtil.FMT_YMD.get().format(new Date());
        logger.info("==== 初始化模型相关配置 ====");
    }

    public static void main(String[] args) {
        Tag233Model tagModel = new Tag233Model();
        tagModel.execute();
    }

    @Override
    public String getType() {
        return ModelType.ML.toString();
    }

    /**
     * 233 	优惠敏感度
     * 234	极度敏感
     * 235	比较敏感
     * 236	一般敏感
     * 237	 不太敏感
     * 238	极度不敏感
     */
    @Override
    @SuppressWarnings("serial")
    public List<? extends Tag> getTag() {
        if (null == tag) {
            // SQL按照level字段升序，确保第一条数据是4级标签
            Dataset<Row> rowDF = session.read().jdbc(
                    modelConfig.getMySQLUrl(),
                    "(SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE id = 233 UNION SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE pid = 233 ORDER BY `level`,`order` ASC) AS btag",
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

                        // HBase表的qualifier
                        Column memberIdCol = new Column("memberId");
                        Column orderSnCol = new Column("orderSn");
                        Column orderAmountCol = new Column("orderAmount");
                        Column couponCodeValueCol = new Column("couponCodeValue");

                        //ra 应收金额
                        //da 优惠金额
                        //aa 实收金额
                        //dotn 优惠订单总数
                        //otn 全部订单总数
                        //dat 优惠总金额
                        //rat 应收总金额
                        Column raCol = new Column("ra");
                        Column daCol = new Column("da");
                        Column aaCol = new Column("aa");
                        Column stateCol = new Column("state");
                        Column aatCol = new Column("aat");
                        Column datCol = new Column("dat");
                        Column ratCol = new Column("rat");
                        Column dotnCol = new Column("dotn");
                        Column otnCol = new Column("otn");
                        String dorStr = "dor";
                        String odrStr = "odr";
                        String daraStr = "dara";
                        String psmScoreStr = "score";
                        String featureStr = "feature";
                        String predictStr = "predic";

                        // SELECT memberId,orderSn,orderAmount,couponCodeValue, CASE WHEN couponCodeValue>0 THEN 1 ELSE 0 END AS state FROM tbl_orders
                        // 用户每个订单的应收金额ra、优惠金额da、实收金额ra
                        Dataset<Row> orderDF = cacheDF.select(orderSnCol, // 订单编号
                                memberIdCol, // 用户ID
                                orderAmountCol.alias("aa"), // 实收金额
                                couponCodeValueCol.alias("da"), //优惠金额
                                orderAmountCol.plus(couponCodeValueCol).alias("ra"),//应收金额
                                functions.when(couponCodeValueCol.gt(0), 1).when(couponCodeValueCol.equalTo(0), 0).alias("state")// 0为非优惠订单，1为优惠订单
                        );

                        // 计算PSM值，得出用户所有订单的应收金额ra、优惠金额da、实收金额ra、优惠订单数dotn、全部订单数otn
                        Dataset<Row> psmDF = orderDF.groupBy(memberIdCol).agg(
                                functions.sum(aaCol).alias("aat"),  //实收总金额
                                functions.sum(daCol).alias("dat"), //优惠总金额
                                functions.sum(raCol).alias("rat"), //应收总金额
                                functions.sum(stateCol).alias("dotn"), //优惠订单总数
                                functions.count(stateCol).alias("otn") //全部订单总数
                        )
                                // 得出优惠订单占比、平均每单优惠占比、优惠总金额于总金额的占比，此处已经是无量纲化的百分比PSM分值
                                .select(memberIdCol,
                                        dotnCol.divide(otnCol).alias(dorStr), // 优惠订单占比discountOrderRatio = 优惠订单总数dotn / 订单总数otn
                                        (datCol.divide(dotnCol)).divide(ratCol.divide(otnCol)).alias(odrStr),  // 平均每单优惠金额占比oneDiscountRatio = (优惠金额汇总dat / 优惠订单总数dotn) / (应收金额汇总rat / 订单总数otn)
                                        datCol.divide(ratCol).alias(daraStr)// 优惠总金额与总金额占比 =  dat / rat
                                ).na().fill(0);

                        // 根据优惠订单占比、平均每单优惠金额占比、惠总金额与总金额占比，这三个指标求和进行聚类，判断标准为：
                        Dataset<Row> psmScoreDF = psmDF.select(memberIdCol, new Column(dorStr).plus(new Column(odrStr)).plus(new Column(daraStr)).alias(psmScoreStr));
                        // >=1 		= 		极度敏感
                        // 0.4~1 	= 		比较敏感
                        // 0.1~0.3 	= 		一般敏感
                        // 0 		= 		不太敏感
                        // <0		= 		极度不敏感
                        MLKMeans mlkMeans = new MLKMeans(5, new String[]{psmScoreStr}, featureStr, predictStr, psmScoreDF, modelPath);
                        Dataset<Row> resultDF = mlkMeans.run();
//						// 获取聚类后经过排序的簇编号和簇质心
                        LinkedList<ClusterMapping> clusters = mlkMeans.getClusters();
                        System.out.println(clusters);
                        // 因标签是按照ASC查询出的，标签rule最小值为数组最后一个元素，逆序赋值到clusters中，即每个元素的结构为{cluster,min,max,center,tagId}
                        for (int i = subTagSize - 2; i >= 0; i--) {
                            long tagId = tag.get(i + 1).getId();
                            clusters.get(i).setTagId(tagId);
                        }
                        // 将预测结果根据cluster升序排序
                        Dataset<Row> predictDF = resultDF.select(memberIdCol, new Column(psmScoreStr), new Column(featureStr), new Column(predictStr)).orderBy(new Column(predictStr).asc());
                        predictDF.printSchema();
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
                                if (!row.isNullAt(3)) {
                                    //所属簇
                                    int cluster = row.getInt(3);
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

    @Override
    public List<?> exampleData() {
        return Arrays.asList(
                new PSMBean("1", "20190511", 199.49, 0.00, 199.49),
                new PSMBean("3", "20190501", 158.00, 4.34, 153.66),
                new PSMBean("2", "20190520", 649.00, 60.00, 589.00),
                new PSMBean("4", "20190517", 1399.00, 420.50, 978.50),
                new PSMBean("4", "20190515", 60.99, 5.00, 55.99),
                new PSMBean("1", "20190512", 499.00, 5.00, 494.00),
                new PSMBean("2", "20190522", 104.80, 23.00, 81.8),
                new PSMBean("1", "20190513", 376.00, 42.95, 333.05),
                new PSMBean("3", "20190502", 1099.00, 150.00, 949.00),
                new PSMBean("3", "20190503", 1099.00, 0.00, 1099.00)
        );
    }

    /**
     * PSM+KMeans的SparkCore实现
     *
     * @param rows
     */
    private void psm(Dataset<Row> rows) {
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(rows.sparkSession().sparkContext());
        jsc.sc().setLogLevel("ERROR");

        JavaRDD<PSMBean> inputRDD = rows.as(Encoders.bean(PSMBean.class)).toJavaRDD();

        //PSM模型
        JavaRDD<PSMBean> psmModelRDD = inputRDD
                .groupBy(b -> b.getUserId())
                .map(tup2 -> {
                    ArrayList<PSMBean> list = new ArrayList<PSMBean>();
                    tup2._2.forEach(b -> list.add(b));
                    return new PSMBean(
                            tup2._1,
                            (int) list.stream().filter(b -> b.getRa() > 0).count(), //应收订单数汇总
                            list.stream().filter(b -> b.getRa() > 0).mapToDouble(PSMBean::getRa).sum(), //应收金额汇总
                            (int) list.stream().filter(b -> b.getDa() > 0).count(), //优惠订单数汇总
                            list.stream().filter(b -> b.getDa() > 0).mapToDouble(PSMBean::getDa).sum()    //优惠金额汇总
                    )
                            // 得出优惠订单占比、平均每单优惠占比、优惠总金额于总金额的占比，此处已经是无量纲化的百分比PSM分值
                            .score();
                });

//		JavaRDD<Vector> psmModeVectorRDD = psmModelRDD
//			.map(b->Vectors.dense((b.getKmt())));
//		
//		//使用KMeans聚类算法，提供选择多个k值进行训练
//		List<Integer> ks = Arrays.asList(2,3,4,5,6,7,8,9,10);
//		//计算每一个k值聚类的WSSSE值
//		Map<Integer, Double> WSSSEs = new HashMap<Integer, Double>();
//		//聚类迭代计算次数
//		int numIterations = 20;
//		//根据每一个k值聚类训练
//	    ks.forEach(k -> {
//	    	KMeansModel clusters = KMeans.train(psmModeVectorRDD.rdd(), k, numIterations);
//	    	// 通过计算平方误差的集合和来评估聚类
//	    	double WSSSE = clusters.computeCost(psmModeVectorRDD.rdd());
//	    	WSSSEs.put(k, WSSSE);
//		});
//	    
//	    //找出最优k值
//	    Entry<Integer, Double> optimize = WSSSEs.entrySet().stream().min((i1,i2) -> i1.getValue().compareTo(i2.getValue())).get();
//		
//	    //使用最优k值聚类
//	    KMeansModel clusters = KMeans.train(psmModeVectorRDD.rdd(), optimize.getKey(), numIterations);
//	    System.out.println("类中心:");
//    	for (Vector center: clusters.clusterCenters()) {
//    		System.out.println(" " + center);
//    	}

        //clusters.predict(points);

        //保存最优k值的模型
//    	String path = "target/KMeans/KMeansModel/"+FORMATTER_YMDHMS.get().format(new Date());
//		clusters.save(jsc.sc(), path);
//    	KMeansModel sameModel = KMeansModel.load(jsc.sc(), path);

        jsc.stop();
    }

}

