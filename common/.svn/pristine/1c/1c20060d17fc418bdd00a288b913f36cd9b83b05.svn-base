package com.steptowin.common.base.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 *@desc: activity代理实现类
 *@author zg
 *@time 16/6/1
 */
public class ActivityDelegateImp implements ActivityDelegate {
    private Activity mActivity;
    private ApplicationDelegate mApplication;

    public static ActivityDelegate createProxy(Context context) {
        return context instanceof Activity ? new ActivityDelegateImp((Activity) context) : null;
    }

    private ActivityDelegateImp(Activity activity) {
        mActivity = activity;
        mApplication = ApplicationDelegateImp.createProxy(mActivity.getApplication());
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass) {
        startActivity(activityClass,true);
    }

    @Override
    public void startActivity(Intent intent) {
        startActivity(intent,true);
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, boolean anim) {
        mActivity.startActivity(new Intent().setClass(mActivity, activityClass));
        if (anim)
            startOverridePendingTransition();
    }

    @Override
    public void startActivity(Intent intent, boolean anim) {
        mActivity.startActivity(intent);
        if(anim)
            startOverridePendingTransition();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
        startOverridePendingTransition();
    }

    @Override
    public void finish() {
        mActivity.finish();
        finishOverridePendingTransition();
    }

    /**
     * @Desc: 子类如果不需要默认动画，可重载并执行空操作
     * @Author: zg
     * @Time: 2016/1/29 15:12
     */
    private void startOverridePendingTransition() {
        mActivity.overridePendingTransition(mApplication.enterAnim(),
                mApplication.exitAnim());
    }

    /**
     * @Desc: 子类如果不需要默认动画，可重载并执行空操作
     * @Author: zg
     * @Time: 2016/1/29 15:12
     */
    private void finishOverridePendingTransition() {
        mActivity.overridePendingTransition(mApplication.popEnterAnim(),
                mApplication.popExitAnim());
    }


}
