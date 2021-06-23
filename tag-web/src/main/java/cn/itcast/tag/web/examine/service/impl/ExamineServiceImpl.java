package cn.itcast.tag.web.examine.service.impl;

import cn.itcast.tag.web.basictag.mapper.UserTagMapMapper;
import cn.itcast.tag.web.examine.bean.TagAuditBean;
import cn.itcast.tag.web.examine.mapper.TagAuditMapper;
import cn.itcast.tag.web.examine.service.ExamineService;
import cn.itcast.tag.web.mergetag.bean.UserMergeTagMapBean;
import cn.itcast.tag.web.mergetag.mapper.UserMergeTagMapMapper;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.basictag.bean.form.UserTagFormBean;
import cn.itcast.tag.web.engine.bean.ModelBean;
import cn.itcast.tag.web.engine.mapper.ModelMapper;
import cn.itcast.tag.web.engine.mapper.RuleMapper;
import cn.itcast.tag.web.search.bean.PageDTO;
import cn.itcast.tag.web.search.bean.QueryDTO;
import cn.itcast.tag.web.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangjunfeng
 * @date 2018年6月10日
 */
@Service
@Transactional
public class ExamineServiceImpl implements ExamineService {

    @Resource
    private TagAuditMapper tagAuditMapper;

    @Resource
    private RuleMapper ruleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private UserTagMapMapper userTagMapMapper;

    @Resource
    private UserMergeTagMapMapper userMergeTagMapMapper;

    @Override
    public PageDTO<TagAuditBean> getAllTags(QueryDTO queryDTO) {
        // 查询基础标签和组合标签
//		List<TagAuditBean> temps = new ArrayList<TagAuditBean>();
//		List<TagAuditBean> basicTags = tagAuditMapper.getBasicTags(tagName);
//		List<TagAuditBean> mergeTags = tagAuditMapper.getMaegeTags(tagName);
//		temps.addAll(basicTags);
//		temps.addAll(mergeTags);

        List<TagAuditBean> temps = tagAuditMapper.getTags(queryDTO.getOffset(), queryDTO.getPageSize(), queryDTO.getTagName());

        // 查询和组装用户数据
        List<UserBean> userBeans = userMapper.queryAll();
        Map<Long, String> userMap = new HashMap<Long, String>();
        for (UserBean userBean : userBeans) {
            userMap.put(userBean.getId(), userBean.getUsername());
        }
        // 查询和组装rule数据
//		RuleBean ruleBean4Search = new RuleBean();
//		ruleBean4Search.setCp(1);
//		ruleBean4Search.setPr(100000000);
//		List<RuleBean> ruleBeans = ruleMapper.queryRuleForScan(ruleBean4Search);
//		Map<String, String> ruleMap = new HashMap<String, String>();
//		for (RuleBean ruleBean : ruleBeans) {
//			ruleMap.put(ruleBean.getTagId() + "-" + ruleBean.getType(), ruleBean.getRule());
//		}
        // 查询和组装model数据
//		ModelBean modelBean4Search = new ModelBean();
//		modelBean4Search.setCp(1);
//		modelBean4Search.setPr(100000000);
//		List<ModelBean> modelBeans = modelMapper.queryModelForScan(modelBean4Search);
//		Map<String, String> modelMap = new HashMap<String, String>();
//		for (ModelBean modelBean : modelBeans) {
//			modelMap.put(modelBean.getTagId() + "-" + modelBean.getType(), modelBean.getModelName());
//		}
        // 组装返回所需数据
        List<TagAuditBean> results = new ArrayList<TagAuditBean>();
        for (TagAuditBean tagAuditBean : temps) {
            tagAuditBean.setApplyUserName(userMap.getOrDefault(tagAuditBean.getApplyUserId(), null));
            //tagAuditBean.setRule(ruleMap.getOrDefault(tagAuditBean.getId() + "-" + tagAuditBean.getType(), null));
            if (tagAuditBean.getType() == 2 || tagAuditBean.getLevel() != 4) {
                tagAuditBean.setModel("暂无");
            } else {
                ModelBean modelBean = new ModelBean();
                modelBean.setTagId(tagAuditBean.getTagId());
                List<ModelBean> modelBeans2 = modelMapper.queryModelForTagId(modelBean);
                //tagAuditBean.setModel(modelMap.getOrDefault(tagAuditBean.getId() + "-" + tagAuditBean.getType(), null));
                if (null != modelBeans2 && modelBeans2.size() > 0) {
                    tagAuditBean.setModel(modelBeans2.get(modelBeans2.size() - 1).getModelName());
                } else {
                    tagAuditBean.setModel("暂无");
                }

            }
            results.add(tagAuditBean);
        }
        PageDTO<TagAuditBean> pageDTO = new PageDTO<>();
        pageDTO.setData(results);
        pageDTO.setPage(queryDTO.getPage());
        pageDTO.setOffset(queryDTO.getOffset());
        pageDTO.setPageSize(queryDTO.getPageSize());
        pageDTO.setCount(tagAuditMapper.getTagsCount(queryDTO.getTagName()));
        return pageDTO;
    }

    @Override
    public int auditUserTag(UserTagFormBean userTagFormBean) {
        UserTagFormBean userTagBean = userTagMapMapper.queryUserTagForId(userTagFormBean);
        if (Objects.nonNull(userTagBean)) {
//			ModelBean modelBean = new ModelBean();
//			modelBean.setTagId(userTagBean.getTagId());
//			modelBean.setType(1);
//			ModelBean queryModel = modelMapper.queryModelForTagIdAndType(modelBean);
//
//			RuleBean ruleBean = new RuleBean();
//			ruleBean.setTagId(userTagBean.getTagId());
//			ruleBean.setType(1);
//			RuleBean queryRule = ruleMapper.queryRuleForTagIdAndType(ruleBean);
//			if (Objects.nonNull(queryModel) && Objects.nonNull(queryRule)) {
            return userTagMapMapper.updateState(userTagFormBean);
//			}
        }
        return -1;
    }

    @Override
    public int auditUserMergeTag(UserMergeTagMapBean userMergeTagMapBean) {
        UserMergeTagMapBean userMergeTag = userMergeTagMapMapper.queryUserMergeTagForId(userMergeTagMapBean);
        if (Objects.nonNull(userMergeTag)) {
//			ModelBean modelBean = new ModelBean();
//			modelBean.setTagId(userMergeTag.getMergeTagId());
//			modelBean.setType(2);
//			ModelBean queryModel = modelMapper.queryModelForTagIdAndType(modelBean);
//
//			RuleBean ruleBean = new RuleBean();
//			ruleBean.setTagId(userMergeTag.getMergeTagId());
//			ruleBean.setType(2);
//			RuleBean queryRule = ruleMapper.queryRuleForTagIdAndType(ruleBean);
            //if (Objects.nonNull(queryModel) && Objects.nonNull(queryRule)) {
            userMergeTagMapMapper.updateState(userMergeTagMapBean);
            //}
        }
        return -1;
    }

}
