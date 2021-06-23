/**
 * 项目名称：mengyao
 * 创建日期：2018年5月24日
 * 修改历史：
 * 1、[2018年5月24日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.RoleBean;

import java.util.List;

/**
 * @author zhaocs
 *
 */
public interface RoleService {

    /**
     * 新增角色
     * @param bean
     * @return
     */
    boolean addRole(RoleBean bean);

    /**
     * 根据ID删除角色
     * @param bean
     * @return
     */
    boolean delForRole(RoleBean bean);

    /**
     *  根据ID修改角色
     * @param bean id、 name、flag、remark
     * @return
     */
    Boolean updateRole(RoleBean bean);

    /**
     * 根据角色id查询
     * @param bean id
     * @return
     */
    public RoleBean queryForId(RoleBean bean);

    /**
     * 查询所有的用户
     * @param bean
     * @return List<UserBean>
     */
    List<RoleBean> queryForConditions(RoleBean bean);

    /**
     * 查询所有的用户总记录数
     * @param bean
     * @return List<UserBean>
     */
    int queryCountForConditions(RoleBean bean);
}
