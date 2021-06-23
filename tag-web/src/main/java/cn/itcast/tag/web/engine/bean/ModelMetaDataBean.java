package cn.itcast.tag.web.engine.bean;

import java.io.Serializable;

/**
 * 模型和元数据混合bean
 *
 * @author liuchengli
 */
public class ModelMetaDataBean implements Serializable {
    private static final long serialVersionUID = 2712657255658653988L;

    // 标签ID
    private long tagId;
    private String modelName;
    // 算法模型主程序入口
    private String modelMain;
    // 算法模型的地址
    private String modelPath;
    // 更新周期:0无、1每天、2每周、3每月、4每年
    private String scheTime;
    // 输入方式：1hive、2hdfs、3mysql、4oracle、5
    private String inType;
    // 驱动：hive驱动、hdfs、mysql驱动、oracle驱动
    private String driver;
    // 地址：hiveUrl、hdfsUrl、mysqlUrl、oracleUrl
    private String url;
    // 数据库和hdfs用户名
    private String username;
    // 数据库和hdfs的密码
    private String password;
    // 如果输入方式为hdfs则为文件名、数据库则使用表名称
    private String schemaOrFile;
    // 如果hdfs则指定分隔符，数据库为sql
    private String speratorOrsql;
    // 输入字段（sql时为空）
    private String inFields;
    // 条件字段（sql时为空）
    private String condFields;
    // 输出字段（sql时为空）
    private String outFields;
    // args 传递给模型的参数:key=value...
    private String args;
    private String business;

    public ModelMetaDataBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ModelMetaDataBean(long tagId, String modelName, String modelMain, String modelPath, String scheTime,
                             String inType, String driver, String url, String username, String password, String schemaOrFile,
                             String speratorOrsql, String inFields, String condFields, String outFields, String args, String business) {
        super();
        this.tagId = tagId;
        this.modelName = modelName;
        this.modelMain = modelMain;
        this.modelPath = modelPath;
        this.scheTime = scheTime;
        this.inType = inType;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.schemaOrFile = schemaOrFile;
        this.speratorOrsql = speratorOrsql;
        this.inFields = inFields;
        this.condFields = condFields;
        this.outFields = outFields;
        this.args = args;
        this.business = business;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelMain() {
        return modelMain;
    }

    public void setModelMain(String modelMain) {
        this.modelMain = modelMain;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getScheTime() {
        return scheTime;
    }

    public void setScheTime(String scheTime) {
        this.scheTime = scheTime;
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

    public String getSchemaOrFile() {
        return schemaOrFile;
    }

    public void setSchemaOrFile(String schemaOrFile) {
        this.schemaOrFile = schemaOrFile;
    }

    public String getSperatorOrsql() {
        return speratorOrsql;
    }

    public void setSperatorOrsql(String speratorOrsql) {
        this.speratorOrsql = speratorOrsql;
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

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @Override
    public String toString() {
        return tagId + "\t" + modelName + "\t" + modelMain + "\t" + modelPath + "\t" + scheTime + "\t" + inType
                + "\t" + driver + "\t" + url + "\t" + username + "\t" + password + "\t" + schemaOrFile + "\t"
                + speratorOrsql + "\t" + inFields + "\t" + condFields + "\t" + outFields + "\t" + args + "\t" + business;
    }


}
