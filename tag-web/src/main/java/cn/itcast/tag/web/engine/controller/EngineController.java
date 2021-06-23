package cn.itcast.tag.web.engine.controller;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.engine.bean.EngineBean;
import cn.itcast.tag.web.engine.service.EngineService;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.utils.PageEnum;
import cn.itcast.tag.web.utils.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.List;

/**
 * EngineController
 *
 * @author liuchengli
 */
@Controller
@RequestMapping(value = "/engine")
public class EngineController extends BaseController {
    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private EngineService engineService;

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public ModelAndView engineView(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.ENGINE_INDEX.getPage());
        modelAndView.addObject("navigation", "engine");
        logger.info("==== engineView@page:{} ====", PageEnum.ENGINE_INDEX);
        return modelAndView;
    }

    /**
     * 启动引擎
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/startEngineTmp")
    public void startEngine(PrintWriter pw, EngineBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        boolean state = false;
        if (bean == null) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== startEngine@params:param error ====");
        }
        logger.info("==== startEngine@params:{} ====", bean);
        state = engineService.startEngine(bean, loginUser);
        if (state) {
            result.set(StateCode.ADD_SUCCESS, "引擎启动成功");
        } else {
            result.set(StateCode.ADD_FAILD, "引擎启动失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== startEngine@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== startEngine@result:服务器错误 ====");
        }
    }

    /**
     * 根据tagId,任务状态,标签名称和模型名称模糊查询
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/queryEngineInfo")
    public void queryEngineInfo(PrintWriter pw, EngineBean bean) {
        Result result = new Result();
        List<EngineBean> bean_ = null;
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        if (null == bean) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== queryEngineInfoByPid@params:param error ====");
        }
        logger.info("==== queryEngineInfoByPid@params:{} ====", bean);

        bean_ = engineService.queryMonitorInfoByAll(bean);

        if (null != bean_) {
            result.set(StateCode.QUERY_SUCCESS, "success", bean_);
        } else {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "none");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryEngineInfoByPid@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== queryEngineInfoByPid@result:服务器错误 ====");
        }
    }

    /**
     * 停止任务
     *
     * @param bean
     */
    @RequestMapping(value = "/stopEngine")
    public void stopEngine(PrintWriter pw, EngineBean bean) {
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        boolean stopResult = engineService.stopEngine(bean, loginUser);
        Result result = new Result();
        if (stopResult) {
            result.set(StateCode.SUCCESS, "任务停止成功");
            logger.info("==== stopEngine@result:{} ====", result);
        } else {
            result.set(StateCode.FAILD, "任务停止失败");
            logger.info("==== stopEngine@result:{} ====", result);
        }
        String jsonResult = JsonUtil.obj2Json(result);
        pw.write(jsonResult);
    }

    /**
     * 暂停任务
     *
     * @param bean
     */
    @RequestMapping(value = "/suspendEngine")
    public void suspendEngine(PrintWriter pw, EngineBean bean) {
        boolean susResult = engineService.suspendEngine(bean);
        Result result = new Result();
        if (susResult) {
            result.set(StateCode.SUCCESS, "任务停止成功");
            logger.info("==== suspendEngine@result:{} ====", result);
        } else {
            result.set(StateCode.FAILD, "任务停止失败");
            logger.info("==== suspendEngine@result:{} ====", result);
        }
        String jsonResult = JsonUtil.obj2Json(result);
        pw.write(jsonResult);
    }

    @ResponseBody
    @RequestMapping(value = "/startEngine")
    public void startEngineByQuartz(PrintWriter pw, EngineBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        boolean state = false;
        if (bean == null) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== startEngineByQuartz@params:param error ====");
        }
        logger.info("==== startEngineByQuartz@params:{} ====", bean);
        state = engineService.startEngineByQuartz(bean, loginUser);
        if (state) {
            result.set(StateCode.ADD_SUCCESS, "引擎启动成功");
        } else {
            result.set(StateCode.ADD_FAILD, "引擎启动失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== startEngine@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== startEngine@result:服务器错误 ====");
        }
    }
}
