package cn.itcast.model.models.ml.commons.sparkml;

import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * An example for Naive Bayes Classification.
 */
public class MLNaiveBayes {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("JavaNaiveBayesExample").getOrCreate();

        // 加载数据
        Dataset<Row> dataFrame = spark.read().format("libsvm").load("data/mllib/sample_libsvm_data.txt");
        // 拆分数据为训练集和测试集
        Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

        // create the trainer and set its parameters
        NaiveBayes nb = new NaiveBayes();

        // train the model
        NaiveBayesModel model = nb.fit(train);

        // Select example rows to display.
        Dataset<Row> predictions = model.transform(test);
        predictions.show();

        // compute accuracy on the test set
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator().setLabelCol("label")
                .setPredictionCol("prediction").setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test set accuracy = " + accuracy);
        // $example off$

        spark.stop();
    }
}