package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.RoleBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoleDao
 *
 * @author zhaocs
 */
@Repository
public interface RoleMapper {

    /**
     * 新增角色
     *
     * @param bean
     * @return
     */
    int addRole(RoleBean bean);

    /**
     * 根据ID删除角色
     *
     * @param bean
     * @return
     */
    int delRoleForId(RoleBean bean);

    /**
     * 根据ID修改角色
     *
     * @param bean id、 name、flag、remark
     * @return
     */
    int updateRole(RoleBean bean);

    /**
     * 根据角色id查询
     *
     * @param bean id
     * @return
     */
    RoleBean queryForId(RoleBean bean);

    /**
     * 查询所有的角色
     *
     * @param bean
     * @return List<UserBean>
     */
    List<RoleBean> queryForConditions(RoleBean bean);

    /**
     * 查询所有的角色总记录数
     *
     * @param bean
     * @return List<UserBean>
     */
    int queryCountForConditions(RoleBean bean);
}
