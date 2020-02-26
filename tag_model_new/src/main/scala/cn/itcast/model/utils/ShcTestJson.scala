package cn.itcast.model.utils

import cn.itcast.model.{HBaseCatalog, HBaseColumn, HBaseTable}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog

import scala.collection.mutable

object ShcTestJson {

  def main(args: Array[String]): Unit = {
    // 问题: Catalog 拼起来好像不是很友好啊
    // 问题: Catalog 使用字符串拼接, 不好动态的处理, 难道要使用 for 循环拼接字符串吗?

    // 解法: 能否通过对象的形式来操作数据, 比如说使用多个对象来表示 Columns

    // 步骤:
    // 1. 创建类, 根据 Catalog 的 JSON 格式来创建
    // 2. 处理 Catalog 对象
    val table = HBaseTable("default", "tbl_test1")

    val columns = mutable.HashMap.empty[String, HBaseColumn]
    // rowkey 的 column 不能是普通的 CF, 必须得是 rowkey
    columns += "rowkey" -> HBaseColumn("rowkey", "id", "string")
    columns += "userName" -> HBaseColumn("default", "username", "string")

    val catalog = HBaseCatalog(table, "id", columns.toMap)

    // 3. 把 Catalog 转成 JSON 形式
    import org.json4s._
    import org.json4s.jackson.Serialization
    import org.json4s.jackson.Serialization.write

    implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)

    val catalogJSON = write(catalog)

    println(catalogJSON)

    // 4. 读取数据
    val spark = SparkSession.builder()
      .appName("test shc json")
      .master("local[10]")
      .getOrCreate()

    spark.read
      .option(HBaseTableCatalog.tableCatalog, catalogJSON)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()
      .show()
  }

  /**
   * 1. 创建类, 根据 Catalog 的 JSON 格式来创建
   * {
     * "table": {
      * "namespace": "default", "name": "tbl_users"
     * },
     * "rowkey": "id",
     * "columns": {
       * "id": {"cf": "rowkey", "col": "id", "type": "string"},
       * "userName": {"cf": "default", "col": "username", "type": "string"}
     * }
   * }
   *
   * 根据 Catalog 的类型, 创建三个 case class, 分别是 HBaseCatalog, 表示最外层
   * HBaseTable 表示 table 对象
   * HBaseColumn 表示 columns 中的 HBase 列信息
   */

}
