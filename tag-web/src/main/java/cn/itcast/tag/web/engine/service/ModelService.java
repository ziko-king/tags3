package cn.itcast.tag.web.engine.service;

import cn.itcast.tag.web.engine.bean.ModelBean;

import java.util.List;

/**
 * ModelService
 *
 * @author mengyao
 */
public interface ModelService {

    /**
     * 新增模型
     *
     * @param bean
     * @return
     */
    public boolean addModel(ModelBean bean);

    /**
     * 删除模型
     *
     * @param bean
     * @return
     */
    public boolean delModel(ModelBean bean);

    /**
     * 根据TagId删除模型
     *
     * @param bean
     * @return
     */
    public boolean delModelTag(ModelBean bean);

    /**
     * 逻辑删除模型
     *
     * @param bean
     * @return
     */
    public boolean logicDelModel(ModelBean bean);

    /**
     * 修改模型名称
     *
     * @param bean
     * @return
     */
    public boolean updModelForName(ModelBean bean);

    /**
     * 根据ID查询模型
     *
     * @param bean
     * @return
     */
    public ModelBean queryForId(ModelBean bean);

    /**
     * 根据标签查询模型
     *
     * @param bean
     * @return
     */
    public List<ModelBean> queryForTag(ModelBean bean);

    /**
     * 查询模型列表
     *
     * @param bean
     * @return
     */
    public List<ModelBean> queryForScan(ModelBean bean);

}
