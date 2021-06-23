package cn.itcast.tag.web.utils.external.hbase;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author mengyao
 */
public class SparkSQLOnHBase {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        SparkSession session = SparkSession.builder()
                .config(conf)
                .appName("SparkOnHBase")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = session.read()
                .format("com.mengyao.tag.utils.external.hbase.HBaseSource")
                .option("zkHosts", "192.168.10.20")
                .option("zkPort", "2181")
                .option("hbaseTable", "tbl_tag_user")
                .option("family", "haier")
                .option("selectFields", "id,username,email,phone")
                .load();

        df.printSchema();
        df.logicalPlan();
        df.explain();
        df.filter("id>10").show();

        session.close();
    }

}
