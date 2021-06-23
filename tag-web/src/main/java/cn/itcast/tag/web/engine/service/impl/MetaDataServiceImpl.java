package cn.itcast.tag.web.engine.service.impl;

import cn.itcast.tag.web.engine.bean.MetaDataBean;
import cn.itcast.tag.web.engine.mapper.MetaDataMapper;
import cn.itcast.tag.web.engine.service.MetaDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * MetaDataServiceImpl
 *
 * @author liuchengli
 */
@Service
@Transactional
public class MetaDataServiceImpl implements MetaDataService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MetaDataMapper metaDataMapper;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean addMetaData(MetaDataBean bean) {
        int state = 0;
        try {
            state = metaDataMapper.addMetaData(bean);
            logger.info("==== addMetaData@exec:{} ====", bean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addMetaData@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delMetaData(MetaDataBean bean) {
        int state = 0;
        try {
            state = metaDataMapper.delMetaDataForId(bean);
            logger.info("==== delMetaData@exec:{} ====", state);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delMetaData@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updMetaDataForId(MetaDataBean bean) {
        int state = 0;
        try {
            state = metaDataMapper.updMetaDataForId(bean);
            logger.info("==== updMetaDataForId@exec:{} ====", state);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updMetaDataForId@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @Override
    public MetaDataBean queryForId(MetaDataBean bean) {
        MetaDataBean bean_ = null;
        try {
            bean_ = metaDataMapper.queryMetaDataForId(bean);
            logger.info("==== queryForId@exec:{} ====", bean_);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForId@err:{} ====", e);
            throw e;
        }
        return bean_;
    }

    @Override
    public boolean logicDelMetaData(MetaDataBean bean) {
        return false;
    }


}
