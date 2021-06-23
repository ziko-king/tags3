package cn.itcast.tag.web.etc.listener;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.bean.TagTaskBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEvent;

/**
 * 标签任务事件
 *
 * @author mengyao
 */
public class TagTaskEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1786431212848130733L;
    private Logger logger = LogManager.getLogger(getClass());


    private TagTaskBean bean;
    private EngineBean ebean;

    public TagTaskEvent(Object source, TagTaskBean bean) {
        super(source);
        this.bean = bean;
        logger.info("==== 已触发标签任务事件:{} ====", bean);
    }

    public TagTaskEvent(Object source, EngineBean ebean) {
        super(source);
        this.ebean = ebean;
        logger.info("==== 已触发标签任务事件:{} ====", ebean);
    }

    public TagTaskBean getBean() {
        return bean;
    }

    public EngineBean getEbean() {
        return ebean;
    }

}
