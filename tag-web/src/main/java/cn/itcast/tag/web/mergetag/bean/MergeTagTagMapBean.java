package cn.itcast.tag.web.mergetag.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 组合标签与基础标签DTO
 *
 * @author zhangwenguo
 */
public class MergeTagTagMapBean implements Serializable {
    private static final long serialVersionUID = 2550843734407871489L;

    private long id;
    // 组合标签ID
    private long mergeTagId;
    // 基础标签ID
    private long basicTagId;
    // 关联条件
    private String conditions;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 备注
    private String remark;

    public MergeTagTagMapBean() {
        super();
    }

    public MergeTagTagMapBean(long id, long mergeTagId, long basicTagId,
                              Date ctime, Date utime, String remark) {
        super();
        this.id = id;
        this.mergeTagId = mergeTagId;
        this.basicTagId = basicTagId;
        this.ctime = ctime;
        this.utime = utime;
        this.remark = remark;
    }

    public void set(long mergeTagId, long basicTagId, String remark) {
        this.mergeTagId = mergeTagId;
        this.basicTagId = basicTagId;
        this.remark = remark;
    }

    public void set(long mergeTagId, long basicTagId, String conditions, Date ctime, Date utime, String remark) {
        this.mergeTagId = mergeTagId;
        this.basicTagId = basicTagId;
        this.conditions = conditions;
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

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
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
        return id + "\t" + mergeTagId + "\t" + basicTagId + "\t" + ctime
                + "\t" + utime + "\t" + remark;
    }
}
