package cn.itcast.tag.web.search.service;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.search.bean.PageDTO;
import cn.itcast.tag.web.search.bean.SearchTagBean;
import cn.itcast.tag.web.search.bean.SearchUserBean;

import java.util.List;

/**
 * 标签查询service
 *
 * @author FengZhen
 */
public interface SearchService {

    /**
     * 标签查询
     *
     * @param id
     * @return
     */
    public BasicTagBean searchTagList(long id);

    /**
     * 根据多个标签id获取用户列表
     *
     * @param ids
     * @param page
     * @param pageSize
     * @return
     */
    public PageDTO<SearchUserBean> searchUserListByTagIds(String ids, Integer page, Integer pageSize);

    /**
     * 标签模糊查询
     *
     * @param name
     * @return
     */
    public List<SearchTagBean> search(String name);

    /**
     * 根据id和level查询tag
     *
     * @param basicTagBean
     */
    public List<SearchTagBean> queryTagByIdAndLevel(BasicTagBean basicTagBean);

    /**
     * 根据标签id获取top5用户
     *
     * @param tagId
     * @return
     */
    public List<SearchUserBean> queryUserInfoByTagId(String tagId);

    /**
     * 根据基础标签id获取用户数量
     *
     * @param tagId
     * @param type  1:基础标签  2:组合标签
     * @return
     */
    public long getUserCountByTagId(long tagId, int type);

    /**
     * 新增标签用户数量关联
     *
     * @param tagId
     * @param tagType
     * @param userCount
     */
    public void insertTagUserCount(long tagId, int tagType, int userCount);

    /**
     * 获取系统涵盖用户数
     *
     * @param tagType
     * @return
     */
    public Long getAllUserCount(int tagType);

}
