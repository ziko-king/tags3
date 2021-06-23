/**
 * 项目名称：mengyao
 * 创建日期：2018年6月4日
 * 修改历史：
 * 1、[2018年6月4日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaocs
 * 角色数据关联实体类（基础标签和组合标签）
 */
public class RoleDataMapBean implements Serializable {
    private static final long serialVersionUID = -6256128473072645177L;

    private Long id;

    private Long roleId;

    private Long tagId;

    private int tagType;    // 1基础标签；2组合标签

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    public RoleDataMapBean() {
        super();
    }

    public RoleDataMapBean(Long roleId) {
        super();
        this.roleId = roleId;
    }

    public RoleDataMapBean(Long roleId, Long tagId, int tagType, int state, Date ctime, Date utime) {
        super();
        this.roleId = roleId;
        this.tagId = tagId;
        this.tagType = tagType;
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

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "RoleDataMapBean [id=" + id + ", roleId=" + roleId + ", tagId=" + tagId + ", tagType=" + tagType
                + ", state=" + state + ", ctime=" + ctime + ", utime=" + utime + ", remark=" + remark + "]";
    }

}
