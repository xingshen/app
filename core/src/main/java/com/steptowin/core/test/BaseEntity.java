package com.steptowin.core.test;

/**
 * @Desc:
 * @Author: zg
 * @Time: $date$ $time$
 */

public class BaseEntity {
    public final static String ROOT = "root";

    private String status;
    private String message;
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
