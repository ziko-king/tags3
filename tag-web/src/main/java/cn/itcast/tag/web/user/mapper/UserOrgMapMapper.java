/**
 * 项目名称：mengyao
 * 创建日期：2018年6月4日
 * 修改历史：
 * 1、[2018年6月4日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.UserOrgMapBean;
import org.springframework.stereotype.Repository;

/**
 * @author zhaocs
 *
 */
@Repository
public interface UserOrgMapMapper {
    /**
     * 新增用户组织关联
     * @param bean
     * @return
     */
    int addUserOrgMap(UserOrgMapBean bean);

    /**
     * 根据ID删除用户组织关联
     * @param bean
     * @return
     */
    int delUserOrgMapForId(UserOrgMapBean bean);

    /**
     * 删除用户组织关联
     * @param bean
     * @return
     */
    int delUserOrgMap(UserOrgMapBean bean);

    /**
     *  根据ID修改用户组织关联
     * @return
     */
    int updateUserOrgMap(UserOrgMapBean bean);

    /**
     * 根据用户组织关联id查询
     * @param bean id
     * @return
     */
    UserOrgMapBean queryForId(UserOrgMapBean bean);
}
