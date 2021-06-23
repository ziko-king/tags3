/**
 * 项目名称：mengyao
 * 创建日期：2018年6月15日
 * 修改历史：
 * 1、[2018年6月15日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.UserResMapBean;

/**
 * @author zhaocs
 *
 */
public interface UserResMapService {
    /**
     * 新增用户资源关联
     * @param bean
     * @return
     */
    Boolean addUserResMap(UserResMapBean bean);

    /**
     * 删除用户资源关联
     * @param bean id、userId、resId、permIds、state
     * @return
     */
    Boolean delUserResMap(UserResMapBean bean);

    /**
     *  根据ID修改用户资源关联
     * @return
     */
    Boolean updateUserResMap(UserResMapBean bean);

    /**
     * 根据用户资源关联id查询
     * @param bean id
     * @return
     */
    UserResMapBean queryForId(UserResMapBean bean);
}
