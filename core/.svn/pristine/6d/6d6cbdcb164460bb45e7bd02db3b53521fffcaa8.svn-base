package com.steptowin.core.common.callback;


/**
 * desc: 可以回调的runnable
 * author：zg
 * date:16/7/22
 * time:下午7:37
 */
public abstract class CallbackRunnable<T> implements Runnable,ICallback<T> {

    public CallbackRunnable() {
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
            start();
            success(obtainResponse());
        } catch (final Throwable throwable) {
            failure(throwable);
        }
    }

    public abstract T obtainResponse();
}
