package cn.itcast.tag.web.mergetag.mapper;

import cn.itcast.tag.web.mergetag.bean.MergeTagTagMapBean;
import org.springframework.stereotype.Repository;

/**
 * 组合标签与基础标签关联
 *
 * @author zhangwenguo
 */
@Repository
public interface MergeTagTagMapMapper {

    /**
     * 更新组合表和基础标签关联DTO
     *
     * @param bean
     * @return
     */
    public int insertMergeTagTagMap(MergeTagTagMapBean bean);

    /**
     * 根据ID删除组合标签关联表
     *
     * @param bean
     * @return
     */
    public int delMergeTagMapForId(MergeTagTagMapBean bean);

}
