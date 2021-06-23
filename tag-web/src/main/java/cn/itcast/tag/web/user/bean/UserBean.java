package cn.itcast.tag.web.user.bean;

import cn.itcast.tag.web.commons.bean.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author mengyao
 */
public class UserBean extends Bean implements Serializable {

    private static final long serialVersionUID = -7979385162515039759L;

    private Long id;

    private String name;

    private String username;

    private String password;

    private String idcard;

    private String phone;

    private String email;

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    private List<UserRoleMapBean> roleMaps;

    private List<UserOrgMapBean> orgMaps;

    public UserBean() {
        super();
    }

    public UserBean(String username) {
        super();
        this.username = username;
    }

    public UserBean(Long id) {
        super();
        this.id = id;
    }

    public UserBean(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public UserBean(String name, String username, String phone, String email) {
        super();
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

    public UserBean(Long id, String name, String username, String phone) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
    }

    public UserBean(Long id, String name, String username, String phone, String email) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

    public List<UserOrgMapBean> getOrgMaps() {
        return orgMaps;
    }

    public void setOrgMaps(List<UserOrgMapBean> orgMaps) {
        this.orgMaps = orgMaps;
    }

    public List<UserRoleMapBean> getRoleMaps() {
        return roleMaps;
    }

    public void setRoleMaps(List<UserRoleMapBean> roleMaps) {
        this.roleMaps = roleMaps;
    }

    @Override
    public String toString() {
        return "UserBean [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password
                + ", idcard=" + idcard + ", phone=" + phone + ", email=" + email + ", state=" + state + ", ctime="
                + ctime + ", utime=" + utime + ", remark=" + remark + ", roleMaps=" + roleMaps + ", orgMaps=" + orgMaps
                + "]";
    }

}
