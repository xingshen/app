package com.steptowin.common.base.delegate;

import android.app.Activity;
import android.app.Application;

/**
 *@desc: app代理接口
 *@author zg
 *@time 16/6/2
 */
public interface ApplicationDelegate {

    Application getApplication();

    /**
     * 如果为true 则打开日志输出
     */
    boolean isDebugMode();

    /**
     * 是否开启友盟访问路径统计，由应用决定。默认关闭 edit by zg
     *
     * @return boolean
     */
    boolean isUmengPathOpen();

    /**
     * @Desc: 从右边进
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    int enterAnim();

    /**
     * @Desc: 从左边出
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    int exitAnim();

    /**
     * @Desc: 从右边出
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    int popEnterAnim();

    /**
     * @Desc: 从右边出
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    int popExitAnim();


    /**
     * @Desc: 判断程序是否在后台运行
     * @Author: zg
     * @Time: 2016/1/29 16:18
     */
    boolean isBackgroundRunning();

    boolean isAppInstalled(String uri);

    String getAppVersionName();

    int getAppVersionCode();

    void pushTaskStack(Activity instance);

    boolean popTaskStack(Activity instance);

    boolean popTaskStack();

    boolean isKillSelf();

    void quit();

    void finishExclude(Activity excludedActivity);

    void popTask(int num);
}
