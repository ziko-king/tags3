package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.ResourceBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;

import java.util.List;

/**
 * UserService
 *
 * @author mengyao
 */
public interface UserService {

    /**
     * 新增用户
     *
     * @param bean
     * @return
     */
    boolean addUser(UserBean bean);

    /**
     * 根据ID删除用户
     *
     * @param bean
     * @return
     */
    boolean delForUser(UserBean bean);

    /**
     * 根据ID修改用户密码
     *
     * @param bean 新密码、ID
     * @return
     */
    boolean updateForPassword(UserBean bean);

    /**
     * ID修改用户信息
     *
     * @param bean ID
     * @return
     */
    boolean update(UserBean bean);

    /**
     * 用户登录
     * 操作比较频繁，已实现ehcache
     *
     * @param bean 用户名、密码
     * @return
     */
    UserBean login(UserBean bean);

    /**
     * 根据用户id查询
     *
     * @param bean id
     * @return
     */
    UserBean queryForId(UserBean bean);

    /**
     * 根据用户username查询
     *
     * @param bean id
     * @return
     */
    UserBean queryForUsername(UserBean bean);

    /**
     * 查询所有的用户
     * pr=0时查询全部；否则确保sr为更新的数据
     * state=0时查询所有的状态
     * name是模糊查询
     *
     * @param bean
     * @return List<UserBean>
     */
    List<UserBean> queryForConditions(UserBean bean);

    /**
     * 查询所有的用户总记录数
     * pr=0时查询全部；否则确保sr为更新的数据
     * state=0时查询所有的状态
     * name是模糊查询
     *
     * @param bean
     * @return List<UserBean>
     */
    int queryCountForConditions(UserBean bean);

    /**
     * 查询用户拥有的资源列表
     * 查询比较频繁，已实现ehcache
     *
     * @param bean userId、roleId
     * @return
     */
    List<ResourceBean> queryCurrentResources(UserRoleMapBean bean);

    /**
     * 查询所有资源列表
     * 超级管理员
     *
     * @return List<ResourceBean>
     */
    List<ResourceBean> queryAllResources();

    /**
     * 删除userCache缓存
     * 7天有效期（不使用），内存不够会存到磁盘
     */
    Boolean clearUserCache();

    /**
     * 删除所有userCache缓存
     * userCache、halfHour、hour、oneDay
     */
    Boolean clearCache();
}
