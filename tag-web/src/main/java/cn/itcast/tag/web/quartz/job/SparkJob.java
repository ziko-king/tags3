package cn.itcast.tag.web.quartz.job;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.mapper.EngineMapper;
import cn.itcast.tag.web.utils.QuartzUtil;
import cn.itcast.tag.web.utils.JobUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行spark Quartz 定时任务的Job类,执行具体的业务逻辑,不使用注入方式
 *
 * @author liuchengli
 */
@Component
public class SparkJob implements Job {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private EngineMapper engineMapper;
    @Autowired
    private QuartzUtil quartzUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDetail detail = context.getJobDetail();
        JobDataMap map = detail.getJobDataMap();
        String command = map.getString("command");
        String regex = map.getString("regex");
        String tagId = map.getString("tagId");
        int execTimeOutSpark = Integer.valueOf(map.getString("execTimeOutSpark"));
        String execTimeOutStatus = map.getString("execTimeOutStatus");
        System.out.println("执行 spark job tagId= " + tagId);
        String jobid = jobUtil.execJob(command, regex, execTimeOutSpark);
        /**
         * 更新数据库任务信息
         */
        EngineBean eBean = new EngineBean();
        eBean.setJobid(jobid);
        eBean.setTagId(Integer.valueOf(tagId));
        eBean.setStatus("3");
        engineMapper.updateStatusAndJobid(eBean);

        logger.info("SparkJob  start running +++++++++++++ jobid=" + jobid);
        //启动状态查询更新job
        Map<String, String> mapJob = new HashMap<String, String>();
        mapJob.put("interval", "1");
        mapJob.put("intervalCount", "15");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 5);

        mapJob.put("startTime", format.format(new Date()));
        mapJob.put("endTime", format.format(c.getTime()));
        mapJob.put("triggerGroup", "StatustriggerGroup");
        mapJob.put("triggerName", "StatustriggerName" + tagId);
        mapJob.put("jobGroup", "StatusjobGroup");
        mapJob.put("jobName", "StatusjobName" + tagId);

        //参数
        mapJob.put("jobid", jobid);
        mapJob.put("tagId", tagId);
        mapJob.put("execTimeOutStatus", execTimeOutStatus);
        quartzUtil.startJob(mapJob, TaskStatusJob.class);

    }

}
