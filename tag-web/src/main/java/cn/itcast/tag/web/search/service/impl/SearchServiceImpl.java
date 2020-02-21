package cn.itcast.tag.web.search.service.impl;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.basictag.bean.form.BasicModelRuleFormBean;
import cn.itcast.tag.web.basictag.service.BasicTagService;
import cn.itcast.tag.web.search.bean.PageDTO;
import cn.itcast.tag.web.search.bean.SearchTagBean;
import cn.itcast.tag.web.search.bean.SearchUserBean;
import cn.itcast.tag.web.search.mapper.TagUserCountMapper;
import cn.itcast.tag.web.search.service.SearchService;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.service.MyShiro;
import cn.itcast.tag.web.utils.HBaseUtil;
import cn.itcast.tag.web.utils.MapToBeanUtil;
import cn.itcast.tag.web.utils.SolrUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    //solr collection
    private final String SOLR_COLLECTION = "socialSecurity";
    //HBase表名
    private final String TABLE_NAME = "tbl_tag_user_profile";//socialSecurityHDFS
    //HBase用户信息列族
    private final String FAMILY_USERINFO = "haier";
    //HBase标签列族
    private final String FAMILY_TAG = "haier";
    @Resource
    BasicTagService basicTagService;
    @Resource
    TagUserCountMapper tagUserCountMapper;
    @Resource
    HBaseUtil hbase;
    private Logger logger = LogManager.getLogger(getClass());

    public static void main(String[] args) {
        SolrUtil solrUtil = new SolrUtil("tagCollectionHDFS");
        List<String> tags = new ArrayList<>();
        tags.add("tb_1363");
        System.out.println(solrUtil.queryRowkeyCountByTags(tags));
//		List<String> rowkeys = solrUtil.queryRowkeyByTags(tags, 0, 10);
//		for (String string : rowkeys) {
//			System.out.println(string);
//		}
        solrUtil.closeSolr();
    }

    @Override
    public BasicTagBean searchTagList(long id) {
        BasicTagBean basicTagBean = null;
        try {
            //根据名称查出对应的tag
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long roleId = roleMaps.get(0).getRoleId();
            BasicTagBean paramTagBean = new BasicTagBean();
            paramTagBean.setId(id);
            List<BasicTagBean> basicTagBeans = basicTagService.queryBasicTagForId(paramTagBean, getCurrentUserBean(), new RoleBean(roleId));
            if (null != basicTagBeans && basicTagBeans.size() > 0) {
                basicTagBean = basicTagBeans.get(0);
                //获取子列表
                List<BasicTagBean> subTags = getSubTagBeanByPid(basicTagBean.getId());
                basicTagBean.setSubTags(subTags);
                //获取父级直到一级
                BasicTagBean levelOneTag = null;
                while (levelOneTag == null || levelOneTag.getLevel() != 1) {
                    List<BasicTagBean> topLevelBeans = new ArrayList<>();
                    topLevelBeans.add(basicTagBean);
                    levelOneTag = getLevelOneTagBeanById(basicTagBean.getPid());
                    if (null == levelOneTag) {
                        break;
                    }
                    levelOneTag.setSubTags(topLevelBeans);
                    basicTagBean = levelOneTag;
                }
            }
        } catch (Exception e) {
            logger.error("==== searchTagList@err:{} ====", e);
        }
        return basicTagBean;
    }

    @Override
    public PageDTO<SearchUserBean> searchUserListByTagIds(String ids, Integer page, Integer pageSize) {
        Integer offset = getOffset(page, pageSize);
        List<SearchUserBean> result = new ArrayList<>();
        List<Map<String, String>> userInfos = new ArrayList<>();
        //先拿到所有五级id
        List<Long> tagIds = new ArrayList<>();
        //solr查询条件
        List<String> tagsParam = new ArrayList<>();
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            String tagId = idArr[i];
            //遍历
            List<Long> subTagIds = getLevel5IdsByTagId(Long.parseLong(tagId));
            if (null != subTagIds) {
                tagIds.addAll(subTagIds);
                for (Long subTagId : subTagIds) {
                    //tagsParam.add("tb_"+subTagId);
                }
            }
        }

