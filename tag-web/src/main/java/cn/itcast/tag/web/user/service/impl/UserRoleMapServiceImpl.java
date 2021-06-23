/**
 * 项目名称：mengyao
 * 创建日期：2018年6月10日
 * 修改历史：
 * 1、[2018年6月10日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.mapper.UserRoleMapMapper;
import cn.itcast.tag.web.user.service.UserRoleMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zhaocs
 *
 */
@Service
@Transactional
public class UserRoleMapServiceImpl implements UserRoleMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserRoleMapMapper userRoleMapMapper;

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserRoleMapService#addUserRoleMap(com.mengyao.tag.user.bean.UserRoleMapBean)
     */
    @Override
    public Boolean addUserRoleMap(UserRoleMapBean bean) {
        int state = 0;
        try {
            state = userRoleMapMapper.addUserRoleMap(bean);
            logger.info("==== addUserRoleMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addUserRoleMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserRoleMapService#delUserRoleMapForId(com.mengyao.tag.user.bean.UserRoleMapBean)
     */
    @Override
    public Boolean delUserRoleMapForId(UserRoleMapBean bean) {
        int state = 0;
        try {
            state = userRoleMapMapper.delUserRoleMapForId(bean);
            logger.info("==== delUserRoleMapForId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delUserRoleMapForId@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    @Override
    public Boolean delUserRoleMap(UserRoleMapBean bean) {
        int state = 0;
        try {
            state = userRoleMapMapper.delUserRoleMap(bean);
            logger.info("==== delUserRoleMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delUserRoleMap@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserRoleMapService#updateUserRoleMap(com.mengyao.tag.user.bean.UserRoleMapBean)
     */
    @Override
    public Boolean updateUserRoleMap(UserRoleMapBean bean) {
        int state = 0;
        try {
            state = userRoleMapMapper.updateUserRoleMap(bean);
            logger.info("==== updateUserRoleMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateUserRoleMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    public Boolean updateUserRoleMapForUserId(UserRoleMapBean bean) {
        int state = 0;
        try {
            state = userRoleMapMapper.updateUserRoleMapForUserId(bean);
            logger.info("==== updateUserRoleMapForUserId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateUserRoleMapForUserId@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserRoleMapService#queryForId(com.mengyao.tag.user.bean.UserRoleMapBean)
     */
    @Override
    public UserRoleMapBean queryForId(UserRoleMapBean bean) {
        UserRoleMapBean userRole = null;
        try {
            userRole = userRoleMapMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", userRole);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return userRole;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserRoleMapService#queryForUserId(java.lang.Long)
     */
    @Override
    public UserRoleMapBean queryForUserId(Long userId) {
        UserRoleMapBean userRole = null;
        try {
            userRole = userRoleMapMapper.queryForUserId(userId);
            logger.info("==== queryForId@exec:{} ====", userRole);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return userRole;
    }

}
