package com.steptowin.core.db.client;

import android.content.Context;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @Desc: 对支持rxjava数据库请求的封装，方便{@link DBWorkObservable}的管理
 * @Author: zg
 * @Time: 2016/1/16 16:02
 */
public class DBWorkObservable<T> {
    Context context;
    Observable<T> observable;
    DBWorkSubscriber<T> dbWorkSubscriber;

    private DBWorkObservable(@Nullable Context context, Observable<T> observable) {
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

    public static <T> DBWorkObservable<T> create(Context context, Observable<T> observable) {
        return new DBWorkObservable<>(context, observable);
    }

    public Subscription subscribe(DBWorkSubscriber<T> subscriber) {
        this.dbWorkSubscriber = subscriber;
        Subscription subscription = this.observable.subscribe(subscriber);
        DBManager.addDBWork(context,subscription);
        return subscription;
    }
}
