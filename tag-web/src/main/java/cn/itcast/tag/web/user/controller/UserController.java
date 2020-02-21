package cn.itcast.tag.web.user.controller;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserOrgMapBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.form.PageBean;
import cn.itcast.tag.web.user.form.SysSetBean;
import cn.itcast.tag.web.user.form.SystemForm;
import cn.itcast.tag.web.user.form.UserForm;
import cn.itcast.tag.web.user.service.RoleService;
import cn.itcast.tag.web.user.service.UserOrgMapService;
import cn.itcast.tag.web.user.service.UserRoleMapService;
import cn.itcast.tag.web.user.service.UserService;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.MD5Util;
import cn.itcast.tag.web.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    UserRoleMapService userRoleMapService;
    @Resource
    UserOrgMapService userOrgMapService;
    private Logger logger = LogManager.getLogger(getClass());
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @ResponseBody
    @RequestMapping(value = "/add")
    public void add(PrintWriter pw, String json) {
        Result result = new Result();
        if (StringUtils.isBlank(json)) {
            logger.info("==== add@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            SysSetBean form = (SysSetBean) JsonUtil.json2Obj(json, SysSetBean.class);
            UserBean bean = null;
            Boolean isSave = false;
            if (null == form) {
                logger.info("==== add@params:param null ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                logger.info("==== add@params:{} ====", form);

                // 验重
                bean = userService.queryForUsername(new UserBean(form.getUserName()));
                if (bean != null && bean.getId() > 0) {
                    result.set(StateCode.ADD_FAILD, "用户登录名重复");
                } else {
                    bean = new UserBean(form.getName(), form.getUserName(), form.getPhone(), form.getEmail());
                    bean.setPassword(MD5Util.getMd5(form.getPassword() + UserUtil.ENCRYPTING_KEY));
                    bean.setState(UserUtil.UserStateEnum.INNER_NORMAL.getState());
                    bean.setCtime(new Date());
                    bean.setUtime(new Date());

                    // 更新用户
                    isSave = userService.addUser(bean);

                    // 暂时再查一次用户得到用户id
                    if (isSave) {
                        bean = userService.queryForUsername(bean);
                    }

                    // 组织关联更新--最好增加try-catch
                    if (form.getGroupId() != null) {
                        userOrgMapService.addUserOrgMap(new UserOrgMapBean(bean.getId(), form.getGroupId(),
                                1, new Date(), new Date(), "增加组织用户对应关系"));
                    }

                    // 更新角色
                    if (form.getRoleId() > 0) {
                        userRoleMapService.addUserRoleMap(new UserRoleMapBean(
                                bean.getId(), form.getRoleId(), 1));
                    }
                }
            }
            if (isSave) {
                result.set(StateCode.ADD_SUCCESS, "增加用户成功");
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
                logger.info("==== add@result:{} ====", resultJson);
            } catch (Exception e) {
                logger.error("==== add@result:服务器错误 ====");
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit")
    public void edit(PrintWriter pw, String json) {
        Result result = new Result();
        if (StringUtils.isBlank(json)) {
            logger.info("==== edit@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            SysSetBean form = (SysSetBean) JsonUtil.json2Obj(json, SysSetBean.class);
            UserBean bean = null;
            Boolean isSave = false;
            if (null == form || form.getUserId() < 1) {
                logger.info("==== edit@params:param null ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                logger.info("==== edit@params:{} ====", form);

                bean = new UserBean(form.getUserId(), form.getName(), form.getUserName(), form.getPhone());

                // 更新角色
                if (form.getRoleId() > 0) {
                    UserRoleMapBean userRole = userRoleMapService.queryForUserId(form.getUserId());
                    if (userRole != null) {
                        userRoleMapService.updateUserRoleMapForUserId(new UserRoleMapBean(
                                form.getUserId(), form.getRoleId(), 1));
                    } else {
                        userRoleMapService.addUserRoleMap(new UserRoleMapBean(
                                form.getUserId(), form.getRoleId(), 1));
                    }

                }

                // 更新用户
                isSave = userService.update(bean);
            }
            if (!isSave) {
                result.set(StateCode.UPD_FAILD, "用户更新失败");
            } else {
                result.set(StateCode.UPD_SUCCESS, "更新用户成功");
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
                logger.info("==== edit@result:{} ====", resultJson);
            } catch (Exception e) {
                logger.error("==== edit@result:服务器错误 ====");
            }
        }
    }

    /**
     * 普通用户的修改
     *
     * @param pw
     * @param json
     */
    @ResponseBody
    @RequestMapping(value = "/easy/edit")
    public void easyEdit(PrintWriter pw, String json) {
        Result result = new Result();
        if (StringUtils.isBlank(json)) {
            logger.info("==== edit@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            UserForm form = (UserForm) JsonUtil.json2Obj(json, UserForm.class);
            UserBean bean = null;
            Boolean isSave = false;
            if (null == form || form.getId() < 1) {
                logger.info("==== edit@params:param null ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                logger.info("==== edit@params:{} ====", form);
                bean = new UserBean(form.getId(), form.getName(), form.getUsername(), form.getPhone(), form.getEmail());

                // 更新用户
                isSave = userService.update(bean);
            }
            if (!isSave) {
                result.set(StateCode.UPD_FAILD, "用户更新失败");
            } else {
                result.set(StateCode.UPD_SUCCESS, "更新用户成功");
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
                logger.info("==== edit@result:{} ====", resultJson);
            } catch (Exception e) {
                logger.error("==== edit@result:服务器错误 ====");
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/default/pwd/reset")
    public void resetDefaultPwd(PrintWriter pw, String userId) {
        Result result = new Result();
        UserBean bean = null;
        if (StringUtils.isBlank(userId)) {
            logger.info("==== resetDefaultPwd@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            bean = userService.queryForId(new UserBean(Long.parseLong(userId)));
        }
        if (bean == null) {
            result.set(StateCode.RECORD_DISABLED, "用户不存在");
        } else {
            bean.setPassword(UserUtil.DEFAULT_PWD);
            userService.updateForPassword(bean);
            result.set(StateCode.UPD_SUCCESS, "更新用户成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== resetDefaultPwd@result:{} ====", resultJson);
        } catch (Exception e) {
            logger.error("==== resetDefaultPwd@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/pwd/reset")
    public void resetPwd(PrintWriter pw, String userId, String pwd, String oldpwd) {
        Result result = new Result();
        UserBean bean = null;
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(pwd) || StringUtils.isBlank(oldpwd)) {
            logger.info("==== edit@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            bean = userService.queryForId(new UserBean(Long.parseLong(userId)));
        }
        if (bean == null || bean.getId() < 1) {
            result.set(StateCode.RECORD_DISABLED, "用户不存在");
        } else if (!bean.getPassword().equals(MD5Util.getMd5(oldpwd + UserUtil.ENCRYPTING_KEY))) {
            result.set(StateCode.FAILD, "老密码错误");
        } else {
            bean.setPassword(MD5Util.getMd5(pwd + UserUtil.ENCRYPTING_KEY));
            userService.updateForPassword(bean);
            result.set(StateCode.UPD_SUCCESS, "更新用户成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== resetPwd@result:{} ====", resultJson);
        } catch (Exception e) {
            logger.error("==== resetPwd@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/forbid")
    public void forbid(PrintWriter pw, String userId, String state) {
        Result result = new Result();
        UserBean bean = null;
        if (StringUtils.isBlank(userId)) {
            logger.info("==== forbid@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            bean = userService.queryForId(new UserBean(Long.parseLong(userId)));
        }
        if (bean == null) {
            result.set(StateCode.RECORD_DISABLED, "用户不存在");
        } else {
            UserBean forbidBean = new UserBean();
            forbidBean.setId(bean.getId());
            if (StringUtils.isBlank(state) || Integer.parseInt(state) == 1) {
                forbidBean.setState(UserUtil.UserStateEnum.DISABLE.getState());
            } else {
                forbidBean.setState(UserUtil.UserStateEnum.INNER_NORMAL.getState());
            }

            userService.update(forbidBean);
            result.set(StateCode.UPD_SUCCESS, "禁用用户成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== forbid@result:{} ====", resultJson);
        } catch (Exception e) {
            logger.error("==== forbid@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delSysUser", method = RequestMethod.GET)
    public void delUser(PrintWriter pw, String userId) {
        Result result = new Result();
        Boolean flag = false;

        if (StringUtils.isEmpty(userId)) {
            logger.info("==== delUser@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            logger.info("==== delUser@params:{} ====", userId);
            flag = userService.delForUser(new UserBean(Long.parseLong(userId)));
        }
        if (!flag) {
            result.set(StateCode.DEL_FAILD, "删除用户失败");
        } else {
            result.set(StateCode.DEL_SUCCESS, "删除用户成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== add@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== add@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public void list(PrintWriter pw, String json) {
        Result result = new Result();
        UserBean bean = new UserBean();
        SystemForm form = (SystemForm) JsonUtil.json2Obj(json, SystemForm.class);
        if (form.getPageBean() == null) {
            PageBean pageBean = new PageBean();
            pageBean.setCurPage(1);
            form.setPageBean(pageBean);
        } else {
            if (StringUtils.isNotBlank(form.getName())) bean.setName(form.getName());
            if (StringUtils.isNotBlank(form.getGroupId())) {
                List<UserOrgMapBean> orgs = new ArrayList<>();
                orgs.add(new UserOrgMapBean(Long.parseLong(form.getGroupId())));
                bean.setOrgMaps(orgs);
            }
        }
        int total = userService.queryCountForConditions(bean);

        if (total < 1) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
        } else {
            if (form.getPageBean() != null) {
                if (form.getPageBean().getPageSize() != null && form.getPageBean().getPageSize() != 10) {
                    bean.setPr(form.getPageBean().getPageSize());
                }
                if (form.getPageBean().getCurPage() != null) {
                    bean.setCp(form.getPageBean().getCurPage());
                    bean.setSr((bean.getCp() - 1) * bean.getPr());
                }
            }
            List<UserBean> beans = userService.queryForConditions(bean);

            form.getPageBean().setRowsCount(total);
            int remainder = total % form.getPageBean().getPageSize(); // 余数
            int quotient = total / form.getPageBean().getPageSize();  // 整数
            if (remainder > 0) {
                quotient++;
            }
            form.getPageBean().setPageCount(quotient);

            Map<String, Object> data = new HashMap<>();
            List<SysSetBean> sysSetBeans = new ArrayList<>();
            for (UserBean userBean : beans) {
                SysSetBean sysSetBean = new SysSetBean(userBean.getId(),
                        userBean.getName(), userBean.getUsername(), userBean.getEmail(),
                        userBean.getPhone());
                if (userBean.getRoleMaps() != null && !userBean.getRoleMaps().isEmpty()) {
                    RoleBean role = roleService.queryForId(new RoleBean(userBean.getRoleMaps().get(0).getRoleId()));
                    if (role != null) {
                        sysSetBean.setRoleName(role.getName());
                        sysSetBean.setRoleId(role.getId());
                    }
                }
                sysSetBean.setState(userBean.getState());
                sysSetBeans.add(sysSetBean);
            }

            data.put("pageBean", form.getPageBean());
            data.put("sysSetBeans", sysSetBeans);
            result.setData(data);
            result.set(StateCode.QUERY_SUCCESS, "查询成功");
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== list@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== list@result:服务器错误 ====");
        }
    }
}
