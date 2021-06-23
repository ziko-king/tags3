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
 *
 */
public class PageBean implements Serializable {
    private static final long serialVersionUID = 622077140294901437L;
    private Integer curPage;       // 当前页
    private Integer pageCount;     // 总页数
    private Integer pageSize = 10;      // 每页记录数
    private Integer rowsCount;     // 总记录数

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Integer rowsCount) {
        this.rowsCount = rowsCount;
    }

}
