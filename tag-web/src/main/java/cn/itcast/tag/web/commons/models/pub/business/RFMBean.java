package cn.itcast.tag.web.commons.models.pub.business;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mengyao
 */
public class RFMBean implements Serializable {
    private static final long serialVersionUID = 1068911057245126755L;
    private static final ThreadLocal<SimpleDateFormat> FORMATTER_YYYYMMMDDHHMMSS = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));
    private String userId;// 用户ID
    private String recency;// 最近一次购买时间
    private int frequency;// 购买次数
    private double monetary;// 总金额

    public RFMBean() {
        super();
    }

    public RFMBean(String userId, String recency, int frequency, double monetary) {
        super();
        this.userId = userId;
        this.recency = recency;
        this.frequency = frequency;
        this.monetary = monetary;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecency() {
        return recency;
    }

    public void setRecency(String recency) {
        this.recency = recency;
    }

    public long getRecencyTime() {
        try {
            Date parse = FORMATTER_YYYYMMMDDHHMMSS.get().parse(recency);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public double getMonetary() {
        return monetary;
    }

    public void setMonetary(double monetary) {
        this.monetary = monetary;
    }

    @Override
    public String toString() {
        return userId + "\t" + recency + "\t" + frequency + "\t" + monetary;
    }
}