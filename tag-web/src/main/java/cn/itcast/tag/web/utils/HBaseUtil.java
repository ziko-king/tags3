package cn.itcast.tag.web.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author liuchengli
 */
@Component
public class HBaseUtil {

    private static Connection connection;
    Logger logger = Logger.getLogger(getClass());
    //bjqt233.qt,bjqt232.qt,bjqt231.qt
    @Value("${hbase.zookeeper.quorum}")
    private String addr;//="bjqt244.qt:2181,bjqt245.qt:2181,bjqt246.qt:2181";
    @Value("${hbase.zookeeper.property.clientPort}")
    private String port = "2181";

    public HBaseUtil() {
        //getConnection();
    }

    public static void main(String[] args) throws IOException {
        HBaseUtil hBaseV1011Template = new HBaseUtil();
        hBaseV1011Template.getConnection();

        Map<String, Map<String, String>> keyValues = new HashMap<String, Map<String, String>>() {{
            put("2_haier", (Map) new HashMap<String, String>() {{
                put("name", "liupw");
            }});
            put("3_haier", (Map) new HashMap<String, String>() {{
                put("name", "zymichelle");
            }});
            put("4_haier", (Map) new HashMap<String, String>() {{
                put("name", "chinadgfy");
            }});
            put("5_haier", (Map) new HashMap<String, String>() {{
                put("name", "383421028");
            }});
            put("6_haier", (Map) new HashMap<String, String>() {{
                put("name", "ctt_65");
            }});
            put("7_haier", (Map) new HashMap<String, String>() {{
                put("name", "jimmy");
            }});
            put("8_haier", (Map) new HashMap<String, String>() {{
                put("name", "liyang79");
            }});
            put("9_haier", (Map) new HashMap<String, String>() {{
                put("name", "cindirila");
            }});
            put("10_haier", (Map) new HashMap<String, String>() {{
                put("name", "498883342");
            }});
            put("11_haier", (Map) new HashMap<String, String>() {{
                put("name", "陈龙平");
            }});
            put("12_haier", (Map) new HashMap<String, String>() {{
                put("name", "ruinajiaxing");
            }});
            put("13_haier", (Map) new HashMap<String, String>() {{
                put("name", "sngmdito");
            }});
            put("14_haier", (Map) new HashMap<String, String>() {{
                put("name", "44858603@qq.com");
            }});
            put("15_haier", (Map) new HashMap<String, String>() {{
                put("name", "2899999@qq.com");
            }});
            put("16_haier", (Map) new HashMap<String, String>() {{
                put("name", "32310367@qq.com");
            }});
            put("17_haier", (Map) new HashMap<String, String>() {{
                put("name", "1231231211@qq.com");
            }});
            put("18_haier", (Map) new HashMap<String, String>() {{
                put("name", "179769200@qq.com");
            }});
            put("19_haier", (Map) new HashMap<String, String>() {{
                put("name", "179769202@qq.com");
            }});
            put("20_haier", (Map) new HashMap<String, String>() {{
                put("name", "1234567@qq.com");
            }});
            put("21_haier", (Map) new HashMap<String, String>() {{
                put("name", "307840758@qq.com");
            }});
            put("22_haier", (Map) new HashMap<String, String>() {{
                put("name", "1464152022@qq.com");
            }});
            put("23_haier", (Map) new HashMap<String, String>() {{
                put("name", "788889878@qq.com");
            }});
            put("24_haier", (Map) new HashMap<String, String>() {{
                put("name", "8899999999");
            }});
            put("25_haier", (Map) new HashMap<String, String>() {{
                put("name", "13242134@qq.com");
            }});
            put("26_haier", (Map) new HashMap<String, String>() {{
                put("name", "402642697@qq.com");
            }});
            put("27_haier", (Map) new HashMap<String, String>() {{
                put("name", "464439931@qq.com");
            }});
            put("28_haier", (Map) new HashMap<String, String>() {{
                put("name", "23445232@qq.com");
            }});
            put("29_haier", (Map) new HashMap<String, String>() {{
                put("name", "2131237@qq.com");
            }});
            put("30_haier", (Map) new HashMap<String, String>() {{
                put("name", "981869175@qq.com");
            }});
            put("31_haier", (Map) new HashMap<String, String>() {{
                put("name", "814775297@qq.com");
            }});
            put("32_haier", (Map) new HashMap<String, String>() {{
                put("name", "141234124@qq.com");
            }});
            put("33_haier", (Map) new HashMap<String, String>() {{
                put("name", "4123125@qq.com");
            }});
            put("34_haier", (Map) new HashMap<String, String>() {{
                put("name", "41231225@qq.com");
            }});
            put("35_haier", (Map) new HashMap<String, String>() {{
                put("name", "3252162343@qq.com");
            }});
            put("36_haier", (Map) new HashMap<String, String>() {{
                put("name", "4151231@qq.com");
            }});
            put("37_haier", (Map) new HashMap<String, String>() {{
                put("name", "415123132@qq.com");
            }});
            put("38_haier", (Map) new HashMap<String, String>() {{
                put("name", "1412523423@qq.com");
            }});
            put("39_haier", (Map) new HashMap<String, String>() {{
                put("name", "132453532@qq.com");
            }});
            put("40_haier", (Map) new HashMap<String, String>() {{
                put("name", "125214452@qq.com");
            }});
            put("41_haier", (Map) new HashMap<String, String>() {{
                put("name", "1252144252@qq.com");
            }});
            put("42_haier", (Map) new HashMap<String, String>() {{
                put("name", "124124124@qq.com");
            }});
            put("43_haier", (Map) new HashMap<String, String>() {{
                put("name", "124124122@qq.com");
            }});
            put("44_haier", (Map) new HashMap<String, String>() {{
                put("name", "242352332@qq.com");
            }});
            put("45_haier", (Map) new HashMap<String, String>() {{
                put("name", "18560683520");
            }});
            put("46_haier", (Map) new HashMap<String, String>() {{
                put("name", "464439930@qq.com");
            }});
            put("47_haier", (Map) new HashMap<String, String>() {{
                put("name", "464439932@qq.com");
            }});
            put("48_haier", (Map) new HashMap<String, String>() {{
                put("name", "123456123@qq.com");
            }});
            put("49_haier", (Map) new HashMap<String, String>() {{
                put("name", "133185212@qq.com");
            }});
            put("50_haier", (Map) new HashMap<String, String>() {{
                put("name", "1451231231@qq.com");
            }});
            put("51_haier", (Map) new HashMap<String, String>() {{
                put("name", "12451313@qq.com");
            }});
            put("52_haier", (Map) new HashMap<String, String>() {{
                put("name", "1512312512@qq.com");
            }});
            put("53_haier", (Map) new HashMap<String, String>() {{
                put("name", "2451231362@qq.com");
            }});
            put("54_haier", (Map) new HashMap<String, String>() {{
                put("name", "1254123125@qq.com");
            }});
            put("55_haier", (Map) new HashMap<String, String>() {{
                put("name", "1451231@qq.com");
            }});
            put("56_haier", (Map) new HashMap<String, String>() {{
                put("name", "241512312@qq.com");
            }});
            put("57_haier", (Map) new HashMap<String, String>() {{
                put("name", "1235123523@qq.com");
            }});
            put("58_haier", (Map) new HashMap<String, String>() {{
                put("name", "2514511231@qq.com");
            }});
            put("59_haier", (Map) new HashMap<String, String>() {{
                put("name", "2145132342@qq.com");
            }});
            put("60_haier", (Map) new HashMap<String, String>() {{
                put("name", "1351254@qq.com");
            }});
            put("61_haier", (Map) new HashMap<String, String>() {{
                put("name", "1231412313@qq.com");
            }});
            put("62_haier", (Map) new HashMap<String, String>() {{
                put("name", "1254123123@qq.com");
            }});
            put("63_haier", (Map) new HashMap<String, String>() {{
                put("name", "12345141@qq.com");
            }});
            put("64_haier", (Map) new HashMap<String, String>() {{
                put("name", "12415133@qq.com");
            }});
            put("65_haier", (Map) new HashMap<String, String>() {{
                put("name", "2135154121@qq.com");
            }});
            put("66_haier", (Map) new HashMap<String, String>() {{
                put("name", "14536722@qq.com");
            }});
            put("67_haier", (Map) new HashMap<String, String>() {{
                put("name", "235723546@qq.com");
            }});
            put("68_haier", (Map) new HashMap<String, String>() {{
                put("name", "3215526242@qq.com");
            }});
            put("69_haier", (Map) new HashMap<String, String>() {{
                put("name", "1245123213@qq.com");
            }});
            put("70_haier", (Map) new HashMap<String, String>() {{
                put("name", "415161313@qq.com");
            }});
            put("71_haier", (Map) new HashMap<String, String>() {{
                put("name", "2451235131@qq.com");
            }});
            put("72_haier", (Map) new HashMap<String, String>() {{
                put("name", "12234341@qq.com");
            }});
            put("73_haier", (Map) new HashMap<String, String>() {{
                put("name", "1234123131@qq.com");
            }});
        }};
        addRows("tbl_tag_user_profile", null, keyValues);

//	    //TableName tableName = TableName.valueOf("portrait");
//        //Table table = connection.getTable(tableName);
//
//        Map<String,Map<String,String>> mapRow = new HashMap<>();
//        Map<String,String> map1 = new HashMap<>();
//        map1.put("name", "张三");
//        map1.put("idnum", "420922199008174270");
//        map1.put("phonenum", "13146048010");
//        map1.put("banknum", "6217002710000684874");
//        map1.put("gender", "1");
//        map1.put("basictagids", "14,15");
//        map1.put("mergetagids", "23");
//        map1.put("qq", "876590834");
//        map1.put("email", "876590834@qq.com");
//        map1.put("type", "0");
//        //0 人 1物
//
//        String row1 = UUID.randomUUID().toString()+"_personOrObjInfo";
//
//        Map<String,String> map2 = new HashMap<>();
//        map2.put("name", "李四");
//        map2.put("idnum", "420922199008174271");
//        map2.put("phonenum", "13146048011");
//        map2.put("banknum", "6217002710000684875");
//        map2.put("gender", "0");
//        map2.put("basictagids", "14,15");
//        map2.put("mergetagids", "23");
//        map2.put("qq", "876590835");
//        map2.put("email", "876590835@qq.com");
//        map2.put("type", "1");
//        String row2 = UUID.randomUUID().toString()+"_personOrObjInfo";
//
//        Map<String,String> map3 = new HashMap<>();
//        map3.put("name", "王五");
//        map3.put("idnum", "420922199008174272");
//        map3.put("phonenum", "13146048013");
//        map3.put("banknum", "6217002710000684876");
//        map3.put("gender", "1");
//        map3.put("basictagids", "14,15");
//        map3.put("mergetagids", "23");
//        map3.put("qq", "876590836");
//        map3.put("email", "876590836@qq.com");
//        map3.put("type", "0");
//        String row3 = UUID.randomUUID().toString()+"_personOrObjInfo";
//
//        Map<String,String> map4 = new HashMap<>();
//        map4.put("name", "赵六");
//        map4.put("idnum", "420922199008174273");
//        map4.put("phonenum", "13146048014");
//        map4.put("banknum", "6217002710000684877");
//        map4.put("gender", "1");
//        map4.put("basictagids", "14,15");
//        map4.put("mergetagids", "23");
//        map4.put("qq", "876590837");
//        map4.put("email", "876590837@qq.com");
//        map4.put("type", "1");
//        String row4 = UUID.randomUUID().toString()+"_personOrObjInfo";
//
//        Map<String,String> map5 = new HashMap<>();
//        map5.put("name", "孙八");
//        map5.put("idnum", "420922199008174274");
//        map5.put("phonenum", "13146048015");
//        map5.put("banknum", "6217002710000684878");
//        map5.put("gender", "0");
//        map5.put("basictagids", "14,15");
//        map5.put("mergetagids", "23");
//        map5.put("qq", "876590838");
//        map5.put("email", "876590838@qq.com");
//        map5.put("type", "0");
//        String row5 = UUID.randomUUID().toString()+"_personOrObjInfo";
//
//        mapRow.put(row1, map1);
//        mapRow.put(row2, map2);
//        mapRow.put(row3, map3);
//        mapRow.put(row4, map4);
//        mapRow.put(row5, map5);
//
//        Collection<Map<String, String>> a =   HBaseUtil.queryForQuilfier("portrait","personOrObjInfo", "basictagids", "192");
//        for (Map<String, String> map : a) {
//        	//System.out.println("#######################");
//        	//for (String q : map.keySet()) {
//        		System.out.println("====="+map.get("rowKey"));
//        		System.out.println("====="+map.get("basictagids"));
//        		deleteByRowKey("portrait",map.get("rowKey"));
//        	//}
//		}

        //addRows("portrait","_",mapRow);
        /*Scan scan = new Scan();
        FilterList filterList = fun2(tags,"tag4ids");
        scan.setFilter(filterList);
        ResultScanner rs = table.getScanner(scan);

	     Map<String, String> row = new HashMap<String,String>();
	     List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
	     for (Result r : rs) {
				Cell[] cells = r.rawCells();
				for (Cell cell : cells) {
					String rowKey = new String(CellUtil.cloneRow(cell));
					row.put("timestamp", cell.getTimestamp() + "");
					row.put("rowKey", rowKey);
					row.put("family", new String(CellUtil.cloneFamily(cell)));
					row.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
					rows.add(row);
				}
				System.out.println(row);
		}*/
        /*Map<String, String> a =  queryForRowKey("portrait","2cc8dc45-2531-445a-9b8f-547559338feb","personOrObjInfo");

        System.out.println(a.get("name"));*/


//        long i = HBaseUtil.queryForQuilfierCount("portrait","personOrObjInfo", "basictagids", "1");
//        System.out.println("count==============="+i);
        //deleteByRowKey("portrait","a3692553-1a69-4f15-b034-b0ea743cb3cc");
//        deleteByRowKey("portrait","cdec61a4-b943-4a29-a483-c565ef68f482");
//        deleteByRowKey("portrait","ea8ef383-e53d-4c26-9bd5-f8c1b231e7d3");
//        deleteByRowKey("portrait","a6ae2e00-5794-4ecb-918a-7e4db7215b4b");
//        deleteByRowKey("portrait","d192c03f-9cb1-43dc-a4e5-aa45f054b63f");

        hBaseV1011Template.close();
//		readTxtFile("E:\\aa\\txtvalueReplace.txt");

//		//单行添加
//		Map<String, String> keyValue = new HashMap<>();
//		keyValue.put("name", "xiaogang");
//		keyValue.put("sex", "男");
//		keyValue.put("addr", "北京");
//		add("test", "0005", "info", keyValue);
//
//		//多行添加
//		Map<String, Map<String, String>> keyValues = new HashMap<String, Map<String, String>>();
//		Map<String, String> keyValue1 = new HashMap<>();
//		keyValue1.put("name", "ligang");
//		keyValue1.put("sex", "男");
//		keyValue1.put("addr", "北京");
//		keyValues.put("0008_info", keyValue1);
//		Map<String, String> keyValue2 = new HashMap<>();
//		keyValue2.put("name", "zhaojun");
//		keyValue2.put("sex", "男");
//		keyValue2.put("addr", "北京");
//		keyValues.put("0009_info", keyValue2);
//		adds("test", "_", keyValues);
//
//		//查询所有
//		System.out.println("1:"+queryForScan("tbl_shopuser", "info"));
//		List<Map<String, String>> list = queryForScan("tbl_shopuser", "info");
//		for (Map<String, String> bean : list) {
//			System.out.println(bean.toString());
//		}

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

    public static void insert() throws IOException {
        Map<String, Map<String, String>> dataMap = new HashMap<>();
        String basicTag1 = "14";
        String basicTag2 = "15";
        String basicTag3 = "18";
        String basicTag4 = "21";

        String mergeTag1 = "23";

        Map<String, String> tagMap1 = new HashMap<>();
        tagMap1.put("basictag-" + basicTag1, basicTag1);
        tagMap1.put("basictag-" + basicTag2, basicTag2);
        tagMap1.put("basictag-" + basicTag3, basicTag3);
        tagMap1.put("basictag-" + basicTag4, basicTag4);
        tagMap1.put("mergetag-" + mergeTag1, mergeTag1);
        Map<String, String> userMap1 = new HashMap<>();
        userMap1.put("name", "张三");
        userMap1.put("idnum", "420922199008174270");
        userMap1.put("phonenum", "15810469847");
        userMap1.put("banknum", "6217002710000684874");
        userMap1.put("gender", "1");
        userMap1.put("qq", "847594757");
        userMap1.put("email", "847594757@qq.com");
        userMap1.put("type", "0");
        String rowKey1 = UUID.randomUUID().toString();
        dataMap.put(rowKey1 + "_personOrObjInfo", userMap1);
        dataMap.put(rowKey1 + "_tag", tagMap1);

        Map<String, String> tagMap2 = new HashMap<>();
        tagMap2.put("basictag-" + basicTag1, basicTag1);
        tagMap2.put("basictag-" + basicTag2, basicTag2);
        tagMap2.put("basictag-" + basicTag3, basicTag3);
        tagMap2.put("basictag-" + basicTag4, basicTag4);
        tagMap2.put("mergetag-" + mergeTag1, mergeTag1);
        Map<String, String> userMap2 = new HashMap<>();
        userMap2.put("name", "李四");
        userMap2.put("idnum", "420922199008174271");
        userMap2.put("phonenum", "15810469848");
        userMap2.put("banknum", "6217002710000684875");
        userMap2.put("gender", "1");
        userMap2.put("qq", "847594758");
        userMap2.put("email", "847594758@qq.com");
        userMap2.put("type", "0");
        String rowKey2 = UUID.randomUUID().toString();
        dataMap.put(rowKey2 + "_personOrObjInfo", userMap2);
        dataMap.put(rowKey2 + "_tag", tagMap2);

        Map<String, String> tagMap3 = new HashMap<>();
        tagMap3.put("basictag-" + basicTag1, basicTag1);
        tagMap3.put("basictag-" + basicTag2, basicTag2);
        tagMap3.put("basictag-" + basicTag3, basicTag3);
        tagMap3.put("basictag-" + basicTag4, basicTag4);
        tagMap3.put("mergetag-" + mergeTag1, mergeTag1);
        Map<String, String> userMap3 = new HashMap<>();
        userMap3.put("name", "王五");
        userMap3.put("idnum", "420922199008174272");
        userMap3.put("phonenum", "15810469849");
        userMap3.put("banknum", "6217002710000684876");
        userMap3.put("gender", "1");
        userMap3.put("qq", "847594759");
        userMap3.put("email", "847594759@qq.com");
        userMap3.put("type", "0");
        String rowKey3 = UUID.randomUUID().toString();
        dataMap.put(rowKey3 + "_personOrObjInfo", userMap3);
        dataMap.put(rowKey3 + "_tag", tagMap3);

        addRows("portrait", "_", dataMap);

    }

    public static void delete() throws IOException {

        Collection<Map<String, String>> a = queryForQuilfier("portrait", "personOrObjInfo", "basictag-14", "14");
        for (Map<String, String> map : a) {
            System.out.println("=====" + map.get("rowKey"));
            deleteByRowKey("portrait", map.get("rowKey"));
        }
    }

    public static void main1(String[] args) throws IOException {
        //HBaseUtil hBaseUtil = new HBaseUtil();
        List<String> result = HBaseUtil.queryForQuilfierName("portrait", "tag", "basictag-14", 5);
        System.out.println(result);
        delete();
        insert();
        //hBaseUtil.qualifierFilter();
//		List<String> list = new ArrayList<>();
//		list.add("1");
//		list.add("2");
//		list.add("3");
//		list.add("4");
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(i+"=="+list.size());
//			String cur = list.get(i);
//			if (i+1 == list.size()) {
//				break;
//			}
//			String nex = list.get(i+1);
//			System.out.println(cur+"----"+nex);
//			list.remove(cur);
//		}
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
    private static void addRow(String tableName, String rowKey, String family, Map<String, String> keyValue) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        for (Entry<String, String> entry : keyValue.entrySet()) {
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
        }
        table.put(put);
        table.close();
        keyValue.clear();
    }

    public static void addRows(String tableName, String rowFamilySeparator, Map<String, Map<String, String>> keyValues) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
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
     * 单行删除
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @throws IOException
     */
    public static void deleteByRowKey(String tableName, String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);
        table.close();
    }


