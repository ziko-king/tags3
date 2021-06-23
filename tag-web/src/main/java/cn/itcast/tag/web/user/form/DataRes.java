/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;

/**
 * @author zhaocs
 *
 */
public class DataRes implements Serializable {
    private static final long serialVersionUID = 4726132962770891105L;
    private long id;
    private long pId;
    // 资源名称
    private String name;
    private String permIds;
    private String allPermIds;
    // 是否展开
    private Boolean open;
    // 是否选中
    private Boolean checked;

    public DataRes() {
        super();
    }

    public DataRes(long id, long pId, String name, String permIds, String allPermIds, Boolean open, Boolean checked) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.permIds = permIds;
        this.allPermIds = allPermIds;
        this.open = open;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermIds() {
        return permIds;
    }

    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    public String getAllPermIds() {
        return allPermIds;
    }

    public void setAllPermIds(String allPermIds) {
        this.allPermIds = allPermIds;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "DataRes [id=" + id + ", pId=" + pId + ", name=" + name + ", permIds=" + permIds + ", allPermIds="
                + allPermIds + ", open=" + open + ", checked=" + checked + "]";
    }

}
