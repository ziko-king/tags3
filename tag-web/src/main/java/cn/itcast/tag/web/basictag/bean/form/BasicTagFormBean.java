package cn.itcast.tag.web.basictag.bean.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基础标签DTO
 *
 * @author zhangwenguo
 */
public class BasicTagFormBean implements Serializable {

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
    private boolean flag;

    //拥有该标签用户数量
    private long userCount;
    private String conditions;

    private List<BasicTagFormBean> subTags;

    public BasicTagFormBean() {
        super();
    }

    public BasicTagFormBean(long id, String name, String industry, String rule, String business, int level, long pid,
                            Date ctime, Date utime, int state, String remark, boolean flag, String conditions, List<BasicTagFormBean> subTags) {
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
        this.flag = flag;
        this.conditions = conditions;
        this.subTags = subTags;
    }

    public void set(String name, long pid, String business, String industry, String rule, int level) {
        this.name = name;
        this.pid = pid;
        this.business = business;
        this.industry = industry;
        this.rule = rule;
        this.level = level;
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

    public List<BasicTagFormBean> getSubTags() {
        return subTags;
    }

    public void setSubTags(List<BasicTagFormBean> subTags) {
        this.subTags = subTags;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "BasicTagFormBean [id=" + id + ", name=" + name + ", industry=" + industry + ", rule=" + rule
                + ", business=" + business + ", level=" + level + ", pid=" + pid + ", ctime=" + ctime + ", utime="
                + utime + ", state=" + state + ", remark=" + remark + ", flag=" + flag + ", userCount=" + userCount
                + ",conditions=" + conditions + ", subTags=" + subTags + "]";
    }

}
