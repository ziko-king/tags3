/**
 * 项目名称：mengyao
 * 创建日期：2018年6月8日
 * 修改历史：
 * 1、[2018年6月8日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.system.controller;

import cn.itcast.tag.web.user.bean.OrganizationBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserOrgMapBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.user.service.OrganizationService;
import cn.itcast.tag.web.user.service.UserService;
import cn.itcast.tag.web.utils.PageEnum;
import cn.itcast.tag.web.utils.UserUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author zhaocs
 *
 */
@Controller
@RequestMapping("/system")
public class SystemController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserService userService;

    @Resource
    private OrganizationService organizationService;

    @RequestMapping(value = {"", "/", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();

        // 清空用户缓存
        userService.clearUserCache();
        Subject subject = SecurityUtils.getSubject();
        MyShiro.Principal principal = (MyShiro.Principal) subject.getPrincipal();
        UserBean user = userService.queryForId(new UserBean(principal.getId()));
        if (subject.hasRole(UserUtil.RoleEnum.USER.getStateInfo())) {
            modelAndView.addObject("user", user);
            modelAndView.setViewName(PageEnum.SYSTEM_INDEX_USER.getPage());
        } else {
            // 获取可见的组织结构数据
            List<OrganizationBean> orgs = Collections.emptyList();
            if (subject.hasRole(UserUtil.RoleEnum.SUPPER_ADMIN.getStateInfo())) {
                // 所有的顶层组织（含有子级）
                OrganizationBean org = new OrganizationBean();
                org.setPid(-1L);
                orgs = organizationService.queryForPid(org);
            } else {
                // 当前用户所在的顶层组织--一个用户只能属于组织的一个节点
                List<UserOrgMapBean> userOrgs = user.getOrgMaps();
                if (userOrgs != null && !userOrgs.isEmpty()) {
                    orgs = organizationService.query(new OrganizationBean(userOrgs.get(0).getId()));
                }
            }

            modelAndView.addObject("orgs", orgs);
            modelAndView.setViewName(PageEnum.SYSTEM_INDEX_ADMIN.getPage());
        }
        modelAndView.addObject("navigation", "system");
        logger.info("==== system@index ====");
        return modelAndView;
    }
}
