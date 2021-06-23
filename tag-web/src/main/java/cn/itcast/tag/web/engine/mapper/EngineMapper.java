package cn.itcast.tag.web.engine.mapper;


import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.bean.ModelMetaDataBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MetaDataDao
 *
 * @author liuchengli
 */
@Repository
public interface EngineMapper {

    /**
     * 启动任务时新增监控信息
     *
     * @param bean
     * @return
     */
    public int addMonitorInfo(EngineBean bean);

    /**
     * 根据tagId 查询任务信息
     *
     * @param bean
     * @return
     */
    public List<EngineBean> queryEngineInfo(EngineBean bean);

    /**
     * 根据标签id查询启动任务所需数据
     *
     * @param bean
     * @return
     */
    public ModelMetaDataBean queryTaskArgs(EngineBean bean);

    /**
     * 更新状态
     *
     * @param bean
     */
    public void updateStatus(EngineBean bean);

    /**
     * 使用quartz提交任务时地更新jobid
     *
     * @param bean
     */
    public void updateStatusAndJobid(EngineBean bean);

}
