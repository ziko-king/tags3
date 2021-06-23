/**
 * 项目名称：mengyao
 * 创建日期：2018年6月10日
 * 修改历史：
 * 1、[2018年6月10日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.UserRoleMapBean;

/**
 * @author zhaocs
 *
 */
public interface UserRoleMapService {
    /**
     * 新增用户角色关联
     * @param bean
     * @return
     */
    Boolean addUserRoleMap(UserRoleMapBean bean);

    /**
     * 根据ID删除用户角色关联
     * @param bean
     * @return
     */
    Boolean delUserRoleMapForId(UserRoleMapBean bean);

    /**
     * 删除用户角色关联
     * @param bean id、userId、roleId、state
     * @return
     */
    Boolean delUserRoleMap(UserRoleMapBean bean);

    /**
     *  根据ID修改用户角色关联
     * @return
     */
    Boolean updateUserRoleMap(UserRoleMapBean bean);

    /**
     *  根据ID修改用户角色关联
     * @return
     */
    Boolean updateUserRoleMapForUserId(UserRoleMapBean bean);

    /**
     * 根据用户角色关联id查询
     * @param bean id
     * @return
     */
    UserRoleMapBean queryForId(UserRoleMapBean bean);

    UserRoleMapBean queryForUserId(Long userId);
}
