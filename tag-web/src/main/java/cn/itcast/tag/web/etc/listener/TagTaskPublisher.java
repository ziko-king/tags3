package cn.itcast.tag.web.etc.listener;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.bean.TagTaskBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 标签任务发布
 *
 * @author mengyao
 */
@Component
public class TagTaskPublisher {

    private Logger logger = LogManager.getLogger(getClass());


    @Resource
    private ApplicationContext applicationContext;

    public void publish(TagTaskBean bean) {
        applicationContext.publishEvent(new TagTaskEvent(this, bean));
        logger.info("==== 发布标签任务:{} ====", bean);
    }

    public void publish(EngineBean bean) {
        applicationContext.publishEvent(new TagTaskEvent(this, bean));
        logger.info("==== 发布标签任务:{} ====", bean);
    }
}
