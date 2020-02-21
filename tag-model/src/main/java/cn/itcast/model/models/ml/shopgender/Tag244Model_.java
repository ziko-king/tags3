package cn.itcast.model.models.ml.shopgender;

import cn.itcast.model.beans.BasicTagBean;
import cn.itcast.model.beans.MetaDataBean;
import cn.itcast.model.beans.Tag;
import cn.itcast.model.models.AbstractModel;
import cn.itcast.model.models.ModelConfig;
import cn.itcast.model.tools.hbase.HBaseTools;
import cn.itcast.model.tools.parser.MetaParser;
import cn.itcast.model.tools.spark.sql.SQLHBase;
import cn.itcast.model.utils.DateUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.*;

import static org.apache.spark.sql.functions.*;

@SuppressWarnings("all")
public class Tag244Model_ extends AbstractModel {

    private static Logger logger = LoggerFactory.getLogger(Tag244Model_.class.getSimpleName());

    private static String appName = Tag244Model_.class.getSimpleName();
    private ModelConfig modelConfig = new ModelConfig();
    private SparkConf sparkConf;
    private SparkSession session;
    private List<BasicTagBean> tag;
    private String modelPath;
    private String ruleSeparator = "#";


    public Tag244Model_() {
        super(appName, "购物性别模型:DecisionTree");
        sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        session = SQLHBase.getSession(sparkConf);
        modelPath = modelConfig.getModelBasePath() + appName + "/" + DateUtil.FMT_YMD.get().format(new Date());
        logger.info("==== 初始化模型相关配置 ====");
    }

    /**
     * 计算AUC
     *
     * @param inputDF
     * @param labelCol
     * @param predictLabelCol
     * @return
     */
    static double evaluateAUC(Dataset<Row> inputDF, Column labelCol, Column predictLabelCol) {
        Dataset<Row> PositiveAndNegativeRatioDF1 = inputDF.select(labelCol, predictLabelCol);
        RDD<Tuple2<Object, Object>> rdd = PositiveAndNegativeRatioDF1.javaRDD().map(row -> new Tuple2<Object, Object>(row.getDouble(0), row.getDouble(1))).rdd();
        BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(rdd);
        double AUC = metrics.areaUnderROC();
        return AUC;
    }

    public static void main(String[] args) {
        Tag244Model_ model = new Tag244Model_();
        model.execute();
    }

    @Override
    public String getType() {
        return ModelType.ML.toString();
    }

