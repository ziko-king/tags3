package cn.itcast.model.models;

import cn.itcast.model.utils.DateUtil;
import cn.itcast.model.utils.PropertiesUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.spark.SparkConf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 标签模型全局配置类
 * Created by mengyao
 * 2018年6月1日
 */
public class ModelConfig {

    public static final Properties PROP = PropertiesUtil.getProperties();
    // Model Config
    public static final String MODEL_DEBUG = "tag.model.debug";
    public static final String SPARK_DEBUG_MASTER = "spark.debug.master";
    public static final String HADOOP_HOME_DIR = "hadoop.home.dir";
    public static final String MODEL_BASE_PATH = "tag.model.base.path";
    // Profile table Config
    public static final String PROFILE_TABLE_NAME = "profile.hbase.table.name";
    public static final String PROFILE_TABLE_FAMILY_USER = "profile.hbase.table.family.user";
    public static final String PROFILE_TABLE_FAMILY_USER_ALIAS = "profile.hbase.table.family.user.alias";
    public static final String PROFILE_TABLE_FAMILY_USER_COL = "profile.hbase.table.family.user.col";
    public static final String PROFILE_TABLE_FAMILY_ITEM = "profile.hbase.table.family.item";
    public static final String PROFILE_TABLE_FAMILY_ITEM_ALIAS = "profile.hbase.table.family.item.alias";
    public static final String PROFILE_TABLE_FAMILY_ITEM_COL = "profile.hbase.table.family.item.col";
    public static final String PROFILE_TABLE_COMMON_COL = "profile.hbase.table.family.common.col";
    // MySQL Config
    public static final String MYSQL_JDBC_DRIVER = "mysql.jdbc.driver";
    public static final String MYSQL_JDBC_URL = "mysql.jdbc.url";
    public static final String MYSQL_JDBC_USERNAME = "mysql.jdbc.username";
    public static final String MYSQL_JDBC_PASSWORD = "mysql.jdbc.password";
    // HDFS Config
    public static final String DEFAULT_FS = "fs.defaultFS";
    public static final String FS_USER = "fs.user";
    // HBase Source Config
    public static final String HBASE_SOURCE = "tag.spark.sql.hbase";
    public static final String ZOOKEEPER_ZNODE_PARENT = "zookeeper.znode.parent";
    public static final String HBASE_FS_TMP_DIR = "hbase.fs.tmp.dir";
    public static final String ZK_HOSTS = "zkHosts";
    public static final String ZK_PORT = "zkPort";
    public static final String HBASE_TABLE = "hbaseTable";
    public static final String HBASE_TABLE_FAMILY = "family";
    public static final String HBASE_TABLE_SELECT_FIELDS = "selectFields";
    public static final String HBASE_TABLE_WHERE_FIELDS = "whereFields";
    // Spark Config
    public static final String MASTER = "spark.master";
    public static final String DEPLOY_MODE = "spark.deploy.mode";
    public static final String DRIVER_MEMORY = "spark.driver.memory";
    public static final String SPARK_SERIALIZER = "spark.serializer";
    public static final String KRYO_SERIALIZER_BUFFER = "spark.kryoserializer.buffer";
    public static final String KRYO_SERIALIZER_BUFFER_MAX = "spark.kryoserializer.buffer.max";
    public static final String SPARK_SQL_WAREHOUSE_DIR = "spark.sql.warehouse.dir";
    public static final String SPARK_SQL_FILES_MAX_PARTITION_BYTES = "spark.sql.files.maxPartitionBytes";
    public static final String SPARK_SQL_FILES_OPEN_COST_IN_BYTES = "spark.sql.files.openCostInBytes";
    public static final String SPARK_SQL_SHUFFLE_PARTITIONS = "spark.sql.shuffle.partitions";
    public static final String SPARK_SQL_AUTO_BROADCAST_JOIN = "spark.sql.autoBroadcastJoinThreshold";
    // Solr Config
    public static final String SOLR_CLOUD_MODEL = "solr.cloud.model";
    public static final String SOLR_ADDR = "solr.addr";
    public static final String SOLR_PROFILE_FIELDS = "solr.profile.fields";
    public static final String SOLR_PROFILE_FAMILY = "solr.profile.family";
    // Oozie Config
    public static final String OOZIE_ADDR = "oozie.addr";
    public static final String OOZIE_NAMENODE = "nameNode";
    public static final String OOZIE_JOB_TRACKER = "jobTracker";
    public static final String OOZIE_QUEUE_NAME = "queueName";
    public static final String OOZIE_USE_SYS_LIBPATH = "oozie.use.system.libpath";
    public static final String OOZIE_WF_APP_PATH = "oozie.wf.application.path";
    public static final String OOZIE_MASTER = "master";
    public static final String OOZIE_JAR_PATH = "jarPath";
    public static final String OOZIE_LIB_PATH = "oozie.libpath";
    public static final String OOZIE_SPARK_OPTS = "sparkOpts";
    public static final String OOZIE_MAIN_CLASS = "mainClass";
    public static String fullRegex = "(.*?)\\[(.*?)\\](.*+)";
    private SparkConf sparkConf;
    private HashMap<String, String> options;


