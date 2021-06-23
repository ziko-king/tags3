/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.RoleResMapBean;

import java.util.List;

/**
 * @author zhaocs
 *
 */
public interface RoleResMapService {
    /**
     * 新增角色资源关联
     * @param bean
     * @return
     */
    Boolean addRoleResMap(RoleResMapBean bean);

    /**
     * 新增角色资源关联
     * @param bean
     * @return
     */
    Boolean addRoleResMap(List<RoleResMapBean> beans);

    /**
     * 根据ID删除角色资源关联
     * @param bean
     * @return
     */
    Boolean delRoleResMapForId(RoleResMapBean bean);

    /**
     * 根据角色ID删除角色资源关联
     * @param bean
     * @return
     */
    Boolean delRoleResMapForRoleId(RoleResMapBean bean);

    /**
     *  根据ID修改角色资源关联
     * @return
     */
    Boolean updateRoleResMap(RoleResMapBean bean);

    /**
     * 根据角色资源关联id查询
     * @param bean id
     * @return
     */
    RoleResMapBean queryForId(RoleResMapBean bean);
}
