package cn.itcast.tag.web.user.bean;

import cn.itcast.tag.web.commons.bean.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhaocs
 */
public class RoleBean extends Bean implements Serializable {
    private static final long serialVersionUID = -968617566578910670L;

    private Long id;

    private String name;

    private int flag;

    private Date ctime;

    private Date utime;

    private String remark;

    private List<RoleResMapBean> resMaps;

    public RoleBean() {
        super();
    }

    public RoleBean(Long id) {
        super();
        this.id = id;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public List<RoleResMapBean> getResMaps() {
        return resMaps;
    }

    public void setResMaps(List<RoleResMapBean> resMaps) {
        this.resMaps = resMaps;
    }

    @Override
    public String toString() {
        return "RoleBean [id=" + id + ", name=" + name + ", flag=" + flag + ", ctime=" + ctime + ", utime=" + utime
                + ", remark=" + remark + ", resMaps=" + resMaps + "]";
    }

}
