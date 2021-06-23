package cn.itcast.tag.web.mergetag.mapper;

import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.commons.bean.Bean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.mergetag.bean.form.UserMergeTagTagFormBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MergeTagDao
 *
 * @author zhangwenguo
 */
@Repository
public interface MergeTagMapper {

    /**
     * 新增组合标签
     *
     * @param bean
     * @return
     */
    public boolean addMergeTag(MergeTagBean bean);

    /**
     * 新增组合标签和基础标签关联信息
     *
     * @param bean
     * @return
     */
    public int addMergeTagTagMap(UserMergeTagTagFormBean bean);

    /**
     * 查询组合标签
     *
     * @param bean
     * @return
     */
    public List<MergeTagBean> queryMergeTag(@Param("bean") MergeTagBean bean, @Param("loginUser") UserBean loginUser);

    /**
     * 编辑组合标签
     *
     * @param bean
     * @return
     */
    public int updateMergeTag(MergeTagBean bean);

    /**
     * 编辑组合标签取出组合标签数据回显
     *
     * @param bean
     * @return
     */
    public MergeTagBean queryUpdateMergeTagData(@Param("bean") MergeTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);


    /**
     * 根据ID删除组合标签
     *
     * @param bean
     * @return
     */
    public int delMergeTagForId(MergeTagBean bean);

    /**
     * 根据NAME查询组合标签(模糊查询)
     *
     * @param bean
     * @return
     */
    public List<MergeTagBean> searchMergeTagForName(@Param("bean") MergeTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据id查询组合标签详情
     *
     * @param id
     * @return
     */
    public MergeTagBean queryMergeTagById(long id);

    /**
     * 根据NAME查询组合标签
     *
     * @param bean
     * @param loginUser
     * @param roleBean
     * @return
     */
    public MergeTagBean isExistMergeTagForName(@Param("bean") MergeTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 用户（超级管理员）持有的组合标签
     *
     * @param userId
     * @return
     */
    public List<MergeTagBean> getMergeTag(@Param("mbean") MergeTagBean mbean, @Param("bean") Bean bean);

    /**
     * 据用户权限和角色查询组合标签
     *
     * @param bean
     * @param loginUser
     * @param roleBean
     * @return
     */
    public List<MergeTagBean> getURMergeTag(@Param("mbean") MergeTagBean mbean, @Param("bean") Bean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 用户（超级管理员）持有的组合标签条数
     *
     * @param userId
     * @return
     */
    public int getMergeTagCount(@Param("mbean") MergeTagBean mbean, @Param("bean") Bean bean);

    /**
     * 据用户权限和角色查询组合标签条数
     *
     * @param bean
     * @param loginUser
     * @param roleBean
     * @return
     */
    public int getURMergeTagCount(@Param("mbean") MergeTagBean mbean, @Param("bean") Bean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

}
