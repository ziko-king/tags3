package cn.itcast.tag.web.examine.bean;

import java.io.Serializable;

/**
 * @author wangjunfeng
 * @date 2018年6月10日
 */
public class TagAuditBean implements Serializable {

    private static final long serialVersionUID = -8054439122971574969L;

    private Long id;

    private Long tagId;

    private int level;
    /**
     * 1 基础标签 2 组合标签
     */
    private Long type;
    private String name;
    private String rule;
    private String business;
    private String model;
    private Long applyUserId;
    private String applyUserName;
    private String state;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

}
