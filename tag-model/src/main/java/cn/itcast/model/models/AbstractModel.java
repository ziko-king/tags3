package cn.itcast.model.models;

import cn.itcast.model.beans.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 标签模型超类
 * Created by mengyao
 * 2018年6月5日
 */
public abstract class AbstractModel {

    public String name;
    public String describe;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractModel(String name, String describe) {
        this.name = name;
        this.describe = describe;
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public String getAppName() {
        return name;
    }

    /**
     * 获取模型算法类型
     *
     * @return
     */
    public abstract String getType();

    /**
     * 获取模型算法描述
     *
     * @return
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * 获取要计算的标签
     *
     * @return
     */
    public abstract List<? extends Tag> getTag();

    /**
     * 初始化
     */
    public void prepare() {
    }

    /**
     * 计算标签模型
     */
    public abstract void compute();

    /**
     * 释放资源
     */
    public void clear() {
    }

    /**
     * 运行标签模型
     */
    public void execute() {
        logger.info("==== Running the model:{} ====", describe);
        prepare();
        try {
            getTag();
            compute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }
    }

    /**
     * 测试样例数据
     *
     * @return
     */
    public List<?> exampleData() {
        return null;
    }

    /**
     * 模型类型
     * Created by mengyao
     * 2018年6月5日
     */
    public enum ModelType {
        MATCH,
        ML,
        STATISTICS
    }

}
