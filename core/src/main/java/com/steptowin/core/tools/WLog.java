package com.steptowin.core.tools;

import android.util.Log;

/**
 * 封装Log的简易实现。使用对应的Class作为键值打印，也可调用静态方法自定义tag
 *
 * @author zg
 * @record 修改记录：2-15-6-16 edit by zg 增加静态方法
 */
public class WLog {

    private final String myName;

    private static final String TEST_TAG = "13leaf";

    private static boolean closed = false;

    public WLog(final Class<?> targetClazz) {
        myName = targetClazz.getSimpleName();
    }

    public static WLog getMyLogger(final Class<?> targetClazz) {
        return new WLog(targetClazz);
    }

    public static void closeLogger() {
        closed = true;
    }

    /**
     * 添加日志输出文件,用于调试使用。 日志的级别将用Debug级别，日志的Tag将用类名
     *
     * @param msg 要输出的日志内容
     */
    public void debugLog(Object msg) {
        if (!closed)
            Log.d(myName, msg + "");
    }

    /**
     * 添加日志输出文件,用于调试使用。 日志的级别将用Error级别，日志的Tag将用类名
     *
     * @param msg 要输出的日志内容
     */
    public void errorLog(Object msg) {
        if (!closed)
            Log.e(myName, msg + "");
    }

    /**
     * 使用13leaf作为专属的Tag,方便筛选显示
     *
     * @param msg
     */
    public void testLog(Object msg) {
        if (!closed)
            Log.d(TEST_TAG, msg + "");
    }

    public void warnLog(Object msg) {
        if (!closed)
            Log.w(myName, msg + "");
    }

    public void infoLog(Object msg) {
        if (!closed)
            Log.i(myName, msg + "");
    }

    public void verboseLog(Object msg) {
        if (!closed)
            Log.v(myName, msg + "");
    }

    public static void verbose(String tag, String msg) {
        if (!closed)
            Log.v(tag, msg);
    }

    public static void debug(String tag, String msg) {
        if (!closed)
            Log.d(tag, msg);
    }

    public static void info(String tag, String msg) {
        if (!closed)
            Log.i(tag, msg);
    }

    public static void warn(String tag, String msg) {
        if (!closed)
            Log.w(tag, msg);
    }

    public static void error(String tag, String msg) {
        if (!closed)
            Log.e(tag, msg);
    }
}
