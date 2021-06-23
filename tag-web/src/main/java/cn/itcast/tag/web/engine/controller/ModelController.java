package cn.itcast.tag.web.engine.controller;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.engine.service.ModelService;
import cn.itcast.tag.web.engine.bean.ModelBean;
import cn.itcast.tag.web.utils.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.List;

/**
 * ModelController
 *
 * @author mengyao
 */
@Controller
@RequestMapping(value = "/model")
public class ModelController {
    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private ModelService modelService;


    /**
     * 新增模型
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/addModel")
    public void addModel(PrintWriter pw, ModelBean bean) {
        Result result = new Result();
        boolean state = false;
        if (bean == null) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== addModel@params:param error ====");
        }
        logger.info("==== addModel@params:{} ====", bean);
        String modelName = bean.getModelName();
        String modelMain = bean.getModelMain();
        String modelPath = bean.getModelPath();
        if (modelPath != null && modelMain != null && modelName != null) {
            state = modelService.addModel(bean);
            logger.info("==== addModel@conditions:{},{},{} ====", modelName, modelMain, modelPath);
        } else {
            logger.info("==== addModel@conditions:condition error ====");
        }
        if (state) {
            result.set(StateCode.ADD_SUCCESS, "模型新增成功");
        } else {
            result.set(StateCode.ADD_FAILD, "模型新增失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== addModel@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addModel@result:服务器错误 ====");
        }
    }

    /**
     * 逻辑删除模型
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/delModel")
    public void delModel(PrintWriter pw, ModelBean bean) {
        Result result = new Result();
        boolean state = false;
        if (bean == null) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== delModel@params:param error ====");
        }
        logger.info("==== delModel@params:{} ====", bean);
        long id = bean.getId();
        if (id > 0) {
            state = modelService.logicDelModel(bean);
            logger.info("==== delModel@conditions:{} ====", id);
        } else {
            logger.info("==== delModel@conditions:condition error ====");
        }
        if (state) {
            result.set(StateCode.DEL_SUCCESS, "模型删除成功");
        } else {
            result.set(StateCode.DEL_FAILD, "模型删除失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== delModel@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delModel@result:服务器错误 ====");
        }
    }

    /**
     * 模型名称修改
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/updateModelName")
    public void updateModelName(PrintWriter pw, ModelBean bean) {
        Result result = new Result();
        boolean state = false;
        if (bean == null) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== updateModelName@params:param error ====");
        }
        logger.info("==== updateModelName@params:{} ====", bean);
        long id = bean.getId();
        String modelName = bean.getModelName();
        if (modelName != null && id > 0) {
            state = modelService.updModelForName(bean);
            logger.info("==== updateModelName@conditions:{},{} ====", id, modelName);
        } else {
            logger.info("==== updateModelName@conditions:condition error ====");
        }
        if (state) {
            result.set(StateCode.UPD_SUCCESS, "模型修改成功");
        } else {
            result.set(StateCode.UPD_FAILD, "模型修改失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== updateModelName@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updateModelName@result:服务器错误 ====");
        }
    }

    /**
     * 根据ID查询模型
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/queryForId")
    public void queryForId(PrintWriter pw, ModelBean bean) {
        Result result = new Result();
        ModelBean bean_ = null;
        if (null == bean) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== queryForId@params:param error ====");
        }
        logger.info("==== queryForId@params:{} ====", bean);
        long id = bean.getId();
        if (id > 0) {
            bean_ = modelService.queryForId(bean);
            logger.info("==== queryForId@conditions:{} ====", id);
        } else {
            logger.info("==== queryForId@conditions:condition error ====");
        }
        if (null != bean_) {
            result.set(StateCode.QUERY_SUCCESS, "模型查询成功");
        } else {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无此模型数据");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryForId@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForId@result:服务器错误 ====");
        }
    }

    /**
     * 根据标签ID查询模型
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/queryForTag")
    public void queryForTag(PrintWriter pw, ModelBean bean) {
        Result result = new Result();
        List<ModelBean> beans = null;
        if (null == bean) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== queryForTag@params:param error ====");
        }
        logger.info("==== queryForTag@params:{} ====", bean);
        long tagId = bean.getTagId();
        if (tagId > 0) {
            beans = modelService.queryForTag(bean);
            logger.info("==== queryForTag@conditions:{} ====", tagId);
        } else {
            logger.info("==== queryForTag@conditions:condition error ====");
        }
        if (null != beans) {
            result.set(StateCode.QUERY_SUCCESS, "模型查询成功");
        } else {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无此模型数据");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryForTag@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForTag@result:服务器错误 ====");
        }
    }

    /**
     * 查询所有模型
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/queryForScan")
    public void queryForScan(PrintWriter pw, ModelBean bean) {
        Result result = new Result();
        List<ModelBean> beans = null;
        if (null == bean) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== queryForScan@params:param error ====");
        }
        logger.info("==== queryForScan@params:{} ====", bean);
        //当前页
        int cp = bean.getCp();
        if (cp > 0) {
            beans = modelService.queryForScan(bean);
            logger.info("==== queryForScan@conditions:{} ====", cp);
        } else {
            logger.info("==== queryForScan@conditions:condition error ====");
        }
        if (null != beans) {
            result.set(StateCode.QUERY_SUCCESS, "模型列表查询成功");
        } else {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryForScan@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForScan@result:服务器错误 ====");
        }
    }

}
