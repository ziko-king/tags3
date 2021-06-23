package cn.itcast.tag.web.api.inner.service;

import cn.itcast.tag.web.api.inner.bean.SearchTagUserBean;
import cn.itcast.tag.web.api.inner.bean.SearchUserBean;

import java.util.List;

public interface InnerService {
    /**
     * 根据标签id、标签类型、条数获取用户数据
     *
     * @param tagId
     * @param type
     * @param count
     */
    public List<SearchUserBean> search(long tagId, int type, int count);

    /**
     * 根据标签id、标签类型、条数获取用户数据
     *
     * @param basicTags
     * @param mergeTags
     * @param count
     */
    public List<SearchTagUserBean> searchByMoreTagIds(String basicTags, String mergeTags, int count);

    /**
     * 根据身份证号查用户信息
     *
     * @param idCard
     * @return
     */
    public SearchUserBean searchByIdCard(String idCard);
}
