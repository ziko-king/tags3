package cn.itcast.tag.web.basictag.mapper;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.basictag.bean.form.UserTagFormBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BasicTagDao
 *
 * @author zhangwenguo
 */
@Repository
public interface BasicTagMapper {

    /**
     * 新增基础标签(一至三级)
     *
     * @param bean
     * @return
     */
    public void addMainBasicTag(BasicTagBean bean);

    /**
     * 更新基础标签(一至三级)
     *
     * @param bean
     * @return
     */
    public int updateMainBasicTagForId(BasicTagBean bean);

    /**
     * 更新基础标签
     *
     * @param bean
     * @return
     */
    public int addFourthBasicTag(BasicTagBean bean);

    /**
     * 新增基础标签(五级)
     *
     * @param bean
     * @return
     */
    public void addFifthBasicTag(BasicTagBean bean);

    /**
     * 新增基础标签(五级)
     *
     * @param bean
     * @return
     */
    public int insertUserTagMap(UserTagFormBean bean);

    public List<BasicTagBean> isExistBasicTagForName(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 查询主基础标签(一至三级)
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryMainBasicTag(BasicTagBean bean);

    /**
     * 根据Level获取基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForLevel(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据Id获取基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForId(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据Pid获取基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForPid(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据标签名称获取基础标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForName(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据ID删除基础标签
     *
     * @param bean
     * @return
     */
    public int delBasicTagForId(BasicTagBean bean);

    /**
     * 根据ID删除基础标签
     *
     * @param bean
     * @return
     */
    public int delUserTagForId(UserTagFormBean bean);

    /**
     * 根据PID删除基础标签
     *
     * @param bean
     * @return
     */
    public int delBasicTagForPid(BasicTagBean bean);

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
    public int updateFourthBasicTag(BasicTagBean bean);

    /**
     * 根据ID更新基础标签状态
     *
     * @param bean
     * @return
     */
    public int updateStateForId(BasicTagBean bean);

    /**
     * 根据ID更新基础标签(第五级)
     *
     * @param bean
     * @return
     */
    public int updateFifthBasicTag(BasicTagBean bean);

    /**
     * 根据ID和行业名称查询标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForUserId(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据pid和用户id查询标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForWithPid1(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据pid和用户id查询标签
     *
     * @param bean
     * @return
     */
    public List<BasicTagBean> queryBasicTagForWithPid2(@Param("bean") BasicTagBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 获取全部的基础标签
     *
     * @return
     */
    public List<BasicTagBean> queryAllBasicTags();

    /**
     * 根据level获取tag数量
     *
     * @param level
     * @return
     */
    public Long queryTagCountByLevel(int level);
}
