package cn.itcast.tag.web.search.bean;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;

import java.util.List;

/**
 * 标签查询Bean
 *
 * @author FengZhen
 */
public class SearchTagBean extends BasicTagBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 更新周期
     */
    private String scheTime;

    /**
     * 五级下对应的用户top5
     */
    private List<SearchUserBean> top5Users;

    /**
     * 拥有该标签用户数量
     */
    private long userCount;

    /**
     * 该行业下拥有该标签用户占比
     */
    private double userPercent;

    /**
     * 子级标签
     */
    private List<SearchTagBean> subSearchTags;

    /**
     * 路径名称
     */
    private String tagPathName;


    public List<SearchUserBean> getTop5Users() {
        return top5Users;
    }

    public void setTop5Users(List<SearchUserBean> top5Users) {
        this.top5Users = top5Users;
    }

    public String getTagPathName() {
        return tagPathName;
    }

    public void setTagPathName(String tagPathName) {
        this.tagPathName = tagPathName;
    }

    public List<SearchTagBean> getSubSearchTags() {
        return subSearchTags;
    }

    public void setSubSearchTags(List<SearchTagBean> subSearchTags) {
        this.subSearchTags = subSearchTags;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public double getUserPercent() {
        return userPercent;
    }

    public void setUserPercent(double userPercent) {
        this.userPercent = userPercent;
    }

    public String getScheTime() {
        return scheTime;
    }

    public void setScheTime(String scheTime) {
        this.scheTime = scheTime;
    }

}