    public ModelConfig() {
        initializer();
    }

    /**
     * 当whereFileds条件为time[EQ|GT|GE|NE|LT|LE]conditionTime
     *
     * @param conditionTime(${number}${y|m|d}) ≥0.1  (0.5y = 近半年)
     *                                         ≥1~≤12(1m   = 一个月)
     *                                         ≥1    (60d  = 60天)
     * @param sdf                              格式化
     * @param isTs                             是否将最终返回值转换为时间戳
     * @return
     */
    public static String getConditionTimeValue(String conditionTime, SimpleDateFormat sdf, boolean isTs) {
        String[] vals = new String[2];
        if (conditionTime.endsWith("y")) {//0.1 ~ (this year - 1970)
            vals[0] = conditionTime.substring(0, conditionTime.length() - 1);
            vals[1] = conditionTime.substring(conditionTime.length() - 1);
            double dt = Double.parseDouble(vals[0]);
            if (dt <= 1) {
                dt = dt * DateUtil.getCurYear();
            } else {
                dt = dt * 365;
            }
            Date date = DateUtil.getRecentOfDays((int) dt);
            return isTs == true ? date.getTime() + "" : sdf.format(date);
        }
        if (conditionTime.endsWith("m")) {//1 ~ 12
            vals[0] = conditionTime.substring(0, conditionTime.length() - 1);
            vals[1] = conditionTime.substring(conditionTime.length() - 1);
            int dt = Integer.parseInt(vals[0]);
            Date date = DateUtil.getRecentOfMonths(dt);
            return isTs == true ? date.getTime() + "" : sdf.format(date);
        }
        if (conditionTime.endsWith("d")) {//≥1
            vals[0] = conditionTime.substring(0, conditionTime.length() - 1);
            vals[1] = conditionTime.substring(conditionTime.length() - 1);
            int dt = Integer.parseInt(vals[0]);
            Date date = DateUtil.getRecentOfDays(dt);
            return isTs == true ? date.getTime() + "" : sdf.format(date);
        }
        return null;
    }

