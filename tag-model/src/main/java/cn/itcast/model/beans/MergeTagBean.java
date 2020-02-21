package cn.itcast.model.beans;

import java.io.Serializable;

/**
 * 组合标签DTO
 * Created by mengyao
 * 2019年6月3日
 */
public class MergeTagBean extends Tag implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3943274262867026309L;
    private long id;
    private String name;        //组合标签名称
    private String condition;    //组合标签条件
    private String intro;        //组合标签含义
    private String purpose;        //组合用途
    private int state;            //状态：1申请中、2开发中、3开发完成、4已上线、5已下线、6已禁用
    private String ctime;        //创建时间
    private String utime;        //修改时间
    private String remark;        //备注

    public MergeTagBean() {
        super();
    }

    public MergeTagBean(long id, String name, String condition, String intro, String purpose, int state, String ctime,
                        String utime, String remark) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + condition + "\t" + intro + "\t" + purpose + "\t" + state + "\t" + ctime + "\t"
                + utime + "\t" + remark;
    }

}
