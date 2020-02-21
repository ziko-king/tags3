package cn.itcast.model.tools.hbase;

import cn.itcast.model.beans.BasicTagBean;
import cn.itcast.model.models.ModelConfig;
import cn.itcast.model.models.ml.commons.ClusterMapping;
import cn.itcast.model.models.ml.commons.sparkml.MLKMeans;
import cn.itcast.model.tools.spark.sql.SQLHBase;
import cn.itcast.model.utils.DateUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.storage.StorageLevel;

import java.util.*;

/**
 * DataFrame Reader
 * 测试SparkSQL的DSL
 * Created by mengyao
 * 2019年8月16日
 */
public class DFReader {

    private static String appName = DFReader.class.getSimpleName();

    public static void main(String[] args) {

        //tag9("tbl_users", "detail", "id,birthday");
        //tag21("tbl_orders", "detail", "id,finishTime");
        //tag24("tbl_orders", "detail", "memberId,paymentCode");
        //tag21("tbl_orders", "detail", "memberId,finishTime");
        tag22("tbl_orders", "detail", "memberId,finishTime,orderSn,orderAmount");
    }


    private static void tag22(String table, String family, String selectFields) {
        List<BasicTagBean> tag = new ArrayList<BasicTagBean>() {{
            add(new BasicTagBean() {{
                setId(132);
                setName("超高");
            }});
            add(new BasicTagBean() {{
                setId(133);
                setName("高");
            }});
            add(new BasicTagBean() {{
                setId(134);
                setName("中上");
            }});
            add(new BasicTagBean() {{
                setId(135);
                setName("中");
            }});
            add(new BasicTagBean() {{
                setId(136);
                setName("中下");
            }});
            add(new BasicTagBean() {{
                setId(137);
                setName("低");
            }});
            add(new BasicTagBean() {{
                setId(138);
                setName("很低");
            }});
        }};
        ModelConfig modelConfig = new ModelConfig();
        Map<String, String> options = modelConfig.getOptions(table, family, selectFields);
        String format = modelConfig.getFormat();
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        SparkSession session = SQLHBase.getSession(sparkConf);
        Dataset<Row> rowDF = session.read()
                .format(format)
                .options(options)
                .load();
        Dataset<Row> cacheDF = rowDF.persist(StorageLevel.DISK_ONLY());
        //memberId,finishTime,orderSn,orderAmount
        Column memberId = new Column("memberId");
        Column finishTime = new Column("finishTime");
        Column orderSn = new Column("orderSn");
        Column orderAmount = new Column("orderAmount");
        Column recency = functions.datediff(functions.current_date(), functions.max(functions.from_unixtime(finishTime))).as("recency");
        Column frequency = functions.count("orderSn").cast(DataTypes.DoubleType).as("frequency");
        Column monetary = functions.sum("orderAmount").as("monetary");
        Dataset<Row> rfmTagDF = cacheDF.select(memberId, finishTime, orderSn, orderAmount).groupBy(memberId).agg(recency, frequency, monetary);

        Column recencyNew = new Column("recency");
        Column frequencyNew = new Column("frequency");
        Column monetaryNew = new Column("monetary");
        // RFM评分
        Dataset<Row> rfmScoreDF = rfmTagDF
                .select(memberId,
                        functions
                                .when(recencyNew.between(1, 3), 5)
                                .when(recencyNew.between(4, 6), 4)
                                .when(recencyNew.between(7, 9), 3)
                                .when(recencyNew.between(10, 15), 2)
                                .when(recencyNew.geq(16), 1).alias("recency")
                        ,
                        functions
                                .when(frequencyNew.geq(200), 5)
                                .when(frequencyNew.between(150, 199), 4)
                                .when(frequencyNew.between(100, 149), 3)
                                .when(frequencyNew.between(50, 99), 2)
                                .when(frequencyNew.between(1, 49), 1).alias("frequency")
                        ,
                        functions
                                .when(monetaryNew.geq(200000), 5)
                                .when(monetaryNew.between(100000, 199999), 4)
                                .when(monetaryNew.between(50000, 99999), 3)
                                .when(monetaryNew.between(10000, 49999), 2)
                                .when(monetaryNew.leq(10000), 0).alias("monetary")
                ).where(recencyNew.isNotNull().and(frequencyNew.isNotNull()).and(monetaryNew.isNotNull()));

        MLKMeans mlkMeans = new MLKMeans(7, new String[]{"recency", "frequency", "monetary"}, "feature", "predic", rfmScoreDF, modelConfig.getModelBasePath() + "Tag22Model/" + DateUtil.FMT_YMD.get().format(new Date()));
        Dataset<Row> run = mlkMeans.run();
        Column feature = new Column("feature");
        Column predic = new Column("predic");
        //
        LinkedList<ClusterMapping> clusters = mlkMeans.getClusters();
        //获取簇中样本的最大和最小值
        Dataset<Row> predictGroupDF = run.groupBy("predic").agg(functions.min(recencyNew.plus(frequencyNew).plus(monetaryNew)), functions.max(recencyNew.plus(frequencyNew).plus(monetaryNew))).sort(predic.asc());
        predictGroupDF.printSchema();
        predictGroupDF.show(1000, false);
        List<Row> gbRows = predictGroupDF.collectAsList();
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
        for (int i = tag.size() - 1; i >= 0; i--) {
            long tagId = tag.get(i).getId();
            clusters.get(i).setTagId(tagId);
        }
        Dataset<Row> predictDF = run.select(memberId, recencyNew, frequencyNew, monetaryNew, recencyNew.plus(frequencyNew).plus(monetaryNew).alias("score"), feature, predic).orderBy(predic.asc());
        predictDF.show(1000, false);
        predictDF.printSchema();
        List<Row> rows = predictDF.collectAsList();
        if (rows.size() > 0) {
            Map<String, Map<String, String>> kvs = new HashMap<String, Map<String, String>>();
            rows.forEach(row -> {
                Map<String, String> kv = new HashMap<String, String>();
                String userId = row.getString(0);
                int cluster = row.getInt(6);
                clusters.forEach(b -> {
                    if (b.getCluster() == cluster) {
                        long tagId = b.getTagId();
                        kv.put("tagIds", tagId + "");
                        kv.put("userId", userId);
                    }
                });
                kvs.put("u_" + userId, kv);
            });
            kvs.forEach((k, v) -> System.out.println(k + "=" + v));
        }

    }

