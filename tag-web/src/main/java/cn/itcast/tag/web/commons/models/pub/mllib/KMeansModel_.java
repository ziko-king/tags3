package cn.itcast.tag.web.commons.models.pub.mllib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class KMeansModel_ {

    private static final ThreadLocal<SimpleDateFormat> FORMATTER_YMDHMS = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

    private static List<Vector> exampleData = Arrays.asList(
            Vectors.dense(0.1, 0.1),
            Vectors.dense(0.3, 0.3),
            Vectors.dense(10.1, 10.1),
            Vectors.dense(10.3, 10.3),
            Vectors.dense(20.1, 20.1),
            Vectors.dense(20.3, 20.3),
            Vectors.dense(30.1, 30.1),
            Vectors.dense(30.3, 30.3));

    public static void main(String[] args) {
        //
        System.setProperty("hadoop.home.dir", "D:\\softs\\developer\\apache\\hadoop-2.6.5");

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("KMeansModel_")
                .set("spark.kryoserializer.buffer", "64k")
                .set("spark.kryoserializer.buffer.max", "64m")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .registerKryoClasses(new Class[]{});
        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.sc().setLogLevel("ERROR");

        JavaRDD<Vector> inputRDD = jsc.parallelize(exampleData);

        //使用KMeans聚类算法，提供选择多个k值进行训练
        List<Integer> ks = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
        //计算每一个k值聚类的WSSSE值
        Map<Integer, Double> WSSSEs = new HashMap<Integer, Double>();
        //聚类迭代计算次数
        int numIterations = 20;
        //根据每一个k值聚类训练
        ks.forEach(k -> {
            KMeansModel clusters = KMeans.train(inputRDD.rdd(), k, numIterations);
            // 通过计算平方误差的集合和来评估聚类
            double WSSSE = clusters.computeCost(inputRDD.rdd());
            WSSSEs.put(k, WSSSE);
        });

        //找出最优k值参数
        Entry<Integer, Double> optimize = WSSSEs.entrySet().stream().min((i1, i2) -> i1.getValue().compareTo(i2.getValue())).get();
        System.out.println("ALL: ==== " + WSSSEs);
        System.out.println("ONE: ==== " + optimize);

        //使用最优k值聚类
        KMeansModel clusters = KMeans.train(inputRDD.rdd(), optimize.getKey(), numIterations);
        Vector[] clusterCenters = clusters.clusterCenters();
        for (int i = 0; i < clusterCenters.length; i++) {
            Vector clusterCenter = clusterCenters[i];
            System.out.println(i + "类中心 : " + clusterCenter);
        }

        //保存最优k值的模型
        //String path = "target/models/pub/KMeans/Model/"+FORMATTER_YMDHMS.get().format(new Date());
        //clusters.save(jsc.sc(), path);
        //KMeansModel sameModel = KMeansModel.load(jsc.sc(), path);
    }


}
