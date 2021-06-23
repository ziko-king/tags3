/**
 * 项目名称：mengyao
 * 创建日期：2018年6月4日
 * 修改历史：
 * 1、[2018年6月4日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import org.springframework.stereotype.Repository;

/**
 * @author zhaocs
 *
 */
@Repository
public interface UserRoleMapMapper {
    /**
     * 新增用户角色关联
     * @param bean
     * @return
     */
    int addUserRoleMap(UserRoleMapBean bean);

    /**
     * 根据ID删除用户角色关联
     * @param bean
     * @return
     */
    int delUserRoleMapForId(UserRoleMapBean bean);

    /**
     * 删除用户角色关联
     * @param bean id、userId、roleId、state
     * @return
     */
    int delUserRoleMap(UserRoleMapBean bean);

    /**
     *  根据ID修改用户角色关联
     * @return
     */
    int updateUserRoleMap(UserRoleMapBean bean);

    /**
     *  根据用户ID修改用户角色关联
     * @return
     */
    int updateUserRoleMapForUserId(UserRoleMapBean bean);

    /**
     * 根据用户角色关联id查询
     * @param bean id
     * @return
     */
    UserRoleMapBean queryForId(UserRoleMapBean bean);

    UserRoleMapBean queryForUserId(Long userId);
}
