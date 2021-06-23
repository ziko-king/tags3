package cn.itcast.tag.web.micro.bean;

import java.util.List;

public class MicroPortraitUserBean {
    /**
     *
     */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    /**
     * 身份证号
     */
    private String idNum;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 邮箱
     */
    private String email;

    /**
     * QQ
     */
    private String qq;

    /**
     * 标识 0：人 1：物
     */
    private String type;

    private List<MicroPortraitTag> tags;


    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public List<MicroPortraitTag> getTags() {
        return tags;
    }

    public void setTags(List<MicroPortraitTag> tags) {
        this.tags = tags;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    @Override
    public String toString() {
        return "MicroPortraitUserBean [idNum=" + idNum + ", phoneNum=" + phoneNum + ", email=" + email + ", qq=" + qq
                + ", type=" + type + ", tags=" + tags + "]";
    }


}
