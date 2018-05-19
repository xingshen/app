package com.steptowin.core.common.callback;

import rx.Subscriber;

/**
 * desc:有开始、结束、异常
 * author：zg
 * date:16/12/14
 * time:下午3:31
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    /**
     * 请求失败
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
