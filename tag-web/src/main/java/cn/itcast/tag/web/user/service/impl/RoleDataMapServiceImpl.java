/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.RoleDataMapBean;
import cn.itcast.tag.web.user.mapper.RoleDataMapMapper;
import cn.itcast.tag.web.user.service.RoleDataMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaocs
 *
 */
@Service
@Transactional
public class RoleDataMapServiceImpl implements RoleDataMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RoleDataMapMapper roleDataMapMapper;

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleDataMapService#addRoleDataMap(com.mengyao.tag.user.bean.RoleDataMapBean)
     */
    @Override
    public Boolean addRoleDataMap(RoleDataMapBean bean) {
        int state = 0;
        List<RoleDataMapBean> beans = new ArrayList<>();
        try {
            beans.add(bean);
            state = roleDataMapMapper.addRoleDataMap(beans);
            logger.info("==== addRoleDataMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addRoleDataMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    public Boolean addRoleDataMap(List<RoleDataMapBean> beans) {
        int state = 0;
        try {
            state = roleDataMapMapper.addRoleDataMap(beans);
            logger.info("==== addRoleDataMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addRoleDataMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleDataMapService#delRoleDataMapForId(com.mengyao.tag.user.bean.RoleDataMapBean)
     */
    @Override
    public Boolean delRoleDataMapForId(RoleDataMapBean bean) {
        int state = 0;
        try {
            state = roleDataMapMapper.delRoleDataMapForId(bean);
            logger.info("==== delRoleDataMapForId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delRoleDataMapForId@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleDataMapService#delRoleDataMapForRoleId(com.mengyao.tag.user.bean.RoleDataMapBean)
     */
    @Override
    public Boolean delRoleDataMapForRoleId(RoleDataMapBean bean) {
        int state = 0;
        try {
            state = roleDataMapMapper.delRoleDataMapForRoleId(bean);
            logger.info("==== delRoleDataMapForRoleId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delRoleDataMapForRoleId@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleDataMapService#updateRoleDataMap(com.mengyao.tag.user.bean.RoleDataMapBean)
     */
    @Override
    public Boolean updateRoleDataMap(RoleDataMapBean bean) {
        int state = 0;
        try {
            state = roleDataMapMapper.updateRoleDataMap(bean);
            logger.info("==== updateRoleDataMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateRoleDataMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleDataMapService#queryForId(com.mengyao.tag.user.bean.RoleDataMapBean)
     */
    @Override
    public RoleDataMapBean queryForId(RoleDataMapBean bean) {
        RoleDataMapBean roleDataMap = null;
        try {
            roleDataMap = roleDataMapMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", roleDataMap);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return roleDataMap;
    }

}
