package cn.itcast.tag.web.mergetag.bean.form;

import java.io.Serializable;
import java.util.List;

/**
 * 组合标签与页数DTO
 *
 * @author zhangwenguo
 */
public class MergeTagPageFormBean implements Serializable {

    private static final long serialVersionUID = -4512403076981696429L;

    // 起始行(start record):从0开始
    private int sr;
    // 结束行(end record)
    private int er;
    // 总条数(total record)
    private int tr;
    // 当前页(current page)：从1开始
    private int cp;
    // 每页条数(page record)
    private int pr = 10;
    // 页总数量
    private int pc;
    // 标签状态
    private int state;

    private List<UserMergeTagTagFormBean> subPage;

    public MergeTagPageFormBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MergeTagPageFormBean(int sr, int er, int tr, int cp, int pr, int pc, int state) {
        super();
        this.sr = sr;
        this.er = er;
        this.tr = tr;
        this.cp = cp;
        this.pr = pr;
        this.pc = pc;
        this.state = state;
    }

    public int getSr() {
        return sr;
    }

    public void setSr(int sr) {
        this.sr = sr;
    }

    public int getEr() {
        return er;
    }

    public void setEr(int er) {
        this.er = er;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getPr() {
        return pr;
    }

    public void setPr(int pr) {
        this.pr = pr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public List<UserMergeTagTagFormBean> getSubPage() {
        return subPage;
    }

    public void setSubPage(List<UserMergeTagTagFormBean> subPage) {
        this.subPage = subPage;
    }

    @Override
    public String toString() {
        return sr + "\t" + er + "\t" + tr + "\t" + cp + "\t" + pr + "\t" + state;
    }

}
