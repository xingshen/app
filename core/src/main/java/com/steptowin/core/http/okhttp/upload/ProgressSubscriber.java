package com.steptowin.core.http.okhttp.upload;

import rx.Subscriber;

/**
 * @Desc:
 * @Author: zg
 * @Time: $date$ $time$
 */
public class ProgressSubscriber extends Subscriber<Progress> {
    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {

    }

    public void onCompleted(String response){};

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Progress progress) {

    }
}
