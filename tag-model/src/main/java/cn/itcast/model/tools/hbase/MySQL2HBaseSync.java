package cn.itcast.model.tools.hbase;

import cn.itcast.model.models.ModelConfig;
import cn.itcast.model.tools.spark.sql.SQLHBase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 同步MySQL数据到HBase
 * Created by mengyao
 * 2019年5月22日
 */
@SuppressWarnings("all")
public class MySQL2HBaseSync {

    private static String appName = MySQL2HBaseSync.class.getSimpleName();
    private static ModelConfig modelConfig = new ModelConfig();
    private static HBaseTools build = HBaseTools.build();
    private static String url = "jdbc:mysql://bd001:3306/tags_dat?useUnicode=true&characterEncoding=utf8&user=root&password=123456";
    private static String driver = "com.mysql.jdbc.Driver";
    private static String[] tables = {"tbl_users", "tbl_orders", "tbl_goods", "tbl_logs"};
    private static String family = "detail";


    public static void main(String[] args) throws Exception {
//		build.ifNotTable("test2", "default");

        SparkConf conf = new SparkConf()
                .setAppName(appName)
                .setMaster(modelConfig.getSparkMaster());
        SparkSession session = SQLHBase.getSession(conf);//SparkSession.builder().config(conf).getOrCreate();
//		// 同步MySQL的tbl_users表数据到HBase的tbl_users表中
//		syncTable(session, tables[0], tables[0], family, "id");

        // 同步MySQL的tbl_orders表数据到HBase的tbl_orders表中
//		syncTable(session, tables[1], tables[1], family, "id");

        // 同步MySQL的tbl_goods表数据到HBase的tbl_goods表中
        syncTable(session, tables[2], tables[2], family, "id");

        // 同步MySQL的tbl_logs表数据到HBase的tbl_logs表中
//		syncTable(session, tables[3], tables[3], family, "id");
    }

    /**
     * @param session
     * @param tableName
     * @param family
     * @param rowKeyColumn
     * @throws IOException
     */
    private static void syncTable(SparkSession session, String dbTable, String hbaseTable, String family, String rowKeyColumn) throws IOException {
        Configuration hbaseConf = HBaseConfiguration.create(session.sparkContext().hadoopConfiguration());
        hbaseConf.set("hbase.zookeeper.quorum", modelConfig.getZkHosts());
        hbaseConf.set("hbase.zookeeper.property.clientPort", modelConfig.getZkPort() + "");
        hbaseConf.set("zookeeper.znode.parent", modelConfig.getZooKeeperZNodeParent());
        hbaseConf.set("mapreduce.output.fileoutputformat.outputdir", modelConfig.getHBaseFsTmpDir());
        hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, dbTable);
        // 如果表不存在则创建
        build.ifNotTable(hbaseTable, family);
        Job job = Job.getInstance(hbaseConf);
        job.setOutputKeyClass(ImmutableBytesWritable.class);
        job.setOutputValueClass(Result.class);
        job.setOutputFormatClass(TableOutputFormat.class);
        // 读取MySQL库的表
        Dataset<Row> rowDF = session.read()
                .jdbc(url, dbTable, new Properties() {{
                    setProperty("driver", driver);
                }});
        // 转换DataFrame为Map<String, String>
        JavaRDD<HashMap<String, String>> beanRDD = rowDF.toJavaRDD().coalesce(1).mapPartitions(iter -> {
            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            iter.forEachRemaining(row -> {
                HashMap<String, String> bean = new HashMap<String, String>();
                String[] fieldNames = row.schema().fieldNames();
                for (int i = 0; i < fieldNames.length; i++) {
                    if (!row.isNullAt(i)) {
                        bean.put(fieldNames[i], row.getAs(fieldNames[i]) + "");
                    }
                }
                list.add(bean);
            });
            return list.iterator();
        });
        // 写入HBase表
        beanRDD.mapToPair(m -> {
            String rowKey = m.get(rowKeyColumn);
            Put put = new Put(Bytes.toBytes(rowKey));//modelConfig.Str2MD5(rowKey)));
            //m.remove(rowKeyColumn);
            m.forEach((k, v) -> put.addColumn(Bytes.toBytes(family), Bytes.toBytes(k), Bytes.toBytes(v)));
            return new Tuple2<>(new ImmutableBytesWritable(), put);
        }).saveAsNewAPIHadoopDataset(job.getConfiguration());
    }

}
