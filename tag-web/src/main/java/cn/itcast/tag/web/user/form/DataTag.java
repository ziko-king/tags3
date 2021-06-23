/**
 * 项目名称：mengyao
 * 创建日期：2018年6月11日
 * 修改历史：
 * 1、[2018年6月11日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;

/**
 * @author zhaocs
 *
 */
public class DataTag implements Serializable {
    private static final long serialVersionUID = 6615471747879834435L;
    private long id;
    // 父标签ID
    private long pId;
    // 标签名称
    private String name;
    // 标签类型
    private int type;
    // 是否展开
    private Boolean open;
    // 是否选中
    private Boolean checked;

    public DataTag() {
        super();
    }

    public DataTag(long id, long pId, String name, int type) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.type = type;
    }

    public DataTag(long id, long pId, String name, int type, Boolean open) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.type = type;
        this.open = open;
    }

    public DataTag(long id, long pId, String name, int type, Boolean open, Boolean checked) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.type = type;
        this.open = open;
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pId;
    }

    public void setPid(long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        return "DataTag [id=" + id + ", pId=" + pId + ", name=" + name + ", type=" + type + ", open=" + open + "]";
    }

}
