package cn.itcast.tag.web.user.service.impl;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.user.mapper.DataMapper;
import cn.itcast.tag.web.user.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据权限
 *
 * @author mengyao
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DataServiceImpl implements DataService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Resource
    private DataMapper dataMapper;

    @Override
    public List<BasicTagBean> getBasicTag() {
        List<BasicTagBean> tags = null;
        try {
            tags = dataMapper.getBasicTag();
            logger.info("==== getBasicTag@exec:{} ====", tags);
        } catch (Exception e) {
            logger.error("==== getBasicTag@err:{} ====", e);
        }
        return tags;
    }

    @Override
    public List<MergeTagBean> getMergeTag() {
        List<MergeTagBean> tags = null;
        try {
            tags = dataMapper.getMergeTag();
            logger.info("==== getMergeTag@exec:{} ====", tags);
        } catch (Exception e) {
            logger.error("==== getMergeTag@err:{} ====", e);
        }
        return tags;
    }

    @Override
    public List<BasicTagBean> getUserBasicTag(long userId) {
        List<BasicTagBean> tags = null;
        try {
            tags = dataMapper.getUserBasicTag(userId);
            logger.info("==== getUserBasicTag@exec:{} ====", tags);
        } catch (Exception e) {
            logger.error("==== getUserBasicTag@err:{} ====", e);
        }
        return tags;
    }

    @Override
    public List<BasicTagBean> getRoleBasicTag(long roleId) {
        List<BasicTagBean> tags = null;
        try {
            tags = dataMapper.getRoleBasicTag(roleId);
            logger.info("==== getUserBasicTag@exec:{} ====", tags);
        } catch (Exception e) {
            logger.error("==== getUserBasicTag@err:{} ====", e);
        }
        return tags;
    }

    @Override
    public List<MergeTagBean> getUserMergeTag(long userId) {
        List<MergeTagBean> tags = null;
        try {
            tags = dataMapper.getUserMergeTag(userId);
            logger.info("==== getUserMergeTag@exec:{} ====", tags);
        } catch (Exception e) {
            logger.error("==== getUserMergeTag@err:{} ====", e);
        }
        return tags;
    }

    @Override
    public List<MergeTagBean> getRoleMergeTag(long roleId) {
        List<MergeTagBean> tags = null;
        try {
            tags = dataMapper.getRoleMergeTag(roleId);
            logger.info("==== getRoleMergeTag@exec:{} ====", tags);
        } catch (Exception e) {
            logger.error("==== getRoleMergeTag@err:{} ====", e);
        }
        return tags;
    }

}
