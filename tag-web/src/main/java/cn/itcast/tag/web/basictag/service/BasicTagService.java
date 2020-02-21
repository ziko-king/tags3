package cn.itcast.tag.web.basictag.service;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.basictag.bean.form.BasicModelRuleFormBean;
import cn.itcast.tag.web.basictag.bean.form.BasicTagFormBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;

import java.util.List;

/**
 * BasicTagService
 *
 * @author zhangwenguo
 */
public interface BasicTagService {

    /**
     * 新增主基础标签(一至三级标签)
     *
     * @param bean
     * @return
     */
    public int addMainBasicTag(List<BasicTagBean> beans, UserBean userBean, RoleBean roleBean);

    /**
     * 新增基础标签(第四级标签)
     *
     * @param bean
     * @return
     */
    public int addFourthBasicTag(BasicModelRuleFormBean bean, UserBean loginUser);

    /**
     * 新增基础标签(第五级标签)
     *
     * @param bean
     * @return
     */
    public BasicTagBean addFifthBasicTag(BasicTagBean basicTagBean, UserBean loginUser);

    /**
     * 获取带权限的基础标签树
     *
     * @param allTags
     * @return
     */
    public List<BasicTagBean> getBasicTagTree(List<BasicTagBean> allTags);

    /**
     * 获取带权限的基础标签树
     *
     * @param allTags
     * @return
     */
    public List<BasicTagFormBean> getIdentifyBasicTagTree(List<BasicTagFormBean> allTags);


    /**
     * 查询用户和角色持有的所有标签
     *
     * @param user 用户ID
     * @param rule 角色ID
     * @return
     */
    public List<BasicTagBean> queryForAuthTag(UserBean user, RoleBean rule);

    /**
     * 查询主基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryMainBasicTag(BasicTagBean bean);

    /**
     * 根据level获取基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForLevel(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据id获取基础标签(不包括第四级)
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForId(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据id获取第四级基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicModelRuleFormBean> queryFourthBasicTagForId(BasicModelRuleFormBean bean, UserBean loginUser);

    /**
     * 根据Pid获取基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForPid(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据ID删除基础标签
     *
     * @param bean
     * @return
     */
    public int delBasicTagForId(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据ID更新基础标签(一至三级)
     *
     * @param bean
     * @return
     */
    public int updateMainBasicTagForId(BasicTagBean bean);

    /**
     * 根据ID和行业名称查询标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> searchBasicTagForName(BasicTagBean bean);

    /**
     * 根据ID更新基础标签(第四级)
     *
     * @param bean
     * @return
     */
    public boolean updateFourthBasicTag(BasicModelRuleFormBean bean);

    /**
     * 根据ID更新基础标签(第五级)
     *
     * @param bean
     * @return
     */
    public int updateFifthBasicTag(BasicTagBean bean);

    /**
     * 根据userId查询
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForUserId(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据pid、levele和userid查询
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForWithPid(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据pid、levele和userid查询
     *
     * @param bean
     * @return
     */
    public List<BasicModelRuleFormBean> queryBasicTagAndModelForWithPid(BasicModelRuleFormBean bean, UserBean loginUser, RoleBean roleBean);


    /**
     * 根据标签名称进行模糊查询
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForName(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 获取全部的基础标签
     *
     * @return
     */
    public List<BasicTagBean> queryAllBasicTags();

    /**
     * 标签任务启动和停止
     *
     * @param flag
     * @return
     */
    public int taskProcessing(BasicTagBean bean, UserBean loginUser);

    public Boolean isExistForName(BasicTagBean bean, UserBean loginUser, RoleBean roleBean);

    /**
     * 根据level获取tag数量
     *
     * @param level
     * @return
     */
    public Long queryTagCountByLevel(int level);

}