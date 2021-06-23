/**
 * 项目名称：mengyao
 * 创建日期：2018年6月10日
 * 修改历史：
 * 1、[2018年6月10日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.controller;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.RoleDataMapBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.user.service.RoleDataMapService;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.user.form.DataTag;
import cn.itcast.tag.web.user.form.PageBean;
import cn.itcast.tag.web.user.form.RoleDataForm;
import cn.itcast.tag.web.user.form.SystemForm;
import cn.itcast.tag.web.user.service.DataService;
import cn.itcast.tag.web.user.service.RoleService;
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
import java.util.*;

/**
 * @author zhaocs
 *
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private RoleService roleService;

    @Resource
    private DataService dataService;

    @Resource
    private RoleDataMapService roleDataMapService;

    @ResponseBody
    @RequestMapping(value = "/add")
    public void add(PrintWriter pw, String json) {
        Result result = new Result();
        if (StringUtils.isBlank(json)) {
            logger.info("==== add@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            RoleBean bean = (RoleBean) JsonUtil.json2Obj(json, RoleBean.class);
            Boolean isSave = false;
            if (null == bean) {
                logger.info("==== add@params:param null ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                logger.info("==== add@params:{} ====", bean);

                // 更新角色
                bean.setCtime(new Date());
                bean.setUtime(bean.getCtime());
                isSave = roleService.addRole(bean);
            }
            if (!isSave) {
                result.set(StateCode.ADD_FAILD, "角色增加失败");
            } else {
                result.set(StateCode.ADD_SUCCESS, "增加角色成功");
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
                logger.info("==== add@result:{} ====", result);
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
            RoleBean bean = (RoleBean) JsonUtil.json2Obj(json, RoleBean.class);
            Boolean isSave = false;
            if (null == bean || bean.getId() < 1) {
                logger.info("==== edit@params:param null ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                logger.info("==== edit@params:{} ====", bean);
                // 更新用户
                isSave = roleService.updateRole(bean);
            }
            if (!isSave) {
                result.set(StateCode.UPD_FAILD, "用户角色失败");
            } else {
                result.set(StateCode.UPD_SUCCESS, "更新角色成功");
            }
            String resultJson = JsonUtil.obj2Json(result);
            try {
                pw.write(resultJson);
                logger.info("==== edit@result:{} ====", result);
            } catch (Exception e) {
                logger.error("==== edit@result:服务器错误 ====");
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public void delUser(PrintWriter pw, String roleId) {
        Result result = new Result();
        Boolean flag = false;

        if (StringUtils.isEmpty(roleId)) {
            logger.info("==== delUser@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            logger.info("==== delUser@params:{} ====", roleId);
            flag = roleService.delForRole(new RoleBean(Long.parseLong(roleId)));
        }
        if (!flag) {
            result.set(StateCode.DEL_FAILD, "删除角色失败");
        } else {
            result.set(StateCode.DEL_SUCCESS, "删除角色成功");
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
        RoleBean bean = new RoleBean();
        SystemForm form = (SystemForm) JsonUtil.json2Obj(json, SystemForm.class);
        if (form.getPageBean() == null) {
            PageBean pageBean = new PageBean();
            pageBean.setCurPage(1);
            form.setPageBean(pageBean);
        } else {
            if (StringUtils.isNotBlank(form.getName())) bean.setName(form.getName());
        }

        int total = roleService.queryCountForConditions(bean);
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
            List<RoleBean> beans = roleService.queryForConditions(bean);

            form.getPageBean().setRowsCount(total);
            int remainder = total % form.getPageBean().getPageSize(); // 余数
            int quotient = total / form.getPageBean().getPageSize();  // 整数
            if (remainder > 0) {
                quotient++;
            }
            form.getPageBean().setPageCount(quotient);
            Map<String, Object> data = new HashMap<>();

            data.put("pageBean", form.getPageBean());
            data.put("list", beans);
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

    @ResponseBody
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public void data(PrintWriter pw) {
        Result result = new Result();
        List<BasicTagBean> basicTags = null;
        List<MergeTagBean> mergeTags = null;
        Map<String, List<DataTag>> data = new HashMap<>();
        List<DataTag> zNodes = new ArrayList<>();
        List<DataTag> zNodes1 = new ArrayList<>();

        // 获取用户角色下的数据
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("supper_admin")) {
            basicTags = dataService.getBasicTag();
            mergeTags = dataService.getMergeTag();
        } else {
            MyShiro.Principal principal = (MyShiro.Principal) subject.getPrincipal();
            basicTags = dataService.getRoleBasicTag(principal.getRoleMaps().get(0).getRoleId());
            mergeTags = dataService.getRoleMergeTag(principal.getRoleMaps().get(0).getRoleId());
        }

        // 转化为页面使用的数据
        if (basicTags != null && !basicTags.isEmpty()) {
            for (BasicTagBean bean : basicTags) {
                zNodes.add(new DataTag(bean.getId(), bean.getPid(), bean.getName(), 1, bean.getLevel() > 3 ? false : true, false));
            }
        }
        data.put("basicTag", zNodes);
        if (mergeTags != null && !mergeTags.isEmpty()) {
            for (MergeTagBean bean : mergeTags) {
                zNodes1.add(new DataTag(bean.getId(), -1, bean.getName(), 2, true, false));
            }
        }
        data.put("mergeTag", zNodes1);
        if (!zNodes.isEmpty()) {
            result.set(StateCode.QUERY_SUCCESS, "查询成功", data);
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
     * 获取角色下可操作的数据
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
            List<BasicTagBean> basicTags = null;
            List<MergeTagBean> mergeTags = null;
            Map<String, List<DataTag>> data = new HashMap<>();
            List<DataTag> zNodes = new ArrayList<>();
            List<DataTag> zNodes1 = new ArrayList<>();

            // 获取用户角色下的数据
            if (Long.parseLong(roleId) == 1) {
                basicTags = dataService.getBasicTag();
                mergeTags = dataService.getMergeTag();
            } else {
                basicTags = dataService.getRoleBasicTag(Long.parseLong(roleId));
                mergeTags = dataService.getRoleMergeTag(Long.parseLong(roleId));
            }

            // 转化为页面使用的数据
            if (basicTags != null && !basicTags.isEmpty()) {
                for (BasicTagBean bean : basicTags) {
                    zNodes.add(new DataTag(bean.getId(), bean.getPid(), bean.getName(), 1, bean.getLevel() > 3 ? false : true, true));
                }
            }
            data.put("basicTag", zNodes);
            if (mergeTags != null && !mergeTags.isEmpty()) {
                for (MergeTagBean bean : mergeTags) {
                    zNodes1.add(new DataTag(bean.getId(), -1, bean.getName(), 2, true, true));
                }
            }
            data.put("mergeTag", zNodes1);

            if (!zNodes.isEmpty()) {
                result.set(StateCode.QUERY_SUCCESS, "查询成功", data);
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
            RoleDataForm form = (RoleDataForm) JsonUtil.json2Obj(json, RoleDataForm.class);
            if (form.getRoleId() < 1 || form.getDataTags().isEmpty()) {
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                List<DataTag> dataTags = form.getDataTags();
                List<RoleDataMapBean> editTagMaps = new ArrayList<>();

                for (DataTag dataTag : dataTags) {
                    RoleDataMapBean editTagMap = new RoleDataMapBean(form.getRoleId(), dataTag.getId(),
                            dataTag.getType(), 1, new Date(), new Date());
                    editTagMap.setRemark("添加角色数据权限");
                    editTagMaps.add(editTagMap);
                }

                // 1、删除已有的数据权限
                Boolean isDel = roleDataMapService.delRoleDataMapForRoleId(new RoleDataMapBean(form.getRoleId()));
                // 2、添加数据权限
                Boolean isEdit = false;
                if (isDel) {
                    isEdit = roleDataMapService.addRoleDataMap(editTagMaps);
                }

                if (isEdit) {
                    result.set(StateCode.UPD_SUCCESS, "数据权限更新成功");
                } else {
                    result.set(StateCode.UPD_FAILD, "数据权限更新失败");
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
}
