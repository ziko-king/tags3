package cn.itcast.tag.web.job;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.*;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mengyao
 */
@SuppressWarnings("all")
@Component
public class JobManager {

    @Resource
    private Scheduler scheduler;

    /**
     * 添加job
     *
     * @param className      类名
     * @param cronExpression cron表达式
     * @throws Exception
     */
    public void addJob(String className, String cron, JobDataMap para) throws Exception {
        Class clazz = Class.forName(className);
        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity("JobName:" + className, Scheduler.DEFAULT_GROUP)
                //如果需要给任务传递参数，可以用setJobData来设置参数
                .setJobData(para)
                .build();
        //CronTriggerImpl cronTrigger = new CronTriggerImpl();
        CalendarIntervalTriggerImpl calTrigger = new CalendarIntervalTriggerImpl();
        calTrigger.setRepeatIntervalUnit(IntervalUnit.DAY);
        /*cronTrigger.setName("JobTrigger:" + className);
        cronTrigger.setCronExpression(cron);*/
        JobKey jobKey = new JobKey("JobName:" + className, Scheduler.DEFAULT_GROUP);
        if (!scheduler.checkExists(jobKey)) {
            scheduler.scheduleJob(jobDetail, calTrigger);
        }
    }

    /**
     * 暂停job
     *
     * @param className 类名
     * @throws Exception
     */
    public void pause(String className) throws Exception {
        JobKey jobKey = new JobKey("JobName:" + className, Scheduler.DEFAULT_GROUP);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 重启job
     *
     * @param className
     * @throws Exception
     */
    public void resume(String className) throws Exception {
        JobKey jobKey = new JobKey("JobName:" + className, Scheduler.DEFAULT_GROUP);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 停止job
     *
     * @param className
     * @throws Exception
     */
    public void stop(String className) throws Exception {
        JobKey jobKey = new JobKey("JobName:" + className, Scheduler.DEFAULT_GROUP);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 修改job的执行时间
     *
     * @param className
     * @param cronExpression
     * @throws Exception
     */
    public void updateJobTime(String className, String cron) throws Exception {
        TriggerKey triggerKey = new TriggerKey("JobTrigger:" + className, Scheduler.DEFAULT_GROUP);
        CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
            trigger.setCronExpression(cron);
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    /**
     * 获取job信息
     *
     * @param className
     * @return
     * @throws Exception
     */
    public JobDetail getJobDetail(String className) throws Exception {
        JobKey jobKey = new JobKey("JobName:" + className, Scheduler.DEFAULT_GROUP);
        JobDetail detail = scheduler.getJobDetail(jobKey);
        return detail;
    }

    /**
     * 启动所有的任务
     *
     * @throws SchedulerException
     */
    public void startJobs() throws SchedulerException {
        scheduler.start();
    }

    /**
     * shutdown所有的任务
     *
     * @throws SchedulerException
     */
    public void shutdownJobs() throws SchedulerException {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

}
