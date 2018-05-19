package com.steptowin.core.common;

/**
 * @Desc:
 * @Author: zg
 * @Time:
 */
public class BaseEntity {
    private String message;
    private String status;
    private String code;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public boolean isNetWorkSuccess() {
        if (null != status && "200".equals(status))
            return true;
        return false;
    }

}
