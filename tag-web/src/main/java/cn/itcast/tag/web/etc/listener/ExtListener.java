package cn.itcast.tag.web.etc.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 扩展监听器（缓存模块、邮件模块）
 *
 * @author mengyao
 */
public class ExtListener implements ServletContextListener {

    private Logger logger = LogManager.getLogger(getClass());


    public ExtListener() {
        logger.info("==== 初始化系统扩展监听器 ====");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        servletContext.setAttribute("ext", "ext");
    }

}
