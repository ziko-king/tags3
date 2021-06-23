/**
 * 项目名称：mengyao
 * 创建日期：2018年7月5日
 * 修改历史：
 * 1、[2018年7月5日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.utils;

/**
 * @author zhaocs
 *
 */
public class ExcelException extends Exception {
    private static final long serialVersionUID = 1L;

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
