package cn.itcast.tag.web.commons.models.tag;

import cn.itcast.tag.web.commons.models.pub.UserProfileBean;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;

import java.io.Serializable;

/**
 * 品牌偏好标签模型
 *
 * @author mengyao
 */
public class Tag1558Model implements Serializable {

    private static final long serialVersionUID = -6683036568015651444L;

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        SparkSession session = SparkSession.builder()
                .config(conf)
                .appName("SparkOnHBase")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> select = session.read()
                .format("com.mengyao.tag.utils.external.hbase.HBaseSource")
                .option("zkHosts", "192.168.10.20")
                .option("zkPort", "2181")
                .option("hbaseTable", "tbl_tag_user_profile")
                .option("family", "haier")
                .option("selectFields", "tbids")
                .load();
        select.show();
        select.select(new Column("row").alias("userId"), new Column("tbids")).as(Encoders.bean(UserProfileBean.class)).show();
        //.select(new Column("row").alias("userId"), new Column("tbids"));

        select.show();

        session.close();
    }

}
