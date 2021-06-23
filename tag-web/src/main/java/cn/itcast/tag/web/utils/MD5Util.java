/**
 * 项目名称：mengyao
 * 创建日期：2018年5月28日
 * 修改历史：
 * 1、[2018年5月28日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaocs MD5加密工具类
 */
public class MD5Util {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);


    /**
     * 获取MD5加密
     * @param pwd 需要加密的字符串
     * @return String 字符串 加密后的字符串
     */
    public static String getMd5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        logger.info("==== MD5@code:{} ====", MD5Util.getMd5(MD5Util.getMd5("123456") + UserUtil.ENCRYPTING_KEY));
    }

}
