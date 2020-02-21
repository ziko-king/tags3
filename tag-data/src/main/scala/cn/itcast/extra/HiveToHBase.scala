package cn.itcast.extra

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase.client.{ClusterConnection, ColumnFamilyDescriptorBuilder, ConnectionFactory, HRegionLocator, TableDescriptorBuilder}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, TableOutputFormat}
import org.apache.hadoop.hbase.tool.LoadIncrementalHFiles
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, KeyValue, TableName}
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.sql.SparkSession

object HiveToHBase {
  val defaultCF = "default"
  val defaultNameSpace = "default"
  val tempFileDir = "/user/admin/Spark/extra_temp/"

  def main(args: Array[String]): Unit = {
    if (args.length < 3) {
      return
    }

    val sourceDBName = args(0)
    val sourceTableName = args(1)
    val rkeyField = args(2)

    val conf = HBaseConfiguration.create
    conf.set(TableOutputFormat.OUTPUT_TABLE, sourceTableName)
    conf.set("hbase.mapreduce.hfileoutputformat.table.name", sourceTableName)

    val job = Job.getInstance(conf)
    job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setMapOutputValueClass(classOf[KeyValue])

    val hfilePath = tempFileDir + sourceTableName

    hive2HFile(sourceDBName, sourceTableName, rkeyField, defaultCF, conf, hfilePath)
    bulkLoad2Table(job, hfilePath, defaultNameSpace, sourceTableName, defaultCF)
  }

  def hive2HFile(sourceDB: String, sourceTable: String, rkeyField: String, cf: String, hadoopConfig: Configuration, hfilePath: String): Unit = {
    val fs = FileSystem.get(hadoopConfig)
    if (fs.exists(new Path(hfilePath))) {
      fs.delete(new Path(hfilePath), true)
    }

    val spark = SparkSession.builder()
      .appName("bulk load from hive")
      .enableHiveSupport()
      .getOrCreate()

    spark.read
      .table(sourceDB + "." + sourceTable)
      .rdd
      .filter(row => row.getAs(rkeyField) != null)
      .flatMap(row => {
        val cfBytes = Bytes.toBytes(cf)
        val rowKeyBytes = Bytes.toBytes(row.getAs(rkeyField).toString)

        row.schema
          .filter(field => row.getAs(field.name) != null)
          .sortBy(field => field.name)
          .map(field => {
            val fieldNameBytes = Bytes.toBytes(field.name)
            val valueBytes = Bytes.toBytes(row.getAs(field.name).toString)

            val kv = new KeyValue(rowKeyBytes, cfBytes, fieldNameBytes, valueBytes)
            (new ImmutableBytesWritable(rowKeyBytes), kv)
          })
      })
      .filter(item => item != null)
      .saveAsNewAPIHadoopFile(
        hfilePath,
        classOf[ImmutableBytesWritable],
        classOf[KeyValue],
        classOf[HFileOutputFormat2],
        hadoopConfig
      )

    spark.stop()
  }

  def bulkLoad2Table(job: Job, hfilePath: String, namespace: String, name: String, cf: String): Unit = {
    val connection = ConnectionFactory.createConnection(job.getConfiguration)
    val admin = connection.getAdmin

    val tableName = TableName.valueOf(Bytes.toBytes(namespace), Bytes.toBytes(name))

    if (!admin.tableExists(tableName)) {
      admin.createTable(
        TableDescriptorBuilder.newBuilder(tableName)
          .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build())
          .build()
      )
    }

    val table = connection.getTable(tableName)
    val regionLocator = new HRegionLocator(tableName, connection.asInstanceOf[ClusterConnection])

    HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator)
    val loader = new LoadIncrementalHFiles(job.getConfiguration)
    loader.doBulkLoad(new Path(hfilePath), admin, table, regionLocator)
  }
}
