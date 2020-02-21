package cn.itcast.tag.web.home;

import cn.itcast.tag.web.basictag.service.BasicTagService;
import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.search.service.SearchService;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.PageEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;

/**
 * @author:FengZhen
 * @create:2018年7月2日
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @Resource
    BasicTagService basicTagService;

    @Resource
    SearchService searchService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.HOME_INDEX.getPage());
        modelAndView.addObject("navigation", "home");
        return modelAndView;
    }

    /**
     * 当前有效标签数量
     */
    @RequestMapping("/currentTagCount")
    public void currentTagCount(PrintWriter pw) {
        Result result = new Result();
        try {
//			Long count = basicTagService.queryTagCountByLevel(5);
            Long count = 5287L;
            result.set(StateCode.QUERY_SUCCESS, "查询成功", count);
        } catch (Exception e) {
            result.set(StateCode.FAILD, "操作失败");
        }
        String jString = JsonUtil.obj2Json(result);
        pw.write(jString);
    }

    /**
     * 标签系统覆盖用户数
     *
     * @param pw
     */
    @RequestMapping("/userCount")
    public void userCount(PrintWriter pw) {
        Result result = new Result();
        try {
            Long count = searchService.getAllUserCount(1);
            result.set(StateCode.QUERY_SUCCESS, "查询成功", count);
        } catch (Exception e) {
            result.set(StateCode.FAILD, "操作失败");
        }
        String jString = JsonUtil.obj2Json(result);
        pw.write(jString);
    }
}
