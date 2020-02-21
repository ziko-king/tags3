package cn.itcast.tag.web.api.inner.bean;

/**
 * HBase用户bean
 *
 * @author 83717
 */
public class SearchUserBean {
    /**
     * 身份证
     */
    private String id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 城市
     */
    private String city;
    /**
     * 社保卡卡号
     */
    private String socialSecurityNo;
    /**
     * 性别
     */
    private String gender;
    /**
     * 断缴次数 (近一年)
     */
    private String Interruptions;
    /**
     * 断缴最大时长 (近一年)
     */
    private String DurationOfInterruption;
    /**
     * 缴纳基数跨度（近一年）
     */
    private String DurationoOfInterruption;
    /**
     * 距上次断缴时距
     */
    private String DurationMonthsSinceLastInterruption;
    /**
     * 分值
     */
    private String score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocialSecurityNo() {
        return socialSecurityNo;
    }

    public void setSocialSecurityNo(String socialSecurityNo) {
        this.socialSecurityNo = socialSecurityNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterruptions() {
        return Interruptions;
    }

    public void setInterruptions(String interruptions) {
        Interruptions = interruptions;
    }

    public String getDurationOfInterruption() {
        return DurationOfInterruption;
    }

    public void setDurationOfInterruption(String durationOfInterruption) {
        DurationOfInterruption = durationOfInterruption;
    }

    public String getDurationoOfInterruption() {
        return DurationoOfInterruption;
    }

    public void setDurationoOfInterruption(String durationoOfInterruption) {
        DurationoOfInterruption = durationoOfInterruption;
    }

    public String getDurationMonthsSinceLastInterruption() {
        return DurationMonthsSinceLastInterruption;
    }

    public void setDurationMonthsSinceLastInterruption(String durationMonthsSinceLastInterruption) {
        DurationMonthsSinceLastInterruption = durationMonthsSinceLastInterruption;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SearchUserBean [id=" + id + ", name=" + name + ", city=" + city + ", socialSecurityNo="
                + socialSecurityNo + ", gender=" + gender + ", Interruptions=" + Interruptions
                + ", DurationOfInterruption=" + DurationOfInterruption + ", DurationoOfInterruption="
                + DurationoOfInterruption + ", DurationMonthsSinceLastInterruption="
                + DurationMonthsSinceLastInterruption + ", score=" + score + "]";
    }

}