    /*
     * 判断表是否存在
     *
     *
     * @tableName 表名
     */

    /**
     * 查询所有
     *
     * @param tableName
     * @param family
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> queryForScan(String tableName, String family) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
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
     * 查询所有rowKey
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public static List<String> queryAllRowKeyForScan(String tableName) throws IOException {
        List<String> result = new ArrayList<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static List<Map<String, String>> queryForScan(String tableName) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static List<Map<String, String>> queryForTimeRange(String tableName, String family, long minStamp, long maxStamp) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static Map<String, String> queryForRowKey(String tableName, String rowKey, String family) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static List<Map<String, String>> queryForRowKeys(String tableName, List<String> rowKeys, String family) throws IOException {
        List<Map<String, String>> resultList = new ArrayList<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
        List<Get> getList = new ArrayList();
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
    public static List<Map<String, String>> queryForRowKeyRange(String tableName, String family, String startRow, String stopRow) throws IOException {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static Collection<Map<String, String>> queryForQuilfier(String tableName, String family, String column, String value) throws IOException {
        Map<String, Map<String, String>> rows = new HashMap<String, Map<String, String>>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static Collection<Map<String, String>> queryForQuilfierExactly(String tableName, String family, String column, String value) throws IOException {
        Map<String, Map<String, String>> rows = new HashMap<String, Map<String, String>>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static List<String> queryForQuilfierName(String tableName, String qualifier, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static List<String> queryForMultiQuilfierName(String tableName, String family, Map<String, String> qualifierMap, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static List<String> queryForMultiQuilfierNameOr(String tableName, String family, Map<String, String> qualifierMap, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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

    public static void qualifierFilter() throws IOException {
        Table mTable = connection.getTable(TableName.valueOf("portrait"));
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
    public static List<String> queryForQuilfierName(String tableName, String family, String qualifier, long pageSize) throws IOException {
        Set<String> rowKeys = new HashSet<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
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
    public static long queryForQuilfierCount(String tableName, String family, String column, String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
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

    public static void updateQualifier(String tableName, String rowKey, String family, String qualifier, String value) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            put.setDurability(Durability.SYNC_WAL);
            table.put(put);
            table.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getConnection() {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.10.20");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("zookeeper.znode.parent", "/hbase-unsecure");
        HBaseHelper hBaseHelper = new HBaseHelper(conf);
        try {
            connection = hBaseHelper.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createConnection() {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", addr);
        conf.set("hbase.zookeeper.property.clientPort", port);
        conf.set("zookeeper.znode.parent", "/hbase-unsecure");
        HBaseHelper hBaseHelper = new HBaseHelper(conf);
        try {
            connection = hBaseHelper.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HBaseUtil build() {
        createConnection();
        return this;
    }

    public boolean isExist(String tableName) throws IOException {
        TableName table_name = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        boolean exit = admin.tableExists(table_name);
        admin.close();
        return exit;
    }

    /*
     * 关闭连接
     *
     */
    public void close() {
        /**
         * close connection
         **/
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
