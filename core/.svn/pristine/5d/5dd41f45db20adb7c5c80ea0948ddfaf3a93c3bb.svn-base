package com.steptowin.core.http.okhttp;

import com.steptowin.core.tools.StringTool;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/1/3 下午1:27
 */
public class OkHttpCallManager {

    private ConcurrentHashMap<String, Call> callMap;
    private static OkHttpCallManager manager;

    private OkHttpCallManager() {
        callMap = new ConcurrentHashMap<>();
    }

    public static OkHttpCallManager getInstance() {
        if (manager == null) {
            manager = new OkHttpCallManager();
        }
        return manager;
    }

    public void addCall(String url, Call call) {
        if (call != null && StringTool.isEmpty(url)) {
            callMap.put(url, call);
        }
    }

    public Call getCall(String url) {
        if ( StringTool.isEmpty(url) ) {
            return callMap.get(url);
        }

        return null;
    }

    public void removeCall(String url) {
        if ( StringTool.isEmpty(url) ) {
            callMap.remove(url);
        }
    }

}
