package cn.itcast.tag.web.engine.bean;

import cn.itcast.tag.web.commons.bean.Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则DTO
 *
 * @author mengyao
 */
public class RuleBean extends Bean implements Serializable {
    private static final long serialVersionUID = -5382484644791401080L;

    private long id;
    // 标签ID
    private long tagId;
    // 标签类型：1基础标签、2组合标签
    private int type;
    // 标签规则
    private String rule;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
    private int state;
    // 备注
    private String remark;

    public RuleBean() {
        super();
    }

    public RuleBean(long tagId, int type, String rule, int state, String remark) {
        super();
        this.tagId = tagId;
        this.type = type;
        this.rule = rule;
        this.state = state;
        this.remark = remark;
    }

    public RuleBean(long id, long tagId, int type, String rule, Date ctime, Date utime, int state, String remark) {
        super();
        this.id = id;
        this.tagId = tagId;
        this.type = type;
        this.rule = rule;
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

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
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

    @Override
    public String toString() {
        return id + "\t" + tagId + "\t" + type + "\t" + rule + "\t" + ctime + "\t" + utime + "\t" + state + "\t"
                + remark;
    }

}
