package cn.itcast.tag.web.commons.models.pub.ml;

import org.apache.spark.ml.clustering.BisectingKMeans;
import org.apache.spark.ml.clustering.BisectingKMeansModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 二分KMeans聚类
 *
 * @author mengyao
 */
public class BisectingKMeansModel_ {

    public static void main(String[] args) {
        SparkSession session = SparkSession
                .builder()
                .appName("BisectingKMeansModel_")
                .getOrCreate();

        Dataset<Row> dataset = session.read().format("libsvm").load("data/mllib/sample_kmeans_data.txt");

        // 使用KMeans聚类算法，提供选择多个k值进行训练
        List<Integer> ks = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
        // 计算每一个k值聚类的WSSSE值
        Map<Integer, Double> WSSSEs = new HashMap<Integer, Double>();
        // 聚类迭代计算次数
        int numIterations = 20;

        // 根据每一个k值训练k-means模型
        ks.forEach(k -> {
            BisectingKMeans bkm = new BisectingKMeans()
                    .setK(k)
                    .setMaxIter(numIterations);
            BisectingKMeansModel model = bkm.fit(dataset);
            // 通过计算平方误差的集合和来评估聚类
            double WSSSE = model.computeCost(dataset);
            WSSSEs.put(k, WSSSE);
        });

        // 找出最优k值参数
        Entry<Integer, Double> optimize = WSSSEs.entrySet().stream().min((i1, i2) -> i1.getValue().compareTo(i2.getValue())).get();
        System.out.println("ALL: ==== " + WSSSEs);
        System.out.println("ONE: ==== " + optimize);

        //使用最优k值聚类
        BisectingKMeans bkm = new BisectingKMeans()
                .setK(optimize.getKey())
                .setMaxIter(numIterations);
        BisectingKMeansModel model = bkm.fit(dataset);

        // 获取最终训练结果
        Vector[] clusterCenters = model.clusterCenters();
        for (int i = 0; i < clusterCenters.length; i++) {
            Vector clusterCenter = clusterCenters[i];
            System.out.println(i + "类中心 : " + clusterCenter);
        }

        session.stop();
    }

}
