/**
 * 项目名称：mengyao
 * 创建日期：2018年5月28日
 * 修改历史：
 * 1、[2018年5月28日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;

/**
 * 登录信息
 * @author zhaocs
 *
 */
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 4851346871110311786L;
    private String username;        // 登录名称
    private String password;        // 登录密码
    private String rememberMe;      // 记住我

    public LoginForm() {
        super();
    }

    public LoginForm(String username, String password, String rememberMe) {
        super();
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVo [username=" + username + ", password=" + password + ", rememberMe=" + rememberMe + "]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

}
