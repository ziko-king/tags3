/**
 * 项目名称：mengyao
 * 创建日期：2018年6月8日
 * 修改历史：
 * 1、[2018年6月8日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.examine.controller;

import cn.itcast.tag.web.basictag.bean.form.UserTagFormBean;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.examine.bean.TagAuditBean;
import cn.itcast.tag.web.examine.service.ExamineService;
import cn.itcast.tag.web.mergetag.bean.UserMergeTagMapBean;
import cn.itcast.tag.web.search.bean.PageDTO;
import cn.itcast.tag.web.search.bean.QueryDTO;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.PageEnum;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaocs
 *
 */
@Controller
@RequestMapping("/examine")
public class ExamineController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExamineService examineService;

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public ModelAndView microView(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.EXAMINE_INDEX.getPage());
        modelAndView.addObject("navigation", "examine");
        logger.info("==== microView@page:{} ====", PageEnum.EXAMINE_INDEX);
        return modelAndView;
    }

    @RequestMapping(value = {"/page/apply"}, method = RequestMethod.GET)
    public ModelAndView applyPage(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.EXAMINE_APPLY.getPage());
        modelAndView.addObject("navigation", "examine");
        logger.info("==== microView@page:{} ====", PageEnum.EXAMINE_APPLY);
        return modelAndView;
    }

    @RequestMapping(value = {"/page/examine"}, method = RequestMethod.GET)
    public ModelAndView examinePage(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.EXAMINE_INDEX.getPage());
        modelAndView.addObject("navigation", "examine");
        logger.info("==== microView@page:{} ====", PageEnum.EXAMINE_INDEX);
        return modelAndView;
    }

    @RequestMapping(value = {"/page/develop"}, method = RequestMethod.GET)
    public ModelAndView developPage(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.EXAMINE_DEVELOP.getPage());
        modelAndView.addObject("navigation", "examine");
        logger.info("==== microView@page:{} ====", PageEnum.EXAMINE_DEVELOP);
        return modelAndView;
    }

    @RequestMapping(value = {"/page/release"}, method = RequestMethod.GET)
    public ModelAndView releasePage(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.EXAMINE_RELEASE.getPage());
        modelAndView.addObject("navigation", "examine");
        logger.info("==== microView@page:{} ====", PageEnum.EXAMINE_RELEASE);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public void list(HttpServletRequest request, PrintWriter pw, QueryDTO queryDTO) {
        Result result = new Result();
        Boolean isAdministrator = isAdministrator();
        if (isAdministrator) {
            PageDTO<TagAuditBean> datas = examineService.getAllTags(queryDTO);
            result.set(StateCode.SUCCESS, "查询完成", datas);
        } else {
            logger.info("==== examine list not administrator");
            result.set(StateCode.FAILD, "操作失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== examine list@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== examine list@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/audit"}, method = RequestMethod.POST)
    public void audit(HttpServletRequest request, PrintWriter pw, TagAuditBean tagAuditBean) {
        Result result = new Result();
        int state = 0;
        // 权限校验
        Boolean isAdministrator = isAdministrator();
        if (isAdministrator) {
            if (tagAuditBean.getType().equals(1L)) {
                UserTagFormBean bean = new UserTagFormBean();
                bean.setId(tagAuditBean.getId());
                bean.setState(Integer.valueOf(tagAuditBean.getState()));
                state = examineService.auditUserTag(bean);
            }
            if (tagAuditBean.getType().equals(2L)) {
                UserMergeTagMapBean bean = new UserMergeTagMapBean();
                bean.setId(tagAuditBean.getId());
                bean.setState(Integer.valueOf(tagAuditBean.getState()));
                state = examineService.auditUserMergeTag(bean);
            }

            if (state <= 0) {
                result.set(StateCode.UPD_FAILD, "更新失败");
            } else {
                result.set(StateCode.UPD_SUCCESS, "更新成功");
            }
        } else {
            logger.info("==== examine audit not administrator");
            result.set(StateCode.UPD_FAILD, "更新失败");
        }

        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== examine audit@result:{} ====", result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==== examine audit@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/batchAudit"}, method = RequestMethod.POST)
    public void batchAudit(HttpServletRequest request, PrintWriter pw, String conditions) {
        Result result = new Result();
        // 权限校验
        Boolean isAdministrator = isAdministrator();
        if (isAdministrator) {
            try {
                List<TagAuditBean> list = JSON.parseArray(conditions, TagAuditBean.class);
                for (TagAuditBean tagAuditBean : list) {
                    if (tagAuditBean.getType().equals(1L)) {
                        UserTagFormBean bean = new UserTagFormBean();
                        bean.setId(tagAuditBean.getId());
                        bean.setState(Integer.valueOf(tagAuditBean.getState()));
                        examineService.auditUserTag(bean);
                    }
                    if (tagAuditBean.getType().equals(2L)) {
                        UserMergeTagMapBean bean = new UserMergeTagMapBean();
                        bean.setId(tagAuditBean.getId());
                        bean.setState(Integer.valueOf(tagAuditBean.getState()));
                        examineService.auditUserMergeTag(bean);
                    }
                }
                result.set(StateCode.UPD_SUCCESS, "更新成功");
            } catch (Exception e) {
                result.set(StateCode.UPD_FAILD, "更新失败");
            }
        } else {
            logger.info("==== examine batchAudit not administrator");
            result.set(StateCode.UPD_FAILD, "更新失败");
        }

        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== examine batchAudit@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== examine batchAudit@result:服务器错误 ====");
        }
    }

    // 判断权限
    private Boolean isAdministrator() {
        try {
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            if (Objects.nonNull(curUser)) {
                List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
                Long roleId = roleMaps.get(0).getRoleId();
                // 超级管理员
                if (roleId.equals(1L)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("==== examine @result:服务器错误 ====");
            return false;
        }
        return false;
    }

}