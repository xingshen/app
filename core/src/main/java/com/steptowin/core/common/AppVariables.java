package com.steptowin.core.common;

import android.app.Application;
import android.content.Context;

import com.steptowin.core.common.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 此类只存储在类库当中用到，又需要在项目当中配置的信息
 * @Author: zg
 * @Time: 2016/3/10 19:01
 */
public class AppVariables {
    private static Map<String, Object> configs = new HashMap<>();

    public static Object get(String key) {
        return configs.get(key);
    }

    public static void put(String key, Object value) {
        configs.put(key, value);
    }

    public static String getString(String key) {
        return String.valueOf(get(key));
    }

    public static int getInt(String key) {
        return Integer.valueOf(getString(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.valueOf(getString(key));
    }

    /**
     * 获取app上下文对象,需要在程序启动调用{@link Application#onCreate()}时存储
     * @return
     */
    public static Context getApplicationContext() {
        Object object = get(Constants.KEY_APP_CONTEXT);
        if (null != object && object instanceof Context)
            return (Context) object;
        return null;
    }

}
