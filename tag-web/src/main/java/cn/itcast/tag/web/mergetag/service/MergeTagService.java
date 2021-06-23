package cn.itcast.tag.web.mergetag.service;

import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.commons.bean.Bean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.mergetag.bean.form.UserMergeTagTagFormBean;

import java.util.List;
import java.util.Map;

/**
 * MergeTagService
 *
 * @author zhangwenguo
 */
public interface MergeTagService {

    /**
     * 新增组合基础标签
     *
     * @param bean
     * @return
     */
    public int addMergeTag(UserMergeTagTagFormBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 查询组合标签
     *
     * @param bean
     * @return
     */
    public List<UserMergeTagTagFormBean> queryMergeTagData(MergeTagBean mergeTagBean, Bean bean, UserBean loginUser, RoleBean RoleBean);

    /**
     * 查询组合标签
     *
     * @param bean
     * @return
     */
    public List<UserMergeTagTagFormBean> updateQueryMergeTag(MergeTagBean mergeTagBean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据ID删除组合标签
     *
     * @param bean
     * @return
     */
    public int delMergeTagForId(UserMergeTagTagFormBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据NAME查询组合标签
     *
     * @param bean
     * @return
     */
    public MergeTagBean isExistMergeTagForName(MergeTagBean bean, UserBean loginUser, RoleBean roleId);

    /**
     * 更新时取得组合标签信息回显
     *
     * @param bean
     * @return
     */
    public MergeTagBean queryUpdateMergeTagData(MergeTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据id查询组合标签
     *
     * @param id
     * @return
     */
    public MergeTagBean queryMergeTagById(long id);

    /**
     * 查询用户和角色持有的所有标签
     *
     * @param user 用户ID
     * @param rule 角色ID
     * @return
     */
    public Map<String, Object> queryForAuthTag(MergeTagBean mbean, Bean bean, UserBean user, RoleBean rule);

    /**
     * 统计组合标签含有基础标签个数
     *
     * @param id
     * @return
     */
    public MergeTagBean getBasicTagNum(Long id);

}
