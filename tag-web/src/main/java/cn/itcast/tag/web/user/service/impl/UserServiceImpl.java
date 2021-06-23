package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.ResourceBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.service.UserService;
import cn.itcast.tag.web.user.mapper.UserMapper;
import cn.itcast.tag.web.user.service.ResourceService;
import cn.itcast.tag.web.user.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * UserService
 *
 * @author mengyao
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private ResourceService resourceService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean addUser(UserBean bean) {
        int state = 0;
        try {
            state = userMapper.addUser(bean);
            logger.info("==== addUser@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addUser@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    // 删除缓存
    @CacheEvict(value = "userCache", key = "'UserKey'+#bean.id")
    @Override
    public boolean delForUser(UserBean bean) {
        int state = 0;
        try {
            state = userMapper.delUserForId(bean);
            logger.info("==== delForUser@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delForUser@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    // 更新userCache缓存
    @CachePut(value = "userCache", key = "'UserKey'+#bean.username")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updateForPassword(UserBean bean) {
        int state = 0;
        try {
            state = userMapper.updateForPassword(bean);
            logger.info("==== updateForPassword@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateForPassword@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @CachePut(value = "userCache", key = "'UserKey'+#bean.username")
    @Override
    public boolean update(UserBean bean) {
        int state = 0;
        try {
            state = userMapper.update(bean);
            logger.info("==== update@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== update@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Cacheable(value = "userCache", key = "'UserKey'+#bean.username+#bean.password", unless = "#result == null")
    @Override
    public UserBean login(UserBean bean) {
        UserBean user = null;
        try {
            user = userMapper.queryForUsernameAndPassword(bean);
            logger.info("==== login@exec:{} ====", user);
        } catch (Exception e) {
            logger.error("==== login@err:{} ====", e);
        }
        return user;
    }

    @Cacheable(value = "userCache", key = "'UserKey'+#bean.id", unless = "#result == null")
    @Override
    public UserBean queryForId(UserBean bean) {
        UserBean user = null;
        try {
            user = userMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", user);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
        }
        return user;
    }

    @Cacheable(value = "userCache", key = "'UserKey'+#bean.username", unless = "#result == null")
    @Override
    public UserBean queryForUsername(UserBean bean) {
        UserBean user = null;
        try {
            user = userMapper.queryForUsername(bean);
            logger.info("==== queryForUsername@exec:{} ====", user);
        } catch (Exception e) {
            logger.error("==== queryForUsername@err:{} ====", e);
        }
        return user;
    }

    @Override
    // 如果加缓存需要加组织id
//    @Cacheable(value="oneDay", key="#root.targetClass + #root.methodName +#bean.name", unless="#result == null")
    public List<UserBean> queryForConditions(UserBean bean) {
        List<UserBean> beans = null;
        try {
            beans = userMapper.queryForConditions(bean);
            logger.info("==== queryForConditions@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== queryForConditions@err:{} ====", e);
        }
        return beans;
    }

    //    @Cacheable(value="oneDay", key="#root.targetClass + #root.methodName +#bean.name", unless="#result == null")
    public int queryCountForConditions(UserBean bean) {
        int count = 0;
        try {
            count = userMapper.queryCountForConditions(bean);
            logger.info("==== queryCountForConditions@exec:{} ====", count);
        } catch (Exception e) {
            logger.error("==== queryCountForConditions@err:{} ====", e);
        }
        return count;
    }

    @Cacheable(value = "userCache", key = "'UserResKey'+#bean.userId")
    public List<ResourceBean> queryCurrentResources(UserRoleMapBean bean) {
        if (bean.getUserId() == null || bean.getRoleId() == null) return Collections.emptyList();

        // 获取当前用户资源信息
        List<ResourceBean> beans = resourceService.queryForUserRoleMap(bean);
        if (beans == null || beans.isEmpty()) return Collections.emptyList();

        return beans;
    }

    @Cacheable(value = "userCache", key = "#root.targetClass + #root.methodName", unless = "#result == null")
    public List<ResourceBean> queryAllResources() {
        // 获取当前用户详细信息
        ResourceBean bean = new ResourceBean();
        bean.setPr(0);
        List<ResourceBean> beans = resourceService.queryForConditions(bean);
        if (beans == null || beans.isEmpty()) return Collections.emptyList();

        return beans;
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public Boolean clearUserCache() {
        logger.info("==== 清空缓存：userCache ====");
        return true;
    }

    @CacheEvict(value = {"userCache", "halfHour", "hour", "oneDay"}, allEntries = true)
    public Boolean clearCache() {
        logger.info("==== 清空缓存：userCache、halfHour、hour、oneDay  ====");
        return true;
    }

}
