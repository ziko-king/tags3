/**
 * 项目名称：mengyao
 * 创建日期：2018年5月24日
 * 修改历史：
 * 1、[2018年5月24日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.mapper.RoleMapper;
import cn.itcast.tag.web.user.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaocs
 *
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RoleMapper roleMapper;

    @Override
    public boolean addRole(RoleBean bean) {
        int state = 0;
        try {
            state = roleMapper.addRole(bean);
            logger.info("==== addRole@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addRole@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    // 删除缓存
    @Override
    public boolean delForRole(RoleBean bean) {
        int state = 0;
        try {
            state = roleMapper.delRoleForId(bean);
            logger.info("==== delForRole@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delForRole@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    // 更新userCache缓存  
    @Override
    public Boolean updateRole(RoleBean bean) {
        int state = 0;
        try {
            state = roleMapper.updateRole(bean);
            logger.info("==== updateRole@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateRole@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    public RoleBean queryForId(RoleBean bean) {
        RoleBean role = null;
        try {
            role = roleMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", role);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
            throw e;
        }
        return role;
    }

    @Override
    public List<RoleBean> queryForConditions(RoleBean bean) {
        List<RoleBean> roles = null;
        try {
            roles = roleMapper.queryForConditions(bean);
            logger.info("==== queryForConditions@exec:{} ====", roles);
        } catch (Exception e) {
            logger.error("==== queryForConditions@err:{} ====", e);
            throw e;
        }
        return roles;
    }

    @Override
    public int queryCountForConditions(RoleBean bean) {
        int count = 0;
        try {
            count = roleMapper.queryCountForConditions(bean);
            logger.info("==== queryCountForConditions@exec:{} ====", count);
        } catch (Exception e) {
            logger.error("==== queryCountForConditions@err:{} ====", e);
            throw e;
        }
        return count;
    }

}
