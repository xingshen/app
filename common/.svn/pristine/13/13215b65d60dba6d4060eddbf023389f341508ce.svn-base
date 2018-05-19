package com.steptowin.common.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.steptowin.common.base.BaseActivity;
import com.steptowin.common.base.FragmentManagerDelegate;

/**
 * desc:全局上下文代理
 * author：zg
 * date:16/6/16
 * time:上午11:12
 */
public class AppContextDelegateImp implements AppContextDelegate {
    Context context;


    private FragmentManagerDelegate mFragmentManagerDelegate;
    private FragmentManagerDelegate mChildFragmentManagerDelegate;
    private ActivityDelegate mActivityDelegate;
    private ApplicationDelegate mApplicationDelegate;


    public static AppContextDelegate create(Context context){
        return new AppContextDelegateImp(context);
    }

    private AppContextDelegateImp(Context context){
        this.context = context;
    }

    @Override
    public AppContextDelegate getAppContextDelegate() {
        return this;
    }

    @Override
    public BaseActivity getHoldingActivity() {
        if (null != context && context instanceof BaseActivity)
            return (BaseActivity) context;
        return null;
    }

    @Override
    public ActivityDelegate getActivityDelegate() {
        if (null == mActivityDelegate) {
            mActivityDelegate = ActivityDelegateImp.createProxy(context);
        }
        return mActivityDelegate;
    }

    @Override
    public ApplicationDelegate getApplicationDelegate() {
        if (null == mApplicationDelegate)
            mApplicationDelegate = ApplicationDelegateImp.createProxy((Application) context.getApplicationContext());
        return mApplicationDelegate;
    }

    @Override
    public FragmentManagerDelegate getChildFragmentManagerDelegate(Fragment fragment) {
        if (null == mChildFragmentManagerDelegate) {
            mChildFragmentManagerDelegate = new FragmentManagerDelegate((FragmentActivity) context);
            if(null != fragment)
                mChildFragmentManagerDelegate.setFragmentManager(fragment.getChildFragmentManager());
        }
        return mChildFragmentManagerDelegate;
    }

    @Override
    public FragmentManagerDelegate getFragmentManagerDelegate() {
        if (null == mFragmentManagerDelegate) {
            mFragmentManagerDelegate = new FragmentManagerDelegate((FragmentActivity) context);
        }
        return mFragmentManagerDelegate;
    }

    @Override
    public boolean eventEnable() {
        return false;
    }

    @Override
    public void destroy() {
        mChildFragmentManagerDelegate = null;
        mFragmentManagerDelegate = null;
        mActivityDelegate = null;
        mApplicationDelegate = null;
    }
}
