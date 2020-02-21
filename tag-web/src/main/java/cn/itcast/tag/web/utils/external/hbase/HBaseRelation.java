package cn.itcast.tag.web.utils.external.hbase;

import com.google.common.base.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.sources.TableScan;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mengyao
 */
public class HBaseRelation extends BaseRelation implements Serializable, TableScan {
    private static final long serialVersionUID = 4234614443074355432L;
    private static transient Logger logger = LoggerFactory.getLogger(HBaseRelation.class);
    private final String HBASE_ZK_PORT_KEY = "hbase.zookeeper.property.clientPort";
    private final String HBASE_ZK_PORT_VALUE = "zkPort";
    private final String HBASE_ZK_QUORUM_KEY = "hbase.zookeeper.quorum";
    private final String HBASE_ZK_QUORUM_VALUE = "zkHosts";
    private final String HBASE_ZK_PARENT_KEY = "zookeeper.znode.parent";
    private final String HBASE_ZK_PARENT_VALUE = "/hbase-unsecure";
    private final String HBASE_TABLE = "hbaseTable";
    private final String HBASE_TABLE_FAMILY = "family";
    private final String HBASE_TABLE_SELECT_FIELDS = "selectFields";
    private final String sperator = ",";
    private final String ROW = "row";
    private SQLContext sqlContext;
    private java.util.Map<String, String> options;
    private StructType schema = null;
    private boolean updateSchema = true;

    public HBaseRelation(SQLContext sqlContext, Map<String, String> options) {
        this.sqlContext = sqlContext;
        this.options = JavaConverters.mapAsJavaMapConverter(options).asJava();
    }

    @Override
    public RDD<Row> buildScan() {
        validParams(options);
        return scan(sqlContext, options);
    }

    @Override
    public StructType schema() {
        if (updateSchema || schema == null) {
            List<StructField> fields = new ArrayList<>();
            fields.add(DataTypes.createStructField(ROW, DataTypes.StringType, false));
            String fieldsStr = options.get(HBASE_TABLE_SELECT_FIELDS);
            String[] fieldStrs = fieldsStr.split(sperator);
            Stream.of(fieldStrs).forEach(field -> fields.add(DataTypes.createStructField(field, DataTypes.StringType, false)));
            schema = DataTypes.createStructType(fields);
            updateSchema = false;
        }
        logger.info("==== HBaseSource Schema is:{} ====", schema);
        return schema;
    }

    @Override
    public SQLContext sqlContext() {
        return sqlContext;
    }

    private void validParams(java.util.Map<String, String> options) {
        String zkHosts = options.get(HBASE_ZK_QUORUM_VALUE);
        Preconditions.checkNotNull(zkHosts, "zkHosts not null!");
        String zkPort = options.get(HBASE_ZK_PORT_VALUE);
        Preconditions.checkNotNull(zkPort, "zkPort not null!");
        String family = options.get(HBASE_TABLE_FAMILY);
        Preconditions.checkNotNull(family, "family not null!");
        String fieldsStr = options.get(HBASE_TABLE_SELECT_FIELDS);
        Preconditions.checkNotNull(fieldsStr, "fieldsStr not null!");
    }

    private RDD<Row> scan(SQLContext sqlContext, java.util.Map<String, String> options) {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set(HBASE_ZK_PORT_KEY, options.get(HBASE_ZK_PORT_VALUE));
            conf.set(HBASE_ZK_QUORUM_KEY, options.get(HBASE_ZK_QUORUM_VALUE));
            conf.set(HBASE_ZK_PARENT_KEY, HBASE_ZK_PARENT_VALUE);
            String family = options.get(HBASE_TABLE_FAMILY);
            String fieldsStr = options.get(HBASE_TABLE_SELECT_FIELDS);
            String[] selectFileds = fieldsStr.split(sperator);

            Scan scan = new Scan();
            conf.set(TableInputFormat.INPUT_TABLE, options.get(HBASE_TABLE));
            ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
            String scanStr = Base64.encodeBytes(proto.toByteArray());
            conf.set(TableInputFormat.SCAN, scanStr);
            logger.info("==== HBaseSource Scan is:{} ====", scanStr);

            RDD<Tuple2<ImmutableBytesWritable, Result>> hbaseRdd = sqlContext.sparkContext().newAPIHadoopRDD(conf,
                    TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

            return hbaseRdd.toJavaRDD().map(t -> t._2).map(r -> {
                LinkedList<String> vals = new LinkedList<>();
                String row = Bytes.toString(r.getRow());
                vals.add(row);
                Stream.of(selectFileds).forEach(field -> {
                    String val = Bytes.toString(r.getValue(Bytes.toBytes(family), Bytes.toBytes(field)));
                    vals.add(val);
                });
                return (Row) RowFactory.create(vals.toArray());
            }).rdd();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}