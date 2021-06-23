package cn.itcast.tag.web.search.bean;

import java.io.Serializable;

/**
 * @author:FengZhen
 * @create:2018年6月29日 查询DTO
 */
public class QueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private int page = 1;

    /**
     * 每页数量
     */
    private int pageSize = 15;

    /**
     * 起始索引
     */
    private int offset;

    /**
     * 五级id
     */
    private String ids;

    /**
     * 标签名称
     */
    private String tagName;

    public int getOffset() {
        offset = (page - 1) * pageSize;
        return offset < 0 ? 0 : offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

}
