package cn.itcast.model.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Properties文件读写工具类
 * Created by mengyao
 * 2019年6月6日
 */
public class PropertiesUtil {

    private static final String DEFAULT_CONFIG = "config.properties";
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);


    private PropertiesUtil() {
    }

    public static Properties getProperties() {
        return getProperties(DEFAULT_CONFIG);
    }

    public static Properties getProperties(String path) {
        Properties prop = new Properties();
        loadConfig(prop, path);
        return prop;
    }

    private static void loadConfig(Properties prop, String path) {
        InputStream is = null;
        if (StringUtils.isEmpty(path)) {
            is = getInputStream(DEFAULT_CONFIG);
            logger.info("加载默认配置文件：{}", DEFAULT_CONFIG);
        } else {
            is = getInputStream(path);
            logger.info("加载指定配置文件：{}", path);
        }
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("配置文件找不到!");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info("IO流关闭时发生异常!");
                }
            }
        }
    }

    // 获取输入流
    private static InputStream getInputStream(String conf) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = null;
        if (null != classLoader) {
            is = classLoader.getResourceAsStream(conf);
        }
        return is;
    }

    // 获取输出流
    private static OutputStream getOutPutStream(String conf) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        OutputStream out = null;
        if (null != classLoader) {
            String filePath = classLoader.getResource(conf).getFile();
            try {
                out = new FileOutputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error("配置文件找不到!");
            }
        }
        return out;
    }

    // 获取指定key的value
    public static String getValue(Properties prop, String key) {
        String value = prop.getProperty(key);
        return StringUtils.isEmpty(value) ? StringUtils.EMPTY : value;
    }

    // 设置指定key的value
    public static void setValue(String conf, String key, String value) {
        Properties p = getProperties(conf);
        OutputStream out = getOutPutStream(conf);
        p.setProperty(key, value);
        try {
            p.store(out, "set:" + key + "=" + value);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("设置{}={}失败!", key, value);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("stream close fail!");
                }
            }
        }
    }

}