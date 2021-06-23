package cn.itcast.tag.web.commons.bean;

import java.io.Serializable;

/**
 * 分页
 *
 * @author mengyao
 */
public class Bean implements Serializable {

    private static final long serialVersionUID = -1927950737644524798L;
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

    public void set(int sr, int er) {
        this.sr = sr;
        this.er = er;
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
        return cp == 0 ? 1 : cp;
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

}
