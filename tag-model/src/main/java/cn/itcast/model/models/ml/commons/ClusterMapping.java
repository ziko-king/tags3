package cn.itcast.model.models.ml.commons;

/**
 * Created by mengyao
 * 2019年8月2日
 */
public class ClusterMapping {

    private int cluster;
    private double center;
    private double min;
    private double max;
    private long tagId;

    public ClusterMapping() {
        super();
    }

    public ClusterMapping(int cluster, double center) {
        super();
        this.cluster = cluster;
        this.center = center;
    }

    public ClusterMapping(int cluster, double center, double min, double max, long tagId) {
        super();
        this.cluster = cluster;
        this.center = center;
        this.min = min;
        this.max = max;
        this.tagId = tagId;
    }

    public void set(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public void set(double min, double max, long tagId) {
        this.min = min;
        this.max = max;
        this.tagId = tagId;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public Double getCenter() {
        return center;
    }

    public void setCenter(double center) {
        this.center = center;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return cluster + "\t" + center + "\t" + min + "\t" + max + "\t" + tagId;
    }
}
