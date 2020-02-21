package cn.itcast.tag.web.examine.service;

import cn.itcast.tag.web.basictag.bean.form.UserTagFormBean;
import cn.itcast.tag.web.examine.bean.TagAuditBean;
import cn.itcast.tag.web.mergetag.bean.UserMergeTagMapBean;
import cn.itcast.tag.web.search.bean.PageDTO;
import cn.itcast.tag.web.search.bean.QueryDTO;

public interface ExamineService {
    /**
     * 获取所有审核所需的标签数据
     *
     * @return
     * @author kiwi
     * @date 2018年6月10日
     */
    PageDTO<TagAuditBean> getAllTags(QueryDTO queryDTO);

    /**
     * 审核基础标签
     *
     * @param userTagFormBean
     * @return
     */
    int auditUserTag(UserTagFormBean userTagFormBean);

    /**
     * 审核组合标签
     *
     * @param userMergeTagMapBean
     * @return
     */
    int auditUserMergeTag(UserMergeTagMapBean userMergeTagMapBean);

}
