package cn.itcast.tag.web.commons.models.pub.ml;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 随机森林分类器
 *
 * @author mengyao
 */
public class RandomForestClassifierModel_ {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("RandomForestClassifierModel_")
                .getOrCreate();

        // 加载数据并生成DataFrame
        Dataset<Row> data = spark.read().format("libsvm").load("data/mllib/sample_libsvm_data.txt");

        //索引标签，向标签列添加元数据。适用于整个数据集以包含索引中的所有标签
        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(data);

        // 自动识别分类功能并对其进行索引。设置最大范围，>4个不同值的特征被视为连续。
        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(4)
                .fit(data);

        // 将数据拆分为训练集和测试集（30%用于测试）
        Dataset<Row>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testData = splits[1];

        // 训练随机森林模型
        RandomForestClassifier rf = new RandomForestClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures");

        // 将索引标签转换回原始标签
        IndexToString labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

        // 链式索引器和管道中的森林
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{labelIndexer, featureIndexer, rf, labelConverter});

        // 基于管道的模型训练并运行索引数据
        PipelineModel model = pipeline.fit(trainingData);

        // 开始预测
        Dataset<Row> predictions = model.transform(testData);

        // 选择要显示的5行样本
        predictions.select("predictedLabel", "label", "features").show(5);

        // 选择（预测，真实标签）并计算测试错误
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test Error = " + (1.0 - accuracy));

        RandomForestClassificationModel rfModel = (RandomForestClassificationModel) (model.stages()[2]);
        System.out.println("Learned classification forest model:\n" + rfModel.toDebugString());

        spark.stop();
    }
}