package com.steptowin.app.model.service;

import com.steptowin.app.presenter.HttpPresenter;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * desc:
 * author：zg
 * date:2016-03-30
 */
public interface HttpService {

    @GET("msi/login.php")
    Observable<HttpPresenter.LoginInfo> loginObs(@Query("login_name") String name, @Query("login_pass") String pass);

}
