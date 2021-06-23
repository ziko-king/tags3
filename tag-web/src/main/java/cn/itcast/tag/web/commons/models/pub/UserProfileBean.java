package cn.itcast.tag.web.commons.models.pub;

import java.io.Serializable;

public class UserProfileBean implements Serializable {

    private static final long serialVersionUID = -99118346451426894L;
    private String userId;
    private String tbids;

    public UserProfileBean() {
        super();
    }

    public UserProfileBean(String userId, String tbids) {
        super();
        this.userId = userId;
        this.tbids = tbids;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTbids() {
        return tbids;
    }

    public void setTbids(String tbids) {
        this.tbids = tbids;
    }

    @Override
    public String toString() {
        return "UserProfileBean [userId=" + userId + ", tbids=" + tbids + "]";
    }


}
