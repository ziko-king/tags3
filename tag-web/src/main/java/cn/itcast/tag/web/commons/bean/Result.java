package cn.itcast.tag.web.commons.bean;

import java.io.Serializable;

public class Result implements Serializable {
    private static final long serialVersionUID = 4903252637631426494L;

    private int code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(int code, String msg, Object data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public void set(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void set(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return code + "\t" + msg + "\t" + data;
    }

}
