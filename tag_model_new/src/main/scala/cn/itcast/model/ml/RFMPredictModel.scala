package cn.itcast.model.ml

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.ml.clustering.KMeansModel
import org.apache.spark.sql.DataFrame

/**
 * 用户价值标签, 预测模型
 */
object RFMPredictModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "客户价值"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    import spark.implicits._
    import org.apache.spark.sql.functions._
    // 1. 预测结果
    // source 依然需要计算 RFM, 并对 RFM 进行打分
    val rfm = RFMTrainModel.rfmScore(source)
    val vectored = RFMTrainModel.assembleFeatures(rfm)

    // 模型读取
    val model = KMeansModel.load(RFMTrainModel.MODEL_PATH)

    // 执行预测
    val predicted = model.transform(vectored)

    // 2. 对结果进行排序

    // 确定分组过后的结果序号是有序的
//    predicted.groupBy('predict)
//      .agg(max('r_score + 'f_score + 'm_score) as "max_rfm", min('r_score + 'f_score + 'm_score) as "min_rfm")
//      .sort('predict)
//      .show()

    // 假定 可以按照 RFM 之和来排序
    // 找到每一个簇的中心点, 找到所有的中心点, 质心
    // id, r, f, m, predict
    // 注意点: predict 的序号, 其实就是和 clusterCenters 中的原始顺序是一致的
    // model.clusterCenters.indices.map(i => (i, model.clusterCenters(i).toArray.sum)) -> (predict序号, rfm sum)
    val sortedCenters = model.clusterCenters.indices.map(i => (i, model.clusterCenters(i).toArray.sum))
      .sortBy(t => t._2)
      .reverse

    // (predict, rfm), 让这个集合按照 rfm 和进行有序
    // (predict, sorted index)
    // (5, 1), (0, 2), (1, 3)
    val centerIndex = sortedCenters.indices.map(i => (sortedCenters(i)._1, i + 1)).toDF("predict", "index")

    // Join, 把真正的序号 Join 进原始数据集中
    // 如果 join 中一个表特别小, 小于 32k 的话, 会自动的进行相应的 Map 端 Join 优化
    predicted.join(centerIndex, predicted.col("predict") === centerIndex.col("predict"))
      .select(predicted.col("id"), centerIndex.col("index") as outFields.head)
      .show
    null
  }
}
