package cn.itcast.tag.web.basictag.mapper;

import cn.itcast.tag.web.basictag.bean.form.UserTagFormBean;
import org.springframework.stereotype.Repository;

/**
 * 用户基础标签关联
 */
@Repository
public interface UserTagMapMapper {

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public UserTagFormBean queryUserTagForId(UserTagFormBean bean);

    /**
     * 更新状态
     *
     * @param bean
     * @return
     */
    public int updateState(UserTagFormBean bean);

    /**
     * 更新用户和基础标签关联表
     *
     * @param bean
     * @return
     */
    public int insertUserTagMap(UserTagFormBean bean);

}
