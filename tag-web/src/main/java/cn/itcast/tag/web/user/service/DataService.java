package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;

import java.util.List;

/**
 * @author mengyao
 */
public interface DataService {

    /**
     * 用户（超级管理员）持有的基础标签
     *
     * @param userId
     * @return
     */
    public List<BasicTagBean> getBasicTag();

    /**
     * 用户（超级管理员）持有的标签
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
    public List<BasicTagBean> getUserBasicTag(long userId);

    /**
     * 角色（管理员、普通用户）持有的基础标签
     *
     * @param roleId
     * @return
     */
    public List<BasicTagBean> getRoleBasicTag(long roleId);

    /**
     * 用户（管理员、普通用户）持有的组合标签
     *
     * @param userId
     * @return
     */
    public List<MergeTagBean> getUserMergeTag(long userId);

    /**
     * 角色（管理员、普通用户）持有的组合标签
     *
     * @param roleId
     * @return
     */
    public List<MergeTagBean> getRoleMergeTag(long roleId);

}
