package cn.itcast.model.tools.spark.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * SparkSQL HBase Data Source
 * Created by mengyao
 * 2018年6月2日
 */
public class SQLHBase {

    private static String appName = SQLHBase.class.getSimpleName();

    public static SparkSession getSession(SparkConf conf) {
        return SparkSession.builder()
                .config(conf)
                .getOrCreate();
    }

    public static SparkSession getSession(SparkConf conf, String appName) {
        return getSession(conf, appName, "local[*]");
    }

    public static SparkSession getSession(SparkConf conf, String appName, String master) {
        return SparkSession.builder()
                .config(conf)
                .appName(appName)
                .master(master)
                .getOrCreate();
    }


    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//				.set("spark.driver.memory", "2g")//driver内存4g
//                .set("spark.driver.cores", "1")//driver计算vcore数量为1
//                .set("spark.executor.memory", "4g")//executor内存为4g
                ;

        SparkSession session = getSession(conf, appName, "local[*]");

        Dataset<Row> df = session.read()
                .format("com.mengyao.tools.spark.sql.HBaseSource")
                .option("zkHosts", "192.168.10.20")
                .option("zkPort", "2181")
                .option("hbaseTable", "tbl_users")
                .option("family", "detail")
                .option("selectFields", "id,username,email,phone")
                /**
                 * 不区分大小写，多个条件使用英文逗号,分割
                 * 	EQ = EQUAL等于 
                 NE = NOT EQUAL不等于
                 GT = GREATER THAN大于　
                 LT = LESS THAN小于
                 GE = GREATER THAN OR EQUAL 大于等于
                 LE = LESS THAN OR EQUAL 小于等于
                 例如："utime[GT]20190101,name[EQ]zhangsan,age[LT]19"
                 */
                //.option("whereFields", "gender[eq]2")
                //.option("whereFields", "utime[GT]20190101")// eq、ne、gt、ge、lt、le
                .load();

        df.printSchema();
        df.foreachPartition(rows -> {
            while (rows.hasNext()) {
                System.out.println("==== " + rows.next().toString());
            }
        });

        System.out.println("==== " + df.count());
        int i = 0;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i < 1000) {
                break;
            }
            i++;
        }

        session.close();
    }

}