package cn.itcast.model.tools.hbase;

import cn.itcast.model.models.ModelConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * HBase工具类
 * Created by mengyao
 * 2018年6月2日
 */
public class HBaseTools {

    private static HBaseTools hBaseTools;
    private final String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    private final String ZOOKEEPER_CLIENT_PORT = "hbase.zookeeper.property.clientPort";
    private final String ZOOKEEPER_ZNODE_PARENT = "zookeeper.znode.parent";
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModelConfig modelConfig = new ModelConfig();
    private Configuration conf;
    private Connection connection;


    private HBaseTools() {
        initializer();
    }

    public static HBaseTools build() {
        if (null == hBaseTools) {
            hBaseTools = new HBaseTools();
        }
        return hBaseTools;
    }

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {
        HBaseTools build = HBaseTools.build();

        System.out.println(build.isExist("tbl_orders"));
//
//		//根据rowKey查询
//		System.out.println("2:"+queryForRowKey("tbl_shopuser", "5", "info"));
//
//		//根据时间戳范围查询（默认包钱不包后）
//		System.out.println("3:"+queryForTimeRange("test", "info", 1492510703521L, 1492664183955L));
//
//		//根据rowKey查询（默认包前不包后）
//		System.out.println("4:"+queryForRowKeyRange("test", "info", "0001", "0003"));
//
//		//根据指定列名和列值查询1
//		System.out.println("5:"+queryForQuilfier("test", "info", "name", "xiaoming"));
//
//		//根据指定列名和列值查询1
//		System.out.println("6:"+queryForQuilfier("test", "info", "sex", "男"));
//
//		//根据指定列名和列值查询1
//		System.out.println("7:"+queryForQuilfier("tbl_shopuser", "info", "mobile", "18560683520"));
//
//		//根据rowKey删除
//		deleteByRowKey("test", "0005", "info");

    }

    /**
     * 初始化工具类配置
     */
    private void initializer() {
        conf = HBaseConfiguration.create();
        conf.set(HBASE_ZOOKEEPER_QUORUM, modelConfig.getZkHosts());
        conf.set(ZOOKEEPER_CLIENT_PORT, modelConfig.getZkPort() + "");
        conf.set(ZOOKEEPER_ZNODE_PARENT, modelConfig.getZooKeeperZNodeParent());
        logger.info("==== Initializer HBase Configuration:{} ====", conf);
    }

