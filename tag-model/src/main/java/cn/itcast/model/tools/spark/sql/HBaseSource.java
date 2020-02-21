package cn.itcast.model.tools.spark.sql;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.sources.RelationProvider;
import scala.collection.immutable.Map;

/**
 * SparkSQL HBase Data Source HBaseSource
 * Created by mengyao
 * 2018年6月2日
 */
public class HBaseSource implements RelationProvider {
    @Override
    public BaseRelation createRelation(SQLContext sqlContext, Map<String, String> options) {
        return new HBaseRelation(sqlContext, options);
    }
}