package cn.itcast.model.ml

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.DataFrame

object RFMTrainModel extends BasicModel {
  val MODEL_PATH = "/models/rfm/kmeans"

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "客户价值"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    val rfm = rfmScore(source)
    val vectored = assembleFeatures(rfm)
    train(vectored, fiveTags.length)
    null
  }

  /**
   * 1. 求 RFM
   *
   */
  def rfmScore(dataFrame: DataFrame): DataFrame = {
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 求得 R, 最后一次消费距今的时间
    val rCol = datediff(date_sub(current_timestamp(), 190), from_unixtime(max('finishtime))) as "r"
    // 求得 F, 消费频率
    val fCol = count('ordersn) as "f"
    // 求得 M, 总消费金额
    val mCol = sum('orderamount) as "m"

    // 2. 为 RFM 打分
    // R: 1-3天=5分，4-6天=4分，7-9天=3分，10-15天=2分，大于16天=1分
    // F: ≥200=5分，150-199=4分，100-149=3分，50-99=2分，1-49=1分
    // M: ≥20w=5分，10-19w=4分，5-9w=3分，1-4w=2分，<1w=1分
    val rScore = when('r >= 1 and 'r <= 3, 5)
      .when('r >= 4 and 'r <= 6, 4)
      .when('r >= 7 and 'r <= 9, 3)
      .when('r >= 10 and 'r <= 15, 2)
      .when('r >= 16, 1)
      .as("r_score")

    val fScore = when('f >= 200, 5)
      .when(('f >= 150) && ('f <= 199), 4)
      .when((col("f") >= 100) && (col("f") <= 149), 3)
      .when((col("f") >= 50) && (col("f") <= 99), 2)
      .when((col("f") >= 1) && (col("f") <= 49), 1)
      .as("f_score")

    val mScore = when(col("m") >= 200000, 5)
      .when(col("m").between(100000, 199999), 4)
      .when(col("m").between(50000, 99999), 3)
      .when(col("m").between(10000, 49999), 2)
      .when(col("m") <= 9999, 1)
      .as("m_score")

    dataFrame.groupBy('memberid)
      .agg(rCol, fCol, mCol)
      .select('memberid as "id", rScore, fScore, mScore)
  }

  /**
   * 2. 特征处理
   */
  def assembleFeatures(dataFrame: DataFrame): DataFrame = {
    new VectorAssembler()
      .setInputCols(Array("r_score", "f_score", "m_score"))
      .setOutputCol("features")
      .setHandleInvalid("skip")
      .transform(dataFrame)
  }

  /**
   * 3. 训练
   */
  def train(dataFrame: DataFrame, k: Int): Unit = {
    val classification = new KMeans()
      .setK(k)
      .setSeed(10)
      .setMaxIter(10)
      .setFeaturesCol("features")
      .setPredictionCol("predict")

    // 训练产生模型
    // 存储模型到 HDFS 中, 在另外一个 程序 中读取模型, 执行预测
    classification.fit(dataFrame).save(MODEL_PATH)
  }
}
