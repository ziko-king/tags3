package cn.itcast.tag.web.basictag.bean.form;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础标签DTO
 *
 * @author zhangwenguo
 */
public class UserTagFormBean implements Serializable {

    private static final long serialVersionUID = -4512403076981696429L;

    private long id;
    // 用户ID
    private long userId;
    // 标签ID
    private long tagId;
    // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
    private int state;
    // 操作权限
    private String perm_ids;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 备注
    private String remark;

    public UserTagFormBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UserTagFormBean(long id, long userId, long tagId, int state, String perm_ids,
                           Date ctime, Date utime, String remark) {
        super();
        this.id = id;
        this.userId = userId;
        this.tagId = tagId;
        this.state = state;
        this.perm_ids = perm_ids;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getPerm_ids() {
        return perm_ids;
    }

    public void setPerm_ids(String perm_ids) {
        this.perm_ids = perm_ids;
    }

    @Override
    public String toString() {
        return id + "\t" + userId + "\t" + tagId + "\t" + state + "\t" + perm_ids + "\t"
                + ctime + "\t" + utime + "\t" + remark;
    }

}
