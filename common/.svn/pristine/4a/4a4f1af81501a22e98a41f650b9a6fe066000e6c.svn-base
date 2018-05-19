package com.steptowin.common.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import com.steptowin.common.base.delegate.ActivityDelegate;
import com.steptowin.common.base.delegate.AppContextDelegate;
import com.steptowin.common.base.delegate.AppContextDelegateImp;
import com.steptowin.common.base.delegate.ApplicationDelegate;
import com.steptowin.core.event.Event;
import com.steptowin.core.event.EventSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc:listfragment基类,非mvp
 * author：zg
 * date:16/6/16
 * time:上午11:05
 */
public class BaseListFragment extends ListFragment implements AppContextDelegate, EventSubscriber {
    AppContextDelegate appContextDelegate;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContextDelegate = AppContextDelegateImp.create(activity);
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

    @Override
    public AppContextDelegate getAppContextDelegate() {
        return appContextDelegate;
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
        return appContextDelegate.getChildFragmentManagerDelegate(this);
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
        appContextDelegate.destroy();
    }
}
