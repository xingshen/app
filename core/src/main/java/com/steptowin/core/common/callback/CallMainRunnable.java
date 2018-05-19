package com.steptowin.core.common.callback;

import com.steptowin.core.common.thread.MainThreadExecutor;

/**
 * desc:回调主线程的runnable
 * author：zg
 * date:16/7/22
 * time:下午8:15
 */
public abstract class CallMainRunnable<T> implements Runnable,ICallback<T> {
    MainThreadExecutor uiExecutor;

    public CallMainRunnable() {
        uiExecutor = new MainThreadExecutor();
    }

    @Override
    public void start() {

    }

    @Override
    public void success(T t) {

    }

    @Override
    public void failure(Throwable throwable) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public final void run() {
        try {
            uiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });

            final T t = obtainResponse();

            uiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    success(t);
                }
            });

        } catch (final Throwable throwable) {
            uiExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    failure(throwable);
                }
            });
        }
    }

    public abstract T obtainResponse();
}
