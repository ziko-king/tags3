/**
 * 项目名称：mengyao
 * 创建日期：2018年6月27日
 * 修改历史：
 * 1、[2018年6月27日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.utils;

/**
 * @author zhaocs
 * 页面路径统一处理（建议超过两次使用）
 */
public enum PageEnum {
    LOGIN("login", "登录页面"),


    HOME_INDEX("home", "首页"),

    BASICTAG_INDEX("basictag/basictag", "基础标签首页"),
    MERGETAG_INDEX("mergetag/mergetag", "组合标签首页"),
    MERGETAG_CREATE("mergetag/create", "组合标签创建页面"),

    ENGINE_INDEX("labeltask/labelingtask", "标签任务首页"),

    EXAMINE_INDEX("examine/examineManagement", "审核管理审批页面"),
    EXAMINE_APPLY("examine/applyManagement", "审核管理申请页面"),
    EXAMINE_DEVELOP("examine/developManagement", "审核管理开发页面"),
    EXAMINE_RELEASE("examine/releaseManagement", "审核管理发布页面"),

    MICRO_INDEX("micro/microPortrait", "微观画像首页"),

    SEARCH_INDEX("searchTag/tagquery", "标签查询首页"),
    SEARCH_LIST("searchTag/tagqueryList", "标签查询首页"),

    SYSTEM_INDEX_ADMIN("system/systemManage", "系统设置管理页面"),
    SYSTEM_INDEX_USER("system/systemUser", "系统设置普通用户页面");

    private String page;
    private String pageInfo;

    private PageEnum(String page, String pageInfo) {
        this.page = page;
        this.pageInfo = pageInfo;
    }

    public String getPage() {
        return page;
    }

    public String getPageInfo() {
        return pageInfo;
    }
}
