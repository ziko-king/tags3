/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.RoleResMapBean;
import cn.itcast.tag.web.user.mapper.RoleResMapMapper;
import cn.itcast.tag.web.user.service.RoleResMapService;
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
public class RoleResMapServiceImpl implements RoleResMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RoleResMapMapper roleResMapMapper;

    @Override
    public Boolean addRoleResMap(RoleResMapBean bean) {
        int state = 0;
        List<RoleResMapBean> beans = new ArrayList<>();
        try {
            beans.add(bean);
            state = roleResMapMapper.addRoleResMap(beans);
            logger.info("==== addRoleResMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addRoleResMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleResMapperService#addUserRoleMap(java.util.List)
     */
    @Override
    public Boolean addRoleResMap(List<RoleResMapBean> beans) {
        int state = 0;
        try {
            state = roleResMapMapper.addRoleResMap(beans);
            logger.info("==== addRoleResMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addRoleResMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleResMapperService#delRoleResMapForId(com.mengyao.tag.user.bean.RoleResMapBean)
     */
    @Override
    public Boolean delRoleResMapForId(RoleResMapBean bean) {
        int state = 0;
        try {
            state = roleResMapMapper.delRoleResMapForId(bean);
            logger.info("==== delRoleResMapForId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delRoleResMapForId@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    @Override
    public Boolean delRoleResMapForRoleId(RoleResMapBean bean) {
        int state = 0;
        try {
            state = roleResMapMapper.delRoleResMapForRoleId(bean);
            logger.info("==== delRoleResMapForRoleId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delRoleResMapForRoleId@err:{} ====", e);
            throw e;
        }
        return state >= 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleResMapperService#updateRoleResMap(com.mengyao.tag.user.bean.RoleResMapBean)
     */
    @Override
    public Boolean updateRoleResMap(RoleResMapBean bean) {
        int state = 0;
        try {
            state = roleResMapMapper.updateRoleResMap(bean);
            logger.info("==== updateRoleResMap@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateRoleResMap@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.RoleResMapperService#queryForId(com.mengyao.tag.user.bean.RoleResMapBean)
     */
    @Override
    public RoleResMapBean queryForId(RoleResMapBean bean) {
        RoleResMapBean roleResMap = null;
        try {
            roleResMap = roleResMapMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", roleResMap);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return roleResMap;
    }

}
