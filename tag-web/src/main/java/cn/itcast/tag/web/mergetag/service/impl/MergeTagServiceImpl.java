package cn.itcast.tag.web.mergetag.service.impl;

import cn.itcast.tag.web.basictag.mapper.BasicTagMapper;
import cn.itcast.tag.web.commons.bean.Bean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.mergetag.bean.MergeTagTagMapBean;
import cn.itcast.tag.web.mergetag.bean.UserMergeTagMapBean;
import cn.itcast.tag.web.mergetag.bean.form.UserMergeTagTagFormBean;
import cn.itcast.tag.web.mergetag.mapper.MergeTagMapper;
import cn.itcast.tag.web.mergetag.mapper.MergeTagTagMapMapper;
import cn.itcast.tag.web.mergetag.mapper.UserMergeTagMapMapper;
import cn.itcast.tag.web.mergetag.mapper.UserMergeTagTagMapper;
import cn.itcast.tag.web.mergetag.service.MergeTagService;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.mapper.DataMapper;
import cn.itcast.tag.web.user.service.MyShiro;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

/**
 * MergeTagService
 *
 * @author zhangwenguo
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MergeTagServiceImpl implements MergeTagService {

    private static final int SUCCESS = 1;
    private static final int FAILE = 0;
    private static final int EXIST = -1;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private MergeTagMapper mergeTagMapper;
    @Resource
    private MergeTagTagMapMapper mergeTagTagMapMapper;
    @Resource
    private UserMergeTagMapMapper userMergeTagMapper;
    @Resource
    private UserMergeTagTagMapper userMergeTagTagMapper;
    @Resource
    private BasicTagMapper basicTagMapper;
    @Resource
    private DataMapper dataMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int addMergeTag(UserMergeTagTagFormBean bean, UserBean loginUser, RoleBean roleBean) {
        // 返回值：-1表示标签已经存在，0表示标签创建失败，1表示标签创建成功
        // 判断该组合标签是否已存在(mergeTagName)
        try {
            MergeTagBean mergeTagBean = new MergeTagBean();
            String mergeTagName = bean.getMergeTagName();
            String condition = bean.getCondition();
            String intro = bean.getIntro();
            String purpose = bean.getPurpose();
            Date ctime = bean.getCtime();
            Date utime = bean.getUtime();
            String remark = bean.getRemark();
            int state = 1;
            long userId = loginUser.getId();
            if (isAdministrator()) {
                state = 4;//已上线
            }
            mergeTagBean.set(mergeTagName, condition, intro, purpose, state, ctime, utime, remark);
            MergeTagBean tmpBean = mergeTagMapper.isExistMergeTagForName(mergeTagBean, loginUser, roleBean);
            if (tmpBean != null) {
                return EXIST;
            }
            mergeTagMapper.addMergeTag(mergeTagBean);
            if (mergeTagBean.getId() > 0) {
                UserMergeTagMapBean userMergeTagMapBean = new UserMergeTagMapBean();
                long mergeId = mergeTagBean.getId();
                userMergeTagMapBean.set(mergeId, userId, ctime, utime, state, remark);
                userMergeTagMapper.insertUserMergeTagMap(userMergeTagMapBean);
                if (userMergeTagMapBean.getId() > 0) {
                    MergeTagTagMapBean mergeTagTagMapBean = new MergeTagTagMapBean();
                    String[] ids = StringUtils.deleteWhitespace(bean.getListBasicTagId()).split("and|or");
                    String[] conditionWithIds = bean.getListBasicTagId().split(" ");
                    List<String> conditions = new ArrayList<>();
                    for (int i = 0; i < conditionWithIds.length; i++) {
                        Pattern pattern = Pattern.compile("[0-9]*");
                        if (!"".equals(conditionWithIds[i].trim()) && !pattern.matcher(conditionWithIds[i]).matches()) {
                            conditions.add(conditionWithIds[i]);
                        }
                    }
                    if (ids != null && ids.length > 0) {
                        for (int i = 0; i < ids.length; i++) {
                            if (!"".equals(ids[i].trim())) {
                                mergeTagTagMapBean.set(mergeId, Long.valueOf(ids[i].trim()), conditions.get(i), ctime, utime, remark);
                                mergeTagTagMapMapper.insertMergeTagTagMap(mergeTagTagMapBean);
                                if (mergeTagTagMapBean.getId() <= 0) {
                                    return FAILE;
                                }
                            }
                        }
                    }
                } else {
                    return FAILE;
                }
            } else {
                return FAILE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addMergeTag@err:{} ====", e);
            return FAILE;
        }
        return SUCCESS;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<UserMergeTagTagFormBean> queryMergeTagData(MergeTagBean mergeTagBean, Bean bean, UserBean loginUser, RoleBean roleBean) {
        List<UserMergeTagTagFormBean> beans = null;
        try {
            beans = userMergeTagTagMapper.queryMergeTagData(mergeTagBean, bean, loginUser, roleBean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryMergeTagData@err:{} ====", e);
        }
        return beans;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int delMergeTagForId(UserMergeTagTagFormBean bean, UserBean loginUser, RoleBean roleBean) {
        int flag = 0;
        try {
            // del
            MergeTagTagMapBean delBean1 = new MergeTagTagMapBean();
            delBean1.setMergeTagId(bean.getMergeTagId());
            flag = mergeTagTagMapMapper.delMergeTagMapForId(delBean1);
            if (flag <= 0) {
                return FAILE;
            }
            MergeTagBean delBean2 = new MergeTagBean();
            delBean2.setId(bean.getMergeTagId());
            flag = mergeTagMapper.delMergeTagForId(delBean2);
            if (flag <= 0) {
                return FAILE;
            }
            UserMergeTagTagFormBean delBean3 = new UserMergeTagTagFormBean();
            delBean3.setMergeTagId(bean.getMergeTagId());
            flag = userMergeTagMapper.delUserMergeTagMapForId(delBean3, loginUser);
            if (flag <= 0) {
                return FAILE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== mergeTagMapper@err:{} ====", e);
        }
        return SUCCESS;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public MergeTagBean queryMergeTagById(long id) {
        MergeTagBean bean = null;
        try {
            bean = mergeTagMapper.queryMergeTagById(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryMergeTagById@err:{} ====", e);
        }
        return bean;
    }

    @Override
    public Map<String, Object> queryForAuthTag(MergeTagBean mbean, Bean bean, UserBean loginUser, RoleBean roleBean) {
        Map<String, Object> allTags = new HashMap<>();
        Long roleId = roleBean.getId();
        if (roleId == 1) {//超级管理员
            List<MergeTagBean> tags = mergeTagMapper.getMergeTag(mbean, bean);
            int count = mergeTagMapper.getMergeTagCount(mbean, bean);
            allTags.put("tags", tags);
            allTags.put("count", count);
        }
        if (roleId == 2 || roleId == 3) {//2普通管理员、3普通用户
            List<MergeTagBean> ruTags = mergeTagMapper.getURMergeTag(mbean, bean, loginUser, roleBean);
            int count = mergeTagMapper.getURMergeTagCount(mbean, bean, loginUser, roleBean);
            allTags.put("tags", ruTags);
            allTags.put("count", count);
        }

        return allTags;
    }

    @Override
    public List<UserMergeTagTagFormBean> updateQueryMergeTag(MergeTagBean mergeTagBean, UserBean loginUser,
                                                             RoleBean roleBean) {
        List<UserMergeTagTagFormBean> beans = null;
        try {
            beans = userMergeTagTagMapper.updateQueryMergeTag(mergeTagBean, loginUser, roleBean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updateQueryMergeTag@err:{} ====", e);
        }
        return beans;
    }

    @Override
    public MergeTagBean queryUpdateMergeTagData(MergeTagBean bean, UserBean loginUser, RoleBean roleBean) {
        try {
            bean = mergeTagMapper.queryMergeTagById(bean.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryUpdateMergeTagData@err:{} ====", e);
        }
        return bean;
    }

    @Override
    public MergeTagBean getBasicTagNum(Long id) {
        MergeTagBean tag = null;
        try {
            tag = dataMapper.getBasicTagNum(id);
            logger.info("==== getBasicTagNum@exec:{} ====", tag);
        } catch (Exception e) {
            logger.error("==== getBasicTagNum@err:{} ====", e);
        }
        return tag;
    }

    @Override
    public MergeTagBean isExistMergeTagForName(MergeTagBean bean, UserBean loginUser, RoleBean roleId) {
        MergeTagBean tag = null;
        try {
            tag = mergeTagMapper.isExistMergeTagForName(bean, loginUser, roleId);
            logger.info("==== isExistMergeTagForName@exec:{} ====", tag);
        } catch (Exception e) {
            logger.error("==== isExistMergeTagForName@err:{} ====", e);
        }
        return tag;
    }

    // 判断权限
    private Boolean isAdministrator() {
        try {
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            if (Objects.nonNull(curUser)) {
                List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
                Long roleId = roleMaps.get(0).getRoleId();
                // 超级管理员
                if (roleId.equals(1L)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("==== examine @result:服务器错误 ====");
            return false;
        }
        return false;
    }

}
