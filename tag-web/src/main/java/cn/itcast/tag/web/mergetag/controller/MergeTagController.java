package cn.itcast.tag.web.mergetag.controller;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.basictag.bean.form.BasicTagFormBean;
import cn.itcast.tag.web.basictag.service.BasicTagService;
import cn.itcast.tag.web.commons.bean.Bean;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.mergetag.bean.form.UserMergeTagTagFormBean;
import cn.itcast.tag.web.mergetag.service.MergeTagService;
import cn.itcast.tag.web.search.service.SearchService;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.PageEnum;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping(value = "/merge")
public class MergeTagController extends BaseController {

    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private MergeTagService mergeTagService;
    @Resource
    private BasicTagService basicTagService;
    @Resource
    private SearchService searchService;

    /**
     * 返回组合标签页
     *
     * @param request
     * @return
     */
    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public ModelAndView mergeTagView(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.MERGETAG_INDEX.getPage());
        modelAndView.addObject("navigation", "merge");
        return modelAndView;
    }

    /**
     * 返回组合标签页
     *
     * @param request
     * @return
     */
    @RequestMapping(value = {"/updateMergeTagView"}, method = RequestMethod.GET)
    public ModelAndView updateMergeTagView(HttpServletRequest request) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long userId = curUser.getId();
        loginUser.setId(userId);
        Long roleId = roleMaps.get(0).getRoleId();
        RoleBean roleBean = new RoleBean(roleId);
        StringBuffer sb = new StringBuffer();
        List<BasicTagBean> basicTagTree = null;
        List<BasicTagFormBean> basicTag = null;
        List<BasicTagBean> listbean = null;
        List<BasicTagFormBean> list1 = null;
        List<BasicTagFormBean> list = new ArrayList<>();
        UserMergeTagTagFormBean userMergeTagTagFormBean = null;
        long id = Long.parseLong(request.getParameter("id"));
        if (id > 0) {
            MergeTagBean mergeTagBean = new MergeTagBean();
            mergeTagBean.setId(id);
            List<UserMergeTagTagFormBean> bean = mergeTagService.updateQueryMergeTag(mergeTagBean, loginUser, roleBean);
            if (null != bean) {
                // 取组合标签
                userMergeTagTagFormBean = new UserMergeTagTagFormBean();
                for (UserMergeTagTagFormBean val : bean) {
                    userMergeTagTagFormBean.setMergeTagId(val.getMergeTagId());
                    userMergeTagTagFormBean.setMergeTagName(val.getMergeTagName());
                    userMergeTagTagFormBean.setUserId(val.getUserId());
                    userMergeTagTagFormBean.setCondition(val.getCondition());
                    userMergeTagTagFormBean.setIntro(val.getIntro());
                    userMergeTagTagFormBean.setPurpose(val.getPurpose());
                    userMergeTagTagFormBean.setState(val.getState());
                    break;
                }
                // 取第五级基础标签
                basicTag = new ArrayList<>();
                BasicTagFormBean basicForm = new BasicTagFormBean();
                for (UserMergeTagTagFormBean val : bean) {
                    basicForm.setId(val.getPid());
                    basicForm.setName(val.getBasicTagName());
                    basicForm.setPid(val.getBasicTagId());
                    basicForm.setConditions(val.getConditions());
                    basicTag.add(basicForm);
                    basicForm = new BasicTagFormBean();
                }
                // 取第四级基础标签
                BasicTagBean baisc = new BasicTagBean();
                for (BasicTagFormBean val1 : basicTag) {
                    baisc.setId(val1.getId());
                    // 取出第四级基础标签
                    listbean = basicTagService.queryBasicTagForId(baisc, loginUser, roleBean);
                    sb.append(val1.getPid()).append(" ").append(val1.getConditions()).append(" ");
                    long temp = 0;
                    temp = val1.getPid(); // ID
                    val1.setPid(val1.getId());
                    val1.setId(temp);
                    list1 = new ArrayList<>();
                    for (BasicTagBean val2 : listbean) {
                        basicForm = new BasicTagFormBean();
                        basicForm.setId(val2.getId());
                        basicForm.setName(val2.getName());
                        basicForm.setBusiness(val2.getBusiness());
                        basicForm.setConditions(val1.getConditions());
                        basicForm.setIndustry(val2.getIndustry());
                        basicForm.setRule(val2.getRule());
                        basicForm.setPid(val2.getPid());
                        list1.add(val1);
                        basicForm.setSubTags(list1);
                        list.add(basicForm);
                        basicForm = new BasicTagFormBean();
                    }
                    baisc = new BasicTagBean();
                }
                List<BasicTagBean> beans = basicTagService.queryForAuthTag(loginUser, roleBean);
                if (null != beans) {
                    basicTagTree = basicTagService.getBasicTagTree(beans);
                } else {
                    result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
                }
            } else {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
            }
            String listBasicTagId = sb.toString();
            listBasicTagId = listBasicTagId.substring(0, listBasicTagId.length() - 1);
            userMergeTagTagFormBean.setListBasicTagId(listBasicTagId);
        }

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("basictag", list);
        modelAndView.addObject("mergetag", userMergeTagTagFormBean);
        modelAndView.addObject("basicTagList", basicTagTree);
        modelAndView.addObject("navigation", "statag");
        modelAndView.setViewName(PageEnum.MERGETAG_CREATE.getPage());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = {"/queryUpdateMergeTagData"}, method = RequestMethod.GET)
    public void queryUpdateMergeTagData(PrintWriter pw, UserMergeTagTagFormBean bean) throws Exception {
        Result result = new Result();
        List<BasicTagBean> beans = null;
        BasicTagFormBean basicTagFormBean = null;
        List<BasicTagFormBean> basicTagFormBeans = null;
        Map<String, Object> map = new HashMap<>();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long userId = curUser.getId();
        loginUser.setId(userId);
        Long roleId = roleMaps.get(0).getRoleId();
        RoleBean roleBean = new RoleBean(roleId);
        if (bean != null) {
            MergeTagBean mergeTagBean = new MergeTagBean();
            mergeTagBean.setId(bean.getMergeTagId());
            BasicTagBean basicTagBean = new BasicTagBean();
            basicTagBean.setLevel(bean.getBaisicLevel());
            mergeTagBean = mergeTagService.queryUpdateMergeTagData(mergeTagBean, loginUser, roleBean);
            beans = basicTagService.queryBasicTagForLevel(basicTagBean, loginUser, new RoleBean(roleId));
            if (beans != null) {
                basicTagFormBeans = new ArrayList<>();
                for (BasicTagBean val : beans) {
                    basicTagFormBean = new BasicTagFormBean();
                    if (bean.getBasicTagName() != null && bean.getBasicTagName().trim().equals(val.getName().trim())) {
                        basicTagFormBean.setName(val.getName());
                        basicTagFormBean.setFlag(true);
                    } else {
                        basicTagFormBean.setName(val.getName());
                        basicTagFormBean.setFlag(false);
                    }
                    basicTagFormBeans.add(basicTagFormBean);
                }
                map.put("basicTag", basicTagFormBeans);
                map.put("mergeTag", mergeTagBean);
                result.set(StateCode.QUERY_SUCCESS, "查询完成", map);
            } else {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
            }
        } else {
            logger.info("==== queryUpdateMergeTagData@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        System.out.println("========= " + resultJson);
        try {
            pw.write(resultJson);
            logger.info("==== queryUpdateMergeTagData@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryUpdateMergeTagData@result:服务器错误 ====");
        }
    }

    /**
     * 请求组合标签列表带有分页
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/queryMergeTagData")
    public void queryMergeTagData(PrintWriter pw, String json) {
        Result result = new Result();
        List<MergeTagBean> list = null;
        Map<String, Object> resultMap = null;
        MergeTagBean mergeTagBean = new MergeTagBean();
        ;
        Bean bean = null;
        MergeTagBean form = (MergeTagBean) JsonUtil.json2Obj(json, MergeTagBean.class);
        if (form.getBean() != null) {
            bean = form.getBean();
            if (StringUtils.isNotBlank(form.getName()))
                mergeTagBean.setName(form.getName());
            logger.info("==== queryMergeTagData@params:{} ====", bean);
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long userId = curUser.getId();
            Long roleId = roleMaps.get(0).getRoleId();
            if (null != curUser) {
                UserBean loginUser = new UserBean();
                loginUser.setId(userId);
                int curpage = form.getBean().getCp(); // 当前页
                int pageCount = 0;
                int pageSize = form.getBean().getPr(); // 页大小
                int rowsCount = 0;
                int sr = (curpage - 1) * pageSize; //
                int er = pageSize * curpage; //
                bean = new Bean();
                bean.set(sr, er);
                Map<String, Object> map = null;
                List<MergeTagBean> listBean = null;
                int count = 0;
                map = mergeTagService.queryForAuthTag(mergeTagBean, bean, loginUser, new RoleBean(roleId));
                if (map != null) {
                    Iterator<Entry<String, Object>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        String key = (String) entry.getKey();
                        if ("tags".equals(key)) {
                            listBean = (List<MergeTagBean>) entry.getValue();
                        }
                        if ("count".equals(key)) {
                            count = Integer.valueOf(String.valueOf(entry.getValue()));
                        }
                    }
                    if (listBean != null) {
                        list = new ArrayList<>();
                        for (MergeTagBean val : listBean) {
                            long mergeTagId = val.getId();
                            long userNum = searchService.getUserCountByTagId(mergeTagId, 2);
                            MergeTagBean mtBean = mergeTagService.getBasicTagNum(mergeTagId);
                            mergeTagBean = new MergeTagBean();
                            mergeTagBean.setId(mergeTagId);
                            mergeTagBean.setName(val.getName());
                            mergeTagBean.setIntro(val.getIntro());
                            mergeTagBean.setPurpose(val.getPurpose());
                            mergeTagBean.setState(val.getState());
                            if (mtBean != null) {
                                mergeTagBean.setTagNum(mtBean.getTagNum());
                            } else {
                                mergeTagBean.setTagNum(0);
                            }
                            mergeTagBean.setUserNum(userNum);
                            mergeTagBean.setCtime(val.getCtime());
                            mergeTagBean.setRemark(val.getRemark());
                            list.add(mergeTagBean);
                        }
                    }
                }
                rowsCount = count;
                pageCount = rowsCount % pageSize;
                if (pageCount == 0) {
                    pageCount = rowsCount / pageSize;
                } else {
                    pageCount = rowsCount / pageSize + 1;
                }
                bean = new Bean();
                bean.setCp(curpage); // 当前页(current page)：从1开始
                bean.setTr(rowsCount); // 总条数(total record)
                bean.setPr(pageSize); // 每页条数(page record)
            }
            resultMap = new HashMap<>();
            if (list == null) {
                resultMap.put("data", list);
                resultMap.put("page", bean);
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据", resultMap);
            } else {
                resultMap.put("data", list);
                resultMap.put("page", bean);
                result.set(StateCode.QUERY_SUCCESS, "查询完成", resultMap);
            }
        } else {
            logger.info("==== addMergeTag@params:param error ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryMergeTagData@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== queryMergeTagData@result:服务器错误 ====");
        }
    }

    /**
     * 创建组合标签时，取出基础标签数据
     *
     * @param request
     * @param basicTagBean
     * @param modelAndView
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createMergeTag")
    public ModelAndView createMergeTag(HttpServletRequest request, BasicTagBean basicTagBean,
                                       ModelAndView modelAndView) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        if (null != curUser) {
            UserBean loginUser = new UserBean();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long userId = curUser.getId();
            loginUser.setId(userId);
            Long roleId = roleMaps.get(0).getRoleId();
            List<BasicTagBean> beans = basicTagService.queryForAuthTag(loginUser, new RoleBean(roleId));
            if (null != beans) {
                List<BasicTagBean> basicTagTree = basicTagService.getBasicTagTree(beans);
                if (null != basicTagTree) {
                    modelAndView.addObject("basicTagList", basicTagTree);
                } else {
                    result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
                }
            } else {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
            }
        }
        modelAndView.setViewName(PageEnum.MERGETAG_CREATE.getPage());
        return modelAndView;
    }

    /**
     * 添加组合标签
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/addMergeTag")
    public void addMergeTag(PrintWriter pw, UserMergeTagTagFormBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        long userId = curUser.getId();
        UserBean loginUser = new UserBean();
        loginUser.setId(userId);
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        loginUser.setId(userId);
        Long roleId = roleMaps.get(0).getRoleId();
        if (null != bean) {
            logger.info("==== addMergeTag@params:{} ====", bean);
            int flag = mergeTagService.addMergeTag(bean, loginUser, new RoleBean(roleId));
            if (flag == -1) {
                result.set(StateCode.RECORD_DUP_FAILD, "该组合标签已经存在");
                String resultJson = JsonUtil.obj2Json(result);
                try {
                    pw.write(resultJson);
                    logger.info("==== addMergeTag@result:{} ====", result);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("==== addMergeTag@result:服务器错误 ====");
                }
            } else if (flag == 0) {
                result.set(StateCode.ADD_FAILD, "创建失败");
            } else if (flag == 1) {
                result.set(StateCode.ADD_SUCCESS, "创建成功");
            }
        } else {
            logger.info("==== addMergeTag@params:param error ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== addMergeTag@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== addMergeTag@result:服务器错误 ====");
        }
    }

    /**
     * 更新组合标签
     *
     * @param pw
     * @param bean
     */
    @ResponseBody
    @RequestMapping(value = "/updateMergeTag")
    public void updateMergeTag(PrintWriter pw, UserMergeTagTagFormBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        long userId = curUser.getId();
        UserBean loginUser = new UserBean();
        loginUser.setId(userId);
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        if (null != bean) {
            logger.info("==== updateMergeTag@params:{} ====", bean);
            // 第一步删除
            int flag = mergeTagService.delMergeTagForId(bean, loginUser, new RoleBean(roleId));
            if (flag == 0) {
                result.set(StateCode.UPD_FAILD, "更新失败");
                String resultJson = JsonUtil.obj2Json(result);
                try {
                    pw.write(resultJson);
                    logger.info("==== updateMergeTag@result:{} ====", result);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("==== updateMergeTag@result:服务器错误 ====");
                }
            }
            // 第二步创建
            flag = mergeTagService.addMergeTag(bean, loginUser, new RoleBean(roleId));
            if (flag == -1 || flag == 0) {
                result.set(StateCode.UPD_FAILD, "更新失败");
                String resultJson = JsonUtil.obj2Json(result);
                try {
                    pw.write(resultJson);
                    logger.info("==== updateMergeTag@result:{} ====", result);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("==== updateMergeTag@result:服务器错误 ====");
                }
            } else {
                result.set(StateCode.UPD_SUCCESS, "更新成功");
            }
        } else {
            logger.info("==== updateMergeTag@params:param error ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== updateMergeTag@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== updateMergeTag@result:服务器错误 ====");
        }
    }

    /**
     * 根据ID删除组合标签
     *
     * @param pw
     * @param mergeTagMapFormBean
     */
    @ResponseBody
    @RequestMapping(value = "/delMergeTagForId")
    public void delMergeTagForId(PrintWriter pw, UserMergeTagTagFormBean bean) {
        Result result = new Result();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        long userId = curUser.getId();
        UserBean loginUser = new UserBean();
        loginUser.setId(userId);
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        if (null != bean) {
            logger.info("==== delMergeTagForId@params:{} ====", bean);
            int flag = mergeTagService.delMergeTagForId(bean, loginUser, new RoleBean(roleId));
            if (flag == 0) {
                result.set(StateCode.DEL_FAILD, "删除失败");
            } else {
                result.set(StateCode.DEL_SUCCESS, "删除成功");
            }
        } else {
            logger.info("==== delMergeTagForId@params:param error ====");
            result.set(StateCode.PARAM_FAILD, "参数输入错误");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== delMergeTagForId@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== delMergeTagForId@result:服务器错误 ====");
        }
    }
}