    @Override
    public List<? extends Tag> getTag() {
        if (null == tag) {
            // SQL按照level字段升序，确保第一条数据是4级标签
            Dataset<Row> rowDF = session.read().jdbc(
                    modelConfig.getMySQLUrl(),
                    "(SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE id = 244 UNION SELECT `id`,`name`,`industry`,`rule`,`business`,`level`,`pid`,`ctime`,`utime`,`state`,`remark`,`order` FROM `tags`.`tbl_basic_tag` WHERE pid = 244 ORDER BY `level`,`order` ASC) AS btag",
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
                        Map<String, String> options1 = null;
                        Map<String, String> options2 = null;
                        // 获取数据源表配置
                        String tables = meta.getHbaseTable();
                        Preconditions.checkNotNull(tables, "数据源表名必须不为空！");
                        // tbl_order表名
                        String table1 = null;
                        // tbl_goods表名
                        String table2 = null;
                        // 如果是多张表
                        if (tables.contains(ruleSeparator)) {
                            String[] tableArray = tables.split(ruleSeparator);
                            table1 = tableArray[0];
                            table2 = tableArray[1];
                        }
                        String familys = meta.getFamily();
                        Preconditions.checkNotNull(familys, "数据源表的列簇必须不为空");
                        // tbl_order表family
                        String family1 = null;
                        // tbl_goods表family
                        String family2 = null;
                        if (familys.contains(ruleSeparator)) {
                            String[] familyArray = familys.split(ruleSeparator);
                            family1 = familyArray[0];
                            family2 = familyArray[1];
                        }
                        String selectFields = meta.getSelectFieldNames();
                        Preconditions.checkNotNull(selectFields, "数据源表的查询展示字段必须不为空！");
                        // tbl_order表family
                        String selectField1 = null;
                        // tbl_goods表family
                        String selectField2 = null;
                        if (selectFields.contains(ruleSeparator)) {
                            String[] selectFieldArray = selectFields.split(ruleSeparator);
                            selectField1 = selectFieldArray[0];
                            selectField2 = selectFieldArray[1];
                        }
                        String whereFields = meta.getWhereFieldNames();
                        // 验证数据源的查询条件
                        if (StringUtils.isEmpty(whereFields)) {
                            //如果没有有查询条件
                            options1 = modelConfig.getOptions(table1, family1, selectField1);
                            logger.info("==== 数据源table={},family={},selectFileds={} ====", table1, family1, selectField1);
                            options2 = modelConfig.getOptions(table2, family2, selectField2);
                            logger.info("==== 数据源table={},family={},selectFileds={} ====", table2, family2, selectField2);
                        } else {
                            //如果有查询条件
                            String whereField1 = null;
                            String whereField2 = null;
                            if (whereFields.contains(ruleSeparator)) {
                                String[] whereFieldArray = whereFields.split(ruleSeparator);
                                whereField1 = whereFieldArray[0];
                                whereField2 = whereFieldArray[1];
                            }
                            options1 = modelConfig.getOptions(table1, family1, selectField1, whereField1);
                            logger.info("==== 数据源table={},family={},selectFileds={},whereFields={} ====", table1, family1, selectField1, whereField1);
                            options2 = modelConfig.getOptions(table2, family2, selectField2, whereField2);
                            logger.info("==== 数据源table={},family={},selectFileds={},whereFields={} ====", table2, family2, selectField2, whereField2);
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
                        // 获取订单表数据
                        Dataset<Row> ordersDF = session.read()
                                .format(format)
                                .options(options2)
                                .load();
                        Dataset<Row> ordersCacheDF = ordersDF.persist(StorageLevel.DISK_ONLY());
                        Column orderSnCol = col("orderSn");
                        Column memberIdCol = col("memberId");

                        // 获取订单商品表数据
                        Dataset<Row> orderGoodsDF = session.read()
                                .format(format)
                                .options(options1)
                                .load();
                        Dataset<Row> ordersGoodsCacheDF = orderGoodsDF.persist(StorageLevel.DISK_ONLY());
                        ordersGoodsCacheDF.printSchema();
                        // 使用决策树对商品进行性别分类
                        Column cOrderSnCol = col("cOrderSn");
                        Column ogColorCol = col("ogColor");
                        Column productTypeCol = col("productType");
                        String label = "label";
                        Column labelCol = col("label");
                        Dataset<Row> inputDF = ordersGoodsCacheDF.select(cOrderSnCol,
                                // ogColor字段预处理
                                when(ogColorCol.equalTo("银色"), 1).
                                        when(ogColorCol.equalTo("香槟金色"), 2).
                                        when(ogColorCol.equalTo("黑色"), 3).
                                        when(ogColorCol.equalTo("白色"), 4).
                                        when(ogColorCol.equalTo("梦境极光【卡其金】"), 5).
                                        when(ogColorCol.equalTo("梦境极光【布朗灰】"), 6).
                                        when(ogColorCol.equalTo("粉色"), 7).
                                        when(ogColorCol.equalTo("金属灰"), 8).
                                        when(ogColorCol.equalTo("金色"), 9).
                                        when(ogColorCol.equalTo("乐享金"), 10).
                                        when(ogColorCol.equalTo("布鲁钢"), 11).
                                        when(ogColorCol.equalTo("月光银"), 12).
                                        when(ogColorCol.equalTo("时尚光谱【浅金棕】"), 13).
                                        when(ogColorCol.equalTo("香槟色"), 14).
                                        when(ogColorCol.equalTo("香槟金"), 15).
                                        when(ogColorCol.equalTo("灰色"), 16).
                                        when(ogColorCol.equalTo("樱花粉"), 17).
                                        when(ogColorCol.equalTo("蓝色"), 18).
                                        when(ogColorCol.equalTo("金属银"), 19).
                                        when(ogColorCol.equalTo("玫瑰金"), 20).otherwise(0).alias("ogColor"),
                                // productType字段预处理
                                when(productTypeCol.equalTo("4K电视"), 9).
                                        when(productTypeCol.equalTo("Haier/海尔冰箱"), 10).
                                        when(productTypeCol.equalTo("Haier/海尔冰箱"), 11).
                                        when(productTypeCol.equalTo("LED电视"), 12).
                                        when(productTypeCol.equalTo("Leader/统帅冰箱"), 13).
                                        when(productTypeCol.equalTo("冰吧"), 14).
                                        when(productTypeCol.equalTo("冷柜"), 15).
                                        when(productTypeCol.equalTo("净水机"), 16).
                                        when(productTypeCol.equalTo("前置过滤器"), 17).
                                        when(productTypeCol.equalTo("取暖电器"), 18).
                                        when(productTypeCol.equalTo("吸尘器/除螨仪"), 19).
                                        when(productTypeCol.equalTo("嵌入式厨电"), 20).
                                        when(productTypeCol.equalTo("微波炉"), 21).
                                        when(productTypeCol.equalTo("挂烫机"), 22).
                                        when(productTypeCol.equalTo("料理机"), 23).
                                        when(productTypeCol.equalTo("智能电视"), 24).
                                        when(productTypeCol.equalTo("波轮洗衣机"), 25).
                                        when(productTypeCol.equalTo("滤芯"), 26).
                                        when(productTypeCol.equalTo("烟灶套系"), 27).
                                        when(productTypeCol.equalTo("烤箱"), 28).
                                        when(productTypeCol.equalTo("燃气灶"), 29).
                                        when(productTypeCol.equalTo("燃气热水器"), 30).
                                        when(productTypeCol.equalTo("电水壶/热水瓶"), 31).
                                        when(productTypeCol.equalTo("电热水器"), 32).
                                        when(productTypeCol.equalTo("电磁炉"), 33).
                                        when(productTypeCol.equalTo("电风扇"), 34).
                                        when(productTypeCol.equalTo("电饭煲"), 35).
                                        when(productTypeCol.equalTo("破壁机"), 36).
                                        when(productTypeCol.equalTo("空气净化器"), 37).otherwise(0).alias("productType"),
                                // 生成决策树预测label
                                when(
                                        ogColorCol.equalTo("樱花粉").
                                                or(ogColorCol.equalTo("白色")).
                                                or(ogColorCol.equalTo("香槟色")).
                                                or(ogColorCol.equalTo("香槟金")).
                                                or(productTypeCol.equalTo("料理机")).
                                                or(productTypeCol.equalTo("挂烫机")).
                                                or(productTypeCol.equalTo("吸尘器/除螨仪")), 1)
                                        .otherwise(0).alias(label)
                                // 异常值剔除
                        ).where(ogColorCol.notEqual(0).and(productTypeCol.notEqual(0)))
                                // 商品关联订单
                                .join(ordersCacheDF, orderSnCol.equalTo(cOrderSnCol), "leftouter")
                                .where(memberIdCol.isNotNull())
                                // 要构建决策树的输入数据源
                                .select(memberIdCol, orderSnCol, labelCol, ogColorCol.cast(DataTypes.DoubleType), productTypeCol.cast(DataTypes.DoubleType));

                        // 将多个列合并为向量列的特征变换器
                        VectorAssembler assembler = new VectorAssembler()
                                .setInputCols(new String[]{"ogColor", "productType"})
                                .setOutputCol("features");
                        Dataset<Row> assemblerDF = assembler.transform(inputDF);
                        assemblerDF.show(10, false);
                        // 索引标签，将元数据添加到标签列中
                        StringIndexerModel labelIndexer = new StringIndexer()
                                .setInputCol(label)
                                .setOutputCol("indexedLabel")
                                .fit(assemblerDF);
                        // 自动识别分类的特征并进行索引,大于5个不同的值的特征被视为连续特征
                        VectorIndexerModel featureIndexer = new VectorIndexer()
                                .setInputCol("features")
                                .setOutputCol("indexedFeatures")
                                .setMaxCategories(3)
                                .fit(assemblerDF);
                        // 将数据分为训练和测试集（30%进行测试）
                        Dataset<Row>[] splits = assemblerDF.randomSplit(new double[]{0.7, 0.3});
                        Dataset<Row> trainingData = splits[0];
                        Dataset<Row> testData = splits[1];
                        // 训练决策树模型
                        DecisionTreeClassifier dt = new DecisionTreeClassifier()
                                .setLabelCol("indexedLabel")
                                .setFeaturesCol("indexedFeatures")
                                .setImpurity("gini")    // Gini不纯度，entropy熵
                                .setMaxBins(5)            // 离散化连续特征的最大划分数
                                .setMaxDepth(5)        // 树的最大深度
                                ;
                        IndexToString labelConverter = new IndexToString()
                                .setInputCol("prediction")
                                .setOutputCol("predictedLabel")
                                .setLabels(labelIndexer.labels());
                        Pipeline pipeline = new Pipeline()
                                .setStages(new PipelineStage[]{labelIndexer, featureIndexer, dt, labelConverter});
                        // 使用训练出最优的决策树参数
                        Map<String, Object> dtParam = new HashMap<String, Object>() {{
                            put("maxDepth", 7);
                            put("impurity", "gini");
                            put("maxBins", 24);
                        }};
                        if (null != dtParam && dtParam.size() == 3) {
                            dt.setImpurity(dtParam.get("impurity").toString());
                            dt.setMaxBins(Integer.parseInt(dtParam.get("maxBins").toString()));
                            dt.setMaxDepth(Integer.parseInt(dtParam.get("maxDepth").toString()));
                        }
                        // 训练模型
                        PipelineModel model = pipeline.fit(trainingData);
                        // 预测训练数据
                        Dataset<Row> trainDF = model.transform(trainingData);
                        // 预测测试数据
                        Dataset<Row> testDF = model.transform(testData);
                        // 训练集的AUC
                        double trainAUC = evaluateAUC(trainDF, col("indexedLabel"), col("predictedLabel").cast(DataTypes.DoubleType));
                        // 测试集的AUC
                        double testAUC = evaluateAUC(testDF, col("indexedLabel"), col("predictedLabel").cast(DataTypes.DoubleType));
                        // 使用MulticlassClassificationEvaluator计算训练集和测试集的错误率
                        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                                .setLabelCol("indexedLabel")
                                .setPredictionCol("prediction")
                                .setMetricName("accuracy");
                        //训练集的ACC
                        double trainACC = evaluator.evaluate(trainDF);
                        //训练集的错误率
                        double trainErr = 1.0 - trainACC;
                        //测试集的ACC
                        double testACC = evaluator.evaluate(testDF);
                        //测试集的错误率
                        double testErr = 1.0 - testACC;
                        logger.info("==== 购物性别模型所使用DecisionTree模型验证：【训练集的ACC={}, AUC={}, ERR={}】；【测试集的ACC={}, AUC={}, ERR={}】 ====", trainACC, trainAUC, trainErr, testACC, testAUC, testErr);
                        // 查看决策树
                        DecisionTreeClassificationModel treeModel =
                                (DecisionTreeClassificationModel) (model.stages()[2]);
                        logger.debug("==== 购物性别模型所使用DecisionTree模型的树的结构:{} ====", treeModel.toDebugString());
                        // 购物性别所用的决策树必须达到80%才可以使用
                        if (trainACC > 0.8 && testACC > 0.8) {
                            // treeModel.save(modelPath);
                            logger.info("==== 购物性别模型所用的决策树模型的训练集ACC测试集和ACC大于80% ====");
                            // 合并模型输出的训练集和测试集，计算用户的购物性别分类
                            Dataset<Row> outputDF = trainDF.union(testDF);
                            // 五级 购物性别男标签
                            BasicTagBean shopGenderMaleTag = tag.get(1);
                            long shopGenderMaleId = shopGenderMaleTag.getId();
                            Integer shopGenderMaleRule = Integer.parseInt(shopGenderMaleTag.getRule());
                            // 五级 购物性别女标签
                            BasicTagBean shopGenderFemaleTag = tag.get(2);
                            long shopGenderFemaleId = shopGenderFemaleTag.getId();
                            Integer shopGenderFemaleRule = Integer.parseInt(shopGenderFemaleTag.getRule());
                            // ======= 由于是家电产品，一个订单中通常只有一个商品。调整A规则为B规则：
                            // ===A===     原来按照每个订单的男性商品>=80%则认定为该订单的用户为男，或女商品比例达到80%则认定为该订单的用户为女；
                            // ======= 最后统计算近半年内所有订单的男性商品占比超过60%则认定该用户为男，或近半年内所有订单的女性商品占比超过60%则认定该用户为女 ====
                            // ===B===     计算每个用户近半年内所有订单中的男性商品超过60%则认定该用户为男，或近半年内所有订单中的女性品超过60%则认定该用户为女

                            // 得到每个用户memberId近半年内的所有男性商品数maleCnt、女性商品数femaleCnt、总商品数totalCnt
                            Dataset<Row> userTotalOrderGoodsGenderRatioDF = outputDF.select(memberIdCol, col("predictedLabel").cast(DataTypes.IntegerType).alias("predicGender"))
                                    .select(memberIdCol,
                                            when(col("predicGender").plus(1).equalTo(shopGenderMaleRule), 1).otherwise(0).alias("male"),        //计算每个用户所有订单中的男性商品的订单数
                                            when(col("predicGender").plus(1).equalTo(shopGenderFemaleRule), 1).otherwise(0).alias("female"),    //计算每个用户所有订单中的女性商品的订单数
                                            col("predicGender"))
                                    .groupBy(memberIdCol)
                                    .agg(
                                            sum("male").cast(DataTypes.DoubleType).alias("maleCnt"),
                                            sum("female").cast(DataTypes.DoubleType).alias("femaleCnt"),
                                            count("predicGender").cast(DataTypes.DoubleType).alias("totalCnt")
                                    ).cache();
                            userTotalOrderGoodsGenderRatioDF.printSchema();
                            // 注册shopGender函数的UDF计算每个用户的购物性别，近半年男性商品数>=60%属于男性，近半年女性商品数>=60%属于女性
                            session.udf().register("shopGender", (UDF2<Double, Double, Long>) (maleRatio, femaleRatio) -> {
                                if (maleRatio >= 0.6D) {
                                    return shopGenderMaleId;
                                }
                                if (femaleRatio >= 0.6D) {
                                    return shopGenderFemaleId;
                                }
                                return 0L;
                            }, DataTypes.LongType);
                            // 使用shopGender函数获取每个用户的购物性别(userId,tagId)
                            Dataset<Row> userTagDF = userTotalOrderGoodsGenderRatioDF
                                    .select(memberIdCol, callUDF("shopGender", col("maleCnt").divide(col("totalCnt")).alias("maleRatio"), col("femaleCnt").divide(col("totalCnt")).alias("femaleRatio")).alias("tagId"))
                                    .where(col("tagId").gt(0));
                            // 创建HBase连接
                            HBaseTools build = HBaseTools.build();
                            // 获取已存在的画像数据
                            Map<String, Map<String, String>> existProfileData = build.scan(profileTable, userFamily);
                            // 封装画像数据
                            List<Row> rows = userTagDF.select(memberIdCol, col("tagId").cast(DataTypes.StringType)).collectAsList();
                            if (rows.size() > 0) {
                                Map<String, Map<String, String>> kvs = new HashMap<String, Map<String, String>>();
                                rows.forEach(row -> {
                                    Map<String, String> kv = new HashMap<String, String>();
                                    String rowKey = null;
                                    if (!row.isNullAt(0)) {
                                        rowKey = row.getString(0);
                                        kv.put(userIdColumn, rowKey);
                                    }
                                    if (!row.isNullAt(1)) {
                                        kv.put(tagIdsColumn, row.getString(1));
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
                        } else {
                            logger.info("==== TestErr高于20%，请重新训练决策树 ====");
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

    public void loggerSetting() {
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
        org.apache.log4j.Logger.getLogger("org").setLevel(org.apache.log4j.Level.INFO);
        org.apache.log4j.Logger.getLogger("com").setLevel(org.apache.log4j.Level.INFO);
    }

}
