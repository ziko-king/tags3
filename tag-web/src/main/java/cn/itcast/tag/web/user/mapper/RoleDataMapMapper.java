/**
 * 项目名称：mengyao
 * 创建日期：2018年6月4日
 * 修改历史：
 * 1、[2018年6月4日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.RoleDataMapBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaocs
 *
 */
@Repository
public interface RoleDataMapMapper {
    /**
     * 新增角色数据关联
     * @param bean
     * @return
     */
    int addRoleDataMap(List<RoleDataMapBean> beans);

    /**
     * 根据ID删除角色数据关联
     * @param bean
     * @return
     */
    int delRoleDataMapForId(RoleDataMapBean bean);

    /**
     * 根据角色ID删除角色数据关联
     * @param bean
     * @return
     */
    int delRoleDataMapForRoleId(RoleDataMapBean bean);

    /**
     *  根据ID修改角色数据关联
     * @return
     */
    int updateRoleDataMap(RoleDataMapBean bean);

    /**
     * 根据角色数据关联id查询
     * @param bean id
     * @return
     */
    RoleDataMapBean queryForId(RoleDataMapBean bean);
}
