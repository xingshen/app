package com.steptowin.core.http.retrofit;

import android.content.Context;
import android.support.annotation.Nullable;

import com.steptowin.core.common.BaseEntity;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @Desc: 对支持rxjava网络请求的封装，方便{@link NetWorkManager}的管理
 * @Author: zg
 * @Time: 2016/1/16 16:02
 */
public class NetWorkObservable<T extends BaseEntity> {
    Context context;
    Observable<T> observable;
    NetWorkSubscriber<T> netWorkSubscriber;

    private NetWorkObservable(@Nullable Context context, Observable<T> observable) {
        this.context = context;
        /**
         * 指定网络请求过程中的线程，{@link Observable#doOnSubscribe(Action0)}在{@link Subscriber#onStart()}之后执行，
         * 区别在于前者可以指定线程。下面第二个subscribeOn即指定了doOnSubscribe执行时所在线程
         */
        this.observable = observable.subscribeOn(Schedulers.io()).doOnSubscribe(new Action0() {
            @Override
            public void call() {
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T extends BaseEntity> NetWorkObservable<T> create(Context context, Observable<T> observable) {
        return new NetWorkObservable<>(context, observable);
    }

    public Subscription subscribe(NetWorkSubscriber<T> subscriber) {
        this.netWorkSubscriber = subscriber;
        Subscription subscription = this.observable.subscribe(subscriber);
        NetWorkManager.addNetWork(context, subscription);
        return subscription;
    }
}