//		for (String subTagId : idArr) {
//			tagsParam.add("tb_"+subTagId);
//		}

//		SolrUtil solrUtil = new SolrUtil(SOLR_COLLECTION);
        try {
            hbase.createConnection();
            for (Long tagId : tagIds) {
                userInfos.addAll(hbase.queryForQuilfier(TABLE_NAME, FAMILY_TAG, "tbids", tagId + ""));
            }
            //List<String> rowKeys = solrUtil.queryRowkeyByTags(tagsParam, offset, pageSize);
            //userInfos = HBaseUtil.queryForRowKeys(TABLE_NAME, rowKeys, FAMILY_USERINFO);
            for (Map<String, String> map : userInfos) {
                String name = map.get("name");
//				   String banknum = map.get("bankNum");
//				   String phonenum = map.get("phone");
//				   String idnum = map.get("id");
//				   map.put("bankNum", getSafeData(banknum));
//				   map.put("phone", getSafeData(phonenum));
//				   map.put("idNum", getSafeData(idnum));
                map.put("name", name);//.substring(0, 1) + "**");
                try {
                    SearchUserBean sBean = (SearchUserBean) MapToBeanUtil.convertMap(SearchUserBean.class, map);
                    result.add(sBean);
                } catch (Exception e) {
                    logger.error("==== searchUserListByTagIds@err:{} ====", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PageDTO<SearchUserBean> pageDTO = new PageDTO<>();
        pageDTO.setData(result);
        pageDTO.setPage(page);
        pageDTO.setPageSize(pageSize);
        pageDTO.setOffset(offset);
//		pageDTO.setCount(solrUtil.queryRowkeyCountByTags(tagsParam));
//		solrUtil.closeSolr();
        return pageDTO;
    }

    //递归获取所有的五级id
    public List<Long> getLevel5IdsByTagId(Long tagId) {
        List<Long> tagIds = new ArrayList<>();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        //校验当前id是否为5级
        BasicTagBean paramTagBean = new BasicTagBean();
        paramTagBean.setId(tagId);
        List<BasicTagBean> tmpTags = basicTagService.queryBasicTagForId(paramTagBean, loginUser, new RoleBean(roleId));
        if (null != tmpTags && tmpTags.size() > 0) {
            BasicTagBean tmpTagBean = tmpTags.get(0);
            if (tmpTagBean.getLevel() == 5) {
                tagIds.add(tagId);
                return tagIds;
            }
        }
        BasicTagBean basicTagBean = new BasicTagBean();
        basicTagBean.setPid(tagId);
        List<BasicTagBean> subTags = basicTagService.queryBasicTagForPid(basicTagBean, loginUser, new RoleBean(roleId));
        if (null == subTags) {
            return tagIds;
        }
        for (BasicTagBean subTag : subTags) {
            if (subTag.getLevel() == 5) {
                tagIds.add(subTag.getId());
            } else {
                List<Long> subIds = getLevel5IdsByTagId(subTag.getId());
                tagIds.addAll(subIds);
            }
        }
        return tagIds;
    }

    @Override
    public List<SearchTagBean> search(String name) {
        List<SearchTagBean> result = new ArrayList<>();
        try {
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            UserBean loginUser = new UserBean();
            Long userId = curUser.getId();
            loginUser.setId(userId);
            Long roleId = roleMaps.get(0).getId();
            BasicTagBean paramTagBean = new BasicTagBean();
            paramTagBean.setName(name);
            List<BasicTagBean> basicTagBeans = basicTagService.queryBasicTagForName(paramTagBean, loginUser, new RoleBean(roleId));
            Map<Long, BasicTagBean> basicTagBeanMap = new HashMap<>();
            if (null != basicTagBeans) {
                for (BasicTagBean basicTagBean : basicTagBeans) {
                    //获取父级直到一级
                    BasicTagBean levelOneTag = null;
                    while (levelOneTag == null || levelOneTag.getLevel() != 1) {
                        List<BasicTagBean> topLevelBeans = new ArrayList<>();
                        topLevelBeans.add(basicTagBean);
                        if (!basicTagBeanMap.containsKey(basicTagBean.getPid())) {
                            levelOneTag = getLevelOneTagBeanById(basicTagBean.getPid());
                            basicTagBeanMap.put(basicTagBean.getPid(), levelOneTag);
                        }
                        levelOneTag = basicTagBeanMap.get(basicTagBean.getPid());
                        if (null == levelOneTag) {
                            break;
                        }
                        levelOneTag.setSubTags(topLevelBeans);
                        basicTagBean = levelOneTag;
                    }
                    String pathTagName = "";
                    while (true) {
                        if (null == basicTagBean.getSubTags()) {
                            break;
                        }
                        if (!pathTagName.equals("")) {
                            pathTagName += " --- ";
                        }
                        pathTagName += basicTagBean.getName();
                        basicTagBean = basicTagBean.getSubTags().get(0);
                    }
                    SearchTagBean searchTagBean = new SearchTagBean();
                    BeanUtils.copyProperties(basicTagBean, searchTagBean);
                    searchTagBean.setTagPathName(pathTagName);
                    result.add(searchTagBean);
                }
            }
        } catch (Exception e) {
            logger.error("==== search@err:{} ====", e);
        }
        return result;
    }

    @Override
    public List<SearchTagBean> queryTagByIdAndLevel(BasicTagBean basicTagBean) {
        List<SearchTagBean> result = new ArrayList<>();
        try {
            MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
            List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
            Long roleId = roleMaps.get(0).getRoleId();
            RoleBean roleBean = new RoleBean();
            roleBean.setId(roleId);
            BasicModelRuleFormBean basicModelRuleFormBean = new BasicModelRuleFormBean();
            basicModelRuleFormBean.setTagId(basicTagBean.getId());
            basicModelRuleFormBean.setLevel(basicTagBean.getLevel());
            basicModelRuleFormBean.setTagName(basicTagBean.getName());
            List<BasicModelRuleFormBean> basicModelRuleFormBeans = basicTagService.queryBasicTagAndModelForWithPid(basicModelRuleFormBean, getCurrentUserBean(), roleBean);
            if (null != basicModelRuleFormBeans) {
                for (BasicModelRuleFormBean tagBean : basicModelRuleFormBeans) {
                    SearchTagBean searchTagBean = new SearchTagBean();
                    BeanUtils.copyProperties(tagBean, searchTagBean);
                    searchTagBean.setId(tagBean.getTagId());
                    searchTagBean.setName(tagBean.getTagName());
                    searchTagBean.setScheTime(tagBean.getScheTime());
                    List<BasicModelRuleFormBean> subTags = tagBean.getSubTags();
                    List<SearchTagBean> subSearchs = new ArrayList<>();
                    if (null != subTags) {
                        for (BasicModelRuleFormBean subTag : subTags) {
                            SearchTagBean subSearchTag = new SearchTagBean();
                            BeanUtils.copyProperties(subTag, subSearchTag);
                            subSearchTag.setId(subTag.getTagId());
                            subSearchTag.setName(subTag.getTagName());
                            subSearchTag.setScheTime(subTag.getScheTime());
                            List<SearchUserBean> userBeans = queryUserInfoByTagId(String.valueOf(subTag.getTagId()));
                            subSearchTag.setTop5Users(userBeans);
                            subSearchTag.setUserCount(getUserCountByTagId(subTag.getTagId(), 1));
                            subSearchTag.setUserPercent(0.95);
                            subSearchs.add(subSearchTag);

                        }
                    }
                    searchTagBean.setSubSearchTags(subSearchs);
                    result.add(searchTagBean);
                }
            }
        } catch (Exception e) {
            logger.error("==== queryTagByIdAndLevel@err:{} ====", e);
        }
        return result;
    }

    /**
     * 根据pid获取子标签
     *
     * @param pid
     * @return
     */
    public List<BasicTagBean> getSubTagBeanByPid(Long pid) {
        BasicTagBean basicTagBean = new BasicTagBean();
        basicTagBean.setPid(pid);
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        UserBean loginUser = new UserBean();
        loginUser.setId(curUser.getId());
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicTagBean> subTags = basicTagService.queryBasicTagForPid(basicTagBean, loginUser, new RoleBean(roleId));
        if (null == subTags) {
            return subTags;
        }
        if (subTags.size() > 0) {
            for (BasicTagBean tagBean : subTags) {
                if (tagBean.getLevel() == 5) {
                    break;
                }
                List<BasicTagBean> result = getSubTagBeanByPid(tagBean.getId());
                tagBean.setSubTags(result);
            }
        }
        return subTags;
    }

    /**
     * 根据id获取标签
     */
    public BasicTagBean getLevelOneTagBeanById(Long pid) {
        BasicTagBean paramTag = new BasicTagBean();
        paramTag.setId(pid);
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        List<BasicTagBean> resultTagBeans = basicTagService.queryBasicTagForId(paramTag, getCurrentUserBean(), new RoleBean(roleId));
        if (null == resultTagBeans) {
            return null;
        }
        if (resultTagBeans.size() > 0) {
            BasicTagBean basicTagBean = resultTagBeans.get(0);
            return basicTagBean;
        }
        return null;
    }

    @Override
    public List<SearchUserBean> queryUserInfoByTagId(String tagId) {
        List<SearchUserBean> list = new ArrayList<>();
        List<String> rowKeys = null;
        List<Map<String, String>> top5 = new ArrayList<>();
        try {
            HBaseUtil hBaseUtil = new HBaseUtil();
            String column = "basicTag_" + tagId;
            rowKeys = hBaseUtil.queryForQuilfierName(TABLE_NAME, column, 5);
            top5 = hBaseUtil.queryForRowKeys(TABLE_NAME, rowKeys, FAMILY_USERINFO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map<String, String> map : top5) {
            String name = map.get("name");
            String banknum = map.get("bankNum");
            String phonenum = map.get("phone");
            String idnum = map.get("id");
            map.put("bankNum", getSafeData(banknum));
            map.put("phone", getSafeData(phonenum));
            map.put("idNum", getSafeData(idnum));
            map.put("name", name.substring(0, 1) + "**");
            try {
                SearchUserBean sBean = (SearchUserBean) MapToBeanUtil.convertMap(SearchUserBean.class, map);
                list.add(sBean);
            } catch (Exception e) {
                logger.error("==== queryUserInfoByTagId@err:{} ====", e);
            }
        }
        return list;
    }

    /**
     * 前三后三
     *
     * @param value
     * @return
     */
    public String getSafeData(String value) {
        if (null == value) {
            return "";
        }
        if (value.length() < 6) {
            return value;
        }
        String header3 = value.substring(0, 3);
        String tail3 = value.substring(value.length() - 3);
        StringBuffer result = new StringBuffer();
        result.append(header3);
        for (int i = 0; i < value.length() - 6; i++) {
            result.append("*");
        }
        result.append(tail3);
        return result.toString();
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public UserBean getCurrentUserBean() {
        UserBean userBean = new UserBean();
        MyShiro.Principal curUser = (MyShiro.Principal) SecurityUtils.getSubject().getPrincipal();
        userBean.setId(curUser.getId());
        return userBean;
    }

    @Override
    public long getUserCountByTagId(long tagId, int type) {
//		String columnHeader = type==1?"basicTag":"mergeTag";
//		String columnName = columnHeader + "_" + tagId;
//		HBaseUtil hBaseUtil = new HBaseUtil();
//		int userCount = 0;
//		try {
//			List<String> rowKeys = hBaseUtil.queryForQuilfierName(TABLE_NAME,columnName,Long.MAX_VALUE);
//			if (null != rowKeys) {
//				userCount = rowKeys.size();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return userCount;

        long userCount = 0;
        userCount = tagUserCountMapper.queryUserCountByTagIdAndType(tagId, type);
        return userCount;
    }

    @Override
    public void insertTagUserCount(long tagId, int tagType, int userCount) {
        tagUserCountMapper.insert(tagId, tagType, userCount);
    }

    @Override
    public Long getAllUserCount(int tagType) {
        return tagUserCountMapper.queryAllUserCount(tagType);
    }

    /**
     * 获取索引
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Integer getOffset(Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return offset < 0 ? 0 : offset;
    }
}
