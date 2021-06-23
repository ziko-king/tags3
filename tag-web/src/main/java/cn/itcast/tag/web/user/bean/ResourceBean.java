package cn.itcast.tag.web.user.bean;

import cn.itcast.tag.web.commons.bean.Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaocs
 */
public class ResourceBean extends Bean implements Serializable {
    private static final long serialVersionUID = 5982145824024713602L;

    private Long id;

    private String name;

    private String url;

    private int type;

    private String sign;

    private int sort;

    private Long pid;

    private String permIds;     // 资源拥有的权限，前端对应字段，数据库没有

    private Date ctime;

    private Date utime;

    private String remark;

//    private List<ResourceBean> children;

    public ResourceBean() {
        super();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "ResourceBean [id=" + id + ", name=" + name + ", url=" + url + ", type=" + type + ", sign=" + sign
                + ", sort=" + sort + ", pid=" + pid + ", permIds=" + permIds + ", ctime=" + ctime + ", utime=" + utime
                + ", remark=" + remark + "]";
    }

}
