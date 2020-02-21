package cn.itcast.tag.web.mergetag.mapper;

import cn.itcast.tag.web.mergetag.bean.UserMergeTagMapBean;
import cn.itcast.tag.web.mergetag.bean.form.UserMergeTagTagFormBean;
import cn.itcast.tag.web.user.bean.UserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户组合标签关联
 *
 * @author zhangwenguo
 */
@Repository
public interface UserMergeTagMapMapper {

    /**
     * 更新组合表和基础标签关联DTO
     *
     * @param bean
     * @return
     */
    public int insertUserMergeTagMap(UserMergeTagMapBean bean);

    /**
     * 根据ID删除组合标签关联表
     *
     * @param bean
     * @return
     */
    public int delUserMergeTagMapForId(@Param("bean") UserMergeTagTagFormBean bean, @Param("loginUser") UserBean loginUser);

    /**
     * 更新状态
     *
     * @param bean
     * @return
     */
    public int updateState(UserMergeTagMapBean bean);

    /**
     * 根据id查询
     *
     * @param bean
     * @return
     */
    public UserMergeTagMapBean queryUserMergeTagForId(UserMergeTagMapBean bean);

}
