package cn.itcast.model.mtag

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.sql.{Column, DataFrame}

object JobModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "职业"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    // 1. 导入 Spark 隐式转换
    import org.apache.spark.sql.functions._
    import spark.implicits._

    // 2. 根据五级标签的规则, 进行匹配
    var conditions: Column = null
    for (tag <- fiveTags) {
      conditions = if (conditions == null) {
        when('job === tag.rule, tag.id)
      } else {
        conditions.when('job === tag.rule, tag.id)
      }
    }
    conditions = conditions.as(outFields.head)

    // 3. 筛选 DataFrame
    source.select('id, conditions)
  }
}
