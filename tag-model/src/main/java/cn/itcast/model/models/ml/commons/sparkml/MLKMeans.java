package cn.itcast.model.models.ml.commons.sparkml;

import cn.itcast.model.models.ml.commons.ClusterMapping;
import cn.itcast.model.tools.hdfs.HdfsTools;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by mengyao
 * 2019年6月11日
 */
public class MLKMeans implements Serializable {

    private static final long serialVersionUID = 3657749849130041842L;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int k;
    private long seed = 1;
    private KMeans kmeans;
    private KMeansModel model;
    // 是否采用优化方式训练
    private boolean optimize;
    // 使用KMeans聚类算法，提供选择多个k值进行训练
    private List<Integer> kArr = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
    // 计算每一个k值聚类的SSE值
    private Map<Integer, Double> sseArr = new HashMap<Integer, Double>();
    // 聚类结果，簇编号和簇质心
    private LinkedList<ClusterMapping> clusters;
    // 聚类迭代计算次数
    private int numIterations = 30;
    // 数据集
    private Dataset<Row> dataset;
    private String modelPath;
    // 输入列
    private String[] inputCols;
    // 输出列
    private String outputCol;
    // 预测列
    private String predictionCol;
    // 向量转换器
    private VectorAssembler vectorAssembler;


    public MLKMeans(String[] inputCols, String outputCol, String predictionCol, Dataset<Row> dataset, String modelPath) {
        this.optimize = true;
        this.inputCols = inputCols;
        this.outputCol = outputCol;
        this.predictionCol = predictionCol;
        vectorAssembler = new VectorAssembler()
                .setInputCols(inputCols)
                .setOutputCol(outputCol);
        this.dataset = dataset;
        this.modelPath = modelPath;
    }

    public MLKMeans(int k, String[] inputCols, String outputCol, String predictionCol, Dataset<Row> dataset, String modelPath) {
        this.k = k;
        this.inputCols = inputCols;
        this.outputCol = outputCol;
        this.predictionCol = predictionCol;
        vectorAssembler = new VectorAssembler()
                .setInputCols(inputCols)
                .setOutputCol(outputCol);
        this.dataset = dataset;
        this.modelPath = modelPath;

    }

    public Dataset<Row> run() {
        if (HdfsTools.build().exist(modelPath)) {
            model = KMeansModel.load(modelPath);
            dataset = vectorAssembler.transform(dataset);
            return predict();
        } else {
            logger.warn("==== The model path is not found! ====");
            if (optimize) {
                kArr.forEach(k -> {
                    train(false);
                    // 采用肘部法则计算簇内平方误差和来评估聚类
                    double sse = model.computeCost(dataset);
                    // 记录每个k值的sse
                    sseArr.put(k, sse);
                });
                // 找出最优k值参数
                Entry<Integer, Double> optimizeK = sseArr.entrySet().stream().min((i1, i2) -> i1.getValue().compareTo(i2.getValue())).get();
                k = optimizeK.getKey();
                // 使用最优有k计算聚类
                train(true);
                return predict();
            } else {
                train(true);
                return predict();
            }
        }
    }

    /**
     * 训练模型
     *
     * @param dataset   数据集
     * @param saveModel 保存模型到hdfs
     */
    private void train(boolean saveModel) {
        dataset = vectorAssembler.transform(dataset);
        kmeans = new KMeans()
                .setK(k)
                .setSeed(numIterations)
                .setFeaturesCol(outputCol)
                .setPredictionCol(predictionCol);
        model = kmeans.fit(dataset);
        if (saveModel) {
            try {
                model.save(modelPath);
                logger.info("==== The model is save to:{} ====", modelPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 预测模型
     *
     * @return
     */
    private Dataset<Row> predict() {
        // 记录分类数据(类索引，类中心)
        clusters = new LinkedList<ClusterMapping>();
        int i = 0;
        for (Vector center : model.clusterCenters()) {
            clusters.add(new ClusterMapping(i, Arrays.stream(center.toArray()).sum()));
            i++;
        }
        return model.transform(dataset);
    }

    /**
     * 获取簇内误差平方和
     *
     * @return
     */
    public Map<Integer, Double> getSse() {
        return sseArr;
    }

    /**
     * 获取聚类的簇编号和簇质心
     *
     * @return
     */
    public LinkedList<ClusterMapping> getClusters() {
        if (null != clusters) {
            clusters.sort((i1, i2) -> i1.getCenter().compareTo(i2.getCenter()));
        }
        return clusters;
    }


}
