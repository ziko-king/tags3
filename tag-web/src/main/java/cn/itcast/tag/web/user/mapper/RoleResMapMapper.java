/**
 * 项目名称：mengyao
 * 创建日期：2018年6月4日
 * 修改历史：
 * 1、[2018年6月4日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.RoleResMapBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaocs
 *
 */
@Repository
public interface RoleResMapMapper {
    /**
     * 新增角色资源关联
     * @param bean
     * @return
     */
    int addRoleResMap(List<RoleResMapBean> beans);

    /**
     * 根据ID删除角色资源关联
     * @param bean
     * @return
     */
    int delRoleResMapForId(RoleResMapBean bean);

    /**
     * 根据角色ID删除角色资源关联
     * @param bean
     * @return
     */
    int delRoleResMapForRoleId(RoleResMapBean bean);

    /**
     *  根据ID修改角色资源关联
     * @return
     */
    int updateRoleResMap(RoleResMapBean bean);

    /**
     * 根据角色资源关联id查询
     * @param bean id
     * @return
     */
    RoleResMapBean queryForId(RoleResMapBean bean);
}
