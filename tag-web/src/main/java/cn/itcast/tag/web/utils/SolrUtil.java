package cn.itcast.tag.web.utils;

//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.CloudSolrServer;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.common.SolrDocument;
//
//import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;

/**
 * Created by mengyao
 * 2018年3月24日
 */
public class SolrUtil {

//    private SolrServer solrClient;

    public SolrUtil(String collection) {
//        solrClient = new CloudSolrServer("bjqt244.qt:2181,bjqt245.qt:2181,bjqt246.qt:2181");
//        if (solrClient instanceof CloudSolrServer) {
//            final CloudSolrServer cloudSolrServer = (CloudSolrServer) solrClient;
//            cloudSolrServer.setDefaultCollection(collection);//tagCollectionHDFS
//            cloudSolrServer.setZkClientTimeout(3000);
//            cloudSolrServer.setZkConnectTimeout(3000);
//            cloudSolrServer.connect();
//        }
    }

    /**
     * 分页查询rowkey
     *
     * @param tags
     * @param offset
     * @param pageSize
     * @return
     */
    public List<String> queryRowkeyByTags(List<String> tags, Integer offset, Integer pageSize) {
//        Set<String> rowkeys = new HashSet<>();
//        String query = "";
//        if (null != tags && tags.size() > 0) {
//            for (String tag : tags) {
//                query += " AND tags:" + tag;
//            }
//        } else {
//            query = "*:*";
//        }
//        query = query.replaceFirst(" AND ", "");
//        SolrQuery params = new SolrQuery();
//        params.setQuery(query);
//        //分页，默认是分页从0开始，每页显示10行
//        params.setStart(null == offset ? 0 : offset);
//        params.setRows(null == pageSize ? 10 : pageSize);
//        QueryResponse queryResponse;
//        try {
//            queryResponse = solrClient.query(params);
//            List<SolrDocument> list = queryResponse.getResults();
//            if (null == list) {
//                return null;
//            }
//            for (SolrDocument solrDocument : list) {
//                String rowkey = (String) solrDocument.get("rowkey");
//                rowkeys.add(rowkey);
//            }
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>(rowkeys);
        return null;
    }

    /**
     * 查询rowkey(不分页)
     *
     * @param tags
     * @return
     */
    public List<String> queryRowkeyByTags(List<String> tags) {
//        Set<String> rowkeys = new HashSet<>();
//        String query = "";
//        if (null != tags && tags.size() > 0) {
//            for (String tag : tags) {
//                query += " <AND> tags:" + tag;
//            }
//        } else {
//            query = "*:*";
//        }
//        query = query.replaceFirst(" AND ", "");
//        SolrQuery params = new SolrQuery();
//        params.setQuery(query);
//        //分页，默认是分页从0开始，每页显示10行
//        params.setStart(0);
//        params.setRows(queryRowkeyCountByTags(tags));
//        QueryResponse queryResponse;
//        try {
//            queryResponse = solrClient.query(params);
//            List<SolrDocument> list = queryResponse.getResults();
//            if (null == list) {
//                return null;
//            }
//            for (SolrDocument solrDocument : list) {
//                String rowkey = (String) solrDocument.get("rowkey");
//                rowkeys.add(rowkey);
//            }
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>(rowkeys);
        return null;
    }

    /**
     * 获取满足条件的总条数
     *
     * @param tags
     * @return
     */
    public Integer queryRowkeyCountByTags(List<String> tags) {
//        String query = "";
//        if (null != tags && tags.size() > 0) {
//            for (String tag : tags) {
//                query += " AND tags:" + tag;
//            }
//        } else {
//            query = "*:*";
//        }
//        query = query.replaceFirst("AND", "");
//        SolrQuery params = new SolrQuery();
//        params.setQuery(query);
//        QueryResponse queryResponse;
//        try {
//            queryResponse = solrClient.query(params);
//            return (int) queryResponse.getResults().getNumFound();
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        }
//        return 0;
        return null;
    }


    /**
     * 关闭连接
     */
    public void closeSolr() {
//        solrClient.shutdown();
    }
}
