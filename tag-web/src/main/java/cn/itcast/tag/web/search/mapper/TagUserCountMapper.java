package cn.itcast.tag.web.search.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagUserCountMapper {

    /**
     * 根据标签id、标签类型查询对应用户数量
     *
     * @param tagId
     * @param type
     * @return
     */
    public long queryUserCountByTagIdAndType(@Param("tagId") long tagId, @Param("tagType") int tagType);

    /**
     * 插入
     *
     * @param tagId
     * @param tagType
     * @param userCount
     */
    public void insert(@Param("tagId") long tagId, @Param("tagType") int tagType, @Param("userCount") int userCount);

    /**
     * 获取所有用户数
     *
     * @return
     */
    public Long queryAllUserCount(int tagType);
}
