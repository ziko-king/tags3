package cn.itcast.tag.web.commons.models.tag;

import breeze.optimize.linear.LinearProgram.Result;
import cn.itcast.tag.web.commons.models.pub.UserProfileBean;
import cn.itcast.tag.web.commons.models.pub.business.RFMBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


/**
 * 消费能力标签模型1554(1555高、1556中、1557低)
 *
 * @author mengyao
 */
public class Tag1554Model implements Serializable {

    private static final long serialVersionUID = -5133384811615373290L;

    private static final ThreadLocal<SimpleDateFormat> FORMATTER_YYYYMMMDDHHMMSS = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        SparkSession session = SparkSession.builder()
                .config(conf)
                .appName("SparkOnHBase")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = session.read()
                .format("com.mengyao.tag.utils.external.hbase.HBaseSource")
                .option("zkHosts", "192.168.10.20")
                .option("zkPort", "2181")
                .option("hbaseTable", "tbl_tag_order")
                .option("family", "haier")
                .option("selectFields", "memberId,payTime,siteId,productAmount")
                .load();

        Dataset<Row> rfmDF = df.select(new Column("memberId").alias("userId"), new Column("payTime").alias("recency"),
                new Column("siteId").cast(DataTypes.IntegerType).alias("frequency"),
                new Column("productAmount").cast(DataTypes.DoubleType).alias("monetary"));
        //rfmDF.printSchema();

        JavaRDD<RFMBean> inputRDD = rfmDF.as(Encoders.bean(RFMBean.class)).toJavaRDD();

