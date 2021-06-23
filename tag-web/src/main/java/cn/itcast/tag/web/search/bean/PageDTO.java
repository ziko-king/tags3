package cn.itcast.tag.web.search.bean;
/**
 * @author:FengZhen
 * @create:2018年6月29日
 */

import java.io.Serializable;
import java.util.List;

/**
 * @author:FengZhen
 * @create:2018年6月29日
 * 分页DTO
 */
public class PageDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 分页数据
     */
    List<T> data;
    /**
     * 当前页码
     */
    private int page;
    /**
     * 当前每页数量
     */
    private int pageSize = 15;
    /**
     * 当前起始索引
     */
    private Integer offset;
    /**
     * 数据总条数
     */
    private Integer count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
