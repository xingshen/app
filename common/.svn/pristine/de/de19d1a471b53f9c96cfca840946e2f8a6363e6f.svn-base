package com.steptowin.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.steptowin.common.base.delegate.ActivityDelegate;
import com.steptowin.common.base.delegate.AppContextDelegate;
import com.steptowin.common.base.delegate.AppContextDelegateImp;
import com.steptowin.common.base.delegate.ApplicationDelegate;
import com.steptowin.common.base.mvp.MvpPresenter;
import com.steptowin.core.event.Event;
import com.steptowin.core.event.EventSubscriber;
import com.steptowin.core.event.EventWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc:基于mvp.替换原有{@link com.steptowin.common.base.mvp.MvpBasePresenter},使用强引用View,
 * 避免因内存不足,view引用被回收.不再使用视图时,一定要解除关联.
 * 添加对于eventbus的支持
 * author：zg
 * date:16/6/1
 * time:下午8:23
 */
public class BasePresenter<V extends BaseView> implements MvpPresenter<V> ,AppContextDelegate,EventSubscriber {

    public static BasePresenter defalut() {
        return new BasePresenter();
    }

    private V attachedView;
    private AppContextDelegate appContextDelegate;

    @Override
    public void attachView(V view) {
        attachedView = view;
        appContextDelegate = AppContextDelegateImp.create(view.getAttachedContext());
        if(eventEnable())
            EventWrapper.register(this);
    }

    /**
     * 返回关联的视图,正常情况下,不会为空
     * 注意:如果视图已经解除关联,子线程仍然在执行,调用此方法会返回为null
     * @return
     */
    public V getView() {
        return attachedView;
    }

    public boolean isViewAttached() {
        return null == attachedView ? true : false;
    }

    @Override
    public void detachView(boolean retainInstance) {
        attachedView = null;
        appContextDelegate.destroy();
        if(eventEnable())
            EventWrapper.unregister(this);
    }

    @Override
    public AppContextDelegate getAppContextDelegate() {
        return appContextDelegate.getAppContextDelegate();
    }

    @Override
    public BaseActivity getHoldingActivity() {
        return appContextDelegate.getHoldingActivity();
    }

    @Override
    public ActivityDelegate getActivityDelegate() {
        return appContextDelegate.getActivityDelegate();
    }

    @Override
    public ApplicationDelegate getApplicationDelegate() {
        return appContextDelegate.getApplicationDelegate();
    }

    @Override
    public FragmentManagerDelegate getChildFragmentManagerDelegate(Fragment fragment) {
        return appContextDelegate.getChildFragmentManagerDelegate(fragment);
    }

    @Override
    public FragmentManagerDelegate getFragmentManagerDelegate() {
        return appContextDelegate.getFragmentManagerDelegate();
    }

    @Override
    public boolean eventEnable() {
        return appContextDelegate.eventEnable();
    }

    @Override
    public void destroy() {

    }

    public Intent getIntent(){
        return getHoldingActivity().getIntent();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    @Override
    public void onEventAsync(Event event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEventMainThread(Event event) {

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    @Override
    public void onEventBackgroundThread(Event event) {

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    @Override
    public void onEventPosting(Event event) {

    }

    public void handleArguments(@NonNull Bundle bundle) {

    }


}
