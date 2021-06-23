/**
 * 项目名称：mengyao
 * 创建日期：2018年6月15日
 * 修改历史：
 * 1、[2018年6月15日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.UserOrgMapBean;

/**
 * @author zhaocs
 *
 */
public interface UserOrgMapService {
    /**
     * 新增用户组织关联
     * @param bean
     * @return
     */
    Boolean addUserOrgMap(UserOrgMapBean bean);

    /**
     * 根据ID删除用户组织关联
     * @param bean
     * @return
     */
    Boolean delUserOrgMapForId(UserOrgMapBean bean);

    /**
     * 删除用户组织关联
     * @param bean
     * @return
     */
    Boolean delUserOrgMap(UserOrgMapBean bean);

    /**
     *  根据ID修改用户组织关联
     * @return
     */
    Boolean updateUserOrgMap(UserOrgMapBean bean);

    /**
     * 根据用户组织关联id查询
     * @param bean id
     * @return
     */
    UserOrgMapBean queryForId(UserOrgMapBean bean);
}
