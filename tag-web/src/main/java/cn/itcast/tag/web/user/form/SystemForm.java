/**
 * 项目名称：mengyao
 * 创建日期：2018年6月8日
 * 修改历史：
 * 1、[2018年6月8日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.user.form;

import java.io.Serializable;

/**
 * @author zhaocs
 * 查询用户参数
 */
public class SystemForm implements Serializable {
    private static final long serialVersionUID = 171494725121226428L;
    private PageBean pageBean;
    private String name;                // 用户名称
    private String groupId;              // 用户Id
    private String userId;              // 用户Id

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
