/**
 * 项目名称：mengyao
 * 创建日期：2018年5月30日
 * 修改历史：
 * 1、[2018年5月30日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.PermissionBean;

/**
 * @author zhaocs
 *
 */
public interface PermissionMapper {
    /**
     * 新增权限
     * @param bean
     * @return
     */
    int addPermission(PermissionBean bean);

    /**
     * 根据ID删除权限
     * @param bean
     * @return
     */
    int delPermissionForId(PermissionBean bean);

    /**
     *  根据ID修改权限
     * @param bean id、 name、flag、remark
     * @return
     */
    int updatePermission(PermissionBean bean);

    /**
     * 根据权限id查询
     * @param bean id
     * @return
     */
    public PermissionBean queryForId(PermissionBean bean);
}
