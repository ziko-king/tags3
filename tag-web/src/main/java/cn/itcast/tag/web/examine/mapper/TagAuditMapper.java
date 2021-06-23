package cn.itcast.tag.web.examine.mapper;

import cn.itcast.tag.web.examine.bean.TagAuditBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangjunfeng
 * @date 2018-06-10
 */
@Repository
public interface TagAuditMapper {

    List<TagAuditBean> getBasicTags(@Param("tagName") String tagName);

    List<TagAuditBean> getMaegeTags(@Param("tagName") String tagName);

    /**
     * 获取标签信息(分页)
     *
     * @param queryDTO
     * @return
     */
    List<TagAuditBean> getTags(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("tagName") String tagName);

    /**
     * 获取标签总条数
     *
     * @param queryDTO
     * @return
     */
    Integer getTagsCount(@Param("tagName") String tagName);
}
