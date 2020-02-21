package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaocs
 */
public class UserRoleMapBean implements Serializable {
    private static final long serialVersionUID = 2364129047181913216L;

    private Long id;

    private Long userId;

    private Long roleId;

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    public UserRoleMapBean() {
        super();
    }

    public UserRoleMapBean(Long roleId) {
        super();
        this.roleId = roleId;
    }

    public UserRoleMapBean(Long userId, Long roleId) {
        super();
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRoleMapBean(Long userId, Long roleId, int state) {
        super();
        this.userId = userId;
        this.roleId = roleId;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return "UserRoleMapBean [id=" + id + ", userId=" + userId + ", roleId=" + roleId + ", state=" + state
                + ", ctime=" + ctime + ", utime=" + utime + ", remark=" + remark + "]";
    }
}