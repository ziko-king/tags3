package cn.itcast.model

import java.util.Properties

import cn.itcast.model.utils.ShcUtils
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.{DataFrame, SparkSession}

trait BasicModel {

  val spark: SparkSession = SparkSession.builder()
    .appName("job model")
    .master("local[10]")
    .getOrCreate()

  val config: Config = ConfigFactory.load()

  def startFlow(): Unit = {
    // 1. 访问 MySQL, tbl_basic_tag, 获取四级标签和五级标签的数据
    val (fourTag, fiveTag) = readBasicTag(tagName())

    // 2. 根据四级标签的 ID, 去 tbl_metadata 中查询对应的元数据
    val metaData = readMetaData(fourTag.id)

    // 3. 读取对应的数据库, 拿到源表数据
    val (source, commonMeta) = createSource(metaData)

    if (source == null) {
      return
    }

    // 4. 计算标签
    val result = process(source, fiveTag, commonMeta.outFields)

    // 5. 写入 HBase
    if (result != null) {
      result.show()
      saveUserProfile(result, commonMeta.outFields)
    }
  }

  def tagName(): String

  def process(source: DataFrame, fiveTags: Array[Tag], outFields: Array[String]): DataFrame

  /**
   * 通过名称, 取四级标签
   * 通过四级标签, 取五级标签
   * 最终返回四级标签和五级标签
   */
  def readBasicTag(tagName: String): (Tag, Array[Tag]) = {
    import spark.implicits._

    val url = config.getString("jdbc.basic_tag.url")
    val table = config.getString("jdbc.basic_tag.table")

    val basicTagDF = spark.read.jdbc(url, table, new Properties())

    // 筛选四级标签
    val fourTag = basicTagDF.where('name === tagName)
      .as[Tag]
      .collect()
      .head

    // 筛选五级标签
    val fiveTag = basicTagDF.where('pid === fourTag.id)
      .as[Tag]
      .collect()

    (fourTag, fiveTag)
  }

  /**
   * 通过四级标签的 ID, 去元信息表中查询对应的元信息
   */
  def readMetaData(fourTagID: String): MetaData = {
    // 1. 导入 Spark 相关的隐式转换
    import org.apache.spark.sql.functions._
    import spark.implicits._

    // 2. 读取相关配置
    val url = config.getString("jdbc.meta_data.url")
    val table = config.getString("jdbc.meta_data.table")
    val matchColumn = config.getString("jdbc.meta_data.match_column")

    // 3. 读取并筛选数据
    spark.read
      .jdbc(url, table, new Properties())
      .where(col(matchColumn) === fourTagID)
      .as[MetaData]
      .collect()
      .head
  }

  /**
   * 根据元数据, 读取源表所在的数据库, 返回DataFrame数据源
   */
  def createSource(metaData: MetaData): (DataFrame, CommonMeta) = {
    // 判断是否是 HBase, 如果是, 则创建 DataFrame
    if (metaData.isHBase) {
      // 1. 创建 Catalog
      // 2. 读取 HBase, 产生 DataFrame
      val hbaseMeta = metaData.toHBaseMeta
      val source = ShcUtils.read(hbaseMeta.tableName, hbaseMeta.commonMeta.inFields, hbaseMeta.columnFamily, spark)

      return (source, hbaseMeta.commonMeta)
    }

    // 判断是否是 MySQL, 如果是, 则创建 DataFrame
    if (metaData.isRDBMS) {

    }

    // 判断是否是 HDFS, 如果是, 则创建 DataFrame
    if (metaData.isHDFS) {

    }

    (null, null)
  }

  /**
   * 将结果集写入 HBase 中
   */
  def saveUserProfile(result: DataFrame, outFields: Array[String]): Unit = {
    ShcUtils.writeToHBase(outFields, result, "5")
  }
}
