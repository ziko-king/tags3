package cn.itcast.model

import org.apache.commons.lang3.StringUtils

/**
 * 所有标签对应的样例类
 */
case class Tag(id: String, name: String, rule: String, pid: String)

/**
 * 元数据信息的数据类型
 * 支持的源库类型有三种
 * * RDBMS MySQL
 * * HDFS file
 * * HBase
 */
case class MetaData(in_type: String, driver: String, url: String, username: String, password: String, db_table: String,
                    in_path: String, sperator: String, in_fields: String, cond_fields: String, out_fields: String,
                    out_path: String, zk_hosts: String, zk_port: String, hbase_table: String, family: String,
                    select_field_names: String, where_field_names: String, where_field_values: String) {

  def isHBase: Boolean = {
    in_type.toLowerCase == "hbase"
  }

  def isRDBMS: Boolean = {
    in_type.toLowerCase == "mysql" || in_type.toLowerCase == "postgresql" ||
    in_type.toLowerCase == "oracle" || in_type.toLowerCase == "rdbms"
  }

  def isHDFS: Boolean = {
    in_type.toLowerCase == "hdfs"
  }

  /**
   * 转换为对应类型的元数据对象, HBase
   */
  def toHBaseMeta: HBaseMeta = {
    if (! isHBase) {
      return null
    }

    if (StringUtils.isEmpty(in_fields) || StringUtils.isEmpty(out_fields)) {
      return null
    }

    val commonMeta = CommonMeta(in_type, in_fields.split(","), out_fields.split(","))

    val hbaseMeta = HBaseMeta(commonMeta, hbase_table, family)

    hbaseMeta
  }

  /**
   * 转为 MySQLMeta
   */
  def toMySQLMeta: MySQLMeta = {
    // 此处应该严谨性判断一下

    val commonMeta = CommonMeta(in_type, in_fields.split(","), out_fields.split(","))

    MySQLMeta(commonMeta, driver, url, username, password, db_table)
  }

  /**
   * 转为 HDFS
   */
  def toHDFSMeta: HdfsMeta = {
    val commonMeta = CommonMeta(in_type, in_fields.split(","), out_fields.split(","))
    HdfsMeta(commonMeta, in_path, sperator)
  }
}

/**
 * 通用的数据类型
 */
case class CommonMeta(inType: String, inFields: Array[String], outFields: Array[String])

/**
 * MySQL 的元数据
 */
case class MySQLMeta(commonMeta: CommonMeta, driver: String, url: String, userName: String, password: String, tableName: String)

/**
 * HBase 的元数据
 */
case class HBaseMeta(commonMeta: CommonMeta, tableName: String, columnFamily: String)

/**
 * HDFS 的元数据
 */
case class HdfsMeta(commonMeta: CommonMeta, inPath: String, separator: String)

case class HBaseCatalog(table: HBaseTable, rowkey: String, columns: Map[String, HBaseColumn])

case class HBaseTable(namespace: String, name: String)

case class HBaseColumn(cf: String, col: String, `type`: String)