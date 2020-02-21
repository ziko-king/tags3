package cn.itcast.model.models.ml.customervalue;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by mengyao
 * 2019年6月2日
 */
public class RFMBean {
    private String userId;        // 用户ID
    private int recency;        // 最近一次购买时间
    private int frequency;        // 购买次数
    private double monetary;    // 总金额

    public RFMBean() {
        super();
    }

    public RFMBean(String userId, String recency, int frequency, long monetary) {
        super();
        this.userId = userId;
        this.recency = dayDiff(recency, "yyyyMMdd");
        this.frequency = frequency;
        this.monetary = monetary;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRecency() {
        return recency;
    }

    public void setRecency(int recency) {
        this.recency = recency;
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

    int dayDiff(String yesterdayStr, String pattern) {
        try {
            long yesterday = new SimpleDateFormat(pattern).parse(yesterdayStr).getTime();
            long today = System.currentTimeMillis();
            return (int) ((today - yesterday) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String toString() {
        return userId + "\t" + recency + "\t" + frequency + "\t" + monetary;
    }
}