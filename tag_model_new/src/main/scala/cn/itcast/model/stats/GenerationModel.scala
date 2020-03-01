package cn.itcast.model.stats

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.sql.{Column, DataFrame}

object GenerationModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "年龄段"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    // 1. 导入 Spark 依赖
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // 2. 执行计算, 匹配年龄范围
    // 五级标签年龄表示: 19500101-19591231
    // 用户表中生日表示: 1983-03-03 => 19830303
    // 输入 -> id, birthday
    // 中间 -> 通过 birthday 匹配年龄段, 年龄段在五级标签里
    // 输出 -> id, tag_generation (五级标签的ID)
    var conditions: Column = null
    for (tag <- fiveTags) {
      val periods = tag.rule.split("-")
      val start = periods(0)
      val end = periods(1)

      if (conditions == null) {
        conditions = when('brithday.between(start, end), tag.id)
      } else {
        conditions = conditions.when('brithday.between(start, end), tag.id)
      }
    }
    conditions = conditions.as(outFields.head)

    // 3. 返回结果
    source.select('id, conditions)
  }
}
