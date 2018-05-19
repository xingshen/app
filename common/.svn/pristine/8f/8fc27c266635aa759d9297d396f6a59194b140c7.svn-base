package com.steptowin.common.base.mvp.lce;

import android.support.annotation.NonNull;

import com.steptowin.common.base.mvp.MvpFragment;
import com.steptowin.common.base.mvp.MvpPresenter;


/**
 * desc:
 * author：zg
 * date:2016/4/1 0001
 * time:下午 2:10
 */
public abstract class MvpLceFragment<M, V extends MvpLceView<M>, P extends MvpPresenter<V>> extends MvpFragment<V,P> implements MvpLceView<M> {
    protected LoadingView loadingView;
    protected ErrorView errorView;


    /**
     *@desc 自定义加载过程中的视图，如果加载过程中需要更新UI，则要实现{@link LoadingView#update()}接口
     *
     *edit by zg
     *
     */
    @NonNull
    public abstract LoadingView createLoadingView();

    public LoadingView getLoadingView(){
        if(null == loadingView)
            loadingView = createLoadingView();
        return loadingView;
    }

    /**
     *@desc 自定义加载时的视图
     *
     *edit by zg
     *
     */
    public abstract ErrorView createErrorView();

    public ErrorView getErrorView(){
        if(null == errorView)
            errorView = createErrorView();
        return errorView;
    }

    @Override
    public void showLoading() {
        getLoadingView().show();
    }

    @Override
    public void showContent() {
        getLoadingView().dismiss();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        getLoadingView().dismiss();
        getErrorView().show();
    }

    @Override
    public void setData(M data) {

    }

    @Override
    public void loadData() {

    }
}
