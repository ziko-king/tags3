package cn.itcast.tag.web.etc.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author mengyao
 */
public class LogInterceptor implements HandlerInterceptor {

    private Logger logger = LogManager.getLogger(getClass());


    public LogInterceptor() {
        logger.info("==== 初始化系统日志拦截器 ====");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
            throws Exception {
        response.setContentType("text/html;charset=utf-8");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setBasePath(request);
        logger.info("==== {}:{} ====", getClientAddr(request), request.getRequestURL());
        return true;
    }

    /**
     * 设置FreeMaker页面使用的${basePath}
     *
     * @param request
     */
    protected void setBasePath(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        String basePath = scheme + "://" + serverName + ":" + port + path;
        request.setAttribute("basePath", basePath);
    }

    /**
     * 获取客户端真实IP
     *
     * @param request
     * @return
     */
    protected String getClientAddr(HttpServletRequest request) {
        String addr = request.getHeader("x-forwarded-for");
        if (addr == null || addr.length() == 0 || "unknown".equalsIgnoreCase(addr)) {
            addr = request.getHeader("Proxy-Client-IP");
        }
        if (addr == null || addr.length() == 0 || "unknown".equalsIgnoreCase(addr)) {
            addr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (addr == null || addr.length() == 0 || "unknown".equalsIgnoreCase(addr)) {
            addr = request.getRemoteAddr();
        }
        return addr;
    }

}
