package cn.itcast.model.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by mengyao
 * 2019年6月6日
 */
@SuppressWarnings("all")
public class DateUtil {

    public static final ThreadLocal<SimpleDateFormat> FMT_YMD = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    public static final ThreadLocal<SimpleDateFormat> FMT_YMDH = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHH"));
    public static final ThreadLocal<SimpleDateFormat> FMT_YMDHM = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmm"));
    public static final ThreadLocal<SimpleDateFormat> FMT_YMDHMS = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmss"));
    public static final ThreadLocal<SimpleDateFormat> FMT_YMDHMSS = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmssSSS"));
    public static final String NO_NUM_REGEX = "\\D";
    public static Calendar cal = Calendar.getInstance();

    /**
     * 预处理时间
     *
     * @param date
     * @return
     */
    private static String prepare(String date) {
        return date.replaceAll(NO_NUM_REGEX, "");
    }


    /**
     * 返回距离当前日期的天数
     *
     * @param dateStr
     * @return
     */
    public static int dayDiff(String dateStr) {
        try {
            long yesterday = FMT_YMD.get().parse(prepare(dateStr)).getTime();
            long today = System.currentTimeMillis();
            return (int) ((today - yesterday) / 86400000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 返回当前时间的yyyyMMddHHmmSS，如20190606131028
     *
     * @return
     */
    public static String today() {
        return FMT_YMDHMS.get().format(new Date());
    }

    /**
     * 返回特定年份有多少天
     *
     * @param year
     * @return
     */
    public static int getDaysOfYear(int year) {
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    /**
     * 返回当前年份
     *
     * @param year
     * @return
     */
    public static int getCurYear() {
        return cal.get(Calendar.YEAR);
    }

    /**
     * 返回过去num天的时间
     *
     * @param num
     * @return
     */
    public static Date getRecentOfDays(int num) {
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -num);
        return cal.getTime();
    }

    /**
     * 返回过去num个月的时间
     *
     * @param num
     * @return
     */
    public static Date getRecentOfMonths(int num) {
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -num);
        return cal.getTime();
    }

    /**
     * 返回过去num年的时间
     *
     * @param num
     * @return
     */
    public static Date getRecentOfYear(int num) {
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -num);
        return cal.getTime();
    }

}
