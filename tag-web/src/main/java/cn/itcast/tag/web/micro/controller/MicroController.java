/**
 * 项目名称：mengyao
 * 创建日期：2018年6月6日
 * 修改历史：
 * 1、[2018年6月6日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.micro.controller;

import cn.itcast.tag.web.commons.bean.Result;
import cn.itcast.tag.web.commons.bean.StateCode;
import cn.itcast.tag.web.commons.controller.BaseController;
import cn.itcast.tag.web.utils.PageEnum;
import cn.itcast.tag.web.micro.bean.MicroPortraitTag;
import cn.itcast.tag.web.micro.bean.MicroPortraitUserBean;
import cn.itcast.tag.web.micro.service.MicroService;
import cn.itcast.tag.web.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author zhaocs
 *
 */
@Controller
@RequestMapping("/micro")
public class MicroController extends BaseController {
    @Resource
    MicroService microService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public ModelAndView microView(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PageEnum.MICRO_INDEX.getPage());
        modelAndView.addObject("navigation", "micro");
        logger.info("==== microView@page:{} ====", PageEnum.MICRO_INDEX);
        return modelAndView;
    }

    @RequestMapping(value = {"query"})
    public ModelAndView query(HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("navigation", "micro");
        modelAndView.setViewName("micro/microPortraitSearch");
        return modelAndView;
    }

    @RequestMapping(value = {"queryByNum"}, method = RequestMethod.POST)
    public void queryByNum(String num, HttpServletRequest request, PrintWriter pw) {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("navigation", "micro");
        try {
            Map<String, Object> dataResult = new HashMap<>();
            //获取数据
            MicroPortraitUserBean microPortraitUserBean = microService.queryByNum(num);
            if (null != microPortraitUserBean) {
                if (null != microPortraitUserBean.getTags()) {
                    dataResult = getData(microPortraitUserBean.getTags(), microPortraitUserBean.getType());
                    //index_ = 0;
                }
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", microPortraitUserBean);
            resultMap.put("data", dataResult);
            //resultMap.put("data", "".equals(dataResult)?JsonUtil.obj2Json(dataResult):dataResult);
            Result result = new Result();
            if (null != microPortraitUserBean) {
                result.set(StateCode.QUERY_SUCCESS, "查询完成", resultMap);
            } else {
                result.set(StateCode.QUERY_ZERO_SUCCESS, "暂无数据");
            }
            pw.write(JsonUtil.obj2Json(result));
            logger.info("==== query@dataResult:{} ====", dataResult);
            logger.info("==== query@result:{} ====", JsonUtil.obj2Json(microPortraitUserBean));
        } catch (Exception e) {
            logger.error("==== query@result:服务器错误 ====");
        }

    }

    /**
     * 无用
     * @param tags
     * @param parentName
     * @return
     */
    public Map<String, Object> getData(List<MicroPortraitTag> tags, String type) {
        Integer index_ = 0;
        //下方一级列表
        List<String> bottomData = new ArrayList<>();
        //数据列表
        List<Map<String, Object>> data = new ArrayList<>();
        //links
        List<Map<String, Object>> links = new ArrayList<>();
        //categories
        List<Map<String, String>> categories = new ArrayList<>();

        String name = type.equals("0") ? "用户" : "物品";
        String imageName = type.equals("0") ? "people.png" : "thing.png";
        String imagePath = "image://../res/imgs/" + imageName;
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("name", name);
        headMap.put("symbolSize", 60);
        headMap.put("symbol", imagePath);
        headMap.put("draggable", "true");
        headMap.put("value", data.size());
        data.add(0, headMap);
        Integer currentIndex = index_;
        for (int i = 0; i < tags.size(); i++) {
            MicroPortraitTag tag = tags.get(i);
            String tagName = tag.getName();
            bottomData.add(tagName);
            Map<String, String> categoriesMap = new HashMap<>();
            categoriesMap.put("name", tagName);
            categories.add(categoriesMap);

            Map<String, Object> parentLink = new HashMap<>();
            parentLink.put("source", currentIndex);
            parentLink.put("target", ++index_);
            links.add(parentLink);
            List<MicroPortraitTag> subTags = tag.getMicroSubTags();
            Map<String, Object> subData = getDatas(subTags, tagName, index_);
            index_ = (Integer) subData.get("index_");
            links.addAll((Collection<? extends Map<String, Object>>) subData.get("links"));
            data.addAll((Collection<? extends Map<String, Object>>) subData.get("data"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("bottomData", bottomData);
        result.put("data", data);
        result.put("links", links);
        result.put("categories", categories);
        return result;
    }

    /**
     * 无用
     * @param tags
     * @param parentName
     * @return Map<String, List < Map < String, Object>>>
     */
    public Map<String, Object> getDatas(List<MicroPortraitTag> tags, String parentName, Integer index_) {
        Map<String, Object> result = new HashMap<>();
        //数据列表
        List<Map<String, Object>> data = new ArrayList<>();
        //links
        List<Map<String, Object>> links = new ArrayList<>();
        Integer currentIndex = index_;
        for (int j = 0; j < tags.size(); j++) {
            MicroPortraitTag tag = tags.get(j);
            String tagName = tag.getName();
            if (j == 0) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("name", parentName);
                dataMap.put("draggable", "true");
                dataMap.put("value", tags.size());
                dataMap.put("symbolSize", tags.size() * 3);
                data.add(dataMap);
            }
            Map<String, Object> linkMap = new HashMap<>();
            linkMap.put("source", currentIndex);
            linkMap.put("target", ++index_);
            links.add(linkMap);
            List<MicroPortraitTag> subTags = tag.getMicroSubTags();
            if (tag.getLevel() == 5) {
                String imagePath = "image://../res/imgs/level5.png";
                for (int k = 0; k < tags.size(); k++) {
                    MicroPortraitTag subTag = tags.get(k);
                    String subTagName = subTag.getName();
                    Map<String, Object> tmpDataMap = new HashMap<>();
                    tmpDataMap.put("name", subTagName);
                    tmpDataMap.put("draggable", "true");
                    tmpDataMap.put("value", 1);
                    tmpDataMap.put("symbolSize", "20");
                    tmpDataMap.put("symbol", imagePath);
                    data.add(tmpDataMap);
                }
            }
            if (null == subTags || subTags.isEmpty()) {
                break;
            }
            Map<String, Object> subData = getDatas(subTags, tagName, index_);
            index_ = (Integer) subData.get("index_");

            links.addAll((Collection<? extends Map<String, Object>>) subData.get("links"));
            data.addAll((Collection<? extends Map<String, Object>>) subData.get("data"));
        }
        result.put("links", links);
        result.put("data", data);
        result.put("index_", index_);
        return result;
    }


    public Map<String, Object> getDataBak(List<MicroPortraitTag> tags, String type) {
        //下方一级列表
        List<String> bottomData = new ArrayList<>();
        //数据列表
        List<Map<String, Object>> data = new ArrayList<>();
        //links
        List<Map<String, Object>> links = new ArrayList<>();
        //categories
        List<Map<String, String>> categories = new ArrayList<>();

        String name = type.equals("0") ? "用户" : "物品";
        String imageName = type.equals("0") ? "people.png" : "thing.png";
        String imagePath = "image://../res/imgs/" + imageName;
        for (int i = 0; i < tags.size(); i++) {
            MicroPortraitTag tag = tags.get(i);
            String tagName = tag.getName();
            bottomData.add(tagName);
            Map<String, String> categoriesMap = new HashMap<>();
            categoriesMap.put("name", tagName);
            categories.add(categoriesMap);

            Map<String, Object> parentLink = new HashMap<>();
            parentLink.put("source", name);
            parentLink.put("target", tagName);
            links.add(parentLink);

            List<MicroPortraitTag> subTags = tag.getMicroSubTags();
            Map<String, List<Map<String, Object>>> subData = getDatasBak(subTags, tagName);

            links.addAll(subData.get("links"));
            data.addAll(subData.get("data"));
        }
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("name", name);
        headMap.put("symbolSize", 60);
        headMap.put("symbol", imagePath);
        headMap.put("draggable", "true");
        headMap.put("value", data.size());
        data.add(0, headMap);

        Map<String, Object> result = new HashMap<>();
        result.put("bottomData", bottomData);
        result.put("data", data);
        result.put("links", links);
        result.put("categories", categories);
        return result;
    }

    public Map<String, List<Map<String, Object>>> getDatasBak(List<MicroPortraitTag> tags, String parentName) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        //数据列表
        List<Map<String, Object>> data = new ArrayList<>();
        //links
        List<Map<String, Object>> links = new ArrayList<>();
        for (int j = 0; j < tags.size(); j++) {
            MicroPortraitTag tag = tags.get(j);
            String tagName = tag.getName();
            if (j == 0) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("name", parentName);
                dataMap.put("draggable", "true");
                dataMap.put("value", tags.size());
                dataMap.put("symbolSize", tags.size() * 3);
                data.add(dataMap);
            }
            if (tag.getLevel() != 5) {
                Map<String, Object> linkMap = new HashMap<>();
                linkMap.put("source", parentName);
                linkMap.put("target", tagName);
                links.add(linkMap);
            }
            List<MicroPortraitTag> subTags = tag.getMicroSubTags();
            if (tag.getLevel() == 5) {
                String imagePath = "image://../res/imgs/level5.png";
                for (int k = 0; k < tags.size(); k++) {
                    MicroPortraitTag subTag = tags.get(k);
                    String subTagName = subTag.getName();
                    Map<String, Object> tmpDataMap = new HashMap<>();
                    tmpDataMap.put("name", subTagName + "-" + subTag.getId());
                    tmpDataMap.put("draggable", "true");
                    tmpDataMap.put("value", 1);
                    tmpDataMap.put("symbolSize", "20");
                    tmpDataMap.put("symbol", imagePath);
                    data.add(tmpDataMap);

                    Map<String, Object> tmpLinkMap = new HashMap<>();
                    tmpLinkMap.put("source", parentName);
                    tmpLinkMap.put("target", subTagName + "-" + subTag.getId());
                    links.add(tmpLinkMap);
                }
            }
            if (null == subTags || subTags.isEmpty()) {
                break;
            }
            Map<String, List<Map<String, Object>>> subData = getDatasBak(subTags, tagName);
            links.addAll(subData.get("links"));
            data.addAll(subData.get("data"));
        }
        result.put("links", links);
        result.put("data", data);
        return result;
    }
}
