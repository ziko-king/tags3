package cn.itcast.tag.web.utils.external.hbase;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.sources.RelationProvider;
import scala.collection.immutable.Map;

/**
 * @author mengyao
 */
public class HBaseSource implements RelationProvider {
    @Override
    public BaseRelation createRelation(SQLContext sqlContext, Map<String, String> options) {
        return new HBaseRelation(sqlContext, options);
    }
}