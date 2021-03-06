package com.steptowin.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.steptowin.common.base.delegate.ActivityDelegate;
import com.steptowin.common.base.delegate.AppContextDelegate;
import com.steptowin.common.base.delegate.AppContextDelegateImp;
import com.steptowin.common.base.delegate.ApplicationDelegate;
import com.steptowin.common.base.mvp.lce.ErrorView;
import com.steptowin.common.base.mvp.lce.LoadingView;
import com.steptowin.common.base.mvp.lce.MvpLceFragment;
import com.steptowin.core.event.Event;
import com.steptowin.core.event.EventSubscriber;
import com.steptowin.core.event.EventWrapper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Desc: fragment基类, 基于mvp
 * 添加对于eventbus的支持
 * @Author: zg
 * @Time: 2015/12/23 18:39
 */
public abstract class BaseFragment<M, V extends BaseView<M>, P extends BasePresenter<V>> extends MvpLceFragment<M, V, P> implements BaseView<M>, AppContextDelegate, EventSubscriber {
    AppContextDelegate appContextDelegate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != getPresenter() && null != getArguments())
            getPresenter().handleArguments(getArguments());
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * @desc 调用{}{@link #addChildFragment(Fragment, boolean)}和{@link #addFragment(Fragment)}
     * 时,会使用此方法返回的id
     * <p/>
     * edit by zg
     */
    protected
    @IdRes
    int getChildFragmentContainerId() {
        return 0;
    }

    @Override
    public LoadingView createLoadingView() {
        return null;
    }

    @Override
    public ErrorView createErrorView() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContextDelegate = AppContextDelegateImp.create(activity);
        if (eventEnable())
            EventWrapper.register(this);
    }

    @Override
    public P createPresenter() {
        return (P) BasePresenter.defalut();
    }

    @Override
    public void setRetainInstance(boolean retainingInstance) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (eventEnable())
            EventWrapper.unregister(this);
        appContextDelegate.destroy();
    }

    @Override
    public AppContextDelegate getAppContextDelegate() {
        return appContextDelegate;
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
    public ActivityDelegate getActivityDelegate() {
        return appContextDelegate.getActivityDelegate();
    }

    @Override
    public ApplicationDelegate getApplicationDelegate() {
        return appContextDelegate.getApplicationDelegate();
    }

    @Override
    public BaseActivity getHoldingActivity() {
        return appContextDelegate.getHoldingActivity();
    }

    protected Object getActivityPresenter() {
        return getHoldingActivity().getPresenter();
    }

    public void addChildFragment(Fragment fragment) {
        addChildFragment(fragment, false);
    }

    public void addChildFragment(Fragment fragment, boolean addToBackStack) {
        getChildFragmentManagerDelegate(this).addFragment(getChildFragmentContainerId(), fragment, addToBackStack);
    }

    public void replaceChildFragment(Fragment fragment) {
        replaceChildFragment(fragment, false);
    }

    public void replaceChildFragment(Fragment fragment, boolean addToBackStack) {
        getChildFragmentManagerDelegate(this).replaceFragment(getChildFragmentContainerId(), fragment, addToBackStack);
    }

    public void addFragment(Fragment fragment) {
        getHoldingActivity().addFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        getHoldingActivity().replaceFragment(fragment);
    }

    @Override
    public Context getAttachedContext() {
        return getHoldingActivity();
    }


    @Override
    public boolean eventEnable() {
        return appContextDelegate.eventEnable();
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
    public void destroy() {

    }

    /**
     * 向前一个fragment传值时调用
     * @param bundle
     */
    protected void setResult(Bundle bundle){
        if(null != getTargetFragmentForBase())
            getTargetFragmentForBase().onFragmentResult(bundle,getTargetRequestCode());
    }

    public BaseFragment getTargetFragmentForBase() {
        if (getTargetFragment() instanceof BaseFragment) {
            return (BaseFragment) getTargetFragment();
        }
        return null;
    }

    /**
     * 启动另一个fragment时,若需要回传结果.可以在启动时调用{@link #setTargetFragment(Fragment, int)}.这样
     * 当另一个fragment执行到{@link #onDetach()}时,通过调用{@link #getTargetFragmentForBase()#onFragmentResult(Bundle, int)}
     * 达到回传的效果.
     *
     * @param bundle
     * @param requestCode
     */
    protected void onFragmentResult(Bundle bundle, int requestCode) {

    }
}