    /**
     * 初始化SparkConf和SparkSQL配置
     */
    @SuppressWarnings("serial")
    private void initializer() {
//		this.sparkConf = new SparkConf();
//		if (isDebug()) {
//			sparkConf.set(MASTER, PROP.getProperty(SPARK_DEBUG_MASTER));
//		} else {
//			sparkConf.set(MASTER, PROP.getProperty(MASTER))
//				.set(DEPLOY_MODE, PROP.getProperty(DEPLOY_MODE))
//				;
//		}
//		sparkConf.set(DRIVER_MEMORY, PROP.getProperty(DRIVER_MEMORY))
//			.set(SPARK_SERIALIZER, PROP.getProperty(SPARK_SERIALIZER))
//			.set(KRYO_SERIALIZER_BUFFER, PROP.getProperty(KRYO_SERIALIZER_BUFFER))
//			.set(KRYO_SERIALIZER_BUFFER_MAX, PROP.getProperty(KRYO_SERIALIZER_BUFFER_MAX))
//			.set(SPARK_SQL_WAREHOUSE_DIR, PROP.getProperty(SPARK_SQL_WAREHOUSE_DIR))//SparkSQL依赖的hive仓库地址
//			.set(SPARK_SQL_FILES_MAX_PARTITION_BYTES, PROP.getProperty(SPARK_SQL_FILES_MAX_PARTITION_BYTES))//SparkSQL读取文件数据时打包到一个分区的最大字节数
//			.set(SPARK_SQL_FILES_OPEN_COST_IN_BYTES, PROP.getProperty(SPARK_SQL_FILES_OPEN_COST_IN_BYTES))//当SparkSQL读取的文件中有大量小文件时，小于该值的文件将被合并处理，默认4M，此处设置为128M
//			.set(SPARK_SQL_SHUFFLE_PARTITIONS, PROP.getProperty(SPARK_SQL_SHUFFLE_PARTITIONS))//SparkSQL运行shuffle的并行度
//			.set(SPARK_SQL_AUTO_BROADCAST_JOIN, PROP.getProperty(SPARK_SQL_AUTO_BROADCAST_JOIN))//设置为64M，执行join时自动广播小于该值的表，默认10M
//			;

        this.options = new HashMap<String, String>() {{
            put(HBASE_SOURCE, PROP.getProperty(HBASE_SOURCE));
            put(ZK_HOSTS, PROP.getProperty(ZK_HOSTS));
            put(ZK_PORT, PROP.getProperty(ZK_PORT));
        }};
    }

    /**
     * 获取模型基础路径
     *
     * @return
     */
    public String getModelBasePath() {
        return get(MODEL_BASE_PATH);
    }

    /**
     * 画像表
     *
     * @return
     */
    public String getProfileTableName() {
        return get(PROFILE_TABLE_NAME);
    }

    /**
     * 画像表-用户画像列簇
     *
     * @return
     */
    public String getProfileTableFamilyUser() {
        return get(PROFILE_TABLE_FAMILY_USER);
    }

    /**
     * 画像表-用户画像列簇-RowKey前缀
     *
     * @return
     */
    public String getProfileTableFamilyUserAlias() {
        return get(PROFILE_TABLE_FAMILY_USER_ALIAS);
    }

    /**
     * 画像表-用户画像列簇-userId列
     *
     * @return
     */
    public String getProfileFamilyUserCol() {
        return get(PROFILE_TABLE_FAMILY_USER_COL);
    }

    /**
     * 画像表-物品画像列簇
     *
     * @return
     */
    public String getProfileTableFamilyItem() {
        return get(PROFILE_TABLE_FAMILY_ITEM);
    }

    /**
     * 画像表-物品画像列簇-RowKey前缀
     *
     * @return
     */
    public String getProfileTableFamilyItemAlias() {
        return get(PROFILE_TABLE_FAMILY_ITEM_ALIAS);
    }

    /**
     * 画像表-物品画像列簇-itemId列
     *
     * @return
     */
    public String getProfileFamilyItemCol() {
        return get(PROFILE_TABLE_FAMILY_ITEM_COL);
    }

    /**
     * 画像表-用户和物品画像列簇的公共列-tagIds
     *
     * @return
     */
    public String getProfileCommonCol() {
        return get(PROFILE_TABLE_COMMON_COL);
    }

    /**
     * 获取MySQL驱动
     *
     * @return
     */
    public String getMySQLDriver() {
        return get(MYSQL_JDBC_DRIVER);
    }

    /**
     * 获取MySQL连接url
     *
     * @return
     */
    public String getMySQLUrl() {
        return get(MYSQL_JDBC_URL);
    }

    /**
     * 获取MySQL用户名
     *
     * @return
     */
    public String getMySQLUsername() {
        return get(MYSQL_JDBC_USERNAME);
    }

    /**
     * 获取MySQL密码
     *
     * @return
     */
    public String getMySQLPassword() {
        return get(MYSQL_JDBC_PASSWORD);
    }

    /**
     * 获取操作HDFS的用户
     *
     * @return
     */
    public String getDefaultFS() {
        return get(DEFAULT_FS);
    }

