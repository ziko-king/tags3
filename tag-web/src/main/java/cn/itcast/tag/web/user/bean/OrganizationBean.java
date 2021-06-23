package cn.itcast.tag.web.user.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhaocs
 */
public class OrganizationBean implements Serializable {
    private static final long serialVersionUID = 1535283882822702969L;

    private Long id;

    private String name;

    private int level;

    private Long pid;

    private int state;

    private Date ctime;

    private Date utime;

    private String remark;

    private List<UserOrgMapBean> userMaps;

    private List<OrganizationBean> children;

    public OrganizationBean() {
        super();
    }

    public OrganizationBean(int level) {
        super();
        this.level = level;
    }

    public OrganizationBean(Long id) {
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public List<UserOrgMapBean> getUserMaps() {
        return userMaps;
    }

    public void setUserMaps(List<UserOrgMapBean> userMaps) {
        this.userMaps = userMaps;
    }

    public List<OrganizationBean> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationBean> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "OrganizationBean [id=" + id + ", name=" + name + ", pid=" + pid + ", state=" + state + ", ctime="
                + ctime + ", utime=" + utime + ", remark=" + remark + ", userMaps=" + userMaps + "]";
    }

}
