package cn.itcast.tag.web.engine.service.impl;

import cn.itcast.tag.web.engine.bean.RuleBean;
import cn.itcast.tag.web.engine.mapper.RuleMapper;
import cn.itcast.tag.web.engine.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * RuleServiceImpl
 *
 * @author mengyao
 */
@Service
@Transactional
public class RuleServiceImpl implements RuleService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RuleMapper ruleMapper;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean addRule(RuleBean bean) {
        int state = 0;
        try {
            state = ruleMapper.addRule(bean);
            logger.info("==== addRule@exec:{} ====", bean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addRule@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delRule(RuleBean bean) {
        int state = 0;
        try {
            state = ruleMapper.delRuleForId(bean);
            logger.info("==== delRule@exec:{} ====", bean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delRule@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean logicDelRule(RuleBean bean) {
        int state = 0;
        try {
            ////设置为已删除状态
            bean.setState(6);
            state = ruleMapper.updRuleForState(bean);
            logger.info("==== logicDelRule@exec:{} ====", bean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== logicDelRule@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updRuleForName(RuleBean bean) {
        int state = 0;
        try {
            state = ruleMapper.updRuleForName(bean);
            logger.info("==== updRuleForName@exec:{} ====", bean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updRuleForName@err:{} ====", e);
            throw e;
        }
        return state > 0;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    @Override
    public RuleBean queryForId(RuleBean bean) {
        RuleBean bean_ = null;
        try {
            bean_ = ruleMapper.queryRuleForId(bean);
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
    public List<RuleBean> queryForTag(RuleBean bean) {
        List<RuleBean> beans = new ArrayList<RuleBean>();
        try {
            beans = ruleMapper.queryRuleForTagId(bean);
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
    public List<RuleBean> queryForScan(RuleBean bean) {
        List<RuleBean> beans = new ArrayList<RuleBean>();
        try {
            beans = ruleMapper.queryRuleForScan(bean);
            logger.info("==== queryForScan@exec:{} ====", beans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForScan@err:{} ====", e);
            throw e;
        }
        return beans;
    }

}
