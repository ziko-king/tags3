package cn.itcast.model.beans;

import java.io.Serializable;

/**
 * 基础标签DTO
 * Created by mengyao
 * 2019年6月3日
 */
public class BasicTagBean extends Tag implements Serializable {

    private static final long serialVersionUID = -3612176209672300279L;
    private long id;
    private String name;    //标签名称
    private String industry;//行业、子行业、业务类型、标签、属性
    private String rule;    //标签规则
    private String business;//业务描述
    private int level;        //标签等级
    private long pid;        //父标签ID
    private String ctime;    //创建时间
    private String utime;    //修改时间
    private int state;        //状态：1申请中、2开发中、3开发完成、4已上线、5已下线、6已禁用
    private String remark;    //备注

    public BasicTagBean() {
        super();
    }

    public BasicTagBean(long id, String name, String industry, String rule, String business, int level, long pid,
                        String ctime, String utime, int state, String remark) {
        super();
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.rule = rule;
        this.business = business;
        this.level = level;
        this.pid = pid;
        this.ctime = ctime;
        this.utime = utime;
        this.state = state;
        this.remark = remark;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + industry + "\t" + rule + "\t" + business + "\t" + level + "\t" + pid + "\t"
                + ctime + "\t" + utime + "\t" + state + "\t" + remark;
    }

}
