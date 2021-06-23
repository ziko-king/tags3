package cn.itcast.tag.web.engine.service;

import cn.itcast.tag.web.engine.bean.MetaDataBean;

/**
 * MetaDataService
 *
 * @author liuchengli
 */
public interface MetaDataService {

    /**
     * 新增
     *
     * @param bean
     * @return
     */
    public boolean addMetaData(MetaDataBean bean);

    /**
     * 删除
     *
     * @param bean
     * @return
     */
    public boolean delMetaData(MetaDataBean bean);

    /**
     * 逻辑删除模型
     *
     * @param bean
     * @return
     */
    public boolean logicDelMetaData(MetaDataBean bean);

    /**
     * 修改
     *
     * @param bean
     * @return
     */
    public boolean updMetaDataForId(MetaDataBean bean);

    /**
     * 查询
     *
     * @param bean
     * @return
     */
    public MetaDataBean queryForId(MetaDataBean bean);


}
