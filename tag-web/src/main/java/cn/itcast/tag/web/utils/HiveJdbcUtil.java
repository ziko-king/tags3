package cn.itcast.tag.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * HiveJdbcUtil
 * core-site.xml配置hive连接用户通过超级代理(*)操作hadoop的用户组及主机
 * hadoop.proxyuser.hive.hosts=*
 * hadoop.proxyuser.hive.groups=*
 *
 * @author mengyao
 */
@Component
public class HiveJdbcUtil {

    private static HiveJdbcUtil hiveJdbcUtil;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${hive.driverClassName}")
    private String driverClassName = "org.apache.hive.jdbc.HiveDriver";
    @Value("${hive.url}")
    private String url = "jdbc:hive2://bjqt234.qt:10000/default?mapred.job.queue.name=default;hive.mapred.mode=nonstrict";
    @Value("${hive.username}")
    private String username = "hive";
    @Value("${hive.password}")
    private String password = "hive";
    private Connection connection;


    /**
     * 私有构造函数声明
     */
    private HiveJdbcUtil() {
        try {
            Class.forName(driverClassName);
            logger.info("==== loaded HiveServer2 driver:{} ====", driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取HiveJdbcUtil单例对象
     *
     * @return
     */
    public static HiveJdbcUtil getInstance() {
        if (null == hiveJdbcUtil) {
            hiveJdbcUtil = new HiveJdbcUtil();
        }
        return hiveJdbcUtil;
    }

    public static void main(String[] args) {
        HiveJdbcUtil instance = HiveJdbcUtil.getInstance();
        Connection connection_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection_ = instance.getConnection();
            ps = connection_.prepareStatement("select * from tbl_sales_volume limit 10");
            rs = ps.executeQuery();
            //System.out.println(instance.getTableInfo(rs));
            while (rs.next()) {
                System.out.println("==== " + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4) + "\t" + rs.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            instance.closeAll(rs, ps, null, connection_);
        }
    }

    /**
     * 获取HiveJdbc连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            logger.info("==== get HiveServer2 Connection:{} ====", connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 获取ResultSet结果中的表名及字段
     *
     * @param rs
     * @return
     */
    @Deprecated
    public Map<String, List<String>> getTableInfo(ResultSet rs) {
        Map<String, List<String>> tableInfo = new HashMap<String, List<String>>();
        String tableName = null;
        List<String> columns = new LinkedList<String>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            tableName = metaData.getTableName(1);
            int cols = metaData.getColumnCount();
            for (int i = 1; i < cols + 1; i++) {
                columns.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableInfo.put(tableName, columns);
        return tableInfo;
    }

    /**
     * 获取ResultSet结果中的字段
     *
     * @param rs
     * @return
     */
    public List<String> getCols(ResultSet rs) {
        List<String> columns = new LinkedList<String>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            for (int i = 1; i < cols + 1; i++) {
                columns.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    /**
     * 关闭连接
     *
     * @param rs
     * @param ps
     * @param st
     * @param connection
     */
    public void closeAll(ResultSet rs, PreparedStatement ps, Statement st, Connection connection) {
        try {
            if (null != rs) {
                rs.close();
                logger.info("==== closed HiveServer2 ResultSet ====");
            }
            if (null != ps) {
                ps.close();
                logger.info("==== closed HiveServer2 PreparedStatement ====");
            }
            if (null != st) {
                st.close();
                logger.info("==== closed HiveServer2 Statement ====");
            }
            if (null != connection) {
                connection.close();
                logger.info("==== closed HiveServer2 Connection ====");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
