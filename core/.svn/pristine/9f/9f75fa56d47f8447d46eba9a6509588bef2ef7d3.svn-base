package com.steptowin.core.db;

import com.steptowin.core.tools.RxSupport;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * @Desc: 数据库操作对rxjava的支持实现
 * @Author: zg
 * @Time: 2016/3/9 17:49
 */
public class RxSupportDB extends RxSupport {

    private final Executor executor;

    public RxSupportDB(Executor executor) {
        this.executor = executor;
    }

    Observable createDaoObservable(final Func1 func1) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Runnable runnable = getRunnable(subscriber,func1);
                FutureTask<Void> task = new FutureTask<Void>(runnable, null);

                // Subscribe to the future task of the dao call allowing unsubscription.
                subscriber.add(Subscriptions.from(task));
                executor.execute(task);
            }
        });
    }

    private Runnable getRunnable(final Subscriber<? super Object> subscriber,final Func1 func1) {
        return new Runnable() {
            @Override
            public void run() {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onNext(func1.call(""));
                subscriber.onCompleted();
            }
        };
    }


    @Override
    public Observable createObservable(Func1 func1) {
        return createDaoObservable(func1);
    }
}
