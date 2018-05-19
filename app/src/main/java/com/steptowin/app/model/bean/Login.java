package com.steptowin.app.model.bean;


import com.steptowin.core.common.BaseEntity;

/**
 * desc:
 * author：zg
 * date:16/7/25
 * time:下午3:09
 */
public class Login extends BaseEntity {

    private String session;
    private String company;

    private int canUseWeekly;

    private int canUseWork;

    private int canUseMedal;

    private int canUseTarget;

    private int isManager;

    private int canManageLog;

    private int canManageWeekly;

    private int canManageTarget;
    private String mission;

    private int unreadMessageNum;

    private int unreadRemindNum;

    private int unreadNoticeNum;
    private User user;

    public void setSession(String session) {
        this.session = session;
    }

    public String getSession() {
        return session;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setCanUseWeekly(int canUseWeekly) {
        this.canUseWeekly = canUseWeekly;
    }

    public int getCanUseWeekly() {
        return canUseWeekly;
    }

    public void setCanUseWork(int canUseWork) {
        this.canUseWork = canUseWork;
    }

    public int getCanUseWork() {
        return canUseWork;
    }

    public void setCanUseMedal(int canUseMedal) {
        this.canUseMedal = canUseMedal;
    }

    public int getCanUseMedal() {
        return canUseMedal;
    }

    public void setCanUseTarget(int canUseTarget) {
        this.canUseTarget = canUseTarget;
    }

    public int getCanUseTarget() {
        return canUseTarget;
    }

    public void setIsManager(int isManager) {
        this.isManager = isManager;
    }

    public int getIsManager() {
        return isManager;
    }

    public void setCanManageLog(int canManageLog) {
        this.canManageLog = canManageLog;
    }

    public int getCanManageLog() {
        return canManageLog;
    }

    public void setCanManageWeekly(int canManageWeekly) {
        this.canManageWeekly = canManageWeekly;
    }

    public int getCanManageWeekly() {
        return canManageWeekly;
    }

    public void setCanManageTarget(int canManageTarget) {
        this.canManageTarget = canManageTarget;
    }

    public int getCanManageTarget() {
        return canManageTarget;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getMission() {
        return mission;
    }

    public void setUnreadMessageNum(int unreadMessageNum) {
        this.unreadMessageNum = unreadMessageNum;
    }

    public int getUnreadMessageNum() {
        return unreadMessageNum;
    }

    public void setUnreadRemindNum(int unreadRemindNum) {
        this.unreadRemindNum = unreadRemindNum;
    }

    public int getUnreadRemindNum() {
        return unreadRemindNum;
    }

    public void setUnreadNoticeNum(int unreadNoticeNum) {
        this.unreadNoticeNum = unreadNoticeNum;
    }

    public int getUnreadNoticeNum() {
        return unreadNoticeNum;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
