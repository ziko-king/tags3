package cn.itcast.tag.web.job;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.engine.mapper.EngineMapper;
import cn.itcast.tag.web.quartz.job.SparkJob;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.QuartzUtil;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "job")
public class JobController {

    String className = "com.mengyao.tag.job.MyJob";
    String cronExpression = "0/3 * * * * ?";
    @Resource
    private JobManager jobManager;
    @Autowired
    private EngineMapper engineMapper;
    @Autowired
    private QuartzUtil quartzUtil;
    @Autowired
    private SparkJob sparkjob;

    /**
     * 添加一个新任务
     *
     * @return
     */
    @RequestMapping(value = "/addjob")
    public void add(PrintWriter pw) {
        Result result = new Result();
        try {
        	/*jobManager.addJob(className,cronExpression, null);
            result.set(StateCode.ADD_SUCCESS, "添加动态任务成功！");
            String jsonStr = JsonUtil.obj2Json(result);
			pw.write(jsonStr);*/

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> mapJob = new HashMap<String, String>();
            String tagId = "1523";
            mapJob.put("interval", "4");
            mapJob.put("intervalCount", "1");
            Date d = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.MINUTE, 30);

            mapJob.put("startTime", format.format(d));
            mapJob.put("endTime", format.format(c.getTime()));
            mapJob.put("triggerGroup", "triggerGroup");
            mapJob.put("triggerName", "triggerName" + tagId);
            mapJob.put("jobGroup", "jobGroup");
            mapJob.put("jobName", "jobName" + tagId);

            mapJob.put("command", "spark-submit --master yarn --deploy-mode cluster --class com.java.WordCount --jars hdfs://bjqt/user/hdfs/oozie/workflow3/lib/depenJar/fastjson-1.2.47.jar hdfs://bjqt/user/hdfs/oozie/workflow3/lib/JavaWC.jar");
            mapJob.put("regex", "application_+\\d{13}+\\_+\\d{4}+");

            mapJob.put("tagId", tagId);
            System.out.println("mapJob = " + mapJob);
            quartzUtil.startJob(mapJob, SparkJob.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停任务
     *
     * @return
     */
    @RequestMapping(value = "/pause")
    public void pause(PrintWriter pw) {
        Result result = new Result();
        try {
            jobManager.pause(className);
            result.set(StateCode.ADD_SUCCESS, "暂停任务成功！");
            String jsonStr = JsonUtil.obj2Json(result);
            pw.write(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 重启任务
     *
     * @return
     */
    @RequestMapping(value = "/resume")
    public void resume(PrintWriter pw) {
        Result result = new Result();
        try {
            jobManager.resume(className);
            result.set(StateCode.ADD_SUCCESS, "重启任务成功！");
            String jsonStr = JsonUtil.obj2Json(result);
            pw.write(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止任务
     *
     * @return
     */
    @RequestMapping(value = "/stop")
    public void stop(PrintWriter pw) {
        Result result = new Result();
        try {
            jobManager.stop(className);
            result.set(StateCode.ADD_SUCCESS, "停止任务成功！");
            String jsonStr = JsonUtil.obj2Json(result);
            pw.write(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改任务执行时间
     *
     * @return
     */
    @RequestMapping(value = "/update")
    public void update(PrintWriter pw) {
        Result result = new Result();
        try {
            jobManager.updateJobTime(className, "0/30 * * * * ?");
            result.set(StateCode.ADD_SUCCESS, "修改任务执行时间成功！");
            String jsonStr = JsonUtil.obj2Json(result);
            pw.write(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查看任务状态
     *
     * @return
     */
    @RequestMapping(value = "/detail")
    public void getJobDetail(PrintWriter pw) {
        Result result = new Result();
        JobDetail jobDetail;
        try {
            jobDetail = jobManager.getJobDetail(className);
            result.set(StateCode.ADD_SUCCESS, "查看任务状态成功！");
            if (null != jobDetail) {
                //JobDetail中的JobBuilder是不能序列化的。因此不能放JobDetail
                result.setData(jobDetail.getDescription());
            }
            String jsonStr = JsonUtil.obj2Json(result);
            pw.write(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}