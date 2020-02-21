package cn.itcast.model.tools.solr;

import cn.itcast.model.models.ModelConfig;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Solr工具类
 * Created by mengyao
 * 2018年6月2日
 */
@SuppressWarnings("all")
public class SolrTools {

    private static SolrTools solrTools;
    public final String ID = "id";
    private final String QUERY = "q";
    private final String FILTER_QUERY = "fq";
    private final String SORT = "sort";
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelConfig modelConfig = new ModelConfig();
    private SolrServer server = null;


    private SolrTools() {
        initializer();
    }

    public static SolrTools build() {
        if (null == solrTools) {
            solrTools = new SolrTools();
        }
        return solrTools;
    }

    /**
     * 初始化工具类配置
     */
    private void initializer() {
        if (modelConfig.isSolrCloudMode()) {
            // solr cloud模式
            server = new CloudSolrServer(modelConfig.getZkHosts());
            logger.info("==== Created by [Solr Cloud] Connection. ====");
        } else {
            // solr单机模式
            server = new HttpSolrServer(modelConfig.getSolrAddr());
            logger.info("==== Created by [Single Solr] Connection. ====");
        }
    }

    /**
     * 校验连接是否有效，如无效则重新连接
     */
    public void checker() {
        SolrPingResponse ping = null;
        try {
            ping = server.ping();
            if (ping.getStatus() != 200) {
                initializer();
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            initializer();
        }
    }

    /**
     * 新增solr索引
     *
     * @param doc
     */
    public void addDoc(SolrInputDocument doc) {
        try {
            logger.info("==== Add document is:{} ====", doc);
            if (!doc.containsKey(ID)) {
                logger.info("==== document id field is null, add error! ====");
            } else {
                UpdateResponse response = server.add(doc);
                server.commit();
                logger.info("==== Add document status: {} ====", response.getStatus());
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 删除Solr索引
     * @param rowkey
     */
    public void delDoc(String rowkey) {
        try {
            server.deleteById(rowkey);
            server.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Solr查询
     *
     * @param fields
     */
    public List<Map<String, Object>> query(Map<String, String> fields) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        SolrQuery query = new SolrQuery();
        if (null == fields || fields.size() == 0) {
            query.setQuery("*:*");
        }
        fields.forEach((k, v) -> {
            query.set(QUERY, k + ":" + v);
        });
        try {
            QueryResponse response = server.query(query);
            response.getResults().forEach(doc -> {
                result.add(doc.getFieldValueMap());
            });
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return result;
    }

}