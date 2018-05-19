package com.steptowin.app.presenter;

import com.steptowin.app.model.bean.Login;
import com.steptowin.app.model.service.HttpService;
import com.steptowin.app.view.activity.http.HttpView;
import com.steptowin.common.base.BasePresenter;
import com.steptowin.core.common.BaseEntity;
import com.steptowin.core.http.retrofit.NetWorkObservable;
import com.steptowin.core.http.retrofit.NetWorkSubscriber;
import com.steptowin.core.http.retrofit.RetrofitClient;
import com.steptowin.core.tools.WLog;

/**
 * desc: 网络请求处理
 * author：zg
 * date:2016-03-30
 */
public class HttpPresenter extends BasePresenter<HttpView> {


    public static class LoginInfo extends BaseEntity{
        private Login data;

        public Login getData() {
            return data;
        }

        public void setData(Login data) {
            this.data = data;
        }
    }
    public void doLogin(String user_name, String user_pass) {
        HttpService service = RetrofitClient.createApi(HttpService.class);
        NetWorkObservable.create(null, service.loginObs(user_name, user_pass)).subscribe(new NetWorkSubscriber<LoginInfo>() {

            @Override
            public void onStart() {
                super.onStart();
                getView().showLoading();
                WLog.info("info", "Start----thread=" + Thread.currentThread());
            }

            @Override
            public void onSuccess(LoginInfo baseEntity) {
//                getView().setData(baseEntity);
                getView().showContent();
//                WLog.info("info", "Success----thread=" + Thread.currentThread());
                WLog.info("info", "success----message=" + baseEntity.getMessage());
            }

            @Override
            public void onFailure(LoginInfo baseEntity) {
                getView().showContent();
                WLog.info("info", "failure----message=" + baseEntity.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                WLog.info("info", "onError----e=" + e);
            }
        });
    }

    public void listShare(){

    }

}
