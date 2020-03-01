package cn.itcast.model.stats

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.expressions.Window

object RecentlyPaymentCodeModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "支付方式"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    // 1. 导入 Spark 相关依赖
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 2. 统计, 找到最近一次的支付方式
    // 错误的计算方式: 第一步可以先按照用户和支付方式分组, 找到最近一次
    // 正确的计算方式: 使用窗口, 找到每个用户的最近一次支付行为
    source.select(
        'memberid as "id",
        'paymentcode,
        row_number() over Window.partitionBy('memberid).orderBy('paytime desc) as "rn"
      )
      .where('rn === 1)

    // 3. 打标签
    var conditions: Column = null
    for (tag <- fiveTags) {
      if (conditions == null) {
        conditions = when('paymentcode === tag.rule, tag.id)
      } else {
        conditions = conditions.when('paymentcode === tag.rule, tag.id)
      }
    }
    conditions = conditions.as(outFields.head)

    source.select('id, conditions)
  }
}