    public Connection getConnection() {
        try {
            if (null == connection || connection.isClosed()) {
                connection = ConnectionFactory.createConnection(conf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean isExist(String tableName) throws IOException {
        TableName table_name = TableName.valueOf(tableName);
        Admin admin = getConnection().getAdmin();
        boolean exit = admin.tableExists(table_name);
        admin.close();
        return exit;
    }

    /**
     * 创建表
     *
     * @param table
     * @param familys
     * @return
     * @throws IOException
     */
    public boolean ifNotTable(String table, String... familys) throws IOException {
        TableName table_name = TableName.valueOf(table);
        Admin admin = getConnection().getAdmin();
        boolean exit = admin.tableExists(table_name);
        if (!exit) {
            HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(table));
            for (int i = 0; i < familys.length; i++) {
                desc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(desc);
        }
        admin.close();
        return exit;
    }

    /**
     * 单行添加
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @param keyValue
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private void addRow(String tableName, String rowKey, String family, Map<String, String> keyValue) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        for (Entry<String, String> entry : keyValue.entrySet()) {
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
        }
        table.put(put);
        table.close();
        keyValue.clear();
    }

    /**
     * 多行添加
     *
     * @param tableName
     * @param rowFamilySeparator
     * @param keyValues
     * @throws IOException
     */
    public void addRows_(String tableName, String rowFamilySeparator, Map<String, Map<String, String>> keyValues) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        List<Put> puts = new ArrayList<Put>();
        for (Entry<String, Map<String, String>> entry : keyValues.entrySet()) {
            String key = entry.getKey();
            if (null == rowFamilySeparator || rowFamilySeparator.isEmpty()) {
                rowFamilySeparator = "_";
            }
            String rowKey = key.split(rowFamilySeparator)[0];
            String family = key.split(rowFamilySeparator)[1];
            Map<String, String> keyValue = entry.getValue();
            Put put = new Put(Bytes.toBytes(rowKey), System.currentTimeMillis());
            for (Entry<String, String> entry2 : keyValue.entrySet()) {
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(entry2.getKey()), Bytes.toBytes(entry2.getValue()));
            }
            puts.add(put);
        }
        table.put(puts);
        table.close();
        keyValues.clear();
    }

    /**
     * 多行添加
     *
     * @param tableName
     * @param family
     * @param keyValues(Map<String,Map<String,String>> keyValues = <rowkey, <qualifiers,values>>)
     * @throws IOException
     */
    public void addRows(String tableName, String family, Map<String, Map<String, String>> keyValues) {
        Table table = null;
        try {
            table = getConnection().getTable(TableName.valueOf(tableName));
            List<Put> puts = new ArrayList<Put>();
            for (Entry<String, Map<String, String>> entry : keyValues.entrySet()) {
                String rowKey = entry.getKey();
                Map<String, String> keyValue = entry.getValue();
                Put put = new Put(Bytes.toBytes(rowKey), System.currentTimeMillis());
                for (Entry<String, String> entry2 : keyValue.entrySet()) {
                    put.addColumn(Bytes.toBytes(family), Bytes.toBytes(entry2.getKey()), Bytes.toBytes(entry2.getValue()));
                }
                puts.add(put);
            }
            table.put(puts);
            table.close();
            keyValues.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != table) {
                    table.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单行删除
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @throws IOException
     */
    public void deleteByRowKey(String tableName, String rowKey) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);
        table.close();
    }

    /**
     * 查询所有
     *
     * @param tableName
     * @param family
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> queryForScan(String tableName, String family) {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = null;
        ResultScanner rs = null;
        try {
            table = getConnection().getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            rs = table.getScanner(scan);
            Map<String, String> row = null;
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                row = new HashMap<String, String>();
                row.put("row", Bytes.toString(r.getRow()));
                for (Cell cell : cells) {
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
                }
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
                if (null != table) {
                    table.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    /**
     * 查询所有
     *
     * @param tableName
     * @param family
     * @return
     * @throws IOException
     */
    public Map<String, Map<String, String>> scan(String tableName, String family) {
        Map<String, Map<String, String>> rows = new HashMap<String, Map<String, String>>();
        Table table = null;
        ResultScanner rs = null;
        try {
            table = getConnection().getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            rs = table.getScanner(scan);
            Map<String, String> row = null;
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                row = new HashMap<String, String>();
                for (Cell cell : cells) {
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
                }
                rows.put(Bytes.toString(r.getRow()), row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
                if (null != table) {
                    table.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    /**
     * 查询所有rowKey
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public List<String> queryAllRowKeyForScan(String tableName) throws IOException {
        List<String> result = new ArrayList<>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                if (null == cells || cells.length <= 0) {
                    continue;
                }
                Cell cell = cells[0];
                String rowKey = new String(CellUtil.cloneRow(cell));
                result.add(rowKey);
            }
        } finally {
            rs.close();
        }
        return result;
    }

    /**
     * 查询所有字段
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> queryForScan(String tableName) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner rs = table.getScanner(scan);
        Map<String, String> row = null;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                row = new HashMap<String, String>();
                for (Cell cell : cells) {
                    row.put("rowKey", new String(CellUtil.cloneRow(cell)));
                    row.put("family", new String(CellUtil.cloneFamily(cell)));
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
                }
                rows.add(row);
            }
        } finally {
            rs.close();
        }
        return rows;
    }

    /**
     * 根据时间范围
     *
     * @param tableName
     * @param family
     * @param minStamp
     * @param maxStamp
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> queryForTimeRange(String tableName, String family, long minStamp, long maxStamp) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
        scan.setTimeRange(minStamp, maxStamp);
        ResultScanner rs = table.getScanner(scan);
        Map<String, String> row = null;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                row = new HashMap<String, String>();
                for (Cell cell : cells) {
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
                }
                rows.add(row);
            }
        } finally {
            rs.close();
        }
        return rows;
    }

    /**
     * 根据RowKey查询
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @return
     * @throws IOException
     */
    public Map<String, String> queryForRowKey(String tableName, String rowKey, String family) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addFamily(Bytes.toBytes(family));
        Scan scan = new Scan(get);
        ResultScanner rs = table.getScanner(scan);
        Map<String, String> row = null;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                row = new HashMap<String, String>();
                for (Cell cell : cells) {
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell), "UTF-8"));
                }
            }
        } finally {
            rs.close();
        }
        return row;
    }

