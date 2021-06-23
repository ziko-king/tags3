package cn.itcast.tag.web.mergetag.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 组合标签、基础标签与用户关联DTO
 *
 * @author zhangwenguo
 */
public class UserMergeTagMapBean implements Serializable {
    private static final long serialVersionUID = 2550843734407871489L;

    private long id;
    // 组合标签ID
    private long mergeTagId;
    // userId
    private long userId;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    private int state;
    // 备注
    private String remark;

    public UserMergeTagMapBean() {
        super();
    }

    public UserMergeTagMapBean(long id, long mergeTagId,
                               long userId, Date ctime, Date utime, int state, String remark) {
        super();
        this.id = id;
        this.mergeTagId = mergeTagId;
        this.userId = userId;
        this.ctime = ctime;
        this.utime = utime;
        this.state = state;
        this.remark = remark;
    }

    public void set(long mergeTagId,
                    long userId, String remark) {
        this.mergeTagId = mergeTagId;
        this.userId = userId;
        this.remark = remark;
    }

    public void set(long mergeTagId,
                    long userId, Date ctime, Date utime, int state, String remark) {
        this.mergeTagId = mergeTagId;
        this.userId = userId;
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

    public long getMergeTagId() {
        return mergeTagId;
    }

    public void setMergeTagId(long mergeTagId) {
        this.mergeTagId = mergeTagId;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return id + "\t" + mergeTagId + "\t" + userId + "\t" + ctime
                + "\t" + utime + "\t" + remark;
    }
}