    /**
     * 获取操作HDFS的用户
     *
     * @return
     */
    public String getHDFSUser() {
        return get(FS_USER);
    }

    /**
     * 获取zk集群地址
     *
     * @return
     */
    public String getZkHosts() {
        return get(ZK_HOSTS);
    }

    /**
     * 获取ZNode
     *
     * @return
     */
    public String getZooKeeperZNodeParent() {
        return get(ZOOKEEPER_ZNODE_PARENT);
    }

    /**
     * 获取zk的端口
     *
     * @return
     */
    public int getZkPort() {
        return getInt(ZK_PORT);
    }

    /**
     * 获取HBase在hdfs上的tmp dir
     *
     * @return
     */
    public String getHBaseFsTmpDir() {
        return get(HBASE_FS_TMP_DIR);
    }

    /**
     * 获取SparkConf
     *
     * @return
     */
    public SparkConf getSparkConf() {
        if (null == sparkConf) {
            this.sparkConf = new SparkConf();
            if (isDebug()) {
                sparkConf.set(MASTER, PROP.getProperty(SPARK_DEBUG_MASTER));
            } else {
                sparkConf.set(MASTER, PROP.getProperty(MASTER))
                        .set(DEPLOY_MODE, PROP.getProperty(DEPLOY_MODE))
                ;
            }
            sparkConf.set(DRIVER_MEMORY, PROP.getProperty(DRIVER_MEMORY))
                    .set(SPARK_SERIALIZER, PROP.getProperty(SPARK_SERIALIZER))
                    .set(KRYO_SERIALIZER_BUFFER, PROP.getProperty(KRYO_SERIALIZER_BUFFER))
                    .set(KRYO_SERIALIZER_BUFFER_MAX, PROP.getProperty(KRYO_SERIALIZER_BUFFER_MAX))
                    .set(SPARK_SQL_WAREHOUSE_DIR, PROP.getProperty(SPARK_SQL_WAREHOUSE_DIR))//SparkSQL依赖的hive仓库地址
                    .set(SPARK_SQL_FILES_MAX_PARTITION_BYTES, PROP.getProperty(SPARK_SQL_FILES_MAX_PARTITION_BYTES))//SparkSQL读取文件数据时打包到一个分区的最大字节数
                    .set(SPARK_SQL_FILES_OPEN_COST_IN_BYTES, PROP.getProperty(SPARK_SQL_FILES_OPEN_COST_IN_BYTES))//当SparkSQL读取的文件中有大量小文件时，小于该值的文件将被合并处理，默认4M，此处设置为128M
                    .set(SPARK_SQL_SHUFFLE_PARTITIONS, PROP.getProperty(SPARK_SQL_SHUFFLE_PARTITIONS))//SparkSQL运行shuffle的并行度
                    .set(SPARK_SQL_AUTO_BROADCAST_JOIN, PROP.getProperty(SPARK_SQL_AUTO_BROADCAST_JOIN))//设置为64M，执行join时自动广播小于该值的表，默认10M
            ;
        }
        return sparkConf;
    }

    /**
     * 在debug模式下，使用本地指定的hadoop进行测试
     */
    public void dev() {
        System.setProperty(HADOOP_HOME_DIR, PROP.getProperty(HADOOP_HOME_DIR));
    }

    /**
     * 获取测试模式的SparkMaster = local[*]
     *
     * @return
     */
    public String getSparkMaster() {
        if (isDebug()) {
            return get(SPARK_DEBUG_MASTER);
        }
        return get(MASTER);
    }

    /**
     * 获取自定义SparkSQL数据源实现类
     *
     * @return
     */
    public String getFormat() {
        return get(HBASE_SOURCE);
    }

    /**
     * 初始化SparkSQL的options，不包括查询条件
     *
     * @param table
     * @param family
     * @param selectFields
     * @return
     */
    public Map<String, String> getOptions(String table, String family, String selectFields) {
        options.put(HBASE_TABLE, table);
        options.put(HBASE_TABLE_FAMILY, family);
        options.put(HBASE_TABLE_SELECT_FIELDS, selectFields);
        return options;
    }

