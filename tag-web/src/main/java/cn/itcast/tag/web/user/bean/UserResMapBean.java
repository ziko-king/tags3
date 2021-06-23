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
 * 用户资源实体类
 */
public class UserResMapBean implements Serializable {
    private static final long serialVersionUID = 6310061614325432547L;

    private Long id;

    private Long userId;

    private Long resId;

    private String permIds;

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    public UserResMapBean() {
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

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
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

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    @Override
    public String toString() {
        return "UserResMapBean [id=" + id + ", userId=" + userId + ", resId=" + resId + ", permIds=" + permIds
                + ", state=" + state + ", ctime=" + ctime + ", utime=" + utime + ", remark=" + remark + "]";
    }

}
