package cn.itcast.tag.web.user.controller;

import cn.itcast.tag.web.basictag.service.BasicTagService;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.user.bean.ResourceBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.form.LoginForm;
import cn.itcast.tag.web.user.service.UserService;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.MD5Util;
import cn.itcast.tag.web.utils.PageEnum;
import cn.itcast.tag.web.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录、登出
 *
 * @author zhaocs
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    private static final String LOGIN_RESULT = "==== login@result:{} ====";
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private UserService userService;
    @Resource
    private BasicTagService basicTagService;

    /**
     * 登录页（默认页）
     *
     * @return
     */
    @RequestMapping(value = {"/", "login"}, method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.LOGIN.getPage());
        return modelAndView;
    }

    @RequestMapping(value = {"login"}, method = RequestMethod.POST)
    public ModelAndView login(LoginForm form, HttpServletRequest request, PrintWriter pw) {
        ModelAndView modelAndView = new ModelAndView();
        Result responseMsg = new Result();
        UserBean user = null;
        // 非空校验
        if (null == form || StringUtils.isBlank(form.getUsername()) || StringUtils.isBlank(form.getPassword())) {
            responseMsg.set(StateCode.PARAM_NULL_FAILD, "参数输入错误");
            modelAndView.setViewName(PageEnum.LOGIN.getPage());
        } else {
            // 获取数据库中用户信息
            logger.info("==== login@params:{} ====", form);
            user = userService.login(new UserBean(form.getUsername(),
                    MD5Util.getMd5(form.getPassword() + UserUtil.ENCRYPTING_KEY)));
            if (null == user) {
                responseMsg.set(StateCode.QUERY_ZERO_SUCCESS, "用户名或者密码错误");
                modelAndView.setViewName(PageEnum.LOGIN.getPage());
                logger.error(LOGIN_RESULT, responseMsg);
            } else if (user.getState() == 0 || null == user.getRoleMaps() || user.getRoleMaps().isEmpty()) {
                responseMsg.set(StateCode.RECORD_DISABLED, "用户没有权限或被禁用");
                modelAndView.setViewName(PageEnum.LOGIN.getPage());
                logger.error(LOGIN_RESULT, responseMsg);
            } else {
                // 校验通过的处理
                return shiroLogin(user, form.getRememberMe() != null && form.getRememberMe().equals("true"), request);
            }
        }

        logger.info(LOGIN_RESULT, responseMsg);

        modelAndView.addObject("data", JsonUtil.obj2Json(responseMsg));
        return modelAndView;
    }

    private ModelAndView shiroLogin(UserBean user, Boolean rememberMe, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        //获取密码令牌并验证
        UsernamePasswordToken token = null;
        token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            //使用shiro来验证
            if (rememberMe) token.setRememberMe(true);
            currentUser.login(token);// 验证角色和权限
        }
        if (currentUser.isAuthenticated()) {
            // 登录成功 
            Subject subject = SecurityUtils.getSubject();
            List<ResourceBean> resources = new ArrayList<>();
            if (subject.hasRole(UserUtil.RoleEnum.SUPPER_ADMIN.getStateInfo())) {
                resources = userService.queryAllResources();
            } else {
                List<UserRoleMapBean> beans = user.getRoleMaps();
                if (beans != null && !beans.isEmpty()) {
                    // 目前用户最多只有一个角色
                    resources = userService.queryCurrentResources(beans.get(0));
                }
            }
            // 头部资源导航栏
            HttpSession session = WebUtils.toHttp(request).getSession();
            session.removeAttribute("resources");
            session.setAttribute("resources", resources);

            modelAndView.setViewName("redirect:" + resources.get(0).getSign() + "/index");
        } else {
            modelAndView.setViewName(PageEnum.LOGIN.getPage());
        }
        return modelAndView;
    }

    /**
     * 检查注册的登录名是否重复
     *
     * @param username 登录名
     * @param request
     * @param pw
     */
    @RequestMapping(value = {"registerCheck"}, method = RequestMethod.POST)
    public void registerCheck(String username, HttpServletRequest request, PrintWriter pw) {
        Result responseMsg = new Result();
        if (StringUtils.isBlank(username)) responseMsg.set(StateCode.PARAM_NULL_FAILD, "参数输入错误");
        else {
            UserBean bean = userService.login(new UserBean(username));
            if (null == bean) responseMsg.set(StateCode.SUCCESS, "用户名不重复");
            else responseMsg.set(StateCode.RECORD_DUP_FAILD, "用户名重复");
        }
        String result = JsonUtil.obj2Json(responseMsg);
        try {
            pw.write(result);
            logger.info("==== registerCheck@result:{} ====", result);
        } catch (RuntimeException e) {
            logger.error("==== registerCheck@result:服务器错误 ====");
        }
    }

    /**
     * 登录校验
     *
     * @param pw
     * @param loginUser
     * @return
     */
    @RequestMapping(value = "/loginChk")
    public ModelAndView loginChk(UserBean bean, HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        if (null != bean) {
            logger.info("==== login@params:{} ====", bean);
            bean = userService.login(bean);
        } else {
            logger.info("==== login@params:param error ====");
        }
        if (null != bean) {
            logger.info("==== login@result: {} login success, go to home! ====", bean);
        } else {
            modelAndView.setViewName(PageEnum.LOGIN.getPage());
            logger.info("==== login@result:login faild, go to login! ====");
        }
        return modelAndView;
    }


    @ResponseBody
    @RequestMapping(value = "/clearUserCache", produces = "text/plain;charset=UTF-8")
    public void clearUserCache(PrintWriter pw) {
        Result result = new Result();
        Boolean isOk = false;
        isOk = userService.clearUserCache();
        if (isOk) {
            result.set(StateCode.SUCCESS, "成功清空用户缓存");
        } else {
            result.set(StateCode.FAILD, "清空用户缓存失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== clearCache@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== clearCache@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/clearCache", produces = "text/plain;charset=UTF-8")
    public void clearCache(PrintWriter pw) {
        Result result = new Result();
        Boolean isOk = false;
        isOk = userService.clearCache();
        if (isOk) {
            result.set(StateCode.SUCCESS, "成功清空所有缓存");
        } else {
            result.set(StateCode.FAILD, "清空所有缓存失败");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== clearCache@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== clearCache@result:服务器错误 ====");
        }
    }
}
