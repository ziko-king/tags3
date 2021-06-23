package cn.itcast.tag.web.engine.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据
 *
 * @author liuchengli
 */
public class MetaDataBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3933396572413054700L;

    private long id;
    // 标签ID
    private long tagId;
    // 输入方式：1hive、2hdfs、3hbase、4mysql
    private String inType;

    //============= Hive、RDBMS =============
    // 驱动：hive驱动、mysql驱动
    private String driver;
    // 地址：hiveUrl、mysqlUrl
    private String url;
    // 数据库用户名
    private String username;
    // 数据库密码
    private String password;
    // 表名称
    private String dbTable;
    // Hive SQL、RDBMS SQL
    private String sql;

    //============= HDFS、Local =============
    // 输入数据文件路径，如果是文件夹则读取该文件夹下所有文件，如果是文件则读取该文件
    private String inPath;
    // 字段分隔符
    private String sperator;
    // 输入字段
    private String inFields;
    // 条件字段
    private String condFields;
    // 输出字段
    private String outFields;
    // 输出数据文件路径
    private String outPath;

    //============= HBase =============
    // hbase.zookeeper.quorum=zk1,zk2,zk3
    private String zkHosts;
    // hbase.zookeeper.property.clientPort=2181
    private int zkPort = 2181;
    // HBase表
    private String hbaseTable;
    // HBase表列簇
    private String family;
    // 展示列名
    private String selectFieldNames;
    // 条件列名
    private String whereFieldNames;
    // 条件列值
    private String whereFieldValues;

    //============= 通用字段 =============
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 状态：0已禁用、1申请中、2开发中、3开发完成、4已上线、5已下线、6已删除
    private int state;
    // 备注
    private String remark;

    public MetaDataBean() {
    }

    public MetaDataBean(long tagId) {
        this.tagId = tagId;
    }

    public MetaDataBean set(int tagId, String remark) {
        this.tagId = tagId;
        this.remark = remark;
        return this;
    }

    /**
     * 构建HiveMeta
     *
     * @param driver
     * @param url
     * @param username
     * @param password
     * @param dbTable
     * @param sql
     * @return
     */
    public MetaDataBean buildHiveMeta(String driver, String url, String username, String password, String dbTable, String sql) {
        this.inType = "hive";
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.dbTable = dbTable;
        this.sql = sql;
        return this;
    }

    /**
     * 构建MySQLMeta
     *
     * @param driver
     * @param url
     * @param username
     * @param password
     * @param dbTable
     * @param sql
     * @return
     */
    public MetaDataBean buildMySQLMeta(String driver, String url, String username, String password, String dbTable, String sql) {
        this.inType = "mysql";
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.dbTable = dbTable;
        this.sql = sql;
        return this;
    }

    /**
     * 构建HDFSMeta
     *
     * @param inPath
     * @param sperator
     * @param inFields
     * @param condFields
     * @param outFields
     * @param outPath
     * @return
     */
    public MetaDataBean buildHDFSMeta(String inPath, String sperator, String inFields, String condFields, String outFields, String outPath) {
        this.inType = "hdfs";
        this.inPath = inPath;
        this.sperator = sperator;
        this.inFields = inFields;
        this.condFields = condFields;
        this.outFields = outFields;
        this.outPath = outPath;
        return this;
    }

    /**
     * 构建HBaseMeta
     *
     * @param inPath
     * @param sperator
     * @param inFields
     * @param condFields
     * @param outFields
     * @param outPath
     * @return
     */
    public MetaDataBean buildHBaseMeta(String zkHosts, int zkPort, String hbaseTable, String family, String selectFieldNames, String whereFieldNames, String whereFieldValues) {
        this.inType = "hbase";
        this.zkHosts = zkHosts;
        this.zkPort = zkPort;
        this.hbaseTable = hbaseTable;
        this.family = family;
        this.selectFieldNames = selectFieldNames;
        this.whereFieldNames = whereFieldNames;
        this.whereFieldValues = whereFieldValues;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbTable() {
        return dbTable;
    }

    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getInPath() {
        return inPath;
    }

    public void setInPath(String inPath) {
        this.inPath = inPath;
    }

    public String getSperator() {
        return sperator;
    }

    public void setSperator(String sperator) {
        this.sperator = sperator;
    }

    public String getInFields() {
        return inFields;
    }

    public void setInFields(String inFields) {
        this.inFields = inFields;
    }

    public String getCondFields() {
        return condFields;
    }

    public void setCondFields(String condFields) {
        this.condFields = condFields;
    }

    public String getOutFields() {
        return outFields;
    }

    public void setOutFields(String outFields) {
        this.outFields = outFields;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public int getZkPort() {
        return zkPort;
    }

    public void setZkPort(int zkPort) {
        this.zkPort = zkPort;
    }

    public String getHbaseTable() {
        return hbaseTable;
    }

    public void setHbaseTable(String hbaseTable) {
        this.hbaseTable = hbaseTable;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getSelectFieldNames() {
        return selectFieldNames;
    }

    public void setSelectFieldNames(String selectFieldNames) {
        this.selectFieldNames = selectFieldNames;
    }

    public String getWhereFieldNames() {
        return whereFieldNames;
    }

    public void setWhereFieldNames(String whereFieldNames) {
        this.whereFieldNames = whereFieldNames;
    }

    public String getWhereFieldValues() {
        return whereFieldValues;
    }

    public void setWhereFieldValues(String whereFieldValues) {
        this.whereFieldValues = whereFieldValues;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return id + "\t" + tagId + "\t" + inType + "\t" + driver + "\t" + url + "\t" + username + "\t" + password + "\t"
                + dbTable + "\t" + sql + "\t" + inPath + "\t" + sperator + "\t" + inFields + "\t" + condFields + "\t"
                + outFields + "\t" + outPath + "\t" + zkHosts + "\t" + zkPort + "\t" + hbaseTable + "\t" + family + "\t"
                + selectFieldNames + "\t" + whereFieldNames + "\t" + whereFieldValues + "\t" + ctime + "\t" + utime
                + "\t" + state + "\t" + remark;
    }

}
