package cn.itcast.tag.web.commons.models;

import cn.itcast.tag.web.commons.parser.MetaParser;
import cn.itcast.tag.web.engine.bean.MetaDataBean;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7908596480996230258L;
    private String appName = this.getClass().getSimpleName();
    private List<String> rules;
    private MetaParser parser;

    public void configure() {
    }

    /**
     * 输入数据
     *
     * @param meta
     */
    public abstract void input(MetaDataBean meta);

    /**
     * 预处理
     */
    public abstract void prepare();

    /**
     * 训练模型
     */
    public abstract void train(String modelPath);

    /**
     * 计算数据
     *
     * @param modelPath
     */
    public abstract void compute(String modelPath, String resPath);

    void run() {
        configure();
        prepare();
        train("");
    }
}
