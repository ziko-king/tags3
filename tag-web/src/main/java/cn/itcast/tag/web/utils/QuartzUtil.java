package cn.itcast.tag.web.utils;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liuchengli
 * QuartzUtil 操作定时任务的工具类
 */
@Component
@SuppressWarnings("all")
public class QuartzUtil {


    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Scheduler scheduler;

    /**
     * @param map      参数
     *                 map中必须包含的参数:
     *                 1.interval  1:秒 2:分 3:小时  4:天  5:周  6:月  7:年
     *                 2.startTime 定时开始 格式:yyyy-MM-dd HH:mm:ss
     *                 3.endTime 定时结束(不再执行定时任务) 格式:yyyy-MM-dd HH:mm:ss
     *                 4.triggerGroup trigger组
     *                 5.triggerName trigger在组中的唯一标识，如果相同，则表示更新
     *                 6.JobGroup Job组
     *                 7.JobName Job在组中的唯一标识，如果相同，则表示更新
     *                 8.intervalCount 间隔数:2秒,2天...
     *                 其他的自定义参数可用来传递给job
     * @param jobClass 自定义逻辑的job
     */
    public boolean startJob(Map<String, String> map, Class<? extends Job> jobClass) {
        boolean flag = false;
        String interval = map.get("interval");
        String intervalCount = map.get("intervalCount");

        String startTime = map.get("startTime");
        String endTime = map.get("endTime");
        String triggerGroup = map.get("triggerGroup");
        String triggerName = map.get("triggerName");
        String jobGroup = map.get("jobGroup");
        String jobName = map.get("jobName");
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("interval", "intervalCount", "startTime",
                "endTime", "triggerGroup", "triggerName", "jobGroup", "jobName"));

        Date start = null;
        Date end = null;
        CalendarIntervalScheduleBuilder cit = null;
        if (interval != null && intervalCount != null &&
                startTime != null && endTime != null &&
                triggerGroup != null && triggerName != null &&
                jobGroup != null && jobName != null
        ) {
            try {
                start = format.parse(startTime);
                end = format.parse(endTime);

                if (interval.equals("1")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInSeconds(Integer.valueOf(intervalCount));
                } else if (interval.equals("2")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInMinutes(Integer.valueOf(intervalCount));
                } else if (interval.equals("3")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInHours(Integer.valueOf(intervalCount));
                } else if (interval.equals("4")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInDays(Integer.valueOf(intervalCount));
                    System.out.println("day = " + cit);
                } else if (interval.equals("5")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInWeeks(Integer.valueOf(intervalCount));
                } else if (interval.equals("6")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInMonths(Integer.valueOf(intervalCount));
                } else if (interval.equals("7")) {
                    cit = CalendarIntervalScheduleBuilder
                            .calendarIntervalSchedule()
                            .withIntervalInYears(Integer.valueOf(intervalCount));
                }
                //定义一个Trigger

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerGroup, triggerName).startNow()
                        .withSchedule(cit)
                        .startAt(start)
                        .endAt(end)
                        .build();
                //定义一个JobDetail
                JobDetail job = JobBuilder.newJob(jobClass)
                        .withIdentity(jobGroup, jobName)
                        .build();

                JobDataMap jobDataMap = job.getJobDataMap();


                for (String key : map.keySet()) {
                    if (set.contains(key)) {
                        continue;
                    } else {
                        jobDataMap.put(key, map.get(key));
                    }
                }


                logger.info("trigger++++++++++" + trigger.toString());
                scheduler.scheduleJob(job, trigger);
                //启动之
                scheduler.start();
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return flag;
    }

    /**
     * 暂停Job任务
     *
     * @param tagId
     * @return
     */
    public boolean suspendJob(String tagId) {
        boolean flag = false;
        JobKey jobKey = new JobKey("jobName" + tagId, "jobGroup");
        try {
            scheduler.pauseJob(jobKey);
            flag = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除job
     *
     * @param tagId
     * @return
     */
    public boolean deleteJob(String tagId) {
        boolean flag = false;
        JobKey jobKey = new JobKey("jobName" + tagId, "jobGroup");
        try {
            scheduler.deleteJob(jobKey);
            flag = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
