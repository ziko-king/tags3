/**
 * 项目名称：mengyao
 * 创建日期：2018年6月12日
 * 修改历史：
 * 1、[2018年6月12日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.system.controller;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.user.bean.ResourceBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.RoleResMapBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.form.DataRes;
import cn.itcast.tag.web.user.form.RoleResForm;
import cn.itcast.tag.web.user.service.*;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhaocs
 *
 */
@Controller
@RequestMapping(value = "/resource")
public class ResourceController extends BaseController {
    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private ResourceService resourceService;

    @Resource
    private RoleResMapService roleResMapService;

    /**
     * 获取当前登录用户角色拥有的资源(可见的模板数据)
     * @param pw
     */
    @ResponseBody
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public void data(PrintWriter pw) {
        Result result = new Result();
        List<ResourceBean> reses = null;
        List<DataRes> resources = new ArrayList<>();

        // 获取用户角色下的数据
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(UserUtil.RoleEnum.SUPPER_ADMIN.getStateInfo())) {
            reses = userService.queryAllResources();
        } else {
            MyShiro.Principal principal = (MyShiro.Principal) subject.getPrincipal();
            // 目前用户最多只有一个角色
            reses = userService.queryCurrentResources(new UserRoleMapBean(principal.getId(), principal.getRoleMaps().get(0).getRoleId()));
        }

        // 转化为页面使用的数据
        if (reses != null && !reses.isEmpty()) {
            for (ResourceBean res : reses) {
//                if(res.getPid() == -1 && res.getType() == 1) {
//                    resources.add(new DataRes(res.getId(),res.getPid(),res.getName(),
//                            "view","view", true,false)); 
//                }else {
                resources.add(new DataRes(res.getId(), res.getPid(), res.getName(),
                        "view", "add;del;edit;view", true, false));
//                }
            }
        }
        if (!resources.isEmpty()) {
            result.set(StateCode.QUERY_SUCCESS, "查询成功", resources);
        } else {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无管理权限");
        }

        try {
            pw.write(JsonUtil.obj2Json(result));
            logger.info("==== data@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== data@result:服务器错误 ====");
        }
    }

    /**
     * 获取指定角色下可操作的数据
     * @param pw
     * @param roleId
     */
    @ResponseBody
    @RequestMapping(value = "/roleData", method = RequestMethod.GET)
    public void roleData(PrintWriter pw, String roleId) {
        Result result = new Result();
        if (StringUtils.isEmpty(roleId) || Long.parseLong(roleId) < 1) {
            logger.info("==== roleData@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            List<ResourceBean> reses = null;
            List<DataRes> resources = new ArrayList<>();
            // 获取用户角色下的数据
            if (Long.parseLong(roleId) == 1) {
                reses = userService.queryAllResources();
            } else {
                // 目前用户最多只有一个角色
                reses = resourceService.queryForRoleBean(new RoleBean(Long.parseLong(roleId)));
            }

            // 转化为页面使用的数据
            if (reses != null && !reses.isEmpty()) {
                for (ResourceBean res : reses) {
//                    Boolean isChecked = false;
//                    if(res.getPermIds()!=null && StringUtils.isNotBlank(res.getPermIds())) {
//                        isChecked = true;
//                    }
//                    if(res.getPid()==-1) {
//                        resources.add(new DataRes(res.getId(),res.getPid(),res.getName(),
//                                "view","view", true,true)); 
//                    }else {
                    resources.add(new DataRes(res.getId(), res.getPid(), res.getName(),
                            (res.getPermIds() != null ? res.getPermIds() : StringUtils.EMPTY),
                            "add;del;edit;view", true, true));
//                    }
                }
            }

            if (!resources.isEmpty()) {
                result.set(StateCode.QUERY_SUCCESS, "查询成功", resources);
            } else {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无管理权限");
            }
        }
        try {
            pw.write(JsonUtil.obj2Json(result));
            logger.info("==== data@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== data@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/roleData/edit", method = RequestMethod.POST)
    public void editRoleData(PrintWriter pw, String json) {
        Result result = new Result();

        if (StringUtils.isEmpty(json)) {
            logger.info("==== delUser@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            RoleResForm form = (RoleResForm) JsonUtil.json2Obj(json, RoleResForm.class);

            if (form.getRoleId() < 1 || form.getDataReses().isEmpty()) {
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                List<DataRes> dataReses = form.getDataReses();
                List<RoleResMapBean> editResMaps = new ArrayList<>();

                for (DataRes dataRes : dataReses) {
                    RoleResMapBean editResMap = new RoleResMapBean(form.getRoleId(), dataRes.getId(),
                            dataRes.getPermIds(), 1, new Date(), new Date());
                    editResMap.setRemark("添加角色资源");
                    editResMaps.add(editResMap);
                }

//                // 1、删除已有的数据权限
                Boolean isDel = roleResMapService.delRoleResMapForRoleId(new RoleResMapBean(form.getRoleId()));
//                // 2、添加数据权限
                Boolean isEdit = false;
                if (isDel) {
                    isEdit = roleResMapService.addRoleResMap(editResMaps);
                }

                if (isEdit) {
                    result.set(StateCode.UPD_SUCCESS, "资源权限更新成功");
                } else {
                    result.set(StateCode.UPD_FAILD, "资源权限更新失败");
                }
            }
        }
        try {
            pw.write(JsonUtil.obj2Json(result));
            logger.info("==== editRoleData@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== editRoleData@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/perm", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public void resPermssion(PrintWriter pw, Integer level) {
        Result result = new Result();
        if (level == null) {
            logger.info("==== roleData@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            List<ResourceBean> reses = null;
            // 获取用户角色下的数据
            reses = resourceService.queryForType(level);

            String perm = StringUtils.EMPTY;
            if (reses != null && !reses.isEmpty()) {
                perm = reses.get(0).getPermIds();
            }

            if (StringUtils.isBlank(perm)) {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无管理权限");

            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询成功", perm);
            }
        }
        try {
            pw.write(JsonUtil.obj2Json(result));
            logger.info("==== data@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== data@result:服务器错误 ====");
        }
    }
}
