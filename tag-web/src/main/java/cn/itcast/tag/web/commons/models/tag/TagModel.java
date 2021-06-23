package cn.itcast.tag.web.commons.models.tag;

import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;

public abstract class TagModel {

    public static void saveTagProfile(SparkSession session, RDD<?> rdd) {
    }

}
