package cn.itcast.tag.web.commons.models.pub.ml;

import org.apache.spark.ml.feature.Normalizer;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;

// $example on$

/**
 * 基于L^1和L^\infty的向量标准化处理
 *
 * @author mengyao
 */
public class NormalizerExec_ {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("NormalizerExec_")
                .getOrCreate();

        List<Row> data = Arrays.asList(RowFactory.create(0, Vectors.dense(1.0, 0.1, -8.0)),
                RowFactory.create(1, Vectors.dense(2.0, 1.0, -4.0)),
                RowFactory.create(2, Vectors.dense(4.0, 10.0, 8.0)));
        StructType schema = new StructType(
                new StructField[]{new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                        new StructField("features", new VectorUDT(), false, Metadata.empty())
                });
        Dataset<Row> dataFrame = spark.createDataFrame(data, schema);

        // 使用 L^1对每个Vector向量进行规范化
        Normalizer normalizer = new Normalizer().setInputCol("features").setOutputCol("normFeatures").setP(1.0);

        Dataset<Row> l1NormData = normalizer.transform(dataFrame);
        l1NormData.show();

        // 使用L^\infty对每个Vector向量进行规范化
        Dataset<Row> lInfNormData = normalizer.transform(dataFrame, normalizer.p().w(Double.POSITIVE_INFINITY));
        lInfNormData.show();

        spark.stop();
    }
}
