package cn.itcast.tag.web.search.controller;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.search.bean.PageDTO;
import cn.itcast.tag.web.search.bean.SearchTagBean;
import cn.itcast.tag.web.search.bean.SearchUserBean;
import cn.itcast.tag.web.search.service.SearchService;
import cn.itcast.tag.web.utils.PageEnum;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.utils.ExcelException;
import cn.itcast.tag.web.utils.ExcelUtil;
import cn.itcast.tag.web.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 标签查询controller
 *
 * @author FengZhen
 */
@Controller
@RequestMapping("/search")
public class SearchTagController {

    private Logger logger = LogManager.getLogger(getClass());

    @Resource
    private SearchService searchService;

    /**
     * 页面跳转
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.SEARCH_INDEX.getPage());
        modelAndView.addObject("navigation", "search");
        return modelAndView;
    }

    /**
     * 根据id获取导航数据
     *
     * @param id
     * @return
     */
    @RequestMapping("/queryById")
    public ModelAndView queryById(long id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("navigation", "search");
        modelAndView.setViewName(PageEnum.SEARCH_LIST.getPage());
        BasicTagBean basicTagBean = searchService.searchTagList(id);
        try {
            List<BasicTagBean> basicTagList = new ArrayList<>();
            basicTagList.add(basicTagBean);
            modelAndView.addObject("basicTagList", basicTagList);
            logger.info("==== queryById@result:{} ====", basicTagList);
        } catch (Exception e) {
            logger.error("==== queryById@result:服务器错误 ====");
        }
        return modelAndView;
    }

    /**
     * 根据id、level、name获取相关数据列表
     *
     * @param basicTagBean
     * @return
     */
    @RequestMapping("/queryTag")
    public void queryTag(BasicTagBean basicTagBean, PrintWriter pw) {
        Result result = new Result();
        List<SearchTagBean> basicTagBeans = searchService.queryTagByIdAndLevel(basicTagBean);
        if (null == basicTagBeans) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", basicTagBeans);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== queryTag@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== queryTag@result:服务器错误 ====");
        }
    }

    /**
     * 根据名称模糊查询返回列表
     *
     * @param name
     * @param pw
     */
    @ResponseBody
    @RequestMapping("/queryByName")
    public void queryByName(String name, PrintWriter pw) {
        Result result = new Result();
        List<SearchTagBean> basicTagBeans = searchService.search(name);
        if (null == basicTagBeans) {
            result.set(StateCode.QUERY_ZERO_SUCCESS, "查询完成,暂无数据");
        } else {
            result.set(StateCode.QUERY_SUCCESS, "查询完成", basicTagBeans);
        }
        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== search@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== search@result:服务器错误 ====");
        }
    }

    @ResponseBody
    @RequestMapping("/queryUserByTagIds")
    public void queryUserByTagIds(
            @RequestParam(value = "ids", required = true) String ids,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
            PrintWriter pw) {
        Result result = new Result();
        if (StringUtils.isBlank(ids)) {
            result.set(StateCode.PARAM_NULL_FAILD, "参数为空");
        } else {
            //需配置solr
            PageDTO<SearchUserBean> pageResult = searchService.searchUserListByTagIds(ids, page, pageSize);
            if (null == pageResult) {
                pageResult = new PageDTO<SearchUserBean>();
                List<SearchUserBean> data = new ArrayList<SearchUserBean>();
                data.add(new SearchUserBean("张治国", "15231238866", "110115199402265244", "1", "男", "MR.Zhang@gmail.com", "", "0"));
                //data.add(new SearchUserBean("张治国", "15231238866", "110115199402265244", "1", "男", "MR.Zhang@gmail.com", "", "1"));
                pageResult.setData(data);
                pageResult.setCount(2);
            }
            if (pageResult.getData() == null || pageResult.getData().isEmpty()) {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
            } else {
                result.set(StateCode.QUERY_SUCCESS, "查询成功", pageResult);
            }
        }

        String resultJson = JsonUtil.obj2Json(result);
        try {
            pw.write(resultJson);
            logger.info("==== search@result:{} ====", result);
        } catch (Exception e) {
            logger.error("==== search@result:服务器错误 ====");
        }
    }

    @RequestMapping(value = "/export2Excel")
    @ResponseBody
    public void exportExcel(@RequestParam(value = "ids", required = true) String ids, HttpServletResponse response) throws ExcelException {
        if (StringUtils.isBlank(ids)) return;

        PageDTO<SearchUserBean> pageResult = searchService.searchUserListByTagIds(ids, 1, Integer.MAX_VALUE);
        List<SearchUserBean> list = pageResult.getData();
        if (list == null || list.isEmpty()) return;

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("name", "用户姓名");
        fieldMap.put("idNum", "身份证号码");
        fieldMap.put("phone", "手机号码");
        fieldMap.put("bankNum", "银行卡号");

        String sheetName = "便签查询数据集";

        ExcelUtil.listToExcel(list, fieldMap, sheetName, response);

    }
}
