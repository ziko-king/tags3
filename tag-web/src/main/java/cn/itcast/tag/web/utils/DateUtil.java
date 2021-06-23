package cn.itcast.tag.web.utils;

import java.text.SimpleDateFormat;

public class DateUtil {

    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDD_NO = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDDHHMM_NO = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmm"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDDHHMMSS_NO = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDDHHMMSSSSS_NO = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmssSSS"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDD_CN = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDDHHMM_CN = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDDHHMMSS_CN = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<SimpleDateFormat> FMT_YYYYMMDDHHMMSSSSS_CN = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"));


    public static void main(String[] args) {
        String scheTime = "每天#2019-06-07 20:34#2019-06-07 20:34";

    }

    public static String getS(String dateStr) {
        return null;
    }

}
