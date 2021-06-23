package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaocs
 */
public class RoleResMapBean implements Serializable {
    private static final long serialVersionUID = 3360528320562912483L;

    private Long id;

    private Long roleId;

    private Long resId;

    private String permIds;

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    public RoleResMapBean() {
        super();
    }

    public RoleResMapBean(Long roleId) {
        super();
        this.roleId = roleId;
    }

    public RoleResMapBean(Long roleId, Long resId, String permIds, int state, Date ctime, Date utime) {
        super();
        this.roleId = roleId;
        this.resId = resId;
        this.permIds = permIds;
        this.state = state;
        this.ctime = ctime;
        this.utime = utime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
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

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    @Override
    public String toString() {
        return "RoleResMapBean [id=" + id + ", roleId=" + roleId + ", resId=" + resId + ", permIds=" + permIds
                + ", state=" + state + ", ctime=" + ctime + ", utime=" + utime + ", remark=" + remark + "]";
    }

}
