package cn.itcast.tag.web.basictag.bean.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BasicModelRuleFormBean implements Serializable {

    private static final long serialVersionUID = -4512403076981696429L;

    private long tagId;
    private String tagName;
    private long threeTagId;
    private String business;
    private String rule;
    private String industry;
    private String modelPath;
    private String modelMain;
    private String modelName;
    private String args;
    private String scheTime;
    private long pid;
    private int level;
    private int state;
    private Date ctime;
    private Date utime;
    private String remark;
    private List<BasicModelRuleFormBean> subTags;
    //拥有该标签用户数量
    private long userCount;

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getThreeTagId() {
        return threeTagId;
    }

    public void setThreeTagId(long threeTagId) {
        this.threeTagId = threeTagId;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getModelMian() {
        return modelMain;
    }

    public String getModelMain() {
        return modelMain;
    }

    public void setModelMain(String modelMain) {
        this.modelMain = modelMain;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getScheTime() {
        return scheTime;
    }

    public void setScheTime(String scheTime) {
        this.scheTime = scheTime;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BasicModelRuleFormBean> getSubTags() {
        return subTags;
    }

    public void setSubTags(List<BasicModelRuleFormBean> subTags) {
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
        return tagId + "\t" + tagName + "\t" + threeTagId + "\t" + business + "\t" + rule + "\t" + industry + "\t"
                + modelPath + "\t" + modelMain + "\t" + modelName + "\t" + args + "\t" + scheTime + "\t" + level + "\t" + pid
                + "\t" + state + "\t" + ctime + "\t" + utime + "\t" + remark;
    }


}
