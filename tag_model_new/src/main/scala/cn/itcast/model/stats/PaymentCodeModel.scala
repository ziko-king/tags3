package cn.itcast.model.stats

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.expressions.Window

/**
 * 取最多的支付方式作为标签
 * 取得三项数据
 * * 用户 ID                memberid
 * * 对应单个订单的支付方式   paymentcode
 * * 下订单的时间            paytime
 */
object PaymentCodeModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "支付方式"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    // 1. 导入 Spark 依赖
    import org.apache.spark.sql.functions._
    import spark.implicits._

    // 2. 每个用户  不同的支付方式   的数量
    // 第一次聚合, 针对用户聚合, 找一个用户的所有订单
    // 第二次聚合, 针对支付方式, 找每个支付方式的所有订单
    // 每一个用户, 每一个支付方式, 的数量
    val stage1 = source.groupBy('memberid, 'paymentcode)
      .agg(count('paymentcode) as "count")
      .sort('memberid)

    // 3. 取得最多的数量的支付方式
    // 下面的方式不行
    // group 以后, 只能取出group的字段, 和agg的字段, 其它的字段取不到
//    stage1.groupBy('memberid)
//      .agg(max('count))
//      .select('memberid, 'paymentcode)
    // 解决方案: 开窗
    // 3.1. 按照 memeberid 分区
    // 3.2. 按照 count 列排序
    // 3.3. 生成序号
    val stage2 = stage1.withColumn("rn", row_number() over Window.partitionBy('memberid).orderBy('count desc))
      .where('rn === 1)

    // 4. 打上标签
    var conditions: Column = null
    for (tag <- fiveTags) {
      if (conditions == null) {
        conditions = when('paymentcode === tag.rule, tag.id)
      } else {
        conditions = conditions.when('paymentcode === tag.rule, tag.id)
      }
    }

    // 5. 生成结果
    stage2.select('memberid as "id", conditions as outFields.head)
  }
}
