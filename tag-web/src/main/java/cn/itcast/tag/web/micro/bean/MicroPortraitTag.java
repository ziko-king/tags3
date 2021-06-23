/**
 * 项目名称：mengyao
 * 创建日期：2018年6月6日
 * 修改历史：
 * 1、[2018年6月6日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.micro.bean;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaocs
 *
 */
public class MicroPortraitTag extends BasicTagBean implements Serializable {
    private static final long serialVersionUID = 3491207119055466387L;

    /**
     * 标签类型 0：基础标签 1：组合标签
     */
    private int type;

    /**
     * 子级标签
     */
    private List<MicroPortraitTag> microSubTags;

    public MicroPortraitTag() {
        super();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<MicroPortraitTag> getMicroSubTags() {
        return microSubTags;
    }

    public void setMicroSubTags(List<MicroPortraitTag> microSubTags) {
        this.microSubTags = microSubTags;
    }

    @Override
    public String toString() {
        return "MicroPortraitTag [type=" + type + ", microSubTags=" + microSubTags + "]";
    }

}
