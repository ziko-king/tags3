package cn.itcast.tag.web.basictag.mapper;

import cn.itcast.tag.web.basictag.bean.form.BasicModelRuleFormBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BasicModelRuleDao
 *
 * @author zhangwenguo
 */
@Repository
public interface BasicModelRuleMapper {


    /**
     * 查询主基础标签(一至三级)
     *
     * @param bean
     * @return
     */
    public List<BasicModelRuleFormBean> queryFourthBasicTagForId(@Param("bean") BasicModelRuleFormBean bean, @Param("loginUser") UserBean loginUser);


    /**
     * 根据pid和用户id查询标签
     *
     * @param bean
     * @return
     */
    public List<BasicModelRuleFormBean> queryBasicTagAndModelForWithPid1(@Param("bean") BasicModelRuleFormBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

    /**
     * 根据pid和用户id查询标签
     *
     * @param bean
     * @return
     */
    public List<BasicModelRuleFormBean> queryBasicTagAndModelForWithPid2(@Param("bean") BasicModelRuleFormBean bean, @Param("loginUser") UserBean loginUser, @Param("roleBean") RoleBean roleBean);

}
