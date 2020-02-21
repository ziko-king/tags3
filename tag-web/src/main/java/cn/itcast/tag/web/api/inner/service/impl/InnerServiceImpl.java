package cn.itcast.tag.web.api.inner.service.impl;

import cn.itcast.tag.web.api.inner.bean.SearchTagUserBean;
import cn.itcast.tag.web.api.inner.bean.SearchUserBean;
import cn.itcast.tag.web.api.inner.service.InnerService;
import cn.itcast.tag.web.utils.HBaseUtil;
import cn.itcast.tag.web.utils.JsonUtil;
import cn.itcast.tag.web.utils.MapToBeanUtil;
import cn.itcast.tag.web.utils.SolrUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InnerServiceImpl implements InnerService {

    //solr collection
    private final String SOLR_COLLECTION = "socialSecurity";//tagCollectionHDFS
    //HBase表名
    private final String TABLE_NAME = "socialSecurity";//socialSecurityHDFS
    //HBase用户信息列族
    private final String FAMILY_USERINFO = "userInfo";
    //HBase标签列族
    private final String FAMILY_TAG = "tag";
    private Logger logger = LogManager.getLogger(getClass());

    public static void testSearch() {
        InnerServiceImpl outSearchServiceImpl = new InnerServiceImpl();
        List<SearchUserBean> result = outSearchServiceImpl.search(39, 1, 100);
        String resultJson = JsonUtil.obj2Json(result);
        System.out.println(resultJson);
    }

    public static void main(String[] args) {
        InnerServiceImpl outSearchServiceImpl = new InnerServiceImpl();
        List<SearchTagUserBean> result = outSearchServiceImpl.searchByMoreTagIds("1363", null, 10);
        String resultJson = JsonUtil.obj2Json(result);
        System.out.println(resultJson);

        SearchUserBean searchUserBean = outSearchServiceImpl.searchByIdCard("110115199402265244");
        String resultJson1 = JsonUtil.obj2Json(searchUserBean);
        System.out.println(resultJson1);
    }

    @Override
    public List<SearchUserBean> search(long tagId, int type, int count) {
        List<SearchUserBean> result = new ArrayList<>();
        HBaseUtil hBaseUtil = new HBaseUtil();
        List<String> tags = new ArrayList<>();
        String tag = "";
        if (type == 1) {
            tag = "tb_" + tagId;
        } else if (type == 2) {
            tag = "tm_" + tagId;
        } else {
            return result;
        }
        tags.add(tag);
        try {
            SolrUtil solrUtil = new SolrUtil(SOLR_COLLECTION);
            List<String> rowKeys = solrUtil.queryRowkeyByTags(tags, 0, count);
            solrUtil.closeSolr();
//			List<String> rowKeys = hBaseUtil.queryForQuilfierName(TABLE_NAME, qualifier, count);
            if (null != rowKeys) {
                if (rowKeys.size() > 0) {
                    List<Map<String, String>> resultList = hBaseUtil.queryForRowKeys(TABLE_NAME, rowKeys, FAMILY_USERINFO);
                    for (Map<String, String> map : resultList) {
                        SearchUserBean searchUserBean = new SearchUserBean();
                        searchUserBean = (SearchUserBean) MapToBeanUtil.convertMap(SearchUserBean.class, map);
                        searchUserBean.setName(searchUserBean.getName().substring(0, 1) + "**");
                        searchUserBean.setId(getSafeData(searchUserBean.getId()));
                        result.add(searchUserBean);
                    }
//					for (String rowKey : rowKeys) {
//						Map<String, String> resultMap = hBaseUtil.queryForRowKey(TABLE_NAME, rowKey, FAMILY_USERINFO);
//						SearchUserBean searchUserBean = new SearchUserBean();
//						searchUserBean = (SearchUserBean) MapToBeanUtil.convertMap(SearchUserBean.class, resultMap);
//						searchUserBean.setName(searchUserBean.getName().substring(0, 1) + "**");
//						searchUserBean.setId(getSafeData(searchUserBean.getId()));
//						result.add(searchUserBean);
//					}
                }
            }
        } catch (IOException | IllegalAccessException | InstantiationException | InvocationTargetException
                | IntrospectionException e) {
            logger.error("==== search@err:{} ====", e);
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<SearchTagUserBean> searchByMoreTagIds(String basicTags, String mergeTags, int count) {
        List<SearchTagUserBean> result = new ArrayList<>();
        HBaseUtil hBaseUtil = new HBaseUtil();
        String[] basicTagArr = new String[]{};
        String[] mergeTagArr = new String[]{};
        List<String> tags = new ArrayList<>();
        if (null != basicTags) {
            basicTagArr = basicTags.split(",");
        }
        if (null != mergeTags) {
            mergeTagArr = mergeTags.split(",");
        }
        Map<String, String> paramMap = new HashMap<>();
        if (basicTagArr.length > 0) {
            for (String tagId : basicTagArr) {
                //paramMap.put("basicTag_"+tagId, tagId);
                tags.add("tb_" + tagId);
            }
        }
        if (mergeTagArr.length > 0) {
            for (String tagId : mergeTagArr) {
                tags.add("tm_" + tagId);
                //paramMap.put("mergeTag_"+tagId, tagId);
            }
        }
        //根据多条件查找并且的关系
        try {
            SolrUtil solrUtil = new SolrUtil(SOLR_COLLECTION);
            List<String> rowKeys = solrUtil.queryRowkeyByTags(tags, 0, count);
            solrUtil.closeSolr();
            if (null != rowKeys) {
                if (rowKeys.size() > 0) {
                    List<Map<String, String>> resultList = hBaseUtil.queryForRowKeys(TABLE_NAME, rowKeys, FAMILY_USERINFO);
                    for (Map<String, String> map : resultList) {
                        SearchTagUserBean searchTagUserBean = new SearchTagUserBean();
                        searchTagUserBean = (SearchTagUserBean) MapToBeanUtil.convertMap(SearchTagUserBean.class, map);
                        searchTagUserBean.setName(searchTagUserBean.getName().substring(0, 1) + "**");
                        searchTagUserBean.setId(getSafeData(searchTagUserBean.getId()));
                        result.add(searchTagUserBean);
                    }
//					for (String rowKey : rowKeys) {
//						Map<String, String> resultMap = hBaseUtil.queryForRowKey(TABLE_NAME, rowKey, FAMILY_USERINFO);
//						SearchTagUserBean searchTagUserBean = new SearchTagUserBean();
//						searchTagUserBean = (SearchTagUserBean) MapToBeanUtil.convertMap(SearchTagUserBean.class, resultMap);
//						searchTagUserBean.setName(searchTagUserBean.getName().substring(0, 1) + "**");
//						searchTagUserBean.setId(getSafeData(searchTagUserBean.getId()));
//						result.add(searchTagUserBean);
//					}
                }
            }
        } catch (Exception e) {
            logger.error("==== search@err:{} ====", e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public SearchUserBean searchByIdCard(String idCard) {
        SearchUserBean searchUserBean = new SearchUserBean();
        try {
            HBaseUtil hBaseUtil = new HBaseUtil();
            Map<String, String> resultMap = hBaseUtil.queryForRowKey(TABLE_NAME, idCard, FAMILY_USERINFO);
            searchUserBean = (SearchUserBean) MapToBeanUtil.convertMap(SearchUserBean.class, resultMap);
            searchUserBean.setName(searchUserBean.getName().substring(0, 1) + "**");
            searchUserBean.setId(getSafeData(searchUserBean.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchUserBean;
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

}
