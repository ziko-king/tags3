/**
 * 项目名称：mengyao
 * 创建日期：2018年5月29日
 * 修改历史：
 * 1、[2018年5月29日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.commons.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author zhaocs
 * 基础Controller，处理公共的业务，如：异常
 */
public class BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @ExceptionHandler
    public String exception(HttpServletRequest request, Exception e) {
        logger.debug("==== exception@error ===={}", e);

        //对异常进行判断做相应的处理  
        if (e instanceof AuthorizationException) {
            return "redirect:/logout";
        }
        return null;
    }

    protected String getClassesDir(HttpServletRequest request) {
        String classesDir = Thread.currentThread().getContextClassLoader().getResource("").toString();
        // 如果classesDir为null时为非war包发布运行，以调试程序处理
        if (StringUtils.isEmpty(classesDir)) {
            classesDir = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            int index = classesDir.indexOf("WEB-INF");
            if (index == -1) {
                index = classesDir.indexOf("classes");
                classesDir = classesDir.substring(0, index);
                return classesDir;
            }
        }
        if (classesDir.endsWith("classes/")) {
            return classesDir;
        }
        return null;
    }

    protected File upload(HttpServletRequest request, String targetPath) throws IOException {
        File file_ = null;
        File dirname = new File(targetPath);
        if (!dirname.isDirectory()) { //目录不存在 
            dirname.mkdirs(); //创建目录
        }
        try {
            // 将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            // 检查form中是否有enctype="multipart/form-data"
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
            if (multipartResolver.isMultipart(request)) {
                // 将request变成多部分request
                MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
                // 获取multiRequest 中所有的文件名
                Iterator<String> iter = multiRequest.getFileNames();

                while (iter.hasNext()) {
                    // 一次遍历所有文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        String originalFilename = file.getOriginalFilename();
                        String subfix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
                        String prefix = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                        String path = targetPath + prefix + "-" + sdf.format(new Date()) + subfix;
                        //上传
                        file_ = new File(path);
                        file.transferTo(file_);
                    }
                }
            }
        } catch (IllegalStateException e) {
            logger.debug("==== upload@error ===={}", e);
        }
        return file_;
    }

    /**
     * 用户对应的应用上下文
     * @return 上下文格式为http://xxx/datacenter/，最后一定有/
     */
    public String getContextPathWithHost(HttpServletRequest request) {
        return request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
    }

    public String getFilePath() {
        return System.getProperty("user.dir");
    }

}
