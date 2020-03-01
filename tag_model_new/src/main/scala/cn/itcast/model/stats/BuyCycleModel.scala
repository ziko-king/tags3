package cn.itcast.model.stats

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.types.LongType

object BuyCycleModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "消费周期"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    // 1. 导入 Spark 的内容
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 3. 将最大的 finishtime 转为距今多少天的形式
    val dayPass = datediff(current_timestamp(), from_unixtime('finishtime)) as "dayPass"

    // 2. 统计用户最大的 finishtime
    val stage1 = source.select('memberid, 'finishtime.cast(LongType).as("finishtime"))
      .groupBy('memberid)
      .agg(max('finishtime) as "finishtime")
      .select('memberid as "id", dayPass)

    // 4. 打标签
    var conditions: Column = null
    for (tag <- fiveTags) {
      val periods = tag.rule.split("-")
      val start = periods(0)
      val end = periods(1)

      if (conditions == null) {
        conditions = when('dayPass.between(start, end), tag.id)
      } else {
        conditions = conditions.when('dayPass.between(start, end), tag.id)
      }
    }
    conditions = conditions.as(outFields.head)

    stage1.select('id, conditions)
  }
}