        rfm(session, inputRDD);
    }

    private static void rfm(SparkSession session, JavaRDD<RFMBean> inputRDD) {
        //获取每个userId的最近一次购买时间
        JavaRDD<Tuple2<String, String>> recencyRDD = inputRDD
                .groupBy(rfm -> rfm.getUserId())
                .map(tup2 -> {
                    ArrayList<RFMBean> list = new ArrayList<RFMBean>();
                    tup2._2.iterator().forEachRemaining(rfm -> list.add(rfm));
                    RFMBean rfm = list.stream()
                            .sorted((o1, o2) -> {
                                int state = 0;
                                try {
                                    state = FORMATTER_YYYYMMMDDHHMMSS.get().parse(o2.getRecency()).compareTo(FORMATTER_YYYYMMMDDHHMMSS.get().parse(o1.getRecency()));
                                } catch (Exception e) {
                                }
                                return state;
                            })
                            .findFirst()
                            .get();
                    return new Tuple2<String, String>(rfm.getUserId(), "recency_" + rfm.getRecency());
                });

        //获取每个userId的购买次数
        JavaRDD<Tuple2<String, String>> frequencyRDD = inputRDD
                .groupBy(rfm -> rfm.getUserId())
                .map(tup2 -> {
                    ArrayList<RFMBean> list = new ArrayList<RFMBean>();
                    tup2._2.iterator().forEachRemaining(rfm -> list.add(rfm));
                    int count = list.stream()
                            .map(rfm -> rfm.getFrequency())
                            .reduce((o1, o2) -> o1 + o2)
                            .get();
                    RFMBean rfm = list.stream().findFirst().get();
                    return new Tuple2<String, String>(rfm.getUserId(), "frequency_" + count);
                });

        //获取每个userId的购买总金额
        JavaRDD<Tuple2<String, String>> monetaryRDD = inputRDD
                .groupBy(rfm -> rfm.getUserId())
                .map(tup2 -> {
                    ArrayList<RFMBean> list = new ArrayList<RFMBean>();
                    tup2._2.iterator().forEachRemaining(rfm -> list.add(rfm));
                    Double count = list.stream()
                            .map(rfm -> rfm.getMonetary())
                            .reduce((o1, o2) -> o1 + o2)
                            .get();
                    RFMBean rfm = list.stream().findFirst().get();
                    return new Tuple2<String, String>(rfm.getUserId(), "monetary_" + count);
                });

        //计算RFM
        JavaRDD<RFMBean> rfmModelRDD = recencyRDD.union(frequencyRDD).union(monetaryRDD)
                .groupBy(tup2 -> tup2._1).map(tup2 -> {
                    RFMBean rfm = new RFMBean();
                    tup2._2.forEach(field -> {
                        String userId = field._1;
                        rfm.setUserId(userId);
                        String uncertainField = field._2;
                        if (uncertainField.contains("recency_")) {
                            rfm.setRecency(uncertainField.split("_")[1]);
                        }
                        if (uncertainField.contains("frequency_")) {
                            rfm.setFrequency(Integer.parseInt(uncertainField.split("_")[1]));
                        }
                        if (uncertainField.contains("monetary_")) {
                            rfm.setMonetary(Double.parseDouble(uncertainField.split("_")[1]));
                        }
                    });
                    return rfm;
                });

        JavaPairRDD<String, Vector> rfmModePredictVectorRDD = rfmModelRDD
                .mapToPair(b -> new Tuple2<String, Vector>(b.getUserId(), Vectors.dense(b.getRecencyTime(), b.getFrequency(), b.getMonetary())));

        //JavaRDD<Vector> normalizerRDD = DataProcessUtil.normalizer(rfmModePredictVectorRDD.values());

        //使用KMeans聚类算法，提供选择多个k值进行训练
        List<Integer> ks = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
        //计算每一个k值聚类的WSSSE值
        Map<Integer, Double> WSSSEs = new HashMap<Integer, Double>();
        //聚类迭代计算次数
        int numIterations = 20;
        //根据每一个k值聚类训练
        ks.forEach(k -> {
            KMeansModel clusters = KMeans.train(rfmModePredictVectorRDD.values().rdd(), k, numIterations);
            // 通过计算平方误差的集合和来评估聚类
            double WSSSE = clusters.computeCost(rfmModePredictVectorRDD.values().rdd());
            WSSSEs.put(k, WSSSE);
        });

        //找出最优k值参数
        Entry<Integer, Double> optimize = WSSSEs.entrySet().stream().min((i1, i2) -> i1.getValue().compareTo(i2.getValue())).get();

        //使用最优k值聚类
        int k = optimize.getKey();
        k = 3;

        KMeansModel clusters = KMeans.train(rfmModePredictVectorRDD.values().rdd(), 3, numIterations);
        System.out.println("类中心:");

        for (Vector center : clusters.clusterCenters()) {
            System.out.println(" " + center);
        }
        //保存最优k值的模型
        try {
            //String path = "target/KMeans/KMeansModel/"+FORMATTER_YYYYMMMDDHHMMSS.get().format(new Date());
            //clusters.save(session.sparkContext(), path);
            //KMeansModel sameModel = KMeansModel.load(session.sparkContext(), path);
            JavaPairRDD<Integer, RFMBean> userProfileRDD = rfmModePredictVectorRDD.mapToPair(v -> {
                String userId = v._1;
                Vector vector = v._2;
                int cluster = clusters.predict(vector);
                return new Tuple2<Integer, RFMBean>(cluster, new RFMBean(userId, vector.apply(0) + "", (int) vector.apply(1), vector.apply(2)));
            })
                    .sortByKey(false);
            //新增用户标签
            saveTagProfile(session, userProfileRDD, "tbl_tag_user_profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void saveTagProfile(SparkSession session, JavaPairRDD<Integer, RFMBean> userProfileRDD, String tableName) {
        Dataset<Row> upDF = session.read()
                .format("com.mengyao.tag.utils.external.hbase.HBaseSource")
                .option("zkHosts", "192.168.10.20")
                .option("zkPort", "2181")
                .option("hbaseTable", "tbl_tag_user_profile")
                .option("family", "haier")
                .option("selectFields", "tbids")
                .load();

        JavaRDD<UserProfileBean> upRDD = upDF.select(new Column("row").alias("userId"), new Column("tbids"))
                .as(Encoders.bean(UserProfileBean.class))
                .javaRDD();

        //已存在的用户标签
        Map<String, String> eup = new HashMap<>();
        upRDD.collect().forEach(up -> eup.put(up.getUserId(), up.getTbids()));

        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(session.sparkContext());
        try {
            Configuration conf = HBaseConfiguration.create(jsc.hadoopConfiguration());
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            conf.set("hbase.zookeeper.quorum", "192.168.10.20");
            conf.set("zookeeper.znode.parent", "/hbase-unsecure");
            conf.set(TableOutputFormat.OUTPUT_TABLE, tableName);
            Job job = Job.getInstance(conf);
            job.setOutputKeyClass(ImmutableBytesWritable.class);
            job.setOutputValueClass(Result.class);
            job.setOutputFormatClass(TableOutputFormat.class);
            //(1555高、1556中、1557低)
            userProfileRDD
                    .filter(tup2 -> {
                        int cluster = tup2._1;
                        String tagId = null;
                        if (cluster == 0) {
                            tagId = "1555";
                        }
                        if (cluster == 1) {
                            tagId = "1556";
                        }
                        if (cluster == 2) {
                            tagId = "1557";
                        }
                        if (eup.size() == 0) {
                            return true;
                        }
                        String userId = tup2._2.getUserId();
                        if (eup.containsKey(userId)) {
                            String tagIds = eup.get(userId);
                            if (tagIds.contains(tagId)) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    })
                    .mapToPair(tup2 -> {
                        int cluster = tup2._1;
                        String userIdStr = tup2._2.getUserId();
                        String tagId = null;
                        if (cluster == 0) {
                            tagId = "1555";
                        }
                        if (cluster == 1) {
                            tagId = "1556";
                        }
                        if (cluster == 2) {
                            tagId = "1557";
                        }
                        byte[] userId = Bytes.toBytes(userIdStr);
                        String existTagIds = eup.get(userIdStr);
                        if (!StringUtils.isEmpty(existTagIds)) {
                            tagId = existTagIds + "," + tagId;
                        }
                        ImmutableBytesWritable ibw = new ImmutableBytesWritable(userId);
                        Put put = new Put(userId).addColumn(Bytes.toBytes("haier"), Bytes.toBytes("tbids"), Bytes.toBytes(tagId));
                        Tuple2<ImmutableBytesWritable, Put> tuple2 = new Tuple2<ImmutableBytesWritable, Put>(ibw, put);
                        return tuple2;
                    })
                    .saveAsNewAPIHadoopDataset(job.getConfiguration());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsc.close();
        session.close();
    }

}
