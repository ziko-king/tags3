package cn.itcast.tag.web.engine.bean;

import cn.itcast.tag.web.commons.bean.Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型DTO
 *
 * @author mengyao
 */
public class ModelBean extends Bean implements Serializable {

    private static final long serialVersionUID = 2359377541105284894L;

    private long id;
    // 标签ID
    private long tagId;
    // 标签类型：1基础标签、2组合标签
    private int type;
    // 算法模型名称
    private String modelName;
    // 算法模型主程序入口
    private String modelMain;
    // 算法模型的地址
    private String modelPath;
    // 更新周期:0无、1每天、2每周、3每月、4每年
    private String scheTime;
    // 创建时间
    private Date ctime;
    // 修改时间
    private Date utime;
    // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
    private int state;
    // 备注
    private String remark;
    // 模型最后操作人，user
    private String operator;
    // 0.创建完成,1 启用 2.暂停 3 停用
    private String operation;
    // 参数
    private String args;

    public ModelBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ModelBean(long tagId) {
        super();
        this.tagId = tagId;
    }

    public ModelBean(long tagId, int type, String modelName, String modelMain, String modelPath, String scheTime,
                     int state, String remark, String operator, String operation, String args) {
        super();
        this.tagId = tagId;
        this.type = type;
        this.modelName = modelName;
        this.modelMain = modelMain;
        this.modelPath = modelPath;
        this.scheTime = scheTime;
        this.state = state;
        this.remark = remark;
        this.operator = operator;
        this.operation = operation;
        this.args = args;
    }

    public ModelBean(long id, long tagId, int type, String modelName, String modelMain, String modelPath, String scheTime,
                     Date ctime, Date utime, int state, String remark, String operator, String operation, String args) {
        super();
        this.id = id;
        this.tagId = tagId;
        this.type = type;
        this.modelName = modelName;
        this.modelMain = modelMain;
        this.modelPath = modelPath;
        this.scheTime = scheTime;
        this.ctime = ctime;
        this.utime = utime;
        this.state = state;
        this.remark = remark;
        this.operator = operator;
        this.operation = operation;
        this.args = args;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return id + "\t" + tagId + "\t" + type + "\t" + modelName + "\t" + modelMain + "\t" + modelPath + "\t"
                + scheTime + "\t" + ctime + "\t" + utime + "\t" + state + "\t" + remark + "\t" + operator + "\t"
                + operation + "\t" + args;
    }


}