    /**
     * 初始化SparkSQL的options，带有查询条件
     *
     * @param table
     * @param family
     * @param selectFields
     * @param whereFields
     * @return
     */
    public Map<String, String> getOptions(String table, String family, String selectFields, String whereFields) {
        options.put(HBASE_TABLE, table);
        options.put(HBASE_TABLE_FAMILY, family);
        options.put(HBASE_TABLE_SELECT_FIELDS, selectFields);
        options.put(HBASE_TABLE_WHERE_FIELDS, whereFields);
        return options;
    }

    /**
     * 是否使用debug模式
     *
     * @return
     */
    public boolean isDebug() {
        return getBoolean(MODEL_DEBUG);
    }

    /**
     * 获取创建Solr索引的HBase Profile表字段
     *
     * @return
     */
    public String getSolrProfileFields() {
        return get(SOLR_PROFILE_FIELDS);
    }

    /**
     * 获取创建Solr索引的HBase Profile表列簇
     *
     * @return
     */
    public String getSolrProfileFamily() {
        return get(SOLR_PROFILE_FAMILY);
    }

    /**
     * Solr的部署模式，true为SlorCloud模式，false为单机部署模式
     *
     * @return
     */
    public boolean isSolrCloudMode() {
        return getBoolean(SOLR_CLOUD_MODEL);
    }

    /**
     * 获取单机模式的Solr地址：例如http://host:8983/solr
     *
     * @return
     */
    public String getSolrAddr() {
        return get(SOLR_ADDR);
    }

    /**
     * 构建OozieConfig
     *
     * @return
     */
    public Properties getOozieConfig() {
        Properties conf = new Properties();
        conf.setProperty("oozieUrl", get(OOZIE_ADDR));
        conf.setProperty("nameNode", get(OOZIE_NAMENODE));
        conf.setProperty("jobTracker", get(OOZIE_JOB_TRACKER));
        conf.setProperty("queueName", get(OOZIE_QUEUE_NAME));
        conf.setProperty("oozie.use.system.libpath", get(OOZIE_USE_SYS_LIBPATH));
//		conf.setProperty("oozie.action.sharelib.for.spark", );
//		conf.setProperty("oozie.rerun.fail.nodes", reRunFailNodes);
        conf.setProperty("oozie.libpath", get(OOZIE_LIB_PATH));
        conf.setProperty("oozieSparkjobMaster", get(OOZIE_MASTER));
//		conf.setProperty("oozieSparkjobMode", mode);
//		conf.setProperty("oozieWorkflowName", wfName);
//		conf.setProperty("oozieWorkflowAppPath", wfAppPath);
//		conf.setProperty("oozieSparkjobJar", sparkjobJar);
//		conf.setProperty("oozieSparkjobMain", sparkjobMain);
//		conf.setProperty("oozieSparkjobOptions", sparkjobOpts);
        return conf;
    }

    /**
     * 获取oozie地址
     *
     * @return
     */
    public String getOozieAddr() {
        return get(OOZIE_ADDR);
    }

    /**
     * 根据key获取字符串类型的value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return PROP.getProperty(key);
    }

    /**
     * 根据key获取boolean类型的value
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return Boolean.valueOf(PROP.getProperty(key));
    }

    /**
     * 根据key获取byte类型的value
     *
     * @param key
     * @return
     */
    public byte getByte(String key) {
        byte value = 0;
        try {
            value = Byte.valueOf(PROP.getProperty(key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据key获取int类型的value
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        int value = 0;
        try {
            value = Integer.valueOf(PROP.getProperty(key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据key获取float类型的value
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        float value = 0f;
        try {
            value = Float.valueOf(PROP.getProperty(key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据key获取double类型的value
     *
     * @param key
     * @return
     */
    public double getDouble(String key) {
        double value = 0d;
        try {
            value = Double.valueOf(PROP.getProperty(key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据key获取long类型的value
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        long value = 0l;
        try {
            value = Long.valueOf(PROP.getProperty(key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 为字符串生成MD5
     *
     * @param str
     * @return
     */
    public String Str2MD5(String str) {
        return DigestUtils.md5Hex(str);
    }

}
