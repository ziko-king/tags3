package cn.itcast.tag.web.engine.service;

import cn.itcast.tag.web.engine.bean.RuleBean;

import java.util.List;

/**
 * RuleService
 *
 * @author mengyao
 */
public interface RuleService {

    /**
     * 新增标签规则
     *
     * @param bean
     * @return
     */
    public boolean addRule(RuleBean bean);

    /**
     * 根据规则ID删除
     *
     * @param bean
     * @return
     */
    public boolean delRule(RuleBean bean);

    /**
     * 删除规则（逻辑）
     *
     * @param bean
     * @return
     */
    public boolean logicDelRule(RuleBean bean);

    /**
     * 修改规则名称
     *
     * @param bean
     * @return
     */
    public boolean updRuleForName(RuleBean bean);

    /**
     * 根据规则ID查询
     *
     * @param bean
     * @return
     */
    public RuleBean queryForId(RuleBean bean);

    /**
     * 根据标签ID查询规则
     *
     * @param bean
     * @return
     */
    public List<RuleBean> queryForTag(RuleBean bean);

    /**
     * 查询所有规则
     *
     * @param bean
     * @return
     */
    public List<RuleBean> queryForScan(RuleBean bean);

}
