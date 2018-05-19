package com.steptowin.app.presenter;

import com.steptowin.app.model.AysnSubscriber;
import com.steptowin.app.model.bean.sql.User;
import com.steptowin.common.base.BasePresenter;
import com.steptowin.common.base.BaseView;
import com.steptowin.core.db.client.DBClient;
import com.steptowin.core.db.client.DBWorkObservable;
import com.steptowin.core.db.operation.Querys;

import java.util.List;

/**
 * desc:
 * author：zg
 * date:2016/4/1 0001
 * time:下午 3:13
 */
public class DbPresenter extends BasePresenter<BaseView<List<User>>> {
    DBClient dbClient;

    public DbPresenter(){
        dbClient = DBClient.create();
    }

    public void insert(User user){
        DBWorkObservable.create(null, dbClient.insertAsyn(user)).subscribe(new AysnSubscriber() {
            @Override
            public void onStarted() {
                getView().showLoading();
            }

            @Override
            public void onCompleted() {
                getView().showContent();
            }
        });
    }

    public void delete(User user){
        DBWorkObservable.create(null, dbClient.deleteAsyn(user)).subscribe(new AysnSubscriber() {
            @Override
            public void onStarted() {
                getView().showLoading();
            }

            @Override
            public void onCompleted() {
                getView().showContent();
            }
        });
    }

    public void update(User user){
        DBWorkObservable.create(null, dbClient.updateAsyn(user)).subscribe(new AysnSubscriber() {
            @Override
            public void onStarted() {
                getView().showLoading();
            }

            @Override
            public void onCompleted() {
                getView().showContent();
            }
        });

    }
    public void query(){
        DBWorkObservable.create(null, dbClient.queryAsyn(User.class, Querys.queryForAll(User.class))).subscribe(new AysnSubscriber<List<User>>() {

            @Override
            public void onStarted() {
                getView().showLoading();
            }

            @Override
            public void onCompleted() {
                getView().showContent();
            }

            @Override
            public void onNext(List<User> users) {
                getView().setData(users);
            }
        });
    }
}
