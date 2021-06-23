package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户数据关联实体类（基础标签和组合标签）
 * <文国已经分开实现了，这里的暂时不用>
 *
 * @author zhaocs
 */
public class UserDataMapBean implements Serializable {
    private static final long serialVersionUID = -8823786426645254506L;

    private Long id;

    private Long userId;

    private Long tagId;

    private int tagType;    // 1基础标签；2组合标签

    private String permIds;

    private Date ctime;

    private Date utime;

    private String remark;

    public UserDataMapBean() {
        super();
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

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
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

    @Override
    public String toString() {
        return "UserDataMapBean [id=" + id + ", userId=" + userId + ", tagId=" + tagId + ", tagType=" + tagType
                + ", permIds=" + permIds + ", ctime=" + ctime + ", utime=" + utime + ", remark=" + remark + "]";
    }

}
