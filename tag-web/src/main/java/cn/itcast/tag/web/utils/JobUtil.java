package cn.itcast.tag.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuchengli
 * 接收Jsch执行命令返回的日志信息，并解析
 */
@Component
public class JobUtil {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JschUtil jschUtil;

    public static void main(String[] args) {
        JobUtil job = new JobUtil();
    }

    /**
     * 执行shell 命令，根据正则返回applicationId
     *
     * @param command
     * @param execTimeOut 命令执行超时时间，为了方便自动关闭channel
     * @return
     */
    public String execJob(String command, String regex, int execTimeOut) {

        List<String> list = jschUtil.exec(command, 200, execTimeOut);
        String content = null;
        Pattern pt = Pattern.compile(regex);
        for (String line : list) {
            if (line.contains("application_")) {
                Matcher m = pt.matcher(line);
                if (m.find()) {
                    content = m.group();
                    break;
                }
            }
        }
        return content;
    }

    /**
     * 根据jobId 查询当前job的运行状态
     *
     * @param command
     * @param execTimeOut 命令超时时间
     * @return
     */
    public String getJobStatus(String jobId, int execTimeOut) {
        List<String> list = jschUtil.exec("yarn  application -status " + jobId, 50, execTimeOut);

        String state = "";
        String finalState = "";
        String status = null;

        for (String line : list) {
            System.out.println("yarn line =" + line);
            if (line.startsWith("	State :")) {
                state = line.split(":")[1].trim();
            }

            if (line.startsWith("	Final-State :")) {
                finalState = line.split(":")[1].trim();
            }

            if (state.equals("ACCEPTED") || state.equals("RUNNING")) {
                status = "3";
            } else if (state.equals("FINISHED") || state.equals("FAILED")) {
                if (finalState.equals("SUCCEEDED")) {
                    status = "1";
                } else {
                    status = "2";
                }
            }
        }

        return status;
    }

}
