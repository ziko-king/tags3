/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.RoleDataMapBean;

import java.util.List;

/**
 * @author zhaocs
 *
 */
public interface RoleDataMapService {
    /**
     * 新增角色数据关联
     * @param bean
     * @return
     */
    Boolean addRoleDataMap(RoleDataMapBean bean);

    /**
     * 新增角色数据关联
     * @param bean
     * @return
     */
    Boolean addRoleDataMap(List<RoleDataMapBean> beans);

    /**
     * 根据ID删除角色数据关联
     * @param bean
     * @return
     */
    Boolean delRoleDataMapForId(RoleDataMapBean bean);

    /**
     * 根据角色ID删除角色数据关联
     * @param bean
     * @return
     */
    Boolean delRoleDataMapForRoleId(RoleDataMapBean bean);

    /**
     *  根据ID修改角色数据关联
     * @return
     */
    Boolean updateRoleDataMap(RoleDataMapBean bean);

    /**
     * 根据角色数据关联id查询
     * @param bean id
     * @return
     */
    RoleDataMapBean queryForId(RoleDataMapBean bean);
}
