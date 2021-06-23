package cn.itcast.tag.web.commons.parser;

import cn.itcast.tag.web.engine.bean.MetaDataBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengyao
 */
public class MetaParser {

    private static Logger logger = LoggerFactory.getLogger(MetaParser.class.getSimpleName());


    /**
     * 规则列表
     *
     * @param ruleItems
     * @return
     */
    @SuppressWarnings("serial")
    private static Map<String, String> ruleToMap(List<String> ruleItems) {
        Map<String, String> rule = new HashMap<String, String>() {
            {
                ruleItems.forEach(kv -> {
                    String[] keyValue = kv.split("=");
                    put(keyValue[0], keyValue[1]);
                });
            }
        };
        return rule;
    }

    /**
     * 规则字符串转换为相关parser类处理
     *
     * @param conf
     * @return
     */
    public static MetaParser getParser(String rule) {
        if (null == rule) {
            logger.info("==== The rule is null! ====");
            return null;
        }
        List<String> lines = Arrays.asList(rule.split("\n"));
        if (null == lines) {
            logger.info("==== The rule lines is null! ====");
            return null;
        } else {
            Map<String, String> ruleMap = ruleToMap(lines);
            String typeStr = ruleMap.get("inType");
            if (StringUtils.isEmpty(typeStr)) {
                logger.info("==== The rule map no type field! ====");
            } else {
                Type type = Type.valueOf(typeStr.trim().toUpperCase());
                if (type == Type.MYSQL) {
                    return new MySQLParser(ruleMap);
                }
                if (type == Type.HIVE) {
                    return new HiveParser(ruleMap);
                }
                if (type == Type.HDFS) {
                    return new HDFSParser(ruleMap);
                }
                if (type == Type.HBASE) {
                    return new HBaseParser(ruleMap);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String rule1 = "inType=hive\r\n" +
                "driver=org.apache.hive.jdbc.HiveDriver\r\n" +
                "url=jdbc:hive2://bjqt234.qt:10000/default?mapred.job.queue.name=default;hive.mapred.mode=nonstrict\r\n" +
                "username=root\r\n" +
                "password=123456\r\n" +
                "dbTable=tbl_sales_volume\r\n" +
                "sql=select * from tbl_sales_volume\r\n";

        String rule2 = "inType=mysql\r\n" +
                "driver=com.jdbc.mysql.Driver\r\n" +
                "url=jdbc:mysql:///tags\r\n" +
                "username=root\r\n" +
                "password=123456\r\n" +
                "dbTable=tbl_sales_volume\r\n" +
                "sql=select * from tbl_sales_volume\r\n";

        String rule3 = "inType=hdfs\r\n" +
                "inPath=/test_data/1/\r\n" +
                "sperator=\\t\r\n" +
                "inFields=a,b,c,d,e\r\n" +
                "condFields=a,c,d\r\n" +
                "outFields=a1,b1,b2" +
                "outPath=/test_data/out/";

        String rule4 = "inType=hbase\r\n" +
                "zkHosts=zk1,zk2,zk3\r\n" +
                "zkPort=2181\n" +
                "hbaseTable=tbl_orders\r\n" +
                "family=info\r\n" +
                "selectFieldNames=utime\r\n" +
                "whereFieldNames=utime\r\n" +
                "whereFieldValues=2019-05-10\r\n";

        System.out.println(MetaParser.getParser(rule1).getMeta());
        System.out.println(MetaParser.getParser(rule2).getMeta());
        System.out.println(MetaParser.getParser(rule3).getMeta());
        System.out.println(MetaParser.getParser(rule4).getMeta());
    }

    public MetaDataBean getMeta() {
        return null;
    }

    public boolean is() {
        return false;
    }

    enum Type {
        HDFS, HIVE, HBASE, MYSQL
    }

}

