package com.steptowin.app.model.bean;

/**
 * desc:
 * author：zg
 * date:16/7/25
 * time:下午3:14
 */
public class User {

    private String userId;

    private String userName;
    private String photo;
    private String exp;

    private String expRoleName;

    private String expMin;

    private int expMax;
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhoto() {
        return photo;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
    public String getExp() {
        return exp;
    }

    public void setExpRoleName(String expRoleName) {
        this.expRoleName = expRoleName;
    }
    public String getExpRoleName() {
        return expRoleName;
    }

    public void setExpMin(String expMin) {
        this.expMin = expMin;
    }
    public String getExpMin() {
        return expMin;
    }

    public void setExpMax(int expMax) {
        this.expMax = expMax;
    }
    public int getExpMax() {
        return expMax;
    }

}
