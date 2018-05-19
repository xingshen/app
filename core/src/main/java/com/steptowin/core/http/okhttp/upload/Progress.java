package com.steptowin.core.http.okhttp.upload;

/**
 * @Desc:
 * @Author: zg
 * @Time: $date$ $time$
 */
public class Progress {
    public int progress;

    public boolean done() {
        if (progress >= 100)
            return true;
        return false;
    }
}
