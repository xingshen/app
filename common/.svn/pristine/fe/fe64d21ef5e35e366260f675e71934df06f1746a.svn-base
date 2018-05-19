package com.steptowin.common.base.delegate;

import android.support.v4.app.Fragment;

import com.steptowin.common.base.BaseActivity;
import com.steptowin.common.base.FragmentManagerDelegate;

/**
 * desc: 全局接口,抽离各个通用接口
 * author：zg
 * date:16/6/15
 * time:下午2:58
 */
public interface AppContextDelegate {

    AppContextDelegate getAppContextDelegate();

    BaseActivity getHoldingActivity();

    ActivityDelegate getActivityDelegate();

    ApplicationDelegate getApplicationDelegate();

    FragmentManagerDelegate getChildFragmentManagerDelegate(Fragment fragment);

    FragmentManagerDelegate getFragmentManagerDelegate();

    boolean eventEnable();

    void destroy();
}
