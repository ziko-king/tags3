package cn.itcast.tag.web.engine.bean;

import java.io.Serializable;

/**
 * 元数据
 *
 * @author liuchengli
 */
public class EngineBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3414735179089362770L;

    private long id;
    // 标签ID
    private long tagId;
    // coordinator Id
    private String jobid;
    //0.未启动 1.成功 2.失败 3.运行中 4.任务暂停 5.任务停用
    private String status;
    // 标签类型
    private String tagType;
    // 标签名称
    private String name;
    // 规则
    private String rule;
    // 模型名称
    private String ModelName;
    // 申请人
    private String applyPerson;
    // 最后执行人
    private String operator;
    // 任务开始时间
    private String startTime;
    // 任务开始时间
    private String endTime;
    // 操作：1启用 2暂停 3停用
    private int operation;
    //查询操作的Code 0.查询所有1.按四级标签查询 2.按三级标签查询
    private int queryCode;
    private String business;
    private String remark;

    public EngineBean() {
        super();
    }

    public EngineBean(long id, long tagId, String jobid, String status, String tagType, String name, String rule,
                      String modelName, String applyPerson, String operator, String startTime, String endTime,
                      int operation, int queryCode, String business) {
        super();
        this.id = id;
        this.tagId = tagId;
        this.jobid = jobid;
        this.status = status;
        this.tagType = tagType;
        this.name = name;
        this.rule = rule;
        this.ModelName = modelName;
        this.applyPerson = applyPerson;
        this.operator = operator;
        this.startTime = startTime;
        this.endTime = endTime;
        this.operation = operation;
        this.queryCode = queryCode;
        this.business = business;
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

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(int queryCode) {
        this.queryCode = queryCode;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return id + "\t" + tagId + "\t" + jobid + "\t" + status + "\t" + tagType + "\t" + name + "\t" + rule
                + "\t" + ModelName + "\t" + applyPerson + "\t" + operator + "\t" + startTime + "\t" + endTime
                + "\t" + operation + "\t" + queryCode + "\t" + business;
    }

}
