package cn.itcast.tag.web.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * operate hdfs file or directory util class
 * 通过客户端操作HDFS
 *
 * @author zhangwenguo
 */
public class HdfsUtil {

    private static HdfsUtil hdfsUtil;
    private Configuration conf;
    private String fsAddr = "fs.defaultFS";
    private String user = "hdfs";
    private Logger logger = LoggerFactory.getLogger(getClass());

    private HdfsUtil() {
        if (conf == null) {
            conf = new Configuration();
        }
    }

    public static HdfsUtil getInstance() {
        if (null == hdfsUtil) {
            hdfsUtil = new HdfsUtil();
        }
        return hdfsUtil;
    }

    public static void main(String[] args) {
        HdfsUtil hu = HdfsUtil.getInstance();
        String dfsPath = "/apps/";
//		try {
//			hu.uploadLocalFile2HDFS("D:\\dspider\\apps\\DSA-20190529202246\\data\\DSA-20190529202246.dat", dfsPath);
//			hu.list(dfsPath);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        String tagModelPath = hu.getPath("/apps/tags/models/Tag_1738/lib/ant-1.6-20190607203510.jar");
        if (tagModelPath.endsWith("/lib")) {
            tagModelPath = tagModelPath.substring(0, tagModelPath.length() - 5);
        }
        System.out.println(tagModelPath);
        /*
         * String a = hu.readFileInfo("/user/hdfs/oozie/workflow3/lib/depenJar/");
         *
         * System.out.println(a);
         */

        //hu.uploadLocalFile2HDFS("C:\\Users\\12209\\Desktop\\重要.txt", "/data/liu");
//		System.out.println(new Path("/mengyao/tag/engine/models/ant-1.6-20190602194446.jar").getName());
//		System.out.println(new Path("/mengyao/tag/engine/models/ant-1.6-20190602194446.jar").getParent().toString());

    }

    public boolean exist(String dfsNewDir) {
        boolean state = false;
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(conf.get(fsAddr)), conf, user);
            state = fs.exists(new Path(dfsNewDir));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    /**
     * 创建hdfs目录
     *
     * @param dfsNewDir
     * @return
     */
    public boolean mkdir(String dfsNewDir) {
        boolean status = false;
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(conf.get(fsAddr)), conf, user);
            if (fs.exists(new Path(dfsNewDir))) {
                logger.info(dfsNewDir + "==== this dir exist ! ====");
                return status;
            }
            status = fs.mkdirs(new Path(dfsNewDir));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 上传文件到HDFS
     *
     * @param srcPath Linux 上文件
     * @param dfsPath Hdfs路径
     * @return
     */
    public boolean uploadLocalFile2HDFS(String srcPath, String dfsPath) {
        boolean status = false;
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(conf.get(fsAddr)), conf, user);
            fs.copyFromLocalFile(new Path(srcPath), new Path(dfsPath));
            status = true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return status;
    }

    public void list(String dfsPath) throws IOException {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(conf.get(fsAddr)), conf, user);
            FileStatus[] listStatus = fs.listStatus(new Path(dfsPath));
            for (FileStatus fsStat : listStatus) {
                System.out.println(fsStat.getLen() + "\t" + fsStat.getPath());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String readFileInfo(String dfsPath) throws IOException {
        FileSystem fs = null;
        String jars = "";
        try {
            fs = FileSystem.get(new URI(conf.get(fsAddr)), conf, user);
            FileStatus[] listStatus = fs.listStatus(new Path(dfsPath));
            String temp = null;
            for (FileStatus fsStat : listStatus) {
                temp = fsStat.getPath().toString();
                jars += temp.substring(temp.lastIndexOf("/") + 1, temp.length()) + ",";
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }

        return jars.substring(0, jars.length() - 1);
    }

    /**
     * 获取绝对路径文件的所在路径
     *
     * @param absolutePath
     * @return
     */
    public String getPath(String absolutePath) {
        return new Path(absolutePath).getParent().toString();
    }

    /**
     * 获取绝对路径文件的名称
     *
     * @param absolutePath
     * @return
     */
    public String getFile(String absolutePath) {
        return new Path(absolutePath).getName();
    }
}