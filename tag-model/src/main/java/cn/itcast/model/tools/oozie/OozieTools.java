package cn.itcast.model.tools.oozie;

import cn.itcast.model.models.ModelConfig;
import cn.itcast.model.utils.DateUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Oozie工具类
 * Created by mengyao
 * 2018年6月2日
 */
@SuppressWarnings("all")
public class OozieTools {

    private static OozieTools oozieTools;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelConfig modelConfig = new ModelConfig();
    private String oozieUrl;
    private OozieClient client;
    private Properties conf;


    private OozieTools() {
        initializer();
    }

    public static OozieTools build() {
        if (null == oozieTools) {
            oozieTools = new OozieTools();
        }
        return oozieTools;
    }

    /**
     * 初始化工具类配置
     */
    private void initializer() {
        conf = modelConfig.getOozieConfig();
        oozieUrl = modelConfig.getOozieAddr();
        client = new OozieClient(oozieUrl);
        client.setDebugMode(1);
    }

    /**
     * 将workflow.xml的配置生成job.properties
     *
     * @param tmpDir
     */
    public void writeToXml(String tmpDir) {
        try {
            client.createConfiguration();
            client.writeToXml(conf, new FileOutputStream(tmpDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动作业
     *
     * @param config
     * @return
     */
    public String start(Properties config) {
        String jobId = null;
        try {
            logger.info("==== conf: {} ====", conf.toString());
            jobId = client.run(conf);
            logger.info("==== submit job:{} to cluster running! ====", jobId);
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return jobId;
    }

    /**
     * 暂停作业
     *
     * @param jobId
     * @return
     */
    public boolean suspend(String jobId) {
        try {
            client.suspend(jobId);
            logger.info("==== suspend job! ====" + jobId);
        } catch (OozieClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取作业状态
     *
     * @param jobId
     * @return
     */
    public String getStatus(String jobid) {
        String statusStr = null;
        try {
            CoordinatorJob cjob = client.getCoordJobInfo(jobid);
            String wJobId = cjob.getActions().get(0).getExternalId();
            WorkflowJob jobInfo = client.getJobInfo(wJobId);
            Status status = jobInfo.getStatus();
            if (status.equals(Status.PREP)) {
                statusStr = "准备";
            } else if (status.equals(Status.RUNNING)) {
                statusStr = "正在运行";
            } else if (status.equals(Status.SUCCEEDED)) {
                statusStr = "运行成功";
            } else if (status.equals(Status.KILLED)) {
                statusStr = "杀死";
            } else if (status.equals(Status.FAILED)) {
                statusStr = "运行失败";
            } else if (status.equals(Status.SUSPENDED)) {
                statusStr = "暂停";
            }
            logger.info("==== getted job status: {} ====", statusStr);
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return statusStr;
    }

    /**
     * 根据JobId获取工作流运行状态
     *
     * @param jobId
     * @return
     */
    public Map<String, String> getWFStatus(String jobId) {
        Map<String, String> wfInfo = new HashedMap<String, String>();
        String statusStr = null;
        try {
            CoordinatorJob cjob = client.getCoordJobInfo(jobId);
            String wJobId = cjob.getActions().get(0).getExternalId();
            WorkflowJob jobInfo = client.getJobInfo(wJobId);
            Status status = jobInfo.getStatus();
            Date startTime = jobInfo.getCreatedTime();
            Date endTime = jobInfo.getEndTime();
            String start = DateUtil.today();
            String end = null;
            if (endTime != null) {
                end = DateUtil.today();
            }
            if (status.equals(Status.PREP)) {
                statusStr = "准备";
            } else if (status.equals(Status.RUNNING)) {
                statusStr = "正在运行";
            } else if (status.equals(Status.SUCCEEDED)) {
                statusStr = "运行成功";
            } else if (status.equals(Status.KILLED)) {
                statusStr = "杀死";
            } else if (status.equals(Status.FAILED)) {
                statusStr = "运行失败";
            } else if (status.equals(Status.SUSPENDED)) {
                statusStr = "暂停";
            }
            wfInfo.put("status", statusStr);
            wfInfo.put("startTime", start);
            wfInfo.put("endTime", end);
            logger.info("==== getted job status: {} ====", statusStr);
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return wfInfo;
    }

    /**
     * 根据WfId 获取cooridnator 的id
     *
     * @param wfId
     * @return
     */
    public String getCoordIdByWFId(String wfId) {
        String coordId = null;
        try {
            WorkflowJob jobInfo = client.getJobInfo(wfId);
            coordId = jobInfo.getParentId();
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return coordId;

    }

    /**
     * 杀死作业
     *
     * @param jobId
     */
    public boolean kill(String jobId) {
        boolean state = false;
        if (StringUtils.isEmpty(jobId)) {
            logger.info("==== Oozie JobId: {} is null! ====", jobId);
            return state;
        }
        try {
            client.kill(jobId);
            state = true;
            logger.info("==== kill job: {} ====", jobId);
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return state;
    }

    public Properties getConf() {
        return conf;
    }

}