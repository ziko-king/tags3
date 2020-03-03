package cn.itcast.model.ml

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.DoubleType

object PsmModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "促销敏感度"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 1. 计算不分组字段
    // 应收金额
    val receivableAmount = ('couponcodevalue + 'orderamount).cast(DoubleType) as "receivableAmount"
    // 优惠金额
    val discountAmount = 'couponcodevalue.cast(DoubleType) as "discountAmount"
    // 实收金额
    val practicalAmount = 'orderamount.cast(DoubleType) as "practicalAmount"
    // 是否优惠, 用于后面累加后悔单量
    val state = when(discountAmount =!= 0.0d, 1)
      .when(discountAmount === 0.0d, 0)
      .as("state")

    val stage1 = source.select('memberid as "id", receivableAmount, discountAmount, practicalAmount, state)
    stage1.show()

    // 2. 计算分组字段
    // 优惠订单数
    val discountCount = sum('state) as "discountCount"
    // 订单总数
    val orderCount = count('state) as "orderCount"
    // 优惠总额
    val totalDiscountAmount = sum('discountAmount) as "totalDiscountAmount"
    // 应收总额
    val totalReceivableAmount = sum('receivableAmount) as "totalReceivableAmount"
    // receivableAmount|discountAmount|practicalAmount|state
    val stage2 = stage1.groupBy('id)
      .agg(discountCount, orderCount, totalDiscountAmount, totalReceivableAmount)
    stage2.show()

    // 3. 计算集成字段
    // 平均优惠金额
    val avgDiscountAmount = ('totalDiscountAmount / 'discountCount) as "avgDiscountAmount"
    // 平均每单应收
    val avgReceivableAmount = ('totalReceivableAmount / 'orderCount) as "avgReceivableAmount"
    // 优惠订单占比
    val discountPercent = ('discountCount / 'orderCount) as "discountPercent"
    // 平均优惠金额占比
    val avgDiscountPercent = (avgDiscountAmount / avgReceivableAmount) as "avgDiscountPercent"
    // 优惠金额占比
    val discountAmountPercent = ('totalDiscountAmount / 'totalReceivableAmount) as "discountAmountPercent"

//    val stage3 = stage2.select('id, avgDiscountAmount, avgReceivableAmount, discountPercent, avgDiscountPercent, discountAmountPercent)
//    stage3.show()

    // 4. 计算 PSM
    // psm = 优惠订单占比 + (平均优惠金额 / 平均每单应收) + 优惠金额占比
    val psmScore = (discountPercent + (avgDiscountAmount / avgReceivableAmount) + discountAmountPercent) as "psm_score"
    val psmDF = stage2.select('id, psmScore)
    psmDF.show()

    // 5. 肘部法则确定 K
    val vectored = new VectorAssembler()
      .setInputCols(Array("psm_score"))
      .setOutputCol("features")
      .setHandleInvalid("skip")
      .transform(psmDF)

//    val kArr = Array(2, 3, 4, 5, 6, 7, 8)
//
//    val costMap = kArr.map(k => {
//      val kmeans = new KMeans()
//        .setK(k)
//        .setMaxIter(10)
//        .setPredictionCol("predict")
//        .setFeaturesCol("features")
//
//      val model = kmeans.fit(vectored)
//      val cost = model.computeCost(vectored)
//      (k, cost)
//    }).toMap
//
//    println(costMap)

    // K 在 4 - 7 之间比较合理
    // 从业务的可解释性上, 5 会更合理

    // 使用 K = 5 计算
    // 排序
    null
  }
}
