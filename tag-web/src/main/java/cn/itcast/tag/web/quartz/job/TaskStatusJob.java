package cn.itcast.tag.web.quartz.job;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.mapper.EngineMapper;
import cn.itcast.tag.web.utils.JobUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查询任务状态并更新到数据库
 *
 * @author liuchengli
 */
@Component
@SuppressWarnings("all")
public class TaskStatusJob implements Job {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private EngineMapper engineMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        JobDataMap map = detail.getJobDataMap();
        String jobid = map.getString("jobid");
        String tagId = map.getString("tagId");
        int execTimeOutStatus = Integer.valueOf(map.getString("execTimeOutStatus"));
        String status = jobUtil.getJobStatus(jobid, execTimeOutStatus);

        System.out.println("status = " + status);
        /**
         * 更新数据库任务信息
         */
        EngineBean eBean = new EngineBean();
        eBean.setTagId(Integer.valueOf(tagId));
        eBean.setStatus(status);
        engineMapper.updateStatus(eBean);
        //如果spark job 已经完成，将不再定时执行查询

        if (status != null && !status.equals("3")) {
            TriggerKey triggerKey = context.getTrigger().getKey();
            try {
                context.getScheduler().unscheduleJob(triggerKey);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

}
