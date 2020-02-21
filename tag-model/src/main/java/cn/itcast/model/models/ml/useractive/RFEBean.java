package cn.itcast.model.models.ml.useractive;

import cn.itcast.model.utils.DateUtil;

/**
 * 6忠诚	（1天内访问2次及以上，每次访问页面不重复）
 * 5活跃	（2天内访问至少1次）
 * 4回流	（3天内访问至少1次）
 * 3新增	（注册并访问）
 * 2不活跃	（7天内未访问）
 * 1流失	（7天以上无访问）
 * Created by mengyao
 * 2019年6月6日
 */
public class RFEBean {

    private String userId;        // 用户ID
    private int recency;        // 最新的页面访问时间与当前日期之间的间隔。当前日期与上次访问之间的间隔越短，R分值越大。
    private int frequency;        // 特定时期内的页面访问次数。客户访问公司页面的频率越高，F分值就越大。
    private int engagements;    // 参与度，客户生成的页面展示次数。页面展示次数越多，E分值越大。
    private double score;        // RFE评分

    public RFEBean() {
        super();
    }

    public RFEBean(String userId, String recency, int frequency, int engagements) {
        super();
        this.userId = userId;
        this.recency = DateUtil.dayDiff(recency);
        this.frequency = frequency;
        this.engagements = engagements;
    }

    // 计算RFE score
    public RFEBean computeScore() {
        double rScore = 0d;
        double fScore = 0d;
        double eScore = 0d;
        // 计算recency得分
        if (recency <= 1) {
            rScore = 6;
        }                        //1天以内
        if (recency >= 2 && recency <= 3) {
            rScore = 5;
        }        //2-3天以内
        if (recency >= 4 && recency <= 5) {
            rScore = 4;
        }        //4-5天以内
        if (recency >= 6 && recency <= 7) {
            rScore = 3;
        }        //6-7天以内
        if (recency >= 8 && recency <= 14) {
            rScore = 2;
        }    //8-14天以内
        if (recency >= 15) {
            rScore = 1;
        }                        //15天以上
        // 计算frequency得分
        if (frequency >= 10) {
            fScore = 6;
        }                    //访问10次及以上
        if (frequency < 10 && frequency >= 8) {
            fScore = 5;
        }    //访问8-9次
        if (frequency < 8 && frequency >= 6) {
            fScore = 4;
        }    //访问6-7次
        if (frequency < 6 && frequency >= 4) {
            fScore = 3;
        }    //访问4-5次
        if (frequency < 4 && frequency >= 2) {
            fScore = 2;
        }    //访问2-3次
        if (frequency < 2 && frequency >= 1) {
            fScore = 1;
        }    //访问1次
        // 计算frequency得分
        if (engagements >= 6) {
            eScore = 6;
        }                        //访问页面6次及以上
        if (engagements < 6 && engagements >= 5) {
            eScore = 5;
        }    //访问页面5次
        if (engagements < 5 && engagements >= 4) {
            eScore = 4;
        }    //访问页面4次
        if (engagements < 4 && engagements >= 3) {
            eScore = 3;
        }    //访问页面3次
        if (engagements < 3 && engagements >= 2) {
            eScore = 2;
        }    //访问页面2次
        if (engagements < 2 && engagements >= 1) {
            eScore = 1;
        }    //访问页面1次
        this.score = rScore + fScore + eScore;
        return this;
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

    public int getEngagements() {
        return engagements;
    }

    public void setEngagements(int engagements) {
        this.engagements = engagements;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return userId + "\t" + recency + "\t" + frequency + "\t" + engagements + "\t" + score;
    }

}
