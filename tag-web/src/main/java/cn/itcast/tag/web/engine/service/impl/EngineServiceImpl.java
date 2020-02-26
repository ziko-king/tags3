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
import org.apache.oozie.client.OozieClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("PlaceholderCountMatchesArgumentCount")
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
    @Value("${oozie-config-dir-name}")
    private String oozieConfigDirName;
    @Value("${oozie-coordinator-file-name}")
    private String coordinatorFileName;
    @Value("${oozieWorkflowName}")
    private String workflowFileName;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean startEngine(EngineBean engineBean, UserBean userBean) {
        try {
            return startEngineActually(engineBean);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    /**
     * 开始一个标签的执行
     * 组织这个标签的 Model 对应的 Oozie 脚本
     * 调用 Oozie 开启 Spark Job
     *
     * @param engineBean id, remark
     */
    private boolean startEngineActually(EngineBean engineBean) {
        ModelBean modelBean = modelMapper.get(new ModelBean(engineBean.getTagId()));
        MetaDataBean metaDataBean = metaDataMapper.get(new MetaDataBean(engineBean.getTagId()));

        // 1. 判断模型和元数据是否存在, 如果不存在则启动失败
        HdfsUtil hdfsUtil = HdfsUtil.getInstance();
        if (!hdfsUtil.exist(modelBean.getModelPath())) {
            logger.error("模型包不存在, 启动失败");
            return false;
        }
        if (StringUtils.isBlank(metaDataBean.getInType())) {
            logger.error("模型元信息不存在或者缺失 InType, 启动失败");
            return false;
        }

        // 2. 上传 Oozie 配置到 HDFS
        String localOozieConfigPath = engineBean.getRemark() + oozieConfigDirName + "/";
        String tagModelPath = hdfsUtil.getPath(new File(modelBean.getModelPath()).getParent());

        hdfsUtil.uploadLocalFile2HDFS(localOozieConfigPath + workflowFileName, tagModelPath);
        hdfsUtil.uploadLocalFile2HDFS(localOozieConfigPath + coordinatorFileName, tagModelPath);

        // 3. 构建 Oozie job 参数
        OozieUtil oozie = oozieUtil.build();
        Properties oozieConf = oozie.getConf();

        // 3.1. Workflow 部分
        String separator = "/";
        oozieConf.setProperty("nameNode", nameNode);
        oozieConf.setProperty("sparkJobMain", modelBean.getModelMain());

        if (StringUtils.isNotBlank(modelBean.getArgs())) {
            oozieConf.setProperty("sparkJobOpts", modelBean.getArgs());
        }

        oozieConf.setProperty("sparkJobJar", "${nameNode}" + modelBean.getModelPath());
        oozieConf.setProperty("sparkContainerCacheFiles", "${nameNode}" + modelBean.getModelPath());
        oozieConf.setProperty(OozieClient.APP_PATH, "${nameNode}" + tagModelPath + separator + workflowFileName);

        // 3.2. Coordinator 部分, Coordinator 中的时间格式是 UTC, 如 2018-06-019T20:01Z
//        oozieConf.setProperty("oozieWorkflowPath", "${nameNode}" + tagModelPath + separator);
//        oozieConf.setProperty("oozie.coord.application.path", "${nameNode}" + tagModelPath + separator + coordinatorFileName);
//
//        // ScheTime 的格式为 每天#2018-06-01 20::01#2018-06-01 20::01
//        String[] scheduleArr = modelBean.getScheTime().split("#");
//        String freq = scheduleArr[0];
//        String startDateTime = scheduleArr[1];
//        String endDateTime = scheduleArr[2];
//
//        // 设置频率
//        String freqStr = "";
//        switch (freq) {
//            case "每天":
//                freqStr = "day";
//                break;
//            case "每周":
//                freqStr = "week";
//                break;
//            case "每月":
//                freqStr = "month";
//                break;
//            case "每年":
//                freqStr = "year";
//                break;
//        }
//        oozieConf.setProperty("freq", freqStr);
//
//        // 设置开始时间
//        String[] startDateTimeArr = startDateTime.split(" ");
//        String startDate = startDateTimeArr[0];
//        String startTime = startDateTimeArr[1];
//        oozieConf.setProperty("start", startDate + "T" + startTime + "Z");
//
//        // 设置结束时间
//        String[] endDateTimeArr = endDateTime.split(" ");
//        String endDate = endDateTimeArr[0];
//        String endTime = endDateTimeArr[1];
//        oozieConf.setProperty("end", endDate + "T" + endTime + "Z");
//
//        logger.info(oozieConf.toString());

        // 4. 提交 Oozie 任务
        String jobId = oozie.start(oozieConf);

        // 5. 设置监控
        EngineBean mEngineBean = new EngineBean();
        mEngineBean.setJobid(jobId);
        mEngineBean.setTagId(engineBean.getTagId());
        mEngineBean.setStatus("3");
        int state = engineMapper.addMonitorInfo(mEngineBean);

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
