/**
 * 项目名称：mengyao
 * 创建日期：2018年6月14日
 * 修改历史：
 * 1、[2018年6月14日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.controller;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.user.bean.OrganizationBean;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.user.service.OrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * @author zhaocs
 *
 */
@Controller
@RequestMapping("/org")
public class OrganizationController extends BaseController {
    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private OrganizationService organizationService;

    @ResponseBody
    @RequestMapping(value = "/add")
    public void add(PrintWriter pw, String json) {
        Result result = new Result();
        if (json == null || StringUtils.isBlank(json)) {
            logger.info("==== add@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            OrganizationBean form = (OrganizationBean) JsonUtil.json2Obj(json, OrganizationBean.class);
            if (null == form) {
                logger.info("==== add@params:param null ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
            } else {
                logger.info("==== add@params:{} ====", form);
                if (form.getPid() == null) {
                    form.setPid(-1L);
                }
                form.setState(1);
                form.setCtime(new Date());
                form.setUtime(new Date());
                form.setRemark("添加组织");
                // level先不处理
                Boolean isSaved = organizationService.addOrganization(form);
                if (isSaved) {
                    result.set(StateCode.ADD_SUCCESS, "增加组织成功");
                } else {
                    result.set(StateCode.ADD_FAILD, "增加组织失败");
                }
            }
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
    @RequestMapping(value = "/del")
    public void del(PrintWriter pw, Long orgId) {
        Result result = new Result();

        if (orgId == null) {
            logger.info("==== del@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            OrganizationBean db = new OrganizationBean();
            db.setId(orgId);
            db = organizationService.queryForId(db);
            if (db == null) {
                logger.info("==== del@params:组织不存在 ====");
                result.set(StateCode.PARAM_NULL_FAILD, "非法组织");
            } else {
                // 1、查询组织下的用户
//                List<UserOrgMapBean> userMaps = db.getUserMaps();

                // 2、删除【组织下用户】的【用户角色】关联数据

                // 3、删除【组织下用户】的【用户基础标签】关联数据

                // 4、删除【组织下用户】的【用户组合标签】关联数据

                // 5、删除【组织下用户】的【用户资源】关联数据

                // 6、删除【组织下用户】的【用户组织】关联数据

                // 7、删除【组织下用户】的数据

                // 8、删除【组织】的数据

                // 组织下没有用户才能删除
                if (null == db.getUserMaps() || db.getUserMaps().isEmpty()) {
                    Boolean isDel = organizationService.delOrganizationForId(db);
                    if (isDel) {
                        logger.info("==== del@删除成功 ====");
                        result.set(StateCode.SUCCESS, "删除成功");
                    } else {
                        logger.info("==== del@删除失败 ====");
                        result.set(StateCode.FAILD, "删除失败，请稍后重试");
                    }
                } else {
                    result.set(StateCode.RECORD_EXIST_FAILD, "包含用户，不允许删除");
                }

            }
        }
//        json

        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== list@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== list@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit")
    public void edit(PrintWriter pw, String json) {
        Result result = new Result();
        if (json == null || StringUtils.isBlank(json)) {
            logger.info("==== edit@params:param null ====");
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            OrganizationBean form = null;
            try {
                form = (OrganizationBean) JsonUtil.json2Obj(json, OrganizationBean.class);
            } catch (Exception e) {
                logger.info("==== edit@params:param error ====");
                result.set(StateCode.PARAM_NULL_FAILD, "参数格式错误");
            }
            Boolean isSaved = organizationService.updateOrganization(form);
            if (isSaved) {
                logger.info("==== edit@params:param{} ====", form);
                result.set(StateCode.UPD_SUCCESS, "修改成功", isSaved);
            } else {
                result.set(StateCode.UPD_FAILD, "修改失败");
            }
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
    @RequestMapping(value = "/list", produces = "text/plain;charset=UTF-8")
    public void list(PrintWriter pw, Integer level) {
        Result result = new Result();
        List<OrganizationBean> beans = null;
        OrganizationBean bean = null;
        if (level == null) {
            bean = new OrganizationBean();
        } else {
            bean = new OrganizationBean(level);
        }
        beans = organizationService.query(bean);
        if (beans == null || beans.isEmpty()) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询成功", beans);
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
