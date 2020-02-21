package cn.itcast.tag.web.mergetag.mapper;

import cn.itcast.tag.web.commons.bean.Bean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.mergetag.bean.form.UserMergeTagTagFormBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户、组合标签、基础标签关联
 *
 * @author zhangwenguo
 */
@Repository
public interface UserMergeTagTagMapper {

    /**
     * 查询组合标签
     *
     * @param bean
     * @return
     */
    public List<UserMergeTagTagFormBean> queryMergeTagData(@Param("mergeTagBean") MergeTagBean mergeTagBean, @Param("bean") Bean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 查询组合标签
     *
     * @param bean
     * @return
     */
    public List<UserMergeTagTagFormBean> updateQueryMergeTag(@Param("mergeTagBean") MergeTagBean mergeTagBean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

}
