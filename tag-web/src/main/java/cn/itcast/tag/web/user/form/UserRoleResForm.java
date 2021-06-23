/**
 * 项目名称：mengyao
 * 创建日期：2018年6月13日
 * 修改历史：
 * 1、[2018年6月13日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;

/**
 * @author zhaocs
 *
 */
public class UserRoleResForm implements Serializable {
    private static final long serialVersionUID = 7867152844637458347L;
    private long userId;
    private long roleId;
    private long resourceId;

    public UserRoleResForm(long userId, long roleId, long resourceId) {
        super();
        this.userId = userId;
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return "UserRoleResForm [userId=" + userId + ", roleId=" + roleId + ", resourceId=" + resourceId + "]";
    }

}
