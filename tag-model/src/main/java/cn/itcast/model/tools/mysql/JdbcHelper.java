package cn.itcast.model.tools.mysql;

import java.sql.*;
import java.util.LinkedList;

/**
 * MySQL Jdbc Helper
 *
 * @author mengyao
 */
@SuppressWarnings("all")
public class JdbcHelper {

    private Connection connection;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    private String driverClass = "com.mysql.jdbc.Driver";
    private String url;
    private String user;
    private String password;


    public JdbcHelper() {
        initial();
    }

    public JdbcHelper(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public JdbcHelper(String driverClass, String url, String user, String password) {
        this(url, user, password);
        this.driverClass = driverClass;
    }

    public static void main(String[] args) {
        LinkedList<String> list = null;
        JdbcHelper jdbcHelper = new JdbcHelper("jdbc:mysql:///test", "root", "123456");

        list = jdbcHelper.list("SELECT * FROM test.tbl_gwpup");
        for (String line : list) {
            System.out.println(line);
        }

        list = jdbcHelper.list("SELECT * FROM test.tbl_gwpup WHERE time=? AND pv_count=?", "##", "1479967094262", "2");
        for (String line : list) {
            System.out.println(line);
        }
    }

    /**
     * 加载MySQL驱动
     */
    private void initial() {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取MySQL连接对象
     *
     * @param driverClass
     * @param url
     * @param user
     * @param password
     * @return
     */
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 查询数据库表
     *
     * @param sql
     * @return
     */
    public LinkedList<String> list(String sql) {
        return list(sql, null, null);
    }

    /**
     * 查询数据库表
     *
     * @param sql
     * @param lineFieldDelimiter
     * @param params
     * @return
     */
    public LinkedList<String> list(String sql, String lineFieldDelimiter, String... params) {
        LinkedList<String> results = new LinkedList<String>();
        Connection connection_ = null;
        PreparedStatement ps_ = null;
        ResultSet rs_ = null;
        try {
            if (null == sql || "".equals(sql)) {
                return null;
            }
            if (null == lineFieldDelimiter || "".equals(lineFieldDelimiter)) {
                lineFieldDelimiter = "\t";
            }
            connection_ = getConnection();
            ps_ = connection_.prepareStatement(sql);
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    String param = params[i];
                    if (null != param) {
                        ps_.setString(i + 1, param);
                    }
                }
            }
            rs_ = ps_.executeQuery();
            LinkedList<String> colNames = getColumnNames(rs_);
            StringBuilder stringBuilder = new StringBuilder();
            while (rs_.next()) {
                stringBuilder.setLength(0);
                for (int i = 1; i <= colNames.size(); i++) {
                    String columnValue = rs_.getString(i);
                    stringBuilder.append(columnValue + lineFieldDelimiter);
                }
                results.add(stringBuilder.toString().trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs_, ps_, null, connection_);
        }

        return results;
    }

    /**
     * 获取表列名
     *
     * @param rs_
     * @return
     * @throws SQLException
     */
    private LinkedList<String> getColumnNames(ResultSet rs_) throws SQLException {
        LinkedList<String> colNames = null;
        ResultSetMetaData metaData = rs_.getMetaData();
        int cols = metaData.getColumnCount();
        if (cols > 0) {
            colNames = new LinkedList<String>();
            for (int i = 1; i <= cols; i++) {
                colNames.add(metaData.getColumnName(i));
            }
        }
        return colNames;
    }

    /**
     * 释放数据库连接资源
     *
     * @param rs
     * @param ps
     * @param st
     * @param connection
     */
    public void close(ResultSet rs, PreparedStatement ps, Statement st, Connection connection) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (st != null) {
                st.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}