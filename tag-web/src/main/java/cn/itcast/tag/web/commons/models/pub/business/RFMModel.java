package cn.itcast.tag.web.commons.models.pub.business;

import cn.itcast.tag.web.commons.models.pub.mllib.DataProcessUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import scala.Tuple2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 用户价值模型（RFM模型）
 *
 * @author mengyao
 */
public class RFMModel {

    private static final ThreadLocal<SimpleDateFormat> FORMATTER_YMDH = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    private static List<RFMBean> exampleData = Arrays.asList(
            new RFMBean("1", "20190519", 1, 1998),
            new RFMBean("1", "20190522", 1, 2998),
            new RFMBean("1", "20190521", 1, 1998),
            new RFMBean("2", "20190523", 1, 1099),
            new RFMBean("2", "20190520", 1, 909),

            new RFMBean("3", "20190519", 1, 1999),
            new RFMBean("1", "20190519", 1, 2499),
            new RFMBean("3", "20190519", 1, 1499),
            new RFMBean("4", "20190519", 1, 1998),
            new RFMBean("1", "20190519", 1, 2099),

            new RFMBean("1", "20190519", 1, 1899),
            new RFMBean("2", "20190519", 1, 499),
            new RFMBean("1", "20190519", 1, 2199),
            new RFMBean("3", "20190519", 1, 1799),
            new RFMBean("1", "20190519", 1, 2249)
    );

    public static void main(String[] args) throws Exception {
        rfm(null);
    }

    public static void rfm(String dataDir) {
        //
        System.setProperty("hadoop.home.dir", "D:\\softs\\developer\\apache\\hadoop-2.6.5");

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("RFM")
                .set("spark.kryoserializer.buffer", "64k")
                .set("spark.kryoserializer.buffer.max", "64m")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .registerKryoClasses(new Class[]{RFMBean.class});
        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.sc().setLogLevel("OFF");

        JavaRDD<RFMBean> inputRDD = null;

        if (StringUtils.isEmpty(dataDir)) {
            inputRDD = jsc.parallelize(exampleData);
        } else {
            inputRDD = jsc.textFile(dataDir)
                    .map(line -> {
                        String[] fields = line.split("\t", 4);
                        return new RFMBean(fields[0], fields[1], Integer.parseInt(fields[2]), Long.parseLong(fields[3]));
                    });
        }

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
                                    state = FORMATTER_YMDH.get().parse(o2.getRecency()).compareTo(FORMATTER_YMDH.get().parse(o1.getRecency()));
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
                    Long count = list.stream()
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
                            rfm.setMonetary(Long.parseLong(uncertainField.split("_")[1]));
                        }
                    });
                    return rfm;
                });

        //
        rfmModelRDD
                .foreachPartition(iter -> {
                    iter.forEachRemaining(System.out::println);
                });

        JavaRDD<Vector> rfmModeVectorRDD = rfmModelRDD
                .map(b -> Vectors.dense(b.getRecencyTime(), b.getFrequency(), b.getMonetary()));

        JavaRDD<Vector> normalizerRDD = DataProcessUtil.normalizer(rfmModeVectorRDD);

        //使用KMeans聚类算法，提供选择多个k值进行训练
        List<Integer> ks = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
        //计算每一个k值聚类的WSSSE值
        Map<Integer, Double> WSSSEs = new HashMap<Integer, Double>();
        //聚类迭代计算次数
        int numIterations = 20;
        //根据每一个k值聚类训练
        ks.forEach(k -> {
            KMeansModel clusters = KMeans.train(normalizerRDD.rdd(), k, numIterations);
            // 通过计算平方误差的集合和来评估聚类
            double WSSSE = clusters.computeCost(normalizerRDD.rdd());
            WSSSEs.put(k, WSSSE);
        });

        //找出最优k值参数
        Entry<Integer, Double> optimize = WSSSEs.entrySet().stream().min((i1, i2) -> i1.getValue().compareTo(i2.getValue())).get();

        //使用最优k值聚类
        KMeansModel clusters = KMeans.train(normalizerRDD.rdd(), optimize.getKey(), numIterations);
        System.out.println("类中心:");
        for (Vector center : clusters.clusterCenters()) {
            System.out.println(" " + center);
        }
        //保存最优k值的模型
        try {
            String path = "target/KMeans/KMeansModel/" + FORMATTER_YMDH.get().format(new Date());
            clusters.save(jsc.sc(), path);
            KMeansModel sameModel = KMeansModel.load(jsc.sc(), path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsc.stop();

    }

    static class RFMBean {
        private String userId;//用户ID
        private String recency;//最近一次购买时间
        private int frequency;//购买次数
        private long monetary;//总金额

        public RFMBean() {
            super();
        }

        public RFMBean(String userId, String recency, int frequency, long monetary) {
            super();
            this.userId = userId;
            this.recency = recency;
            this.frequency = frequency;
            this.monetary = monetary;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRecency() {
            return recency;
        }

        public void setRecency(String recency) {
            this.recency = recency;
        }

        public long getRecencyTime() {
            try {
                Date parse = FORMATTER_YMDH.get().parse(recency);
                return parse.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0l;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public long getMonetary() {
            return monetary;
        }

        public void setMonetary(long monetary) {
            this.monetary = monetary;
        }

        @Override
        public String toString() {
            return userId + "\t" + recency + "\t" + frequency + "\t" + monetary;
        }
    }

}
