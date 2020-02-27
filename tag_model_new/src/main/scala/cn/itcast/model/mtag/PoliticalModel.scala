package cn.itcast.model.mtag

import cn.itcast.model.{BasicModel, Tag}
import org.apache.spark.sql.{Column, DataFrame}

object PoliticalModel extends BasicModel {

  def main(args: Array[String]): Unit = {
    startFlow()
  }

  override def tagName(): String = "政治面貌"

  override def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame = {
    import spark.implicits._
    import org.apache.spark.sql.functions._

    var conditions: Column = null
    for (tag <- fiveTags) {
      if (conditions == null) {
        conditions = when('politicalface === tag.rule, tag.id)
      } else {
        conditions = conditions.when('politicalface === tag.rule, tag.id)
      }
    }
    conditions = conditions.as(outFields.head)

    source.select('id, conditions)
  }
}
