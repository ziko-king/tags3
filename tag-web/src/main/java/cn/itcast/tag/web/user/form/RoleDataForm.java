/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaocs
 * 对接角色数据
 */
public class RoleDataForm implements Serializable {
    private static final long serialVersionUID = 4719742965746102543L;
    private long roleId;
    private List<DataTag> dataTags;

    public RoleDataForm() {
        super();
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<DataTag> getDataTags() {
        return dataTags;
    }

    public void setDataTags(List<DataTag> dataTags) {
        this.dataTags = dataTags;
    }

    @Override
    public String toString() {
        return "RoleDataForm [roleId=" + roleId + ", dataTags=" + dataTags + "]";
    }

}
