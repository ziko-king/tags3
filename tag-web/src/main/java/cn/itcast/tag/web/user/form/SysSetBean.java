/**
 * 项目名称：mengyao
 * 创建日期：2018年6月10日
 * 修改历史：
 * 1、[2018年6月10日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;

/**
 * @author zhaocs
 *
 */
public class SysSetBean implements Serializable {
    private static final long serialVersionUID = -7021158677923258800L;

    private Long userId;

    private String name;

    private String userName;

    private String password;

    private String email;

    private String phone;

    private String roleName;

    private int state;

    private Long roleId;

    private Long groupId;

    public SysSetBean() {
        super();
    }

    public SysSetBean(Long userId, String name, String userName, String email, String phone) {
        super();
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
