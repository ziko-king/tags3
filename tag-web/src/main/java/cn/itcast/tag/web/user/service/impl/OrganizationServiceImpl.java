package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.user.bean.OrganizationBean;
import cn.itcast.tag.web.user.mapper.OrganizationMapper;
import cn.itcast.tag.web.user.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private OrganizationMapper organizationMapper;

    @Override
    public Boolean addOrganization(OrganizationBean bean) {
        int state = 0;
        try {
            state = organizationMapper.addOrganization(bean);
            logger.info("==== addOrganization@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== addOrganization@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    @CacheEvict(value = "userCache", key = "'OrganizationKey'+#bean.id")
    public Boolean delOrganizationForId(OrganizationBean bean) {
        int state = 0;
        try {
            state = organizationMapper.delOrganizationForId(bean);
            logger.info("==== delOrganizationForId@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== delOrganizationForId@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    @CachePut(value = "userCache", key = "'OrganizationKey'+#bean.id")
    public Boolean updateOrganization(OrganizationBean bean) {
        int state = 0;
        try {
            state = organizationMapper.updateOrganization(bean);
            logger.info("==== updateOrganization@exec:{} ====", state);
        } catch (Exception e) {
            logger.error("==== updateOrganization@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Override
    @Cacheable(value = "userCache", key = "'OrganizationKey'+#bean.id", unless = "#result == null")
    public OrganizationBean queryForId(OrganizationBean bean) {
        OrganizationBean org = null;
        try {
            org = organizationMapper.queryForId(bean);
            logger.info("==== queryForId@exec:{} ====", org);
        } catch (Exception e) {
            logger.error("==== queryForId@err:{} ====", e);
            throw e;
        }
        return org;
    }

    @Override
    public List<OrganizationBean> query(OrganizationBean bean) {
        List<OrganizationBean> beans = null;
        try {
            beans = organizationMapper.query(bean);
            logger.info("==== query@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== query@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    @Override
    @Cacheable(value = "userCache", key = "'OrganizationKey'+#bean.pid", unless = "#result == null")
    public List<OrganizationBean> queryForPid(OrganizationBean bean) {
        List<OrganizationBean> beans = null;
        try {
            beans = organizationMapper.queryForPid(bean);
            logger.info("==== queryForPid@exec:{} ====", beans);
        } catch (Exception e) {
            logger.error("==== queryForPid@err:{} ====", e);
            throw e;
        }
        return beans;
    }

}
