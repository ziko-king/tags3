package cn.itcast.tag.web.engine.service.impl;

import cn.itcast.tag.web.engine.bean.ModelBean;
import cn.itcast.tag.web.engine.mapper.ModelMapper;
import cn.itcast.tag.web.engine.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ModelServiceImpl
 *
 * @author mengyao
 */
@Service
@Transactional
public class ModelServiceImpl implements ModelService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ModelMapper modelMapper;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean addModel(ModelBean bean) {
        int state = 0;
        try {
            state = modelMapper.addModel(bean);
            logger.info("==== addModel@exec:{} ====", bean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addModel@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delModel(ModelBean bean) {
        int state = 0;
        try {
            state = modelMapper.delModelForId(bean);
            logger.info("==== delModel@exec:{} ====", state);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delModel@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delModelTag(ModelBean bean) {
        int state = 0;
        try {
            state = modelMapper.delModelForTagId(bean);
            logger.info("==== delModelTag@exec:{} ====", state);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delModelTag@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean logicDelModel(ModelBean bean) {
        int state = 0;
        try {
            //设置为已删除状态
            bean.setState(6);
            state = modelMapper.updModelForState(bean);
            logger.info("==== logicDelModel@exec:{} ====", state);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== logicDelModel@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updModelForName(ModelBean bean) {
        int state = 0;
        try {
            state = modelMapper.updModelForName(bean);
            logger.info("==== updModelForName@exec:{} ====", state);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updModelForName@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @Override
    public ModelBean queryForId(ModelBean bean) {
        ModelBean bean_ = null;
        try {
            bean_ = modelMapper.queryModelForId(bean);
            logger.info("==== queryForId@exec:{} ====", bean_);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForId@err:{} ====", e);
            throw e;
        }
        return bean_;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @Override
    public List<ModelBean> queryForTag(ModelBean bean) {
        List<ModelBean> beans = new ArrayList<ModelBean>();
        try {
            beans = modelMapper.queryModelForTagId(bean);
            logger.info("==== queryForTag@exec:{} ====", beans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForTag@err:{} ====", e);
            throw e;
        }
        return beans;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @Override
    public List<ModelBean> queryForScan(ModelBean bean) {
        List<ModelBean> beans = new ArrayList<ModelBean>();
        try {
            beans = modelMapper.queryModelForScan(bean);
            logger.info("==== queryForScan@exec:{} ====", beans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForScan@err:{} ====", e);
            throw e;
        }
        return beans;
    }

}
