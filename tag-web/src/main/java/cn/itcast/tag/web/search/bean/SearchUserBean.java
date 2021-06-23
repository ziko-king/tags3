package cn.itcast.tag.web.search.bean;

import java.io.Serializable;

/**
 * 标签查询Bean
 *
 * @author FengZhen
 */
public class SearchUserBean implements Serializable {

    private static final long serialVersionUID = 2019712036934912574L;
    // 名称
    private String name;
    // 电话号
    private String phone;
    // 身份证号
    private String idNum;
    // 银行号
    private String bankNum;
    // 性别
    private String gender;
    //邮箱
    private String email;
    //qq
    private String qq;
    //标识 0 ：人  1：物
    private String type;

    public SearchUserBean(String name, String phone, String idNum, String bankNum, String gender, String email,
                          String qq, String type) {
        super();
        this.name = name;
        this.phone = phone;
        this.idNum = idNum;
        this.bankNum = bankNum;
        this.gender = gender;
        this.email = email;
        this.qq = qq;
        this.type = type;
    }

    public SearchUserBean() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    @Override
    public String toString() {
        return "SearchUserBean [name=" + name + ", phone=" + phone + ", idNum=" + idNum + ", bankNum=" + bankNum
                + ", gender=" + gender + ", email=" + email + ", qq=" + qq + ", type=" + type + "]";
    }
}
