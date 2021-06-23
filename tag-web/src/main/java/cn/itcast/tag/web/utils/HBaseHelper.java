package cn.itcast.tag.web.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author liuchengli
 */
public class HBaseHelper {
    private static final String QUORUM = "hbase.zookeeper.quorum";
    private static final String ZK_PORT = "hbase.zookeeper.property.clientPort";
    private String quorum;
    private String zkPort;
    private Configuration conf;

    public HBaseHelper(Configuration conf) {
        this.conf = conf;
    }

    public HBaseHelper(String quorum, String zkPort) {
        this.quorum = quorum;
        this.zkPort = zkPort;
    }

    public HBaseHelper(Configuration conf, String quorum, String zkPort) {
        this.conf = conf;
        this.quorum = quorum;
        this.zkPort = zkPort;
    }

    /**
     * @param conf
     * @return
     * @throws IOException
     */
    public Connection getConnection() throws IOException {
        if (null == conf) {
            conf = HBaseConfiguration.create();
            conf.set(QUORUM, quorum);
            conf.set(ZK_PORT, zkPort);
        }
        if (null == conf.get(QUORUM) || conf.get(QUORUM).isEmpty()) {
            conf.set(QUORUM, quorum);
        }
        if (null == conf.get(ZK_PORT) || conf.get(ZK_PORT).isEmpty()) {
            conf.set(ZK_PORT, zkPort);
        }
        return ConnectionFactory.createConnection(conf);

    }

    /**
     * @param connection
     * @throws IOException
     */
    public void closeAll(Connection connection) throws IOException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

    public String getQuorum() {
        return quorum;
    }

    public void setQuorum(String quorum) {
        this.quorum = quorum;
    }

    public String getZkPort() {
        return zkPort;
    }

    public void setZkPort(String zkPort) {
        this.zkPort = zkPort;
    }
}
