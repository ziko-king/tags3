package cn.itcast.model.tag.`match`

import org.apache.spark.sql.SparkSession

object GenderModel1 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("gender model 1")
      .getOrCreate()


  }
}
