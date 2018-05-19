package com.steptowin.common.base.delegate;

import android.app.Activity;
import android.content.Intent;

/**
 * @Desc: 基于activity的代理接口，优化activity调用方法
 * @Author: zg
 * @Time: 2016/2/17 16:15
 */
public interface ActivityDelegate {

    /**
     * @Desc: 以无参数的模式启动Activity。anim是指activity启动是否有动画
     * @Author: zg
     * @Time: 2016/2/17 16:32
     */
    void startActivity(Class<? extends Activity> activityClass, boolean anim);

    void startActivity(Class<? extends Activity> activityClass);

    void startActivity(Intent intent);

    void startActivity(Intent intent, boolean anim);

    void startActivityForResult(Intent intent, int requestCode);

    void finish();

}
