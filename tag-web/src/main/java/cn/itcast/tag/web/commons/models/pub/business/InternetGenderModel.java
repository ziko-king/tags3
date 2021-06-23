package cn.itcast.tag.web.commons.models.pub.business;

import cn.itcast.tag.web.commons.models.AbstractModel;
import cn.itcast.tag.web.engine.bean.MetaDataBean;
import net.iharder.base64.Base64;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

/**
 * InternetGender（互联网性别模型）
 *
 * @author mengyao
 */
public class InternetGenderModel extends AbstractModel {

    /**
     *
     */
    private static final long serialVersionUID = -5517001419907203298L;

    private SparkConf conf;
    private JavaSparkContext jsc;
    private String dataDir;

    public static void main(String[] args) {


    }

    @Override
    public void configure() {
        conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("Tag1554Model");
        jsc = new JavaSparkContext(conf);
    }

    @Override
    public void input(MetaDataBean meta) {
        try {
            System.out.println(meta.getInType());
            String hbaseTable = meta.getHbaseTable();
            String family = meta.getFamily();
            String selectFieldNames = meta.getSelectFieldNames();
            String whereFieldNames = meta.getWhereFieldNames();
            String whereFieldValues = meta.getWhereFieldValues();

            Scan scan = new Scan()
                    .addFamily(Bytes.toBytes(family));
            String[] fields = selectFieldNames.split(",");
            if (fields.length > 0) {
                for (String field : fields) {
                    scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(field));
                }
            }

            Configuration hadoopConf = HBaseConfiguration.create();
            hadoopConf.set(TableInputFormat.INPUT_TABLE, hbaseTable);
            hadoopConf.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray()));

            JavaPairRDD<ImmutableBytesWritable, Result> hbaseRDD = jsc.newAPIHadoopRDD(hadoopConf,
                    TableInputFormat.class, ImmutableBytesWritable.class,
                    Result.class)
                    .coalesce(1);

            hbaseRDD.map(r -> {
                return Vectors.dense(
                        Bytes.toDouble(r._2.getValue(family.getBytes(), "userId".getBytes())),
                        Bytes.toDouble(r._2.getValue(family.getBytes(), "productId".getBytes())),
                        Bytes.toDouble(r._2.getValue(family.getBytes(), "productType".getBytes())),
                        Bytes.toDouble(r._2.getValue(family.getBytes(), "orderAmount".getBytes())),
                        Bytes.toDouble(r._2.getValue(family.getBytes(), "orderId".getBytes()))
                );
            })
                    .persist(StorageLevel.MEMORY_AND_DISK())
                    .saveAsTextFile(dataDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare() {
        SparkSession session = SparkSession.builder()
                .appName("InternetGenderModel")
                .getOrCreate();
        Dataset<Row> dataset = session.read().format("libsvm").load(dataDir);
    }

    @Override
    public void train(String modelPath) {
        // TODO Auto-generated method stub

    }

    @Override
    public void compute(String modelPath, String resPath) {
        // TODO Auto-generated method stub

    }

}
