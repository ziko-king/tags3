package cn.itcast.tag.web.utils;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author liuchengli
 */
@Component
@SuppressWarnings("all")
public class OozieUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${oozieUser}")
    private String user;
    @Value("${oozieUrl}")
    private String oozieUrl;
    @Value("${nameNode}")
    private String nameNode;
    @Value("${jobTracker}")
    private String jobTracker;
    @Value("${queueName}")
    private String queueName;
    @Value("${oozie.use.system.libpath}")
    private String useSysLibpath;
    @Value("${oozie.action.sharelib.for.spark}")
    private String sharelibForSpark;
    @Value("${oozie.rerun.fail.nodes}")
    private String reRunFailNodes;
    @Value("${oozie.libpath}")
    private String libpath;
    @Value("${oozieSparkjobMaster}")
    private String master;
    @Value("${oozieSparkjobMode}")
    private String mode;
    @Value("${oozieWorkflowAppPath}")
    private String wfAppPath;
    @Value("${oozie.coord.application.path}")
    private String coordAppPath;
    @Value("${oozieWorkflowName}")
    private String wfName;
    @Value("${oozieSparkjobJar}")
    private String sparkjobJar;
    @Value("${oozieSparkjobMain}")
    private String sparkjobMain;
    @Value("${oozieSparkjobOptions}")
    private String sparkjobOpts;
    @Value("${oozie}")
    private String oozie;


    private OozieClient client;
    private Properties conf = new Properties();
    private OozieUtil oozieUtil;

    private OozieUtil() {
    }

    public static void main(String[] args) throws Exception {
        OozieUtil instance = new OozieUtil().build();
        Properties config = new Properties();

        config.setProperty("master", "yarn");
        config.setProperty("mode", "cluster");
        config.setProperty("freq", "day");
        config.setProperty("name", "oozieSparkTest");
        config.setProperty("class", "com.java.WordCount");
        config.setProperty("jarpath", "${nameNode}/user/hdfs/oozie/workflow3/lib/JavaWC.jar");
		/*config.setProperty("sparkopts",
				"--executor-memory 1G "
						+ " --jars ${nameNode}/user/hdfs/oozie/workflow3/lib/depenJar/fastjson-1.2.47.jar "
						+ " --conf spark.eventLog.dir=hdfs://bjqt/spark2-history/  "
						+ " --conf spark.yarn.historyServer.address=http://192.168.1.234:18081 "
						+ " --conf spark.eventLog.enabled=true");*/
        config.setProperty("sparkopts",
                "--executor-memory 1G "
                        + " --jars ${nameNode}/user/hdfs/oozie/workflow3/lib/depenJar/fastjson-1.2.47.jar "
        );

        config.setProperty("jararg", "0");
        config.setProperty("start", "2018-06-027T09:58+0800");

        config.setProperty("end", "2018-06-027T10:25+0800");

        instance.writeToXml("D:/softs/workflow.xml");
        //String jobId = instance.start(config);
        //System.out.println("jobId===" + jobId);
        // instance.kill(jobId);
    }

    public OozieUtil build() {
        if (null == oozieUtil) {
            client = new OozieClient(oozieUrl);
            client.setDebugMode(1);
            configure();
        }
        return this;
    }

    // 初始化基本的oozie参数
    private void configure() {
        conf.setProperty(OozieClient.USER_NAME, user);
        conf.setProperty("oozie.use.system.libpath", "True");
        conf.setProperty("security_enabled", "False");
        conf.setProperty("send_email", "False");
        conf.setProperty("dryrun", "False");
        conf.setProperty("nameNode", nameNode);
        conf.setProperty("jobTracker", jobTracker);
        conf.setProperty("queueName", queueName);
        conf.setProperty("sparkMaster", master);
        conf.setProperty("sparkDeployMode", mode);
        conf.setProperty("oozieWFName", wfName);
        conf.setProperty("sparkJobMain", sparkjobMain);
        conf.setProperty("sparkJobJar", sparkjobJar);
        conf.setProperty("sparkJobOpts", sparkjobOpts);
        conf.setProperty("sparkMainOpts", oozie);
        conf.setProperty("sparkContainerCacheFiles", "");
    }

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
     * @param conf
     * @return
     */
    public String start(Properties config) {
        String jobId = null;
        try {
            logger.info("conf=" + conf.toString());
            jobId = client.run(conf);
            client.start(jobId);
            logger.info("==== submit job to cluster running! ====" + jobId);
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
        return jobId;
    }

    /**
     * 暂停作业
     *
     * @param conf
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
     * gen
     *
     * @param jobId
     * @return
     */
    public Map<String, String> getWFStatus(String jobid) {
        Map<String, String> wfInfo = new HashedMap<String, String>();
        String statusStr = null;
        try {
            CoordinatorJob cjob = client.getCoordJobInfo(jobid);
            String wJobId = cjob.getActions().get(0).getExternalId();

            WorkflowJob jobInfo = client.getJobInfo(wJobId);

            Status status = jobInfo.getStatus();
            Date startTime = jobInfo.getCreatedTime();
            Date endTime = jobInfo.getEndTime();
            String start = format.format(startTime);
            String end = null;
            if (endTime != null) {
                end = format.format(endTime);
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
        String coordId = "";
        try {
            WorkflowJob jobInfo = client.getJobInfo(wfId);
            coordId = jobInfo.getParentId();
        } catch (OozieClientException e) {
            // TODO Auto-generated catch block
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
            logger.info("==== Oozie JobId is null! ====");
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

    private void test1() throws Exception {

    }

    private void test() throws Exception {
        try {
            OozieClient client = new OozieClient("http://bjqt235.qt:11000/oozie");
            Properties conf = client.createConfiguration();
            conf.setProperty("nameNode", "hdfs://bjqt-231:8020");
            conf.setProperty("jobTracker", "bjqt-234:8050");
            conf.setProperty("queueName", "default");
            conf.setProperty(OozieClient.APP_PATH, "/user/hdfs/oozie/workflow3/workflow/");
            conf.setProperty(OozieClient.LIBPATH, "/user/hdfs/oozie/workflow3/lib/");
            conf.setProperty(OozieClient.USE_SYSTEM_LIBPATH, "true");
            conf.setProperty(OozieClient.USER_NAME, "oozie");
            conf.setProperty(OozieClient.RERUN_FAIL_NODES, "true");
            String jobId = client.run(conf);
            System.out.println("Workflow job submitted");
            while (client.getJobInfo(jobId).getStatus() == WorkflowJob.Status.RUNNING) {
                System.out.println("Workflow job running ...");
                Thread.sleep(10 * 1000);
            }
            System.out.println("Workflow job completed ...");
        } catch (OozieClientException e) {
            e.printStackTrace();
        }
    }

    public Properties getConf() {
        return conf;
    }

    public void setConf(Properties conf) {
        this.conf = conf;
    }


}
