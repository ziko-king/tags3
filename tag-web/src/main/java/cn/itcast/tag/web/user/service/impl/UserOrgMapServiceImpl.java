/**
 * 项目名称：mengyao
 * 创建日期：2018年6月15日
 * 修改历史：
 * 1、[2018年6月15日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.UserOrgMapBean;
import cn.itcast.tag.web.user.mapper.UserOrgMapMapper;
import cn.itcast.tag.web.user.service.UserOrgMapService;
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
public class UserOrgMapServiceImpl implements UserOrgMapService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserOrgMapMapper userOrgMapMapper;

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserOrgMapService#addUserOrgMap(com.mengyao.tag.user.bean.UserOrgMapBean)
     */
    @Override
    public Boolean addUserOrgMap(UserOrgMapBean bean) {
        int state = 0;
        try {
            state = userOrgMapMapper.addUserOrgMap(bean);
            logger.info("==== addUserOrgMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addUserOrgMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserOrgMapService#delUserOrgMapForId(com.mengyao.tag.user.bean.UserOrgMapBean)
     */
    @Override
    public Boolean delUserOrgMapForId(UserOrgMapBean bean) {
        int state = 0;
        try {
            state = userOrgMapMapper.delUserOrgMapForId(bean);
            logger.info("==== delUserOrgMapForId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delUserOrgMapForId@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserOrgMapService#delUserOrgMap(com.mengyao.tag.user.bean.UserOrgMapBean)
     */
    @Override
    public Boolean delUserOrgMap(UserOrgMapBean bean) {
        int state = 0;
        try {
            state = userOrgMapMapper.delUserOrgMap(bean);
            logger.info("==== delUserOrgMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delUserOrgMap@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserOrgMapService#updateUserOrgMap(com.mengyao.tag.user.bean.UserOrgMapBean)
     */
    @Override
    public Boolean updateUserOrgMap(UserOrgMapBean bean) {
        int state = 0;
        try {
            state = userOrgMapMapper.updateUserOrgMap(bean);
            logger.info("==== updateUserOrgMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateUserOrgMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.UserOrgMapService#queryForId(com.mengyao.tag.user.bean.UserOrgMapBean)
     */
    @Override
    public UserOrgMapBean queryForId(UserOrgMapBean bean) {
        UserOrgMapBean userOrg = null;
        try {
            userOrg = userOrgMapMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", userOrg);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return userOrg;
    }

}
