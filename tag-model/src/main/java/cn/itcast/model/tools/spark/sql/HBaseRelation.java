package cn.itcast.model.tools.spark.sql;

import com.google.common.base.Preconditions;
import net.iharder.base64.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * SparkSQL HBase Data Source HBaseRelation
 * Created by mengyao
 * 2018年6月2日
 */
public class HBaseRelation extends BaseRelation implements Serializable, TableScan {
    private static final long serialVersionUID = 4234614443074355432L;
    static String fullRegex = "(.*?)\\[(.*?)\\](.*+)";
    private static transient Logger logger = LoggerFactory.getLogger(HBaseRelation.class);
    private static String expressionRegex = "\\[(.*?)\\]";
    private final String HBASE_ZK_PORT_KEY = "hbase.zookeeper.property.clientPort";
    private final String HBASE_ZK_PORT_VALUE = "zkPort";
    private final String HBASE_ZK_QUORUM_KEY = "hbase.zookeeper.quorum";
    private final String HBASE_ZK_QUORUM_VALUE = "zkHosts";
    private final String HBASE_ZK_PARENT_KEY = "zookeeper.znode.parent";
    private final String HBASE_ZK_PARENT_VALUE = "/hbase-unsecure";
    private final String HBASE_TABLE = "hbaseTable";
    private final String HBASE_TABLE_FAMILY = "family";
    private final String sperator = ",";
    private final String HBASE_TABLE_SELECT_FIELDS = "selectFields";
    private final String HBASE_TABLE_WHERE_FIELDS = "whereFields";
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
            String selectFieldsStr = options.get(HBASE_TABLE_SELECT_FIELDS);
            String[] fieldStrs = selectFieldsStr.split(sperator);
            Stream.of(fieldStrs)
                    .forEach(field -> fields.add(DataTypes.createStructField(field, DataTypes.StringType, true)));
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
            String whereFieldsStr = options.get(HBASE_TABLE_WHERE_FIELDS);
            Scan scan = new Scan();
            if (!StringUtils.isEmpty(whereFieldsStr)) {
                FilterList filterList = new FilterList();
                if (whereFieldsStr.contains(sperator)) {
                    String[] whereFields = whereFieldsStr.split(sperator);
                    for (String whereField : whereFields) {
                        String[] condition = getCondition(whereField);
                        if (null != condition && condition.length == 3) {
                            String qualifier = condition[0];
                            CompareOp compareOp = getValueFilterOp(condition[1]);
                            String value = condition[2];
                            filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), compareOp, Bytes.toBytes(value)));
                        }
                    }
                } else {
                    String[] condition = getCondition(whereFieldsStr);
                    if (null != condition && condition.length == 3) {
                        String qualifier = condition[0];
                        CompareOp compareOp = getValueFilterOp(condition[1]);
                        String value = condition[2];
                        filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), compareOp, Bytes.toBytes(value)));
                    }
                }
                if (filterList.getFilters().size() > 0) {
                    scan.setFilter(filterList);
                }
            }
            String table = options.get(HBASE_TABLE);
            conf.set(TableInputFormat.INPUT_TABLE, table);
            ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
            String scanStr = Base64.encodeBytes(proto.toByteArray());
            conf.set(TableInputFormat.SCAN, scanStr);
            logger.info("==== HBaseSource Scan is:{} ====", scanStr);

            RDD<Tuple2<ImmutableBytesWritable, Result>> hbaseRdd = sqlContext.sparkContext().newAPIHadoopRDD(conf,
                    TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

            logger.info("==== HBase Table:{} Record is:{} ====", table, hbaseRdd.count());

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

    /**
     * new String[3]{qualifier, compareOp, value}
     *
     * @param whereField
     * @return
     */
    private String[] getCondition(String whereField) {
        String[] condition = null;
        Pattern pattern = Pattern.compile(fullRegex);
        Matcher matcher = pattern.matcher(whereField);
        if (matcher.find()) {
            System.out.println("==== " + matcher.group(0));
            System.out.println("==== " + matcher.group(1));
            System.out.println("==== " + matcher.group(2));
            System.out.println("==== " + matcher.group(3));
            if (matcher.groupCount() == 3) {
                condition = new String[3];
                condition[0] = matcher.group(1);
                condition[1] = matcher.group(2);
                condition[2] = matcher.group(3);
            }
        }
        return condition;
    }

    /**
     * 获取表达式，不区分大小写
     * 例如："utime[GT]20190101",提取的内容为GT
     * EQ = EQUAL等于
     * NE = NOT EQUAL不等于
     * GT = GREATER THAN大于
     * LT = LESS THAN小于
     * GE = GREATER THAN OR EQUAL 大于等于
     * LE = LESS THAN OR EQUAL 小于等于
     */
    @SuppressWarnings("unused")
    private String getExpression(String whereField) {
        Pattern pattern = Pattern.compile(expressionRegex);
        Matcher matcher = pattern.matcher(whereField);
        while (matcher.find()) {
            String expression = matcher.group(1);
            return expression;
        }
        return null;
    }

    /**
     * 获取表达式对应的ValueFilter操作符
     *
     * @param expression
     * @return
     */
    private CompareOp getValueFilterOp(String expression) {
        final String lcExpression = expression.toLowerCase();
        if (lcExpression.equals("eq")) {
            return CompareOp.EQUAL;
        }
        if (lcExpression.equals("ne")) {
            return CompareOp.NOT_EQUAL;
        }
        if (lcExpression.equals("gt")) {
            return CompareOp.GREATER;
        }
        if (lcExpression.equals("lt")) {
            return CompareOp.LESS;
        }
        if (lcExpression.equals("ge")) {
            return CompareOp.GREATER_OR_EQUAL;
        }
        if (lcExpression.equals("le")) {
            return CompareOp.LESS_OR_EQUAL;
        }
        return CompareOp.NO_OP;
    }
}