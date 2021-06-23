package cn.itcast.tag.web.engine.mapper;


import cn.itcast.tag.web.engine.bean.MetaDataBean;

/**
 * MetaDataDao
 *
 * @author liuchengli
 */
public interface MetaDataMapper {

    /**
     * 新增元数据
     *
     * @param bean
     * @return
     */
    public int addMetaData(MetaDataBean bean);

    /**
     * 根据ID删除元数据
     *
     * @param bean
     * @return
     */
    public int delMetaDataForId(MetaDataBean bean);

    /**
     * 修改元数据
     *
     * @param bean
     * @return
     */
    public int updMetaDataForId(MetaDataBean bean);

    /**
     * 根据TagID查询模型
     *
     * @param bean
     * @return
     */
    public MetaDataBean get(MetaDataBean bean);

    /**
     * 根据ID查询模型
     *
     * @param bean
     * @return
     */
    public MetaDataBean queryMetaDataForId(MetaDataBean bean);

}