    /**
     * 根据多个RowKey查询
     *
     * @param tableName
     * @param rowKeys
     * @param family
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> queryForRowKeys(String tableName, List<String> rowKeys, String family) throws IOException {
        List<Map<String, String>> resultList = new ArrayList<>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        List<Get> getList = new ArrayList<Get>();
        for (String rowKey : rowKeys) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(family));
            getList.add(get);
        }
        Result[] results = table.get(getList);
        for (Result result : results) {//对返回的结果集进行操作
            Map<String, String> row = new HashMap<String, String>();
            for (Cell kv : result.rawCells()) {
                row.put(new String(CellUtil.cloneQualifier(kv)), new String(CellUtil.cloneValue(kv), "UTF-8"));
            }
            resultList.add(row);
        }
        return resultList;
    }

    /**
     * 根据RowKey范围查询
     *
     * @param tableName
     * @param family
     * @param startRow
     * @param stopRow
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> queryForRowKeyRange(String tableName, String family, String startRow, String stopRow) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(stopRow));
        ResultScanner rs = table.getScanner(scan);
        Map<String, String> row = null;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                row = new HashMap<String, String>();
                for (Cell cell : cells) {
                    row.put("timestamp", cell.getTimestamp() + "");
                    row.put("rowKey", new String(CellUtil.cloneRow(cell)));
                    row.put("family", new String(CellUtil.cloneFamily(cell)));
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
                }
                rows.add(row);
            }
        } finally {
            rs.close();
        }
        return rows;
    }

    /**
     * 根据指定列名匹配列值
     *
     * @param tableName
     * @param family
     * @param qualifier
     * @param value
     * @return
     * @throws IOException
     */
    public Collection<Map<String, String>> queryForQuilfier(String tableName, String family, String column, String value) throws IOException {
        Map<String, Map<String, String>> rows = new HashMap<String, Map<String, String>>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        SubstringComparator comp = new SubstringComparator(value);
        SingleColumnValueFilter filter = new SingleColumnValueFilter(family.getBytes(), column.getBytes(), CompareOp.EQUAL, comp);
        filter.setFilterIfMissing(true);
        PageFilter p = new PageFilter(5);
        scan.setFilter(filter);
        scan.setFilter(p);
        ResultScanner rs = table.getScanner(scan);
        Map<String, String> row = null;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                for (Cell cell : cells) {
                    String rowKey = new String(CellUtil.cloneRow(cell));
                    if (null == row || !rows.containsKey(rowKey)) {
                        row = new HashMap<String, String>();
                    }
                    row.put("timestamp", cell.getTimestamp() + "");
                    row.put("rowKey", rowKey);
                    row.put("family", new String(CellUtil.cloneFamily(cell)));
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell), "UTF-8"));
                    rows.put(rowKey, row);
                }
            }
        } finally {
            rs.close();
        }
        return rows.values();
    }

    /**
     * 根据指定列名完全匹配列值
     *
     * @param tableName
     * @param family
     * @param qualifier
     * @param value
     * @return
     * @throws IOException
     */
    public Collection<Map<String, String>> queryForQuilfierExactly(String tableName, String family, String column, String value) throws IOException {
        Map<String, Map<String, String>> rows = new HashMap<String, Map<String, String>>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        SingleColumnValueFilter filter = new SingleColumnValueFilter(family.getBytes(), column.getBytes(), CompareOp.EQUAL, value.getBytes());
        filter.setFilterIfMissing(true);
        scan.setFilter(filter);
        ResultScanner rs = table.getScanner(scan);
        Map<String, String> row = null;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                for (Cell cell : cells) {
                    String rowKey = new String(CellUtil.cloneRow(cell));
                    if (null == row || !rows.containsKey(rowKey)) {
                        row = new HashMap<String, String>();
                    }
                    row.put("timestamp", cell.getTimestamp() + "");
                    row.put("rowKey", rowKey);
                    row.put("family", new String(CellUtil.cloneFamily(cell)));
                    row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell), "UTF-8"));
                    rows.put(rowKey, row);
                }
            }
        } finally {
            rs.close();
        }
        return rows.values();
    }

    /**
     * 获取列名匹配的rowkey
     *
     * @param tableName
     * @param qualifier 列名
     * @param pageSize  数量
     * @return
     * @throws IOException
     */
    public List<String> queryForQuilfierName(String tableName, String qualifier, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new QualifierFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(qualifier)));
        PageFilter pageFilter = new PageFilter(pageSize);
        FilterList filterList = new FilterList();
        filterList.addFilter(filter);
        filterList.addFilter(pageFilter);
        scan.setFilter(filterList);
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                for (Cell cell : cells) {
                    String rowKey = new String(CellUtil.cloneRow(cell));
                    rowKeys.add(rowKey);
                }
            }
        } finally {
            rs.close();
        }
        List<String> rows = new ArrayList<>(rowKeys);
        return rows;
    }

    /**
     * 获取多个列值匹配的rowkey
     *
     * @param tableName
     * @param family
     * @param qualifierMap 列名:列值
     * @param pageSize     数量
     * @return
     * @throws IOException
     */
    public List<String> queryForMultiQuilfierName(String tableName, String family, Map<String, String> qualifierMap, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        for (Entry<String, String> entry : qualifierMap.entrySet()) {
            SingleColumnValueFilter filter = new SingleColumnValueFilter(family.getBytes(), entry.getKey().getBytes(), CompareOp.EQUAL, entry.getValue().getBytes());
            filter.setFilterIfMissing(true);
            filterList.addFilter(filter);
        }
        PageFilter pageFilter = new PageFilter(pageSize);
        filterList.addFilter(pageFilter);
        scan.setFilter(filterList);
        scan.addFamily(Bytes.toBytes(family));
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                for (Cell cell : cells) {
                    String rowKey = new String(CellUtil.cloneRow(cell));
                    rowKeys.add(rowKey);
                }
            }
        } finally {
            rs.close();
        }
        List<String> rows = new ArrayList<>(rowKeys);
        return rows;
    }

    /**
     * 获取多个列值匹配的rowkey(多条件间关联关系为Or)MUST_PASS_ONE
     *
     * @param tableName
     * @param family
     * @param qualifierMap 列名:列值
     * @param pageSize     数量
     * @return
     * @throws IOException
     */
    public List<String> queryForMultiQuilfierNameOr(String tableName, String family, Map<String, String> qualifierMap, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        for (Entry<String, String> entry : qualifierMap.entrySet()) {
            SingleColumnValueFilter filter = new SingleColumnValueFilter(family.getBytes(), entry.getKey().getBytes(), CompareOp.EQUAL, entry.getValue().getBytes());
            filter.setFilterIfMissing(true);
            filterList.addFilter(filter);
        }
        PageFilter pageFilter = new PageFilter(pageSize);
        filterList.addFilter(pageFilter);
        scan.setFilter(filterList);
        scan.addFamily(Bytes.toBytes(family));
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                for (Cell cell : cells) {
                    String rowKey = new String(CellUtil.cloneRow(cell));
                    rowKeys.add(rowKey);
                }
            }
        } finally {
            rs.close();
        }
        List<String> rows = new ArrayList<>(rowKeys);
        return rows;
    }

    public void qualifierFilter() throws IOException {
        Table mTable = getConnection().getTable(TableName.valueOf("portrait"));
        QualifierFilter columnsNameFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator("basictag-14".getBytes()));
        Scan scan = new Scan();
        scan.setFilter(columnsNameFilter);
        ResultScanner rs = mTable.getScanner(scan);
        for (Result r = rs.next(); r != null; r = rs.next()) {
            Cell[] cells = r.rawCells();
            for (Cell cell : cells) {
                System.out.println("==== " + new String(CellUtil.cloneRow(cell)) + "\t" +
                        new String(CellUtil.cloneFamily(cell)) + "\t" +
                        new String(CellUtil.cloneQualifier(cell)) + "\t" +
                        new String(CellUtil.cloneValue(cell)));
            }
        }
    }

    /**
     * 获取列名匹配的rowkey
     *
     * @param tableName
     * @param qualifier 列名
     * @param pageSize  数量
     * @return
     * @throws IOException
     */
    public List<String> queryForQuilfierName(String tableName, String family, String qualifier, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(qualifier.getBytes()));
        PageFilter pageFilter = new PageFilter(pageSize);
        FilterList list = new FilterList();
        list.addFilter(pageFilter);
        list.addFilter(qualifierFilter);
        scan.addFamily(Bytes.toBytes(family));
        scan.setFilter(list);
        System.out.println(scan.toJSON());
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                Cell[] cells = r.rawCells();
                for (Cell cell : cells) {
                    System.out.println("==== " + new String(CellUtil.cloneRow(cell)) + "\t" +
                            new String(CellUtil.cloneFamily(cell)) + "\t" +
                            new String(CellUtil.cloneQualifier(cell)) + "\t" +
                            new String(CellUtil.cloneValue(cell)));
                    String rowKey = new String(CellUtil.cloneRow(cell));
                    rowKeys.add(rowKey);
                }
            }
        } finally {
            rs.close();
        }
        List<String> rows = new ArrayList<>(rowKeys);
        return rows;
    }

    /**
     * 根据指定列名匹配列值条数
     *
     * @param tableName
     * @param family
     * @param qualifier
     * @param value
     * @return
     * @throws IOException
     */
    public long queryForQuilfierCount(String tableName, String family, String column, String value) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        SubstringComparator comp = new SubstringComparator(value);
        SingleColumnValueFilter filter = new SingleColumnValueFilter(family.getBytes(), column.getBytes(), CompareOp.EQUAL, comp);
        filter.setFilterIfMissing(true);
        scan.setFilter(filter);
        ResultScanner rs = table.getScanner(scan);
        long count = 0;
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                count++;
            }
        } finally {
            rs.close();
        }
        return count;
    }

    public void updateQualifier(String tableName, String rowKey, String family, String qualifier, String value) {
        try {
            Table table = getConnection().getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            put.setDurability(Durability.SYNC_WAL);
            table.put(put);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 关闭连接
     *
     */
    public void close() {
        if (connection != null || !connection.isClosed()) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}