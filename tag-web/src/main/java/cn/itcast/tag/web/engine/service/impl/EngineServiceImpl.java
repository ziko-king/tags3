package cn.itcast.tag.web.engine.service.impl;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.bean.MetaDataBean;
import cn.itcast.tag.web.engine.bean.ModelBean;
import cn.itcast.tag.web.engine.bean.ModelMetaDataBean;
import cn.itcast.tag.web.engine.mapper.EngineMapper;
import cn.itcast.tag.web.engine.mapper.MetaDataMapper;
import cn.itcast.tag.web.engine.mapper.ModelMapper;
import cn.itcast.tag.web.engine.service.EngineService;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.utils.HdfsUtil;
import cn.itcast.tag.web.utils.OozieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@Transactional
public class EngineServiceImpl implements EngineService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private EngineMapper engineMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private MetaDataMapper metaDataMapper;
    @Resource
    private OozieUtil oozieUtil;
    @Value("${nameNode}")
    private String nameNode;
    private String separator = "/";
    private String workflowXMLFile = "workflow.xml";
    private String coordinatorXMLFile = "coordinator.xml";
    private String jobPROPERTIESFile = "job.properties";

    public static void main(String[] args) {
        Properties conf = new Properties();
        String scheTime = "每天#2019-06-07 20:34#2019-06-07 20:34";
        String[] arrSche = scheTime.split("#");
        String freqStr = arrSche[0];
        if (freqStr.equals("每天")) {
            conf.setProperty("freq", "day");
        } else if (freqStr.equals("每周")) {
            conf.setProperty("freq", "week");
        } else if (freqStr.equals("每月")) {
            conf.setProperty("freq", "month");
        } else if (freqStr.equals("每年")) {
            conf.setProperty("freq", "year");
        }
        String[] startTimeArr = arrSche[1].split(" ");
        String[] startTimeDayArr = startTimeArr[0].split("-");
        String starttime = startTimeDayArr[0] + "-" + startTimeDayArr[1] + "-" + startTimeDayArr[2] + "T" + startTimeArr[1] + "Z+0800";
        String[] endTimeArr = arrSche[2].split(" ");
        String[] endTimeDayArr = endTimeArr[0].split("-");
        String endtime = endTimeDayArr[0] + "-" + endTimeDayArr[1] + "-" + endTimeDayArr[2] + "T" + endTimeArr[1] + "Z+0800";
        conf.setProperty("start", starttime);
        conf.setProperty("end", endtime);
        System.out.println(starttime + "\n" + endtime);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean startEngine(EngineBean bean, UserBean userBean) {
        int state = 0;
        try {
            //获取标签的模型配置
            ModelBean model = modelMapper.get(new ModelBean(bean.getTagId()));
            //获取标签的元数据配置
            MetaDataBean metaData = metaDataMapper.get(new MetaDataBean(bean.getTagId()));
            //判断模型是否存在
            HdfsUtil hdfs = HdfsUtil.getInstance();
            if (!hdfs.exist(model.getModelPath())) {
                logger.error("==== 标签模型包不存在，启动失败! ====");
                return false;
            }
            //判断元数据是否存在
            if (StringUtils.isEmpty(metaData.getInType())) {
                logger.error("==== 标签元数据不存在，启动失败! ====");
                return false;
            }
            String classesDir = bean.getRemark();
            String oozieConfigDir = "workflows" + separator;
            //每次执行时都生成Oozie的workflow.xml并上传到HDFS中模型jar所在路径下（如果已存在则覆盖）
            String tagModelPath = hdfs.getPath(model.getModelPath());
            if (tagModelPath.endsWith("/lib")) {
                tagModelPath = tagModelPath.substring(0, tagModelPath.length() - 4);
            }
            hdfs.uploadLocalFile2HDFS(classesDir + oozieConfigDir + workflowXMLFile, tagModelPath);
            //每次执行时都生成Oozie的coordinator.xml并上传到HDFS中模型jar所在路径下（如果已存在则覆盖）
            hdfs.uploadLocalFile2HDFS(classesDir + oozieConfigDir + coordinatorXMLFile, tagModelPath);
            // 实例化oozie并获取配置,生成Oozie的workflow.xml对应的job.properties配置文件
            OozieUtil oozie = oozieUtil.build();
            Properties conf = oozie.getConf();
            //oozie的Job名称
            conf.setProperty("oozieWorkflowName", "Tag_" + bean.getTagId());
            //oozie的SparkAction依赖jar路径
            conf.setProperty("oozieSparkjobJar", "${nameNode}" + model.getModelPath());
            //oozie的SparkAction运行main方法
            conf.setProperty("oozieSparkjobMain", model.getModelMain());
            //oozie的SparkAction参数
            String args = model.getArgs();
            if (!StringUtils.isEmpty(args)) {
                conf.setProperty("oozieSparkjobOptions", args);
            }
            //oozie workflow.xml的地址
            //conf.setProperty("oozie.wf.app.path", "${oozie.nameNode}"+tagModelPath+separator+workflowXMLFile);
            conf.setProperty("oozieWorkflowAppPath", "${nameNode}" + tagModelPath + separator);
            //oozie coordinator.xml的地址
            conf.setProperty("oozie.coord.application.path", "${nameNode}" + tagModelPath + separator + coordinatorXMLFile);
            //oozie定时频率,启动结束时间设置2018-06-019T20:01+0800
            String scheTime = model.getScheTime();
            String[] arrSche = scheTime.split("#");
            String freqStr = arrSche[0];
            if (freqStr.equals("每天")) {
                conf.setProperty("freq", "day");
            } else if (freqStr.equals("每周")) {
                conf.setProperty("freq", "week");
            } else if (freqStr.equals("每月")) {
                conf.setProperty("freq", "month");
            } else if (freqStr.equals("每年")) {
                conf.setProperty("freq", "year");
            }
            String[] startTimeArr = arrSche[1].split(" ");
            String[] startTimeDayArr = startTimeArr[0].split("-");
            String starttime = startTimeDayArr[0] + "-" + startTimeDayArr[1] + "-" + startTimeDayArr[2] + "T" + startTimeArr[1] + "+0800";
            String[] endTimeArr = arrSche[2].split(" ");
            String[] endTimeDayArr = endTimeArr[0].split("-");
            String endtime = endTimeDayArr[0] + "-" + endTimeDayArr[1] + "-" + endTimeDayArr[2] + "T" + endTimeArr[1] + "+0800";
            conf.setProperty("start", starttime);
            conf.setProperty("end", endtime);
            // 将job.properties配置文件保存到本地
            String userHomeDir = SystemUtils.getUserHome() + separator + jobPROPERTIESFile;
            File file = new File(userHomeDir);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(userHomeDir);
            conf.store(out, conf.getProperty("name"));
            out.close();
            //将新生成的job.properties文件上传到HDFS中模型jar所在路径下
            hdfs.uploadLocalFile2HDFS(userHomeDir, tagModelPath);
            //提交Oozie任务
            String jobid = oozie.start(conf);

            EngineBean eBean = new EngineBean();
            eBean.setJobid(jobid);
            eBean.setTagId(bean.getTagId());
            eBean.setStatus("3");
            state = engineMapper.addMonitorInfo(eBean);
            logger.info("==== 执行定时任务开始 ====" + jobid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state > 0;
    }

    @Override
    public List<EngineBean> queryMonitorInfoByAll(EngineBean bean) {
        List<EngineBean> engineBeans = null;
        try {
            engineBeans = engineMapper.queryEngineInfo(bean);
            logger.info("==== queryMonitorInfoByAll@exec:{} ====", engineBeans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryMonitorInfoByAll@err:{} ====", e);
        }
        return engineBeans;
    }

    @Override
    public boolean stopEngine(EngineBean bean, UserBean userBean) {
        List<EngineBean> engineBeans = engineMapper.queryEngineInfo(bean);
        if (null == engineBeans || 0 == engineBeans.size()) {
            return false;
        }
        EngineBean engineBean = engineBeans.get(0);
        boolean result = false;
        try {
            result = oozieUtil.build().kill(engineBean.getJobid());
            if (result) {
                // 0.未启动 1.成功 2.失败 3.运行中 4.任务暂停 5.任务停用
                bean.setStatus(5 + "");
                // 操作：1启用 2暂停 3停用
                bean.setOperation(3);
                engineMapper.updateStatus(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean suspendEngine(EngineBean bean) {
        List<EngineBean> engineBeans = engineMapper.queryEngineInfo(bean);
        if (null == engineBeans) {
            return false;
        }
        EngineBean engineBean = engineBeans.get(0);
        boolean result = oozieUtil.build().suspend(engineBean.getJobid());
        if (result) {
            //0.未启动 1.成功 2.失败 3.运行中 4.任务暂停 5.任务停用
            bean.setStatus(4 + "");
            // 操作：1启用 2暂停 3停用
            bean.setOperation(2);
            engineMapper.updateStatus(bean);
        }
        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean startEngineByQuartz(EngineBean bean, UserBean userBean) {
        int state = 0;
        try {
            /**
             * 拼接命令
             */
            StringBuffer command = new StringBuffer("spark-submit --master yarn --deploy-mode cluster --verbose");
            ModelMetaDataBean mmd = engineMapper.queryTaskArgs(bean);
            System.out.println("mmd=====" + mmd);
            String tagId = mmd.getTagId() + "";
            //oozie 频率,启动结束时间设置2018-06-019T20:01+0800
            String scheTime = mmd.getScheTime();
            String[] arrSche = scheTime.split("#");
            String freqStr = arrSche[0];
            String interval = "";
            if (freqStr.equals("每天")) {
                interval = "4";
            } else if (freqStr.equals("每周")) {
                interval = "5";
            } else if (freqStr.equals("每月")) {
                interval = "6";
            } else if (freqStr.equals("每年")) {
                interval = "7";
            }

            String args = mmd.getArgs();
            String sparkopts = "";
            //规定参数以key=value,key=value ...,jararg开头表示传递给程序的参数，允许传递5个参数,
            //starttime,endtime 必须传过来
            String[] arr = args.split(",");
            String[] temp = null;
            String arg = "";
            for (int i = 0; i < arr.length; i++) {
                temp = arr[i].split("=");
                if (temp[0].startsWith("jararg")) {
                    arg = temp[1] + "#" + mmd.getTagId() + "#" + userBean.getId();
                } else {
                    sparkopts += " --" + temp[0] + " " + temp[1] + " ";
                }
            }

            command.append(sparkopts + " ");
            command.append("--class " + mmd.getModelMain() + " ");
            String appPath = nameNode + mmd.getModelPath();
            command.append(appPath + " ");
            command.append(arg);

            Map<String, String> mapJob = new HashMap<String, String>();

            mapJob.put("startTime", arrSche[1]);
            mapJob.put("endTime", arrSche[2]);
            mapJob.put("triggerGroup", "triggerGroup");
            mapJob.put("triggerName", "triggerName" + tagId);
            mapJob.put("jobGroup", "jobGroup");
            mapJob.put("jobName", "jobName" + tagId);
            mapJob.put("interval", interval);
            mapJob.put("intervalCount", "1");

            mapJob.put("command", command.toString());
            mapJob.put("regex", "application_+\\d{13}+\\_+\\d{4}+");
            mapJob.put("tagId", tagId);

            //传入spark任务执行超时时间和yarn status 超时时间
            mapJob.put("execTimeOutSpark", 30 * 60000 + "");
            mapJob.put("execTimeOutStatus", 2 * 60000 + "");
            mapJob.put("tagId", tagId);

            logger.info("command++++++++++++++++++=" + command.toString());

            EngineBean eBean = new EngineBean();
            eBean.setJobid(null);
            eBean.setTagId(bean.getTagId());
            eBean.setStatus("3");
            state = engineMapper.addMonitorInfo(eBean);
            //quartzUtil.startJob(mapJob,SparkJob.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return state > 0;

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean stopEngineByQuartz(EngineBean bean, UserBean userBean) {
        List<EngineBean> engineBeans = engineMapper.queryEngineInfo(bean);
        if (null == engineBeans || 0 == engineBeans.size()) {
            return false;
        }
//			EngineBean engineBean = engineBeans.get(0);
//			boolean result = quartzUtil.deleteJob(engineBean.getTagId()+"");
//			if (result) {
//				// 0.未启动 1.成功 2.失败 3.运行中 4.任务暂停 5.任务停用
//				bean.setStatus(5+"");
//				// 操作：1启用 2暂停 3停用
//				bean.setOperation(3);
//				engineMapper.updateStatus(bean);
//			}
//			return result;
        return false;
    }


}
