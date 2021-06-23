package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaocs
 */
public class PermissionBean implements Serializable {
    private static final long serialVersionUID = -8324455423246757270L;

    private Integer id;

    private String name;

    private Integer type;

    private Date ctime;

    private Date utime;

    private String remark;

    public PermissionBean() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "PermissionBean [id=" + id + ", name=" + name + ", type=" + type + ", ctime=" + ctime + ", utime="
                + utime + ", remark=" + remark + "]";
    }

}
