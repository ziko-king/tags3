package cn.itcast.tag.web.utils;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchengli
 * Jsch 远程操作服务器，提交spark 任务
 * 要配合后面quartz Job 的动态加载，所以不使用spring 管理
 */
@Component
public class JschUtil {

    private static Session session;
    private static JschUtil jschUtil;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${serverHost}")
    private String serverHost = "192.168.1.231";
    @Value("${serverUserName}")
    private String serverUserName = "root";
    @Value("${sshPort}")
    private String sshPort = "22";
    @Value("${serverPasswd}")
    private String serverPasswd = "bjqt*20180101";
    @Value("${connTimeOut}")
    private String connTimeOut = "300000";
    private JSch jsch;
    private ChannelShell exec = null;

    public JschUtil() {
        try {
            jsch = new JSch();
            session = jsch.getSession(serverUserName, serverHost, Integer.valueOf(sshPort));
            session.setPassword(serverPasswd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(Integer.valueOf(connTimeOut));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JschUtil getInstance() {
        if (null == jschUtil) {
            jschUtil = new JschUtil();
        }
        return jschUtil;
    }

    public static void main(String[] args) throws Exception {

        JschUtil jschUtil = JschUtil.getInstance();
        List<String> list = jschUtil.exec("spark-submit --master yarn --deploy-mode cluster --class com.java.WordCount --jars hdfs://bjqt/user/hdfs/oozie/workflow3/lib/depenJar/fastjson-1.2.47.jar hdfs://bjqt/user/hdfs/oozie/workflow3/lib/JavaWC.jar",
                25, 20000);
        for (String line : list) {
            System.out.println("line$$$$$$$$$$$$" + line);
        }
        //jschUtil.exec("source /etc/profile; hdfs dfs -ls /");
        //jschUtil.exec("spark-submit --master yarn --deploy-mode cluster --class com.java.WordCount --jars hdfs://bjqt/user/hdfs/oozie/workflow3/lib/depenJar/fastjson-1.2.47.jar hdfs://bjqt/user/hdfs/oozie/workflow3/lib/JavaWC.jar","");
        //jschUtil.exec("yarn application -list");

		/*String str = "18/07/03 14:53:58 INFO YarnClientImpl: Submitted application application_1529575407574_1080";

		 String regex = "application_+\\d{13}+\\_+\\d{4}+";

		 Pattern pt = Pattern.compile(regex);

		 Matcher m = pt.matcher(str);

		 if(m.find()) {
			 System.out.println(m.group());
		 }*/

    }

    /**
     * 执行命令
     *
     * @param command
     * @param lineCount      需要返回的日志的行数
     * @param channelTimeOut exec shell 连接超时时间，
     *                       因为考虑到spark任务时间较长，而简单shell执行时间短，所以传入不同超时时间，
     *                       让其自动关闭exec channel
     * @return 返回日志集合
     */
    public List<String> exec(String command, int lineCount, int channelTimeOut) {
        BufferedReader reader = null;
        InputStream in = null;
        OutputStream out = null;
        List<String> list = new ArrayList<String>(lineCount);

        try {
            if (!session.isConnected()) {
                session.connect();
            }
            exec = (ChannelShell) session.openChannel("shell");
            exec.connect(channelTimeOut);
            in = exec.getInputStream();//从远程端到达的所有数据都能从这个流中读取到
            out = exec.getOutputStream();//写入该流的所有数据都将发送到远程端

            //好处就是不需要每次手动给字符串加\n
            PrintWriter printWriter = new PrintWriter(out);
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();

            reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            String line = null;
            int a = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println("spark job = " + line);
                if (a < lineCount) {
                    list.add(line);
                } else {
                    break;
                }
                a++;
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
