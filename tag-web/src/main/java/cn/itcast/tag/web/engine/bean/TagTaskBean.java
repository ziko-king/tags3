package cn.itcast.tag.web.engine.bean;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;

import java.io.Serializable;

/**
 * @author mengyao
 */
public class TagTaskBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -197904833328973072L;

    //标签ID
    private long tagId;
    //标签名称
    private String tagName;
    //标签类型：1基础标签、2组合标签
    private int tagType;
    //算法模型名称
    private String modelName;
    //算法模型主程序入口
    private String modelMain;
    //算法模型的地址
    private String modelPath;
    //更新周期：-1无、1每天、2每周、3每月、4每年
    private String scheTime;
    //模型最后操作人
    private long operator;
    //1 启用 2.暂停 3 停用
    private int operation;
    //规则
    private String rule;
    //oozie jobId;
    private String jobId;

    public TagTaskBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TagTaskBean(long tagId, String tagName, int tagType, String modelName, String modelMain, String modelPath,
                       String scheTime, long operator, int operation, String rule, String jobId) {
        super();
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagType = tagType;
        this.modelName = modelName;
        this.modelMain = modelMain;
        this.modelPath = modelPath;
        this.scheTime = scheTime;
        this.operator = operator;
        this.operation = operation;
        this.rule = rule;
        this.jobId = jobId;
    }

    public void setModel(ModelBean bean) {
        this.tagId = bean.getTagId();
        this.tagType = bean.getType();
        this.modelMain = bean.getModelMain();
        this.modelName = bean.getModelName();
        this.modelPath = bean.getModelPath();
        this.scheTime = bean.getScheTime();
        //this.operation=bean.getOperation();
        //this.operator=bean.getOperator();
    }

    public void setBasicTag(BasicTagBean bean) {
        this.tagName = bean.getName();
    }

    public void setMergeTag(MergeTagBean bean) {
        this.tagName = bean.getName();
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
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

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(RuleBean bean) {
        this.rule = bean.getRule();
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


}
