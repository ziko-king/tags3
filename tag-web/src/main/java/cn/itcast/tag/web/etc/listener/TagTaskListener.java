package cn.itcast.tag.web.etc.listener;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.service.EngineService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 标签任务监听器
 *
 * @author mengyao
 */
@Component
public class TagTaskListener implements ApplicationListener<TagTaskEvent> {

    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private EngineService engineService;

    @Override
    public void onApplicationEvent(TagTaskEvent e) {
        EngineBean bean = e.getEbean();
        if (e.getEbean() != null) {
            // engineService.startEngine(bean);
            logger.info("==== 监听到标签任务事件:{} ====", bean);
        }
    }

}
