package cn.itcast.tag.web.basictag.controller;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.basictag.bean.form.BasicModelRuleFormBean;
import cn.itcast.tag.web.basictag.service.BasicTagService;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.engine.service.MetaDataService;
import cn.itcast.tag.web.engine.service.ModelService;
import cn.itcast.tag.web.engine.service.RuleService;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.user.service.ResourceService;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.PageEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/basic")
@SuppressWarnings("all")
public class BasicTagController extends BaseController {

    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private BasicTagService basicTagService;
    @Resource
    private RuleService ruleService;
    @Resource
    private ModelService modelService;
    @Resource
    private MetaDataService metaDataService;

    @Resource
    private ResourceService resourceService;

    /**
     * 请求基础标签列表并返回页面
     *
     * @param basicTagBean
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = {"/index"})
    public ModelAndView queryBasicTag(ModelAndView modelAndView, String resId) {
        Subject subject = SecurityUtils.getSubject();
        MyShiro.Principal curUser = (MyShiro.Principal) subject.getPrincipal();
        if (null != curUser) {
            UserBean loginUser = new UserBean();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long userId = curUser.getId();
            loginUser.setId(userId);
            Long roleId = roleMaps.get(0).getRoleId();
            List<BasicTagBean> beans = basicTagService.queryForAuthTag(
                    loginUser, new RoleBean(roleId));
            List<BasicTagBean> basicTagTree = basicTagService.getBasicTagTree(beans);
            modelAndView.addObject("basicTagList", basicTagTree);
            modelAndView.addObject("navigation", "basic");
            modelAndView.setViewName(PageEnum.BASICTAG_INDEX.getPage());
        }
        return modelAndView;
    }

    /**
     * 请求基础标签列表
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = {"/queryBasicTag"})
    public void queryBasicTag(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        if (null != curUser) {
            UserBean loginUser = new UserBean();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long userId = curUser.getId();
            loginUser.setId(userId);
            Long roleId = roleMaps.get(0).getRoleId();
            List<BasicTagBean> beans = basicTagService.queryForAuthTag(
                    loginUser, new RoleBean(roleId));
            List<BasicTagBean> basicTagTree = basicTagService.getBasicTagTree(beans);
            if (null == basicTagTree) {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询完成", basicTagTree);
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("==== queryBasicTag@result:服务器错误 ====");
            }
        }
    }

    /**
     * 请求基础标签一二三级标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = {"/queryMainBasicTag"})
    public void queryMainBasicTag(PrintWriter pw) {
        Result result = new Result();
        BasicTagBean basicTagBean = null;
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        if (null != curUser) {
            UserBean loginUser = new UserBean();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long userId = curUser.getId();
            loginUser.setId(userId);
            Long roleId = roleMaps.get(0).getId();
            List<BasicTagBean> listBean = new ArrayList<>();
            List<BasicTagBean> beans = basicTagService.queryForAuthTag(
                    loginUser, new RoleBean(roleId));
            for (BasicTagBean val : beans) {
                if (val.getLevel() == 1 || val.getLevel() == 2
                        || val.getLevel() == 3) {
                    basicTagBean = new BasicTagBean();
                    basicTagBean.setId(val.getId());
                    basicTagBean.setName(val.getName());
                    basicTagBean.setPid(val.getPid());
                    basicTagBean.setIndustry(val.getIndustry());
                    basicTagBean.setBusiness(val.getBusiness());
                    basicTagBean.setLevel(val.getLevel());
                    basicTagBean.setState(val.getState());
                    basicTagBean.setRule(val.getRule());
                    listBean.add(basicTagBean);
                }
            }
            if (listBean.size() == 0) {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询完成", listBean);
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
                logger.info("==== queryMainBasicTag@result:{} ====", result);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("==== queryMainBasicTag@result:服务器错误 ====");
            }
        }
    }

    /**
     * 根据级别查询基础标签
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = {"/queryForTagLevel"})
    public void queryForTagLevel(PrintWriter pw, BasicTagBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        Long userId = curUser.getId();
        loginUser.setId(userId);
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getId();
        if (bean == null) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== queryForTagLevel@params:param error ====");
        } else {
            logger.info("==== queryForTagLevel@params:{} ====", bean);
            List<BasicTagBean> beans = basicTagService.queryBasicTagForLevel(
                    bean, loginUser, new RoleBean(roleId));
            if (null == beans) {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询完成", beans);
            }
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryForTagLevel@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryForTagLevel@result:服务器错误 ====");
        }
    }

    /**
     * 添加基础标签一二三级
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/addMainBasicTag", method = {RequestMethod.POST})
    public void addMainBasicTag(PrintWriter pw,
                                @RequestBody List<Map<String, String>> beans) {
        Result responseMsg = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long userId = curUser.getId();
        loginUser.setId(userId);
        Long roleId = roleMaps.get(0).getId();
        // 返回值为1表示创建成果，返回值为0创建失败，返回-11,-12,-13表示已经存在
        int state = 0;
        if (null != beans) {
            List<BasicTagBean> listBean = new ArrayList<BasicTagBean>();
            for (Map<String, String> map : beans) {
                BasicTagBean bean = new BasicTagBean();
                bean.setName(map.get("name"));
                bean.setLevel(Integer.valueOf(map.get("level")));
                bean.setState(3);
                listBean.add(bean);
            }
            logger.info("==== addMainBasicTag@params:{} ====", beans);
            state = basicTagService.addMainBasicTag(
                    listBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== addMainBasicTag@params:param error ====");
            responseMsg.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (state == -11) {
            responseMsg.set(StateCode.RECORD_DUP_LEVEL_ONE_FAILD, "一级基础标签已存在");
        } else if (state == -12) {
            responseMsg.set(StateCode.RECORD_DUP_LEVEL_TWO_FAILD, "二级基础标签已存在");
        } else if (state == -13) {
            responseMsg.set(StateCode.RECORD_DUP_LEVEL_THREE_FAILD, "三级基础标签已存在");
        } else if (state == 1) {
            responseMsg.set(StateCode.ADD_SUCCESS, "基础标签创建成功");
        } else {
            responseMsg.set(StateCode.ADD_FAILD, "基础标签创建失败");
        }
        String resultJson = JsonUtil.obj2Json(responseMsg);
        try {
            pw.write(resultJson);
            logger.info("==== addMainBasicTag@result:{} ====", responseMsg);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addMainBasicTag@result:服务器错误 ====");
        }
    }

    /**
     * 添加第四级基础标签
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/addFourtag")
    public void addFourtag(PrintWriter pw, BasicModelRuleFormBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long userId = curUser.getId();
        loginUser.setId(userId);
        Long roleId = roleMaps.get(0).getId();
        int state = 0;
        if (null == bean) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            logger.info("==== addFourtag@params: params is null! ====");
        } else {
            // 添加第四级标签
            // state : 1添加成功，-1参数为空，0添加失败
            state = basicTagService.addFourthBasicTag(bean, loginUser);
            if (state == 1) {
                result.set(StateCode.ADD_SUCCESS, "标签新增成功");
                logger.info("==== modelUpload@condition: add success ====");
            } else if (state == -1) {
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
                logger.info("==== addFourtag@params: params is null! ====");
            } else if (state == 0) {
                result.set(StateCode.ADD_FAILD, "标签新增失败");
                logger.info("==== addFourtag@condition:add faild！ ====");
            }
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== modelUpload@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== modelUpload@result:服务器错误 ====");
        }
    }

    /**
     * 上传模型文件
     *
     * @param request
     * @param pw
     */
    @ResponseBody
    @RequestMapping(value = "/modelUpload")
    public void modelUpload(HttpServletRequest request, PrintWriter pw) {
        Result result = new Result();
        Calendar now = Calendar.getInstance();
        try {
            // 上传文件
            String separator = File.separator;
            String path = System.getProperty("user.home") +
                    separator + now.get(Calendar.YEAR) + separator
                    + String.valueOf(now.get(Calendar.MONTH) + 1) +
                    separator + Calendar.DAY_OF_MONTH + separator;
            File localFile = this.upload(request, path);
            if (null != localFile && localFile.exists()) {
                String absolutePath = localFile.getAbsolutePath();
                result.set(StateCode.SUCCESS, "上传成功", absolutePath);
                logger.info("==== modelUpload@condition:file={} upload success ====", absolutePath);
            } else {
                result.set(StateCode.FAILD, "上传失败");
                logger.info("==== modelUpload@condition:file upload faild! ====");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== modelUpload@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== modelUpload@result:服务器错误 ====");
        }
    }

    /**
     * 添加第五级基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/addFifthBasicTag")
    public void addFifthBasicTag(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        if (null != basicTagBean) {
            logger.info("==== addFifthBasicTag@params:{ basicTagBean } ====", basicTagBean);
            basicTagService.addFifthBasicTag(basicTagBean, loginUser);
            if (basicTagBean.getId() == 0) {
                result.set(StateCode.ADD_FAILD, "第五级基础标签创建失败");
            } else {
                result.set(StateCode.ADD_SUCCESS, "第五级基础标签创建成功");
            }
        } else {
            logger.info("==== addFifthBasicTag@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== addFifthBasicTag@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addFifthBasicTag@result:服务器错误 ====");
        }
    }

    /**
     * 根据级别查询基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/queryBasicTagForLevel")
    public void queryBasicTagForLevel(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        List<BasicTagBean> bean = null;
        Subject subject = SecurityUtils.getSubject();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long userId = curUser.getId();
        loginUser.setId(userId);
        Long roleId = roleMaps.get(0).getId();
        if (null != basicTagBean) {
            logger.info("==== queryBasicTagForLevel@params:{} ====", basicTagBean);
            bean = basicTagService.queryBasicTagForLevel(
                    basicTagBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== queryBasicTagForLevel@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == bean) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", bean);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicTagForLevel@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicTagForLevel@result:服务器错误 ====");
        }
    }

    /**
     * 根据ID查询基础标签 不包括第四级
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/queryBasicTagForId")
    public void queryBasicTagForId(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicTagBean> bean = null;
        if (null != basicTagBean) {
            logger.info("==== queryBasicTagForId@params:{} ====", basicTagBean);
            bean = basicTagService.queryBasicTagForId(
                    basicTagBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== queryBasicTagForId@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == bean) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", bean);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicTagForId@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicTagForId@result:服务器错误 ====");
        }
    }

    /**
     * 根据名称搜索基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/searchBasicTagForName")
    public void searchBasicTagForName(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        List<BasicModelRuleFormBean> bean = null;
        List<BasicModelRuleFormBean> beans = null;
        BasicModelRuleFormBean basicMRFBean = null;
        List<BasicModelRuleFormBean> tmpBean = null;
        List<BasicModelRuleFormBean> modelBean = null;
        if (null != basicTagBean) {
            logger.info("==== searchBasicTagForName@params:{} ====", basicTagBean);
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            UserBean loginUser = new UserBean();
            loginUser.setId(curUser.getId());
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long roleId = roleMaps.get(0).getRoleId();

            basicMRFBean = new BasicModelRuleFormBean();
            basicMRFBean.setTagId(basicTagBean.getId());
            basicMRFBean.setTagName(basicTagBean.getName());
            basicMRFBean.setLevel(basicTagBean.getLevel());

            if (basicTagBean.getLevel() == 3) {
                bean = basicTagService.queryBasicTagAndModelForWithPid(
                        basicMRFBean, loginUser, (new RoleBean(roleId)));
            } else {
                modelBean = basicTagService.queryBasicTagAndModelForWithPid(
                        basicMRFBean, loginUser, (new RoleBean(roleId)));
            }
            if (basicTagBean.getLevel() == 3) {
                beans = new ArrayList<>();
                if (null == bean) {
                    result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
                } else {
                    for (BasicModelRuleFormBean val : bean) {
                        basicMRFBean = new BasicModelRuleFormBean();
                        basicMRFBean.setTagId(val.getTagId());
                        basicMRFBean.setTagName(val.getTagName());
                        basicMRFBean.setBusiness(val.getBusiness());
                        basicMRFBean.setIndustry(val.getIndustry());
                        basicMRFBean.setLevel(val.getLevel());
                        basicMRFBean.setScheTime(val.getScheTime());
                        basicMRFBean.setPid(val.getPid());
                        basicMRFBean.setRule(val.getRule());
                        basicMRFBean.setState(val.getState());
                        beans.add(basicMRFBean);
                    }
                    result.set(StateCode.QUERY_SUCCESS, "查询完成", beans);
                }
            } else {
                if (null == modelBean) {
                    result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
                } else {
                    result.set(StateCode.QUERY_SUCCESS, "查询完成", modelBean);
                }
            }
        } else {
            logger.info("==== searchBasicTagForName@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== searchBasicTagForName@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== searchBasicTagForName@result:服务器错误 ====");
        }
    }

    /**
     * 根据标签父级ID(PID)搜索基础标签 返回的是PID等于当前传入的PID的 基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/queryBasicTagForPid")
    public void queryBasicTagForPid(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicTagBean> bean = null;
        if (null != basicTagBean) {
            logger.info("==== queryBasicTagForPid@params:{} ====", basicTagBean);
            bean = basicTagService.queryBasicTagForPid(
                    basicTagBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== queryBasicTagForPid@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == bean) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", bean);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicTagForPid@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicTagForPid@result:服务器错误 ====");
        }
    }

    /**
     * 根据ID删除基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/delBasicTagForId")
    public void delBasicTagForId(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        int state = 0;
        if (null != basicTagBean) {
            state = basicTagService.delBasicTagForId(
                    basicTagBean, loginUser, new RoleBean(roleId));
            // state: 0为删除失败，1为删除成功，2为标签下有子标签
            if (state == 0) {
                result.set(StateCode.DEL_FAILD, "删除失败");
            } else if (state == 2) {
                result.set(StateCode.RECORD_EXIST_FAILD, "子层级不为空，不能删除");
            } else if (state == 1) {
                result.set(StateCode.DEL_SUCCESS, "删除成功");
            }
        } else {
            logger.info("==== delBasicTagForId@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== delBasicTagForId@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delBasicTagForId@result:服务器错误 ====");
        }
    }

    /**
     * 更新第四级基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/updateMainBasicTagForId", method = RequestMethod.GET)
    public void updateMainBasicTagForId(PrintWriter pw, BasicTagBean bean) {
        Result result = new Result();
        int state = 0;
        if (null != bean) {
            logger.info("==== updateMainBasicTagForId@params:{} ====", bean);
            state = basicTagService.updateMainBasicTagForId(bean);
            if (state > 0) {
                result.set(StateCode.UPD_SUCCESS, "更新成功");
            } else {
                result.set(StateCode.UPD_FAILD, "更新失败");
            }
        } else {
            logger.info("==== updateMainBasicTagForId@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== updateMainBasicTagForId@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updateMainBasicTagForId@result:服务器错误 ====");
        }
    }

    /**
     * 更新第四级基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/updateFourthBasicTag")
    public void updateFourthBasicTag(PrintWriter pw, BasicModelRuleFormBean bean) {
        Result result = new Result();
        if (null != bean) {
            logger.info("==== updateFourthBasicTag@params:{} ====", bean);
            if (basicTagService.updateFourthBasicTag(bean)) {
                result.set(StateCode.UPD_SUCCESS, "更新成功");
            } else {
                result.set(StateCode.UPD_FAILD, "更新失败");
            }
        } else {
            logger.info("==== updateFourthBasicTag@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== updateFourthBasicTag@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updateFourthBasicTag@result:服务器错误 ====");
        }
    }

    /**
     * 更新第五级基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/updateFifthBasicTag")
    public void updateFifthBasicTag(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        int state = 0;
        if (null != basicTagBean) {
            logger.info("==== updateFifthBasicTag@params:{} ====", basicTagBean);
            state = basicTagService.updateFifthBasicTag(basicTagBean);
        } else {
            logger.info("==== updateFifthBasicTag@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (state <= 0) {
            result.set(StateCode.UPD_FAILD, "更新失败");
        } else {
            result.set(StateCode.UPD_SUCCESS, "更新成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== updateFifthBasicTag@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updateFifthBasicTag@result:服务器错误 ====");
        }
    }

    /**
     * 根据ID查询其子级基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = {"/queryBasicTagForUserId"}, method = RequestMethod.GET)
    public void queryBasicTagForUserId(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicTagBean> bean = null;
        if (null != basicTagBean) {
            logger.info("==== queryBasicTagForPid@params:{} ====", basicTagBean);
            bean = basicTagService.queryBasicTagForUserId(
                    basicTagBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== queryBasicTagForPid@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == bean) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", bean);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicTagForPid@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicTagForPid@result:服务器错误 ====");
        }
    }

    /**
     * 根据pid和level查询基础标签 其中带有名称的模糊查询
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = {"/queryBasicTagAndModelForWithPid"}, method = RequestMethod.GET)
    public void queryBasicTagAndModelForWithPid(PrintWriter pw,
                                                BasicModelRuleFormBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicModelRuleFormBean> bean = null;
        if (null != basicTagBean) {
            logger.info("==== queryBasicTagAndModelForWithPid@params:{} ====", basicTagBean);
            bean = basicTagService.queryBasicTagAndModelForWithPid(
                    basicTagBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== queryBasicTagAndModelForWithPid@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == bean) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", bean);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicTagAndModelForWithPid@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicTagAndModelForWithPid@result:服务器错误 ====");
        }
    }

    /**
     * 根据pid和level查询基础标签 其中带有名称的模糊查询
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = {"/queryBasicTagForWithPid"}, method = RequestMethod.GET)
    public void queryBasicTagForWithPid(PrintWriter pw, BasicTagBean basicTagBean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicTagBean> bean = null;
        if (null != basicTagBean) {
            logger.info("==== queryBasicTagForWithPid@params:{} ====", basicTagBean);
            bean = basicTagService.queryBasicTagForWithPid(
                    basicTagBean, loginUser, new RoleBean(roleId));
        } else {
            logger.info("==== queryBasicTagForWithPid@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == bean) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", bean);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicTagForWithPid@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicTagForWithPid@result:服务器错误 ====");
        }
    }

    /**
     * 根据ID查询第四级基础标签
     *
     * @param pw
     * @param basicTagBean
     */
    @ResponseBody
    @RequestMapping(value = "/queryBasicModelRuleForId")
    public void queryBasicModelRuleForId(PrintWriter pw, BasicModelRuleFormBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<BasicModelRuleFormBean> beans = null;
        if (null != bean) {
            logger.info("==== queryBasicModelRuleForId@params:{} ====", bean);
            beans = basicTagService.queryFourthBasicTagForId(bean, loginUser);
        } else {
            logger.info("==== queryBasicModelRuleForId@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (null == beans) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", beans);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryBasicModelRuleForId@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryBasicModelRuleForId@result:服务器错误 ====");
        }
    }

    /**
     * 任务处理
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = {"/taskProcessing"}, method = RequestMethod.GET)
    public void taskProcessing(HttpServletRequest request, PrintWriter pw, BasicTagBean bean) {
        String classesDir = getClassesDir(request);
        // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        int state = 0;
        if (null != bean) {
            bean.setRemark(classesDir);
            logger.info("==== taskProcessing@params:{} ====", bean);
            state = basicTagService.taskProcessing(bean, loginUser);
        } else {
            logger.info("==== taskProcessing@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        if (state == 0) {
            result.set(StateCode.FAILD, "操作失败");
        } else if (state == 1) {
            result.set(StateCode.SUCCESS, "操作成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== taskProcessing@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== taskProcessing@result:服务器错误 ====");
        }
    }

    /**
     * 判断便器是否存在
     *
     * @param pw
     * @param fivetagname
     * @param pid
     */
    @ResponseBody
    @RequestMapping(value = {"/isExistForName"})
    public void isExistForName(PrintWriter pw, String fivetagname, long pid) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        if (StringUtils.isBlank(fivetagname) || pid < -1) {
            logger.info("==== isExistForName@param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        } else {
            BasicTagBean bean = new BasicTagBean();
            bean.setPid(pid);
            bean.setName(fivetagname);
            Boolean isExist = basicTagService.isExistForName(
                    bean, loginUser, new RoleBean(roleId));
            if (isExist) {
                result.set(StateCode.RECORD_DUP_FAILD, "查询成功,该标签已存在", isExist);
            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询成功,暂无此标签", isExist);
            }
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== taskProcessing@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== taskProcessing@result:服务器错误 ====");
        }
    }
}
