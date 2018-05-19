package com.steptowin.core.http.retrofit;

import com.steptowin.core.common.BaseEntity;

import rx.Subscriber;

/**
 * @Desc:
 * @Author: zg
 * @Time: 2016/3/10 15:11
 */
public  abstract class NetWorkSubscriber<T extends BaseEntity> extends Subscriber<T> {

    public abstract void onSuccess(T t);

    /**
     * 请求成功,但是数据内容有误
     * @param t
     */
    public abstract void onFailure(T t);

    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {
    }

    /**
     * 请求失败
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        if (null != t && t.isNetWorkSuccess())
            onSuccess(t);
        else
            onFailure(t);
    }
}