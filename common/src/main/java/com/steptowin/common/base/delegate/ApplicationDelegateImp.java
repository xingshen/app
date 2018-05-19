package com.steptowin.common.base.delegate;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.steptowin.common.R;
import com.steptowin.core.common.Constants;
import com.steptowin.core.common.AppVariables;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * @Desc: Application全局的设置
 * @Author: zg
 * @Time: 2016/1/29 16:01
 * taskStack存在漏洞，activityA启动activityB,activityA
 * finish掉 ，可能会把activityBpop掉。类型由Stack改为LinkedList
 */
public class ApplicationDelegateImp implements ApplicationDelegate {
    private static ApplicationDelegateImp instance;
    Application mApplication;
    private LinkedList<WeakReference<Activity>> taskStack = new LinkedList<WeakReference<Activity>>();

    public static ApplicationDelegate createProxy(Application application) {
        if (null == instance)
            instance = new ApplicationDelegateImp(application);
        return instance;
    }

    private ApplicationDelegateImp(@NonNull Application application) {
        this.mApplication = application;
    }

    /**
     * @Desc: application 必须被{@link Application}实现
     * @Author: zg
     * @Time: 2016/1/29 16:00
     */
    @Override
    public Application getApplication() {
        return mApplication;
    }

    @Override
    public boolean isDebugMode() {
        return AppVariables.getBoolean(Constants.KEY_DEBUG);
    }

    @Override
    public boolean isUmengPathOpen() {
        return AppVariables.getBoolean(Constants.KEY_IS_UMENG_PATH_OPEN);
    }

    /**
     * @Desc: 从右边进
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    @Override
    public int enterAnim() {
        return R.anim.slide_in_right;
    }

    /**
     * @Desc: 从左边出
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    @Override
    public int exitAnim() {
        return R.anim.slide_out_left;
    }

    /**
     * @Desc: 从右边出
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    @Override
    public int popEnterAnim() {
        return R.anim.slide_in_left;
    }

    /**
     * @Desc: 从右边出
     * @Author: zg
     * @Time: 2015/12/29 18:25
     */
    @Override
    public int popExitAnim() {
        return R.anim.slide_out_right;
    }

    @Override
    public boolean isBackgroundRunning() {
        ActivityManager activityManager = (ActivityManager) getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(getApplication().getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isAppInstalled(String uri) {
        PackageManager pm = mApplication.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public boolean isAppInstalled() {
        return isAppInstalled(mApplication.getPackageName());
    }

    @Override
    public String getAppVersionName() {
        PackageManager pm = getApplication().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getApplication().getPackageName(), PackageManager.GET_ACTIVITIES);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public int getAppVersionCode() {
        int code;
        PackageManager pm = getApplication().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getApplication().getPackageName(), PackageManager.GET_ACTIVITIES);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            code = 0;
        }
        return code;
    }

    @Override
    public void pushTaskStack(Activity instance) {
        taskStack.push(new WeakReference<Activity>(instance));
    }

    @Override
    public boolean popTaskStack(Activity instance) {
        if (!taskStack.isEmpty()) {
            for (WeakReference<Activity> weakActivity : taskStack) {
                if (null != weakActivity
                        && null != weakActivity.get()
                        && !weakActivity.get().isFinishing()
                        && weakActivity.get().getClass().getName()
                        .equals(instance.getClass().getName())) {
                    taskStack.remove(weakActivity);
                    // weakActivity.get().finish();//此语句导致，循环调用
                    break;
                }
            }
        }
        return !taskStack.isEmpty();
    }

    @Override
    public boolean popTaskStack() {
        if (!taskStack.isEmpty()) {
            taskStack.pop();
        }
        return !taskStack.isEmpty();
    }

    @Override
    public boolean isKillSelf() {
        return true;
    }

    @Override
    public void quit() {
        while (!taskStack.isEmpty()) {
            WeakReference<Activity> activityList = taskStack.pop();
            if (activityList != null) {
                Activity activity = activityList.get();
                if (activity != null && !activity.isFinishing())
                    activity.finish();
            }
        }
//		Session.getSession().getServerInfo().clearLogin();
        if (isKillSelf())
            android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void finishExclude(Activity excludedActivity) {
        WeakReference<Activity> excludedWeakActivity = null;
        while (!taskStack.isEmpty()) {
            WeakReference<Activity> weakActivity = taskStack.pop();
            if (null != weakActivity && null != weakActivity.get()) {
                if (weakActivity.get().getClass().getName()
                        .equals(excludedActivity.getClass().getName()))
                    excludedWeakActivity = weakActivity;
                else {
                    weakActivity.get().finish();
                }
            }
        }
        taskStack.push(excludedWeakActivity);
    }

    @Override
    public void popTask(int num) {
        int index = 0;
        while (index++ < num && !taskStack.isEmpty()) {
            WeakReference<Activity> weakActivity = taskStack.pop();
            if (null != weakActivity && null != weakActivity.get()) {
                weakActivity.get().finish();
            }
        }
    }
}
