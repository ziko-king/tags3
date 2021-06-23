/**
 * 项目名称：mengyao
 * 创建日期：2018年5月30日
 * 修改历史：
 * 1、[2018年5月30日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaocs
 * 用户组织关联实体类
 */
public class UserOrgMapBean implements Serializable {

    private static final long serialVersionUID = 4908960568808780149L;

    private Long id;

    private Long userId;

    private Long orgId;

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    public UserOrgMapBean() {
        super();
    }

    public UserOrgMapBean(Long orgId) {
        super();
        this.orgId = orgId;
    }

    public UserOrgMapBean(Long userId, Long orgId, String remark) {
        super();
        this.userId = userId;
        this.orgId = orgId;
        this.remark = remark;
        this.state = 1;
    }

    public UserOrgMapBean(Long userId, Long orgId, int state, Date ctime, Date utime, String remark) {
        super();
        this.userId = userId;
        this.orgId = orgId;
        this.state = state;
        this.ctime = ctime;
        this.utime = utime;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "UserOrgMapBean [id=" + id + ", userId=" + userId + ", orgId=" + orgId + ", state=" + state + ", ctime="
                + ctime + ", utime=" + utime + ", remark=" + remark + "]";
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

}
