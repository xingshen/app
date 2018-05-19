package com.steptowin.core.common.callback;

import com.steptowin.core.common.thread.MainThreadExecutor;

/**
 * desc: 回调接口的简单实现,可以切换到主线程回调
 * author：zg
 * date:16/6/13
 * time:下午5:19
 */
public class SimpleCallback<T> implements ICallback<T> {
    private MainThreadExecutor uiExecutor = new MainThreadExecutor();
    @Override
    public void start() {
        uiExecutor.execute(new Runnable() {
            @Override
            public void run() {
                startOnUI();
            }
        });
    }

    public void startOnUI(){

    }

    @Override
    public void success(final T t) {
        uiExecutor.execute(new Runnable() {
            @Override
            public void run() {
                successOnUI(t);
            }
        });
    }

    public void successOnUI(T t) {

    }

    @Override
    public void failure(final Throwable throwable) {
        uiExecutor.execute(new Runnable() {
            @Override
            public void run() {
                failureOnUI(throwable);
            }
        });
    }

    public void failureOnUI(Throwable throwable) {

    }
}
