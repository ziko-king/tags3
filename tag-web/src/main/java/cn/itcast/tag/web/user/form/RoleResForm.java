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
 *
 */
public class RoleResForm implements Serializable {
    private static final long serialVersionUID = -4501307384295786724L;
    private long roleId;
    private List<DataRes> dataReses;

    public RoleResForm() {
        super();
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<DataRes> getDataReses() {
        return dataReses;
    }

    public void setDataReses(List<DataRes> dataReses) {
        this.dataReses = dataReses;
    }

    @Override
    public String toString() {
        return "RoleResForm [roleId=" + roleId + ", dataReses=" + dataReses + "]";
    }

}
