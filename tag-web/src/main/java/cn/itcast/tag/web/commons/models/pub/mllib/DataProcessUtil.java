package cn.itcast.tag.web.commons.models.pub.mllib;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.Vector;

/**
 * @author mengyao
 */
public class DataProcessUtil {

    /**
     * 数据标准化处理
     *
     * @param data
     * @return
     */
    public static JavaRDD<Vector> normalizer(JavaRDD<Vector> data) {
        JavaRDD<Vector> transform = new Normalizer(1.0).transform(data);
        transform.foreachPartition(itr -> {
            while (itr.hasNext()) {
                Vector next = itr.next();
                //System.out.println("==== "+next.toJson());
            }
        });
        return transform;
    }

}
