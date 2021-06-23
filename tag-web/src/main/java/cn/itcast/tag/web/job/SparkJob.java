package cn.itcast.tag.web.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SparkJob extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (logger.isDebugEnabled()) {
            logger.debug("Validate Job Running task  thread:{} ", Thread.currentThread().getName());
        }
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        JobKey jobKey = jobDetail.getKey();
        logger.info("==== jobName:{}\tjobData:{}", jobKey.getName(), dataMap);
    }
}
