/**
 * 项目名称：mengyao
 * 创建日期：2018年6月4日
 * 修改历史：
 * 1、[2018年6月4日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.user.bean.UserResMapBean;
import org.springframework.stereotype.Repository;

/**
 * @author zhaocs
 *
 */
@Repository
public interface UserResMapMapper {
    /**
     * 新增用户资源关联
     * @param bean
     * @return
     */
    int addUserResMap(UserResMapBean bean);

    /**
     * 删除用户资源关联
     * @param bean id、userId、resId、permIds、state
     * @return
     */
    int delUserResMap(UserResMapBean bean);

    /**
     *  根据ID修改用户资源关联
     * @return
     */
    int updateUserResMap(UserResMapBean bean);

    /**
     * 根据用户资源关联id查询
     * @param bean id
     * @return
     */
    UserResMapBean queryForId(UserResMapBean bean);
}
