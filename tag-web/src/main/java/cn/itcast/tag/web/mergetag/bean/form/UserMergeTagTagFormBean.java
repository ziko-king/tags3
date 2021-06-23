package cn.itcast.tag.web.mergetag.bean.form;

import cn.itcast.tag.web.basictag.bean.form.BasicTagFormBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 组合标签与用户关联DTO
 *
 * @author zhangwenguo
 */
public class UserMergeTagTagFormBean implements Serializable {
    private static final long serialVersionUID = 2550843734407871489L;

    // 组合标签ID
    private long mergeTagId;
    // 基础标签ID
    private long basicTagId;
    // 拼接的集成标签ID
    private String listBasicTagId;
    // userId
    private long userId;
    // pid
    private long pid;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 组合标签名称
    private String mergeTagName;
    // 基础标签名称
    private String basicTagName;
    // 组合标签条件
    private String condition;
    private String conditions;
    // 组合标签含义
    private String intro;
    // 组合标签用途
    private String purpose;
    // 状态
    private int state;
    // 基础标签层级
    private int baisicLevel;
    // 备注
    private String remark;

    private List<BasicTagFormBean> subTags;

    public UserMergeTagTagFormBean() {
        super();
    }

    public UserMergeTagTagFormBean(long mergeTagId, long basicTagId, String listBasicTagId,
                                   Long userId, Long pid, Date ctime, Date utime, String mergeTagName, String condition,
                                   String conditions, String intro, String purpose, int state, int baisicLevel, String remark) {
        super();
        this.mergeTagId = mergeTagId;
        this.basicTagId = basicTagId;
        this.listBasicTagId = listBasicTagId;
        this.userId = userId;
        this.userId = userId;
        this.ctime = ctime;
        this.utime = utime;
        this.mergeTagName = mergeTagName;
        this.condition = condition;
        this.conditions = conditions;
        this.intro = intro;
        this.purpose = purpose;
        this.state = state;
        this.baisicLevel = baisicLevel;
        this.remark = remark;
    }

    public void set(long mergeTagId, Long basicTagId, String remark) {
        this.mergeTagId = mergeTagId;
        this.basicTagId = basicTagId;
        this.remark = remark;
    }

    public long getMergeTagId() {
        return mergeTagId;
    }

    public void setMergeTagId(long mergeTagId) {
        this.mergeTagId = mergeTagId;
    }

    public long getBasicTagId() {
        return basicTagId;
    }

    public void setBasicTagId(long basicTagId) {
        this.basicTagId = basicTagId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMergeTagName() {
        return mergeTagName;
    }

    public void setMergeTagName(String mergeTagName) {
        this.mergeTagName = mergeTagName;
    }

    public String getBasicTagName() {
        return basicTagName;
    }

    public void setBasicTagName(String basicTagName) {
        this.basicTagName = basicTagName;
    }

    public String getListBasicTagId() {
        return listBasicTagId;
    }

    public void setListBasicTagId(String listBasicTagId) {
        this.listBasicTagId = listBasicTagId;
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

    public int getBaisicLevel() {
        return baisicLevel;
    }

    public void setBaisicLevel(int baisicLevel) {
        this.baisicLevel = baisicLevel;
    }

    @Override
    public String toString() {
        return mergeTagId + "\t" + basicTagId + "\t" +
                userId + "\t" + ctime + "\t" + utime + "\t" + remark;
    }
}
