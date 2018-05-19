package com.steptowin.app.model;

/**
 * desc: 自定义异步订阅者
 * author：zg
 * date:2016/4/5 0005
 * time:下午 8:26
 */
public abstract class AysnSubscriber<T> extends com.steptowin.core.db.client.DBWorkSubscriber<T> {


    public abstract void onStarted();

    @Override
    public void onStart() {
        onStarted();
    }

}
