package cn.itcast.model.tools.hbase;

import cn.itcast.model.tools.solr.SolrTools;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 当新增、删除用户持有的标签时，会同步索引到solr中，仅同步用户id，手机、身份证号和邮箱
 * Created by mengyao
 * 2018年6月2日
 */
public class HBaseSolrIndexCoprocesser extends BaseRegionObserver {

    private final Logger logger = LoggerFactory.getLogger(HBaseSolrIndexCoprocesser.class);

    private final String[] qualifiers = new String[]{"userId", "tagIds"};
    private final String family = "user";
    private SolrTools solrTools = SolrTools.build();

    public static void main(String[] args) throws IOException {
        //配置
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.10.20");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("zookeeper.znode.parent", "/hbase-unsecure");
        //连接
        Connection conn = ConnectionFactory.createConnection(conf);
        HTable table = (HTable) conn.getTable(TableName.valueOf("tbl_profile"));
        // 写入数据
        for (int i = 1; i < 5; i++) {
            Put put = new Put(Bytes.toBytes("000" + i));
            put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("userId"), Bytes.toBytes("000" + i));
            put.addColumn(Bytes.toBytes("user"), Bytes.toBytes("tagIds"), Bytes.toBytes("1,2"));
            Cell c1 = put.get("user".getBytes(), "userId".getBytes()).get(0);
            Cell c2 = put.get("user".getBytes(), "tagIds".getBytes()).get(0);
            System.out.println(Bytes.toString(CellUtil.cloneQualifier(c1)) + "=" + Bytes.toString(CellUtil.cloneValue(c1)));
            System.out.println(Bytes.toString(CellUtil.cloneQualifier(c2)) + "=" + Bytes.toString(CellUtil.cloneValue(c2)));
            //System.out.println(put.toMap());
            table.put(put);
        }
        // 关闭资源
        table.close();
        conn.close();

    }

    /**
     * Put操作后同步索引到Solr中
     *
     * @param context
     * @param put
     * @param edit
     * @param durability
     * @throws IOException
     */
    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> context, Put put, WALEdit edit,
                        Durability durability) throws IOException {
        String row = Bytes.toString(put.getRow());
        SolrInputDocument doc = new SolrInputDocument();
        for (String qualifier : qualifiers) {
            List<Cell> list = put.get(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            if (list.size() > 0) {
                Cell cell = list.get(0);
                String qualifierName = Bytes.toString(CellUtil.cloneQualifier(cell));
                String qualifierValue = Bytes.toString(CellUtil.cloneValue(cell));
                if (qualifierName.equals(qualifiers[0])) {
                    doc.addField(solrTools.ID, qualifierValue);
                } else {
                    doc.addField(qualifierName, qualifierValue);
                }
                logger.info("==== Get post put row:{}, family:{}, qualifierName:{}, qualifierValue:{} ====", row, family, qualifierName, qualifierValue);
            } else {
                logger.info("==== Table field get faild! ====");
            }
        }
        solrTools.checker();
        solrTools.addDoc(doc);
    }

    /**
     * Delete操作后清除Solr中的索引
     */
    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> context, Delete delete, WALEdit edit,
                           Durability durability) throws IOException {
        String rowkey = Bytes.toString(delete.getRow());
        solrTools.checker();
        solrTools.delDoc(rowkey);
        logger.info("==== Delete solr index: {} ====", rowkey);
    }

}