package cn.itcast.tag.web.api.inner.controller;

import cn.itcast.tag.web.api.inner.bean.SearchTagUserBean;
import cn.itcast.tag.web.api.inner.bean.SearchUserBean;
import cn.itcast.tag.web.api.inner.service.InnerService;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inner")
public class InnerController {

    @Resource
    InnerService innerService;
    private Logger logger = LogManager.getLogger(getClass());

    /**
     * 根据标签id、标签类型获取用户数据
     *
     * @param tagId
     * @param type
     * @param count
     * @param pw
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public void search(long tagId, int type, int count, PrintWriter pw, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        pw = response.getWriter();
        Result result = new Result();
        if ((type != 1 && type != 2) || tagId <= 0 || count <= 0) {
            result.set(StateCode.PARAM_FAILD, "参数有误！");
        }
        try {
            List<SearchUserBean> resultList = innerService.search(tagId, type, count);
            if (null == resultList) {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询成功", resultList);
            }
            String resultJson = JsonUtil.obj2Json(result);
            pw.write(resultJson);
            logger.info("==== search@result:{} ====", resultJson);
        } catch (Exception e) {
            logger.error("==== search@result:服务器错误 ====");
            e.printStackTrace();
        }
    }

    /**
     * 根据基础标签、组合标签获取用户信息
     *
     * @param basicTags
     * @param mergeTags
     * @param area
     * @param count
     * @param pw
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/searchByMultiTags", method = RequestMethod.GET)
    public void searchByMultiTagIds(String basicTags, String mergeTags, String area,
                                    @RequestParam(value = "count", required = false, defaultValue = "1") int count, PrintWriter pw,
                                    HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        pw = response.getWriter();
        Result result = new Result();
        List<SearchTagUserBean> resultList = new ArrayList<>();
        try {
            if ((null == basicTags && null == mergeTags) || count <= 0) {
                result.set(StateCode.PARAM_FAILD, "参数有误！");
            }
            resultList = innerService.searchByMoreTagIds(basicTags, mergeTags, count);
        } catch (Exception e) {
            logger.error("==== search@result:服务器错误 ====");
            e.printStackTrace();
        }
        if (null == resultList) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询成功", resultList);
        }
        String resultJson = JsonUtil.obj2Json(result);
        pw.write(resultJson);
        logger.info("==== search@result:{} ====", resultJson);
    }

    /**
     * 根据身份证获取用户信息
     *
     * @param idCard
     * @param pw
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/social/idCard", method = RequestMethod.GET)
    public void searchByIdCard(String idCard, PrintWriter pw, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        pw = response.getWriter();
        Result result = new Result();
        List<SearchUserBean> resultList = new ArrayList<>();
        try {
            if (StringUtils.isBlank(idCard)) {
                result.set(StateCode.PARAM_FAILD, "参数有误！");
            } else {
                SearchUserBean searchUserBean = innerService.searchByIdCard(idCard);
                if (null != searchUserBean) {
                    resultList.add(searchUserBean);
                }
                if (null == resultList) {
                    result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
                } else {
                    result.set(StateCode.QUERY_SUCCESS, "查询成功", resultList);
                }
            }
            String resultJson = JsonUtil.obj2Json(result);
            pw.write(resultJson);
            logger.info("==== search@result:{} ====", resultJson);
        } catch (Exception e) {
            logger.error("==== search@result:服务器错误 ====");
            e.printStackTrace();
        }
    }

}
