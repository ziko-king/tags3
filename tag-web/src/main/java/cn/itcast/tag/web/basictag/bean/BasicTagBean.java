package cn.itcast.tag.web.basictag.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基础标签DTO
 *
 * @author zhangwenguo
 */
public class BasicTagBean implements Serializable {

    private static final long serialVersionUID = -4512403076981696429L;

    private long id;
    // 标签名称
    private String name;
    // 行业、子行业、业务类型、标签、属性
    private String industry;
    // 标签规则
    private String rule;
    // 业务描述
    private String business;
    // 标签等级
    private int level;
    // 父标签ID
    private long pid;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
    private int state;
    // 备注
    private String remark;

    private List<BasicTagBean> subTags;

    //拥有该标签用户数量
    private long userCount;


    public BasicTagBean() {
        super();
    }

    public BasicTagBean(long id, String name, String industry, String rule, String business, int level, long pid,
                        Date ctime, Date utime, int state, String remark, List<BasicTagBean> subTags) {
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
        this.subTags = subTags;
    }

    public void set(String name, long pid, String business, String industry, String rule, int level, int state) {
        this.name = name;
        this.pid = pid;
        this.business = business;
        this.industry = industry;
        this.rule = rule;
        this.level = level;
        this.state = state;
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

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
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

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public List<BasicTagBean> getSubTags() {
        return subTags;
    }

    public void setSubTags(List<BasicTagBean> subTags) {
        this.subTags = subTags;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    @Override
    public String toString() {
        return "BasicTagBean [id=" + id + ", name=" + name + ", industry=" + industry + ", rule=" + rule + ", business="
                + business + ", level=" + level + ", pid=" + pid + ", ctime=" + ctime + ", utime=" + utime + ", state="
                + state + ", remark=" + remark + ", subTags=" + subTags + "]";
    }

}
