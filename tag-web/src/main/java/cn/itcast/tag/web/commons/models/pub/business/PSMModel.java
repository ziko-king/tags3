package cn.itcast.tag.web.commons.models.pub.business;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 促销敏感度（PSM模型）
 *
 * @author mengyao
 */
public class PSMModel {

    private static final ThreadLocal<SimpleDateFormat> FORMATTER_YMDHMS = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));

    static List<PSMBean> exampleData = Arrays.asList(
            new PSMBean("1", "20190511", 199.49, 0.00, 199.49),
            new PSMBean("3", "20190501", 158.00, 4.34, 153.66),
            new PSMBean("2", "20190520", 649.00, 60.00, 589.00),
            new PSMBean("4", "20190517", 1399.00, 420.50, 978.50),
            new PSMBean("4", "20190515", 60.99, 5.00, 55.99),
            new PSMBean("1", "20190512", 499.00, 5.00, 494.00),
            new PSMBean("2", "20190522", 104.80, 23.00, 81.8),
            new PSMBean("1", "20190513", 376.00, 42.95, 333.05),
            new PSMBean("3", "20190502", 1099.00, 150.00, 949.00),
            new PSMBean("3", "20190503", 1099.00, 0.00, 1099.00)
    );

    public static void main(String[] args) {
        //
        System.setProperty("hadoop.home.dir", "D:\\softs\\developer\\apache\\hadoop-2.6.5");

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("RFM")
                .set("spark.kryoserializer.buffer", "64k")
                .set("spark.kryoserializer.buffer.max", "64m")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .registerKryoClasses(new Class[]{PSMBean.class});
        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.sc().setLogLevel("ERROR");

        JavaRDD<PSMBean> inputRDD = jsc.parallelize(exampleData);

        //PSM模型
        JavaRDD<PSMBean> psmModelRDD = inputRDD
                .groupBy(b -> b.getUserId())
                .map(tup2 -> {
                    ArrayList<PSMBean> list = new ArrayList<PSMBean>();
                    tup2._2.forEach(b -> list.add(b));
                    return new PSMBean(
                            tup2._1,
                            (int) list.stream().filter(b -> b.getRa() > 0).count(), //应收订单数汇总
                            list.stream().filter(b -> b.getRa() > 0).mapToDouble(PSMBean::getRa).sum(), //应收金额汇总
                            (int) list.stream().filter(b -> b.getDa() > 0).count(), //优惠订单数汇总
                            list.stream().filter(b -> b.getDa() > 0).mapToDouble(PSMBean::getDa).sum()    //优惠金额汇总
                    ).computePSM();
                });

        //psmModelRDD;

        JavaRDD<Vector> psmModeVectorRDD = psmModelRDD
                .map(b -> Vectors.dense((b.getKmt())));

        //使用KMeans聚类算法，提供选择多个k值进行训练
        List<Integer> ks = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
        //计算每一个k值聚类的WSSSE值
        Map<Integer, Double> WSSSEs = new HashMap<Integer, Double>();
        //聚类迭代计算次数
        int numIterations = 20;
        //根据每一个k值聚类训练
        ks.forEach(k -> {
            KMeansModel clusters = KMeans.train(psmModeVectorRDD.rdd(), k, numIterations);
            // 通过计算平方误差的集合和来评估聚类
            double WSSSE = clusters.computeCost(psmModeVectorRDD.rdd());
            WSSSEs.put(k, WSSSE);
        });

        //找出最优k值
        Entry<Integer, Double> optimize = WSSSEs.entrySet().stream().min((i1, i2) -> i1.getValue().compareTo(i2.getValue())).get();

        //使用最优k值聚类
        KMeansModel clusters = KMeans.train(psmModeVectorRDD.rdd(), optimize.getKey(), numIterations);
        System.out.println("类中心:");
        for (Vector center : clusters.clusterCenters()) {
            System.out.println(" " + center);
        }

        //clusters.predict(points);

        //保存最优k值的模型
//    	String path = "target/KMeans/KMeansModel/"+FORMATTER_YMDHMS.get().format(new Date());
//		clusters.save(jsc.sc(), path);
//    	KMeansModel sameModel = KMeansModel.load(jsc.sc(), path);

        jsc.stop();
    }

    static class PSMBean {
        // ==== 业务数据 ====
        private String userId;//用户ID
        private String ctime;//下单时间
        private double ra;//应收金额receivableAmount
        private double da;//优惠金额discountAmount
        private double aa;//实收金额actualAmount
        private int otn;//订单总数orderTotalNum
        private double rat;//应收金额汇总receivableAmountTotal
        private int dotn;//优惠订单总数discountOrderTotalNum
        private double dat;//优惠金额汇总discountAmountTotal
        // ==== PSM 模型参数====
        private double dor;//优惠订单占比discountOrderRatio = 优惠订单总数dotn / 订单总数otn
        private double odr;//平均每单优惠金额占比oneDiscountRatio = (优惠金额汇总dat / 优惠订单总数dotn) / (应收金额汇总rat / 订单总数otn)
        private double dara;//优惠金额与总金额占比 = dat / rat
        // KMeans聚类使用
        private double kmt;

        public PSMBean() {
        }

        public PSMBean(String userId, String ctime, double ra, double da, double aa) {
            this.userId = userId;
            this.ctime = ctime;
            this.ra = ra;
            this.da = da;
            this.aa = aa;
        }

        public PSMBean(String userId, int otn, double rat, int dotn, double dat) {
            this.userId = userId;
            this.otn = otn;
            this.rat = rat;
            this.dotn = dotn;
            this.dat = dat;
        }

        // 计算PSM
        public PSMBean computePSM() {
            this.dor = (double) this.dotn / (double) this.otn;
            this.odr = (this.dat / this.dotn) / (this.rat / this.otn);
            this.dara = this.dat / this.rat;
            this.kmt = dor + odr + dara;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public double getRa() {
            return ra;
        }

        public void setRa(double ra) {
            this.ra = ra;
        }

        public double getDa() {
            return da;
        }

        public void setDa(double da) {
            this.da = da;
        }

        public double getAa() {
            return aa;
        }

        public void setAa(double aa) {
            this.aa = aa;
        }

        public int getOtn() {
            return otn;
        }

        public void setOtn(int otn) {
            this.otn = otn;
        }

        public double getRat() {
            return rat;
        }

        public void setRat(double rat) {
            this.rat = rat;
        }

        public int getDotn() {
            return dotn;
        }

        public void setDotn(int dotn) {
            this.dotn = dotn;
        }

        public double getDat() {
            return dat;
        }

        public void setDat(double dat) {
            this.dat = dat;
        }

        public double getDor() {
            return dor;
        }

        public void setDor(double dor) {
            this.dor = dor;
        }

        public double getOdr() {
            return odr;
        }

        public void setOdr(double odr) {
            this.odr = odr;
        }

        public double getDara() {
            return dara;
        }

        public void setDara(double dara) {
            this.dara = dara;
        }

        public double getKmt() {
            return kmt;
        }

        public void setKmt(double kmt) {
            this.kmt = kmt;
        }

        @Override
        public String toString() {
            return "PSMBean [userId=" + userId + ", ctime=" + ctime + ", ra=" + ra + ", da=" + da + ", aa=" + aa
                    + ", otn=" + otn + ", rat=" + rat + ", dotn=" + dotn + ", dat=" + dat + ", dor=" + dor + ", odr="
                    + odr + ", dara=" + dara + ", kmt=" + kmt + "]";
        }
    }

}
