package cn.itcast.tag.web.mergetag.bean.form;

import java.io.Serializable;
import java.util.Date;

/**
 * 组合标签DTO
 *
 * @author mengyao
 */
public class MergeTagTagFormBean implements Serializable {

    private static final long serialVersionUID = 2550843734407871489L;

    private long id;
    // 组合标签名称
    private String name;
    // 组合标签条件
    private String condition;
    // 组合标签含义
    private String intro;
    // 组合用途
    private String purpose;
    // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
    private int state;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 备注
    private String remark;

    public MergeTagTagFormBean() {
        super();
    }

    public MergeTagTagFormBean(long id, String name, String condition, String intro, String purpose,
                               int state, Date ctime, Date utime, String remark) {
        super();
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.intro = intro;
        this.purpose = purpose;
        this.state = state;
        this.ctime = ctime;
        this.utime = utime;
        this.remark = remark;
    }

    public void set(int state) {
        this.state = state;
    }

    public void set(String name, String condition, String intro, String purpose, int state,
                    Date ctime, Date utime, String remark) {
        this.name = name;
        this.condition = condition;
        this.intro = intro;
        this.purpose = purpose;
        this.state = state;
        this.ctime = ctime;
        this.utime = utime;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + condition + "\t" + intro + "\t" + purpose + "\t" + state + "\t" + ctime
                + "\t" + utime + "\t" + remark;
    }

}
