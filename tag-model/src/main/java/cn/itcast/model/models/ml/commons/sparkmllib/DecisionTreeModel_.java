package cn.itcast.model.models.ml.commons.sparkmllib;

import cn.itcast.model.models.ml.customervalue.RFMBean;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengyao
 * 2019年8月2日
 */
public class DecisionTreeModel_ {


    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "D:\\softs\\developer\\apache\\hadoop-2.7.7");

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("RFM")
                .set("spark.kryoserializer.buffer", "64k")
                .set("spark.kryoserializer.buffer.max", "64m")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .registerKryoClasses(new Class[]{RFMBean.class});
        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.sc().setLogLevel("OFF");

        tree1(jsc);


    }

    private static void tree2(JavaSparkContext jsc) {
        //是否见面,年龄,是否帅(0不帅、,1帅),收入(1 高、 2 中等、 0 少),是否公务员
        List<LabeledPoint> allSet = Arrays.asList(
                new LabeledPoint(0, Vectors.dense(32, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(25, 1, 2, 0)),
                new LabeledPoint(0, Vectors.dense(29, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(24, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(31, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(35, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(30, 0, 1, 0)),
                new LabeledPoint(0, Vectors.dense(31, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(30, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(21, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(21, 1, 2, 0)),
                new LabeledPoint(0, Vectors.dense(21, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(29, 0, 2, 1)),
                new LabeledPoint(0, Vectors.dense(29, 1, 0, 1)),
                new LabeledPoint(0, Vectors.dense(29, 0, 2, 1)));

        JavaRDD<LabeledPoint>[] allDataRDD = jsc.parallelize(allSet).randomSplit(new double[]{0.7, 0.3}, 2);
        JavaRDD<LabeledPoint> trainDataRDD = allDataRDD[0];
        JavaRDD<LabeledPoint> testDataRDD = allDataRDD[1];


        //设置决策树分类器参数
        int numClasses = 2;//分为2类
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();//存储分类特征数量
        String impurity = "gini";//gini=Gini不纯度,entropy=熵。用于信息增益计算的杂质标准
        //树的最大深度，（例如，深度0表示1个叶节点，深度1表示1个内部节点+2个叶节点），建议值为5
        int maxDepth = 5,
                //最大分支数量
                maxBins = 32;

        //训练决策树模型进行分类。
        DecisionTreeModel model = DecisionTree.trainClassifier(trainDataRDD, numClasses,
                categoricalFeaturesInfo, impurity, maxDepth, maxBins);

        //模型预测
        JavaPairRDD<Double, Double> predictionAndLabel =
                testDataRDD.mapToPair(p -> new Tuple2<>(p.label(), model.predict(p.features())));

        //测试值与真实值对比
        System.out.println("label" + "\t" + "prediction");
        predictionAndLabel.take(15).forEach(t2 -> {
            System.out.println(t2._1 + "\t" + t2._2);
        });

        //计算决策树分类器的错误率
        double testErr =
                predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testDataRDD.count();

        System.out.println("Test Error: " + testErr);
        System.out.println("Learned classification tree model:\n" + model.toDebugString());

        //保存模型
        //model.save(jsc.sc(), "target/tmp/myDecisionTreeClassificationModel");
        //DecisionTreeModel sameModel = DecisionTreeModel
        //.load(jsc.sc(), "target/tmp/myDecisionTreeClassificationModel");
    }

    private static void tree1(JavaSparkContext jsc) {
        //是否见面,年龄,是否帅(0不帅、,1帅),收入(1 高、 2 中等、 0 少),是否公务员
        List<LabeledPoint> trainSet = Arrays.asList(
                new LabeledPoint(0, Vectors.dense(32, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(25, 1, 2, 0)),
                new LabeledPoint(1, Vectors.dense(29, 1, 2, 1)),
                new LabeledPoint(1, Vectors.dense(24, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(31, 1, 1, 0)),
                new LabeledPoint(1, Vectors.dense(35, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(30, 0, 1, 0)),
                new LabeledPoint(0, Vectors.dense(31, 1, 1, 0)),
                new LabeledPoint(1, Vectors.dense(30, 1, 2, 1)),
                new LabeledPoint(1, Vectors.dense(21, 1, 1, 0)),
                new LabeledPoint(0, Vectors.dense(21, 1, 2, 0)),
                new LabeledPoint(1, Vectors.dense(21, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(29, 0, 2, 1)),
                new LabeledPoint(0, Vectors.dense(29, 1, 0, 1)),
                new LabeledPoint(0, Vectors.dense(29, 0, 2, 1)),
                new LabeledPoint(1, Vectors.dense(30, 1, 1, 0)));

        List<LabeledPoint> testSet = Arrays.asList(
                new LabeledPoint(0, Vectors.dense(32, 1, 2, 0)),
                new LabeledPoint(1, Vectors.dense(27, 1, 1, 1)),
                new LabeledPoint(1, Vectors.dense(29, 1, 1, 0)),
                new LabeledPoint(1, Vectors.dense(25, 1, 2, 1)),
                new LabeledPoint(0, Vectors.dense(23, 0, 2, 1))
        );

        //0.7 train
        JavaRDD<LabeledPoint> trainDataRDD = jsc.parallelize(trainSet);
        //0.3 test
        JavaRDD<LabeledPoint> testDataRDD = jsc.parallelize(testSet);

        //设置决策树分类器参数
        int numClasses = 2;//分为2类
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();//存储分类特征数量
        String impurity = "gini";//gini=Gini不纯度,entropy=熵。用于信息增益计算的杂质标准
        //树的最大深度，（例如，深度0表示1个叶节点，深度1表示1个内部节点+2个叶节点），建议值为5
        int maxDepth = 5,
                //最大分支数量
                maxBins = 32;

        //训练决策树模型进行分类。
        DecisionTreeModel model = DecisionTree.trainClassifier(trainDataRDD, numClasses,
                categoricalFeaturesInfo, impurity, maxDepth, maxBins);

        //模型预测
        JavaPairRDD<Double, Double> predictionAndLabel =
                testDataRDD.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

        //测试值与真实值对比
        System.out.println("label" + "\t" + "prediction");
        predictionAndLabel.take(15).forEach(t2 -> {
            System.out.println(t2._1 + "\t" + t2._2);
        });

        //计算决策树分类器的错误率
        double testErr =
                predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testDataRDD.count();

        System.out.println("Test Error: " + testErr);
        System.out.println("Learned classification tree model:\n" + model.toDebugString());

        //保存模型
        //model.save(jsc.sc(), "target/tmp/myDecisionTreeClassificationModel");
        //DecisionTreeModel sameModel = DecisionTreeModel
        //.load(jsc.sc(), "target/tmp/myDecisionTreeClassificationModel");
    }

}
