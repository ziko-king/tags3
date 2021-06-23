/**
 * 项目名称：mengyao
 * 创建日期：2018年5月30日
 * 修改历史：
 * 1、[2018年5月30日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.ResourceBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.form.UserRoleResForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaocs
 *
 */
@Repository
public interface ResourceMapper {
    /**
     * 新增资源
     * @param bean
     * @return
     */
    int addResource(ResourceBean bean);

    /**
     * 根据ID删除资源
     * @param bean
     * @return
     */
    int delResourceForId(ResourceBean bean);

    /**
     *  根据ID修改资源
     * @param bean id、 name、flag、remark
     * @return
     */
    int updateResource(ResourceBean bean);

    /**
     * 根据资源id查询
     * @param bean id
     * @return
     */
    public ResourceBean queryForId(ResourceBean bean);

    /**
     * 根据资源pid查询
     * @param bean id
     * @return
     */
    List<ResourceBean> queryForPId(long pid);

    /**
     * 根据资源type查询
     * @param bean type
     * @return
     */
    List<ResourceBean> queryForType(Integer pid);

    /**
     * 查询所有的资源
     * @param bean
     * @return List<UserBean>
     */
    List<ResourceBean> queryForConditions(ResourceBean bean);

    /**
     * 查询所有的资源总记录数
     * @param bean
     * @return List<UserBean>
     */
    int queryCountForConditions(ResourceBean bean);

    /**
     * 查询角色的资源，包括权限
     * @param id
     * @return List<UserBean>
     */
    List<ResourceBean> queryForRoleBean(RoleBean bean);

    /**
     * 查询用户的资源，包括权限
     * @param bean roleId、userId
     * @return List<UserBean>
     */
    List<ResourceBean> queryForUserRoleMap(UserRoleMapBean bean);

    /**
     * 查询用户的资源，包括权限
     * @param bean roleId、userId、resourceId
     * @return List<UserBean>
     */
    List<ResourceBean> queryForUserRoleResId(UserRoleResForm bean);
}
