package cn.itcast.tag.web.user.mapper;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据权限
 *
 * @author mengyao
 */
@Repository
public interface DataMapper {

    /**
     * 用户（超级管理员）持有的基础标签
     *
     * @param userId
     * @return
     */
    public List<BasicTagBean> getBasicTag();

    /**
     * 用户（超级管理员）持有的组合标签
     *
     * @param userId
     * @return
     */
    public List<MergeTagBean> getMergeTag();

    /**
     * 用户（管理员、普通用户）持有的基础标签
     *
     * @param userId
     * @return
     */
    public List<BasicTagBean> getUserBasicTag(@Param("userId") long userId);

    /**
     * 角色（管理员、普通用户）持有的基础标签
     *
     * @param roleId
     * @return
     */
    public List<BasicTagBean> getRoleBasicTag(@Param("roleId") long roleId);

    /**
     * 用户（管理员、普通用户）持有的组合标签
     *
     * @param userId
     * @return
     */
    public List<MergeTagBean> getUserMergeTag(@Param("userId") long userId);

    /**
     * 角色（管理员、普通用户）持有的组合标签
     *
     * @param roleId
     * @return
     */
    public List<MergeTagBean> getRoleMergeTag(@Param("roleId") long roleId);

    /**
     * 统计组合标签含有基础标签个数
     *
     * @param roleId
     * @return
     */
    public MergeTagBean getBasicTagNum(@Param("id") Long id);

}
