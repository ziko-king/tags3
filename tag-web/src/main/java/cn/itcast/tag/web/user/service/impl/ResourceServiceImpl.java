/**
 * 项目名称：mengyao
 * 创建日期：2018年5月30日
 * 修改历史：
 * 1、[2018年5月30日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.ResourceBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.form.UserRoleResForm;
import cn.itcast.tag.web.user.mapper.ResourceMapper;
import cn.itcast.tag.web.user.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author zhaocs
 *
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource

    private ResourceMapper resourceMapper;

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.ResourceService#addResource(com.mengyao.tag.user.bean.ResourceBean)
     */
    @Override
    @Cacheable(value = "userCache", key = "'ResourceKey'+#bean.id")
    public boolean addResource(ResourceBean bean) {
        int state = 0;
        try {
            state = resourceMapper.addResource(bean);
            logger.info("==== addResource@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addResource@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.ResourceService#delResourceForId(com.mengyao.tag.user.bean.ResourceBean)
     */
    // 删除缓存
    @CacheEvict(value = "userCache", key = "'ResourceKey'+#bean.id")
    @Override
    public boolean delResourceForId(ResourceBean bean) {
        int state = 0;
        try {
            state = resourceMapper.delResourceForId(bean);
            logger.info("==== delForResource@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delForResource@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.ResourceService#updateResource(com.mengyao.tag.user.bean.ResourceBean)
     */
    // 更新userCache缓存  
    @CachePut(value = "userCache", key = "'ResourceKey'+#bean.id")
    @Override
    public boolean updateResource(ResourceBean bean) {
        int state = 0;
        try {
            state = resourceMapper.updateResource(bean);
            logger.info("==== updateResource@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateResource@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    /* (non-Javadoc)
     * @see com.mengyao.tag.user.service.ResourceService#queryForId(com.mengyao.tag.user.bean.ResourceBean)
     */
    @Override
    @Cacheable(value = "userCache", key = "'ResourceKey'+#bean.id", unless = "#result == null")
    public ResourceBean queryForId(ResourceBean bean) {
        ResourceBean resource = null;
        try {
            resource = resourceMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", resource);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
            throw e;
        }
        return resource;
    }

    @Override
    @Cacheable(value = "userCache", key = "'ResourceKeyForPId'+#pid", unless = "#result == null")
    public List<ResourceBean> queryForPId(long pid) {
        List<ResourceBean> beans = Collections.emptyList();
        try {
            beans = resourceMapper.queryForPId(pid);
            logger.info("==== queryForPId@exec:{} ====", pid);
        } catch (Exception e) {
            logger.error("==== queryForPId@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    public List<ResourceBean> queryForType(int type) {
        List<ResourceBean> beans = Collections.emptyList();
        try {
            beans = resourceMapper.queryForType(type);
            logger.info("==== queryForPId@exec:{} ====", type);
        } catch (Exception e) {
            logger.error("==== queryForPId@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    @Override
    @Cacheable(value = "oneDay", unless = "#result == null")
    public List<ResourceBean> queryForConditions(ResourceBean bean) {
        List<ResourceBean> beans = Collections.emptyList();
        try {
            beans = resourceMapper.queryForConditions(bean);
            logger.info("==== queryForConditions@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== queryForConditions@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    @Override
    public int queryCountForConditions(ResourceBean bean) {
        int count = 0;
        try {
            count = resourceMapper.queryCountForConditions(bean);
            logger.info("==== queryCountForConditions@exec:{} ====", count);
        } catch (Exception e) {
            logger.error("==== queryCountForConditions@err:{} ====", e);
            throw e;
        }
        return count;
    }

    public List<ResourceBean> queryForRoleBean(RoleBean bean) {
        List<ResourceBean> beans = Collections.emptyList();
        try {
            beans = resourceMapper.queryForRoleBean(bean);
            logger.info("==== queryForRoleBean@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== queryForRoleBean@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    @Override
    @Cacheable(value = "userCache", key = "'ResourceKey'+#bean.userId+#bean.roleId", unless = "#result == null")
    public List<ResourceBean> queryForUserRoleMap(UserRoleMapBean bean) {
        List<ResourceBean> beans = Collections.emptyList();
        try {
            beans = resourceMapper.queryForUserRoleMap(bean);
            logger.info("==== queryForUserRoleMap@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== queryForUserRoleMap@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    @Override
    @Cacheable(value = "userCache", key = "'ResourceKey'+#bean.userId+#bean.roleId+#bean.resourceId", unless = "#result == null")
    public List<ResourceBean> queryForUserRoleResId(UserRoleResForm bean) {
        List<ResourceBean> beans = Collections.emptyList();
        try {
            beans = resourceMapper.queryForUserRoleResId(bean);
            logger.info("==== queryForUserRoleResId@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== queryForUserRoleResId@err:{} ====", e);
            throw e;
        }
        return beans;
    }

}
