package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.UserResMapBean;
import cn.itcast.tag.web.user.mapper.UserResMapMapper;
import cn.itcast.tag.web.user.service.UserResMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author mengyao
 */
@Service
@Transactional
public class UserResMapServiceImpl implements UserResMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserResMapMapper userResMapMapper;

    @Override
    public Boolean addUserResMap(UserResMapBean bean) {
        int state = 0;
        try {
            state = userResMapMapper.addUserResMap(bean);
            logger.info("==== addUserResMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addUserResMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    public Boolean delUserResMap(UserResMapBean bean) {
        int state = 0;
        try {
            state = userResMapMapper.delUserResMap(bean);
            logger.info("==== delUserResMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delUserResMap@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    @Override
    public Boolean updateUserResMap(UserResMapBean bean) {
        int state = 0;
        try {
            state = userResMapMapper.updateUserResMap(bean);
            logger.info("==== updateUserResMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateUserResMap@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    @Override
    public UserResMapBean queryForId(UserResMapBean bean) {
        UserResMapBean userRes = null;
        try {
            userRes = userResMapMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", userRes);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return userRes;
    }

}
