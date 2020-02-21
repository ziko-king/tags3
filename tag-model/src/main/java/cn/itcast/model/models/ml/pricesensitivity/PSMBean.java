package cn.itcast.model.models.ml.pricesensitivity;

/**
 * Created by mengyao
 * 2019年6月6日
 */
public class PSMBean {
    // ==== 业务数据 ====
    private String userId;        // 用户ID
    private String ctime;        // 下单时间
    private double ra;            // 应收金额receivableAmount
    private double da;            // 优惠金额discountAmount
    private double aa;            // 实收金额actualAmount
    private int otn;            // 订单总数orderTotalNum
    private double rat;            // 应收金额汇总receivableAmountTotal
    private int dotn;            // 优惠订单总数discountOrderTotalNum
    private double dat;            // 优惠金额汇总discountAmountTotal
    // ==== PSM 模型参数====
    private double dor;            // 优惠订单占比discountOrderRatio = 优惠订单总数dotn / 订单总数otn
    private double odr;            // 平均每单优惠金额占比oneDiscountRatio = (优惠金额汇总dat / 优惠订单总数dotn) / (应收金额汇总rat / 订单总数otn)
    private double dara;        // 优惠金额与总金额占比 = dat / rat
    // KMeans聚类使用
    private double kmt;

    public PSMBean() {
    }

    /**
     * @param userId
     * @param ctime  下单时间
     * @param ra     应收金额
     * @param da     优惠金额
     * @param aa     实收金额
     */
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
    public PSMBean score() {
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
        return "PSMBean [userId=" + userId + ", ctime=" + ctime + ", ra=" + ra + ", da=" + da + ", aa=" + aa + ", otn="
                + otn + ", rat=" + rat + ", dotn=" + dotn + ", dat=" + dat + ", dor=" + dor + ", odr=" + odr + ", dara="
                + dara + ", kmt=" + kmt + "]";
    }
}