    /**
     * 24   支付方式
     * 143 支付宝=alipay
     * 144 微信=wxpay
     * 145 银联=chinapay
     * 146 货到付款=cod
     */
    private static void tag24(String table, String family, String selectFields) {
        ModelConfig modelConfig = new ModelConfig();
        Map<String, String> options = modelConfig.getOptions(table, family, selectFields);
        String format = modelConfig.getFormat();
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        SparkSession session = SQLHBase.getSession(sparkConf);
        Dataset<Row> rowDF = session.read()
                .format(format)
                .options(options)
                .load();
        Dataset<Row> cacheDF = rowDF.persist(StorageLevel.DISK_ONLY());
        Dataset<Row> payTagDF = cacheDF.select(new Column("memberId"), functions
                .when(new Column("paymentCode").equalTo("alipay"), 143)//如果是支付宝
                .when(new Column("paymentCode").equalTo("wxpay"), 144)//如果是微信
                .when(new Column("paymentCode").equalTo("chinapay"), 145)//如果是银联
                .when(new Column("paymentCode").equalTo("cod"), 146)//如果是货到付款
                .alias("tagId")).where(new Column("tagId").isNotNull())
                .groupBy("memberId")
                .agg(functions.first("tagId"), functions.count("tagId").as("cnt"))
                .orderBy(new Column("cnt").desc());
        payTagDF.foreachPartition(rows -> {
            rows.forEachRemaining(row -> System.out.println("==== " + row));
        });
        System.out.println("==== total: " + payTagDF.count());
    }

    /**
     * 21   消费周期
     * 127 近7天=0-7
     * 128 近14天=8-14
     * 129 近30天=15-30
     * 130 近60天=31-60
     * 131 近90天=61-90
     */
    private static void tag21(String table, String family, String selectFields) {
        ModelConfig modelConfig = new ModelConfig();
        Map<String, String> options = modelConfig.getOptions(table, family, selectFields);
        String format = modelConfig.getFormat();
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        SparkSession session = SQLHBase.getSession(sparkConf);
        Dataset<Row> rowDF = session.read()
                .format(format)
                .options(options)
                .load();
        Dataset<Row> cacheDF = rowDF.persist(StorageLevel.DISK_ONLY());
        cacheDF.show();
        cacheDF.printSchema();
        Dataset<Row> finishTimeTagDF = cacheDF.select(new Column("memberId"), new Column("finishTime").cast(DataTypes.LongType))
                .groupBy("memberId").agg(functions.max("finishTime").alias("ft"))
                .select(new Column("memberId"), functions
                        .when(functions.datediff(functions.current_date(), functions.from_unixtime(new Column("ft"))).between("0", "3"), "3")
                        .when(functions.datediff(functions.current_date(), functions.from_unixtime(new Column("ft"))).between("8", "14"), "14")
                        .when(functions.datediff(functions.current_date(), functions.from_unixtime(new Column("ft"))).between("15", "30"), "30")
                        .when(functions.datediff(functions.current_date(), functions.from_unixtime(new Column("ft"))).between("31", "60"), "60")
                        .when(functions.datediff(functions.current_date(), functions.from_unixtime(new Column("ft"))).between("61", "90"), "90").alias("tagId"));
        finishTimeTagDF.show(false);
        finishTimeTagDF.printSchema();
    }


    /**
     * birthday取值范围
     * 52=19500101-19591231
     * 53=19600101-19691231
     * 54=19700101-19791231
     * 55=19800101-19891231
     * 56=19900101-19991231
     * 57=20000101-20091231
     * 58=20100101-20191231
     * 59=20200101-20291231
     *
     * @param cacheDF
     */
    private static void tag9(String table, String family, String selectFields) {
        ModelConfig modelConfig = new ModelConfig();
        Map<String, String> options = modelConfig.getOptions(table, family, selectFields);
        String format = modelConfig.getFormat();
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster())
                .registerKryoClasses(new Class[]{BasicTagBean.class});
        SparkSession session = SQLHBase.getSession(sparkConf);
        Dataset<Row> rowDF = session.read()
                .format(format)
                .options(options)
                .load();
        Dataset<Row> cacheDF = rowDF.persist(StorageLevel.DISK_ONLY());
        //用户-关联标签,该DataFrame的Schema为userid,tagId,job
        Dataset<Row> birthdayTagDF = cacheDF.select(new Column("id"), functions
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("19500101", "19591231"), "52")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("19600101", "19691231"), "53")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("19700101", "19791231"), "54")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("19800101", "19891231"), "55")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("19900101", "19991231"), "56")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("20000101", "20091231"), "57")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("20100101", "20191231"), "58")//如果是
                .when(functions.regexp_replace(new Column("birthday"), "-", "").between("20200101", "20291231"), "59")//如果是
                .alias("tagId"), new Column("birthday"));
        birthdayTagDF.show(false);

    }

}
