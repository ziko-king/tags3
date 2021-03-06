package cn.itcast.tag.web.engine.service.impl;

import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.bean.MetaDataBean;
import cn.itcast.tag.web.engine.mapper.EngineMapper;
import cn.itcast.tag.web.engine.mapper.MetaDataMapper;
import cn.itcast.tag.web.engine.mapper.ModelMapper;
import cn.itcast.tag.web.engine.service.EngineService;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.utils.OozieUtil;
import cn.itcast.tag.web.engine.bean.ModelBean;
import cn.itcast.tag.web.engine.bean.ModelMetaDataBean;
import cn.itcast.tag.web.utils.HdfsUtil;
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
     * ???????????????????????????
     * ????????????????????? Model ????????? Oozie ??????
     * ?????? Oozie ?????? Spark Job
     *
     * @param engineBean id, remark
     */
    private boolean startEngineActually(EngineBean engineBean) {
        ModelBean modelBean = modelMapper.get(new ModelBean(engineBean.getTagId()));
        MetaDataBean metaDataBean = metaDataMapper.get(new MetaDataBean(engineBean.getTagId()));

        // 1. ????????????????????????????????????, ??????????????????????????????
        HdfsUtil hdfsUtil = HdfsUtil.getInstance();
        if (!hdfsUtil.exist(modelBean.getModelPath())) {
            logger.error("??????????????????, ????????????");
            return false;
        }
        if (StringUtils.isBlank(metaDataBean.getInType())) {
            logger.error("???????????????????????????????????? InType, ????????????");
            return false;
        }

        // 2. ?????? Oozie ????????? HDFS
        String localOozieConfigPath = engineBean.getRemark() + oozieConfigDirName + "/";
        String tagModelPath = hdfsUtil.getPath(new File(modelBean.getModelPath()).getParent());

        hdfsUtil.uploadLocalFile2HDFS(localOozieConfigPath + workflowFileName, tagModelPath);
        hdfsUtil.uploadLocalFile2HDFS(localOozieConfigPath + coordinatorFileName, tagModelPath);

        // 3. ?????? Oozie job ??????
        OozieUtil oozie = oozieUtil.build();
        Properties oozieConf = oozie.getConf();

        // 3.1. Workflow ??????
        String separator = "/";
        oozieConf.setProperty("nameNode", nameNode);
        oozieConf.setProperty("sparkJobMain", modelBean.getModelMain());

        if (StringUtils.isNotBlank(modelBean.getArgs())) {
            oozieConf.setProperty("sparkJobOpts", modelBean.getArgs());
        }

        oozieConf.setProperty("sparkJobJar", "${nameNode}" + modelBean.getModelPath());
        oozieConf.setProperty("sparkContainerCacheFiles", "${nameNode}" + modelBean.getModelPath());
        oozieConf.setProperty(OozieClient.APP_PATH, "${nameNode}" + tagModelPath + separator + workflowFileName);

        // 3.2. Coordinator ??????, Coordinator ????????????????????? UTC, ??? 2018-06-019T20:01Z
//        oozieConf.setProperty("oozieWorkflowPath", "${nameNode}" + tagModelPath + separator);
//        oozieConf.setProperty("oozie.coord.application.path", "${nameNode}" + tagModelPath + separator + coordinatorFileName);
//
//        // ScheTime ???????????? ??????#2018-06-01 20::01#2018-06-01 20::01
//        String[] scheduleArr = modelBean.getScheTime().split("#");
//        String freq = scheduleArr[0];
//        String startDateTime = scheduleArr[1];
//        String endDateTime = scheduleArr[2];
//
//        // ????????????
//        String freqStr = "";
//        switch (freq) {
//            case "??????":
//                freqStr = "day";
//                break;
//            case "??????":
//                freqStr = "week";
//                break;
//            case "??????":
//                freqStr = "month";
//                break;
//            case "??????":
//                freqStr = "year";
//                break;
//        }
//        oozieConf.setProperty("freq", freqStr);
//
//        // ??????????????????
//        String[] startDateTimeArr = startDateTime.split(" ");
//        String startDate = startDateTimeArr[0];
//        String startTime = startDateTimeArr[1];
//        oozieConf.setProperty("start", startDate + "T" + startTime + "Z");
//
//        // ??????????????????
//        String[] endDateTimeArr = endDateTime.split(" ");
//        String endDate = endDateTimeArr[0];
//        String endTime = endDateTimeArr[1];
//        oozieConf.setProperty("end", endDate + "T" + endTime + "Z");
//
//        logger.info(oozieConf.toString());

        // 4. ?????? Oozie ??????
        String jobId = oozie.start(oozieConf);

        // 5. ????????????
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
                // 0.????????? 1.?????? 2.?????? 3.????????? 4.???????????? 5.????????????
                bean.setStatus(5 + "");
                // ?????????1?????? 2?????? 3??????
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
            //0.????????? 1.?????? 2.?????? 3.????????? 4.???????????? 5.????????????
            bean.setStatus(4 + "");
            // ?????????1?????? 2?????? 3??????
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
             * ????????????
             */
            StringBuffer command = new StringBuffer("spark-submit --master yarn --deploy-mode cluster --verbose");
            ModelMetaDataBean mmd = engineMapper.queryTaskArgs(bean);
            System.out.println("mmd=====" + mmd);
            String tagId = mmd.getTagId() + "";
            //oozie ??????,????????????????????????2018-06-019T20:01+0800
            String scheTime = mmd.getScheTime();
            String[] arrSche = scheTime.split("#");
            String freqStr = arrSche[0];
            String interval = "";
            if (freqStr.equals("??????")) {
                interval = "4";
            } else if (freqStr.equals("??????")) {
                interval = "5";
            } else if (freqStr.equals("??????")) {
                interval = "6";
            } else if (freqStr.equals("??????")) {
                interval = "7";
            }

            String args = mmd.getArgs();
            String sparkopts = "";
            //???????????????key=value,key=value ...,jararg???????????????????????????????????????????????????5?????????,
            //starttime,endtime ???????????????
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

            //??????spark???????????????????????????yarn status ????????????
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
//				// 0.????????? 1.?????? 2.?????? 3.????????? 4.???????????? 5.????????????
//				bean.setStatus(5+"");
//				// ?????????1?????? 2?????? 3??????
//				bean.setOperation(3);
//				engineMapper.updateStatus(bean);
//			}
//			return result;
        return false;
    }


}
