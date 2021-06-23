package cn.itcast.tag.web.engine.mapper;

import cn.itcast.tag.web.engine.bean.RuleBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RuleDao
 *
 * @author mengyao
 */
@Repository
public interface RuleMapper {

    /**
     * 新增标签规则
     *
     * @param bean
     * @return
     */
    public int addRule(RuleBean bean);

    /**
     * 根据规则ID删除
     *
     * @param bean
     * @return
     */
    public int delRuleForId(RuleBean bean);

    /**
     * 修改规则状态
     *
     * @param bean
     * @return
     */
    public int updRuleForState(RuleBean bean);

    /**
     * 修改规则名称
     *
     * @param bean
     * @return
     */
    public int updRuleForName(RuleBean bean);

    /**
     * 根据标签ID更新规则
     *
     * @param bean
     * @return
     */
    public int updRuleForTagId(RuleBean bean);

    /**
     * 根据规则ID查询
     *
     * @param bean
     * @return
     */
    public RuleBean queryRuleForId(RuleBean bean);

    /**
     * 根据标签ID查询规则
     *
     * @param bean
     * @return
     */
    public List<RuleBean> queryRuleForTagId(RuleBean bean);

    /**
     * 查询所有规则
     *
     * @param bean
     * @return
     */
    public List<RuleBean> queryRuleForScan(RuleBean bean);

    /**
     * 根据标签ID、标签类型查询规则
     *
     * @param bean
     * @return
     */
    public RuleBean queryRuleForTagIdAndType(RuleBean bean);

}
