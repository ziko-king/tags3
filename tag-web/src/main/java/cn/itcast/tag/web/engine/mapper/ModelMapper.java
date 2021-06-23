package cn.itcast.tag.web.engine.mapper;

import cn.itcast.tag.web.engine.bean.ModelBean;

import java.util.List;

/**
 * ModelDao
 *
 * @author mengyao
 */
public interface ModelMapper {

    /**
     * 新增模型
     *
     * @param bean
     * @return
     */
    public int addModel(ModelBean bean);

    /**
     * 根据ID删除模型
     *
     * @param bean
     * @return
     */
    public int delModelForId(ModelBean bean);

    /**
     * 根据TagID删除模型
     *
     * @param bean
     * @return
     */
    public int delModelForTagId(ModelBean bean);

    /**
     * 修改模型状态
     *
     * @param bean
     * @return
     */
    public int updModelForState(ModelBean bean);

    /**
     * 修改模型名称
     *
     * @param bean
     * @return
     */
    public int updModelForName(ModelBean bean);

    /**
     * 根据标签ID修改模型
     *
     * @param bean
     * @return
     */
    public int updModelForTagId(ModelBean bean);

    /**
     * 根据ID查询模型
     *
     * @param bean
     * @return
     */
    public ModelBean queryModelForId(ModelBean bean);

    /**
     * 根据TagID查询模型
     *
     * @param bean
     * @return
     */
    public ModelBean get(ModelBean bean);

    /**
     * 根据标签ID查询模型
     *
     * @param bean
     * @return
     */
    public List<ModelBean> queryModelForTagId(ModelBean bean);

    /**
     * 查询所有模型
     *
     * @param bean
     * @return
     */
    public List<ModelBean> queryModelForScan(ModelBean bean);

    /**
     * 根据标签ID、标签类型查询模型
     *
     * @param bean
     * @return
     */
    public ModelBean queryModelForTagIdAndType(ModelBean bean);

}
