package com.steptowin.core.http.retrofit;

import com.steptowin.core.test.UploadReponse;
import com.steptowin.core.tools.BitmapTool;
import com.steptowin.core.tools.WLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * @Desc: retrofit类库使用介绍
 * @Author: zg
 * @Time: 2016/1/16 15:58
 */
public class ApiExample {

    private void example() {
//              添加Cookie管理
//              CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(getContext()),
//                        CookiePolicy.ACCEPT_ALL);
//                CookieHandler.setDefault(cookieHandler);

        ServiceExample service = RetrofitClient.createApi(ServiceExample.class);


        Map<String, Object> bookParams = new HashMap<>();
        bookParams.put("name", "测试bookFieldsBimap2");
        bookParams.put("author", "zg");
        String bookPath = "/storage/emulated/0/weixue/1444281024920_696.bmp";
        File bookPic = new File(bookPath);
        bookParams.put("Filedata", BitmapTool.createBitmapFromFile(bookPath));

        NetWorkObservable.create(null,service.upload(bookPic)).subscribe(new NetWorkSubscriber<UploadReponse>() {
            @Override
            public void onSuccess(UploadReponse baseEntity) {
                WLog.info("info", "success----message=" + baseEntity.getMessage());
            }

            @Override
            public void onFailure(UploadReponse baseEntity) {
                WLog.info("info", "failure----message=" + baseEntity.getMessage());
            }

            @Override
            public void onError(Throwable e) {
                WLog.info("info", "onError----e=" + e);
            }
        });

//        NetWorkObservable.unsubscribeAll();
    }

    public interface ServiceExample {


        //zg135246   2256f36aa64ff69c86020ad3a5e0fc7b
        //1          c4ca4238a0b923820dcc509a6f75849b
        //get请求
//        @GET("/msi/login.php")
//        void login(@Query("login_name") String name, @Query("login_pass") String pass, retrofit.Callback<User> callback);

        //post请求
//        @FormUrlEncoded
//        @POST("/msi/login.php")
//        void loginPost(@Field("login_name") String name, @Field("login_pass") String pass, retrofit.Callback<User> callback);

        /**
         * @Desc: get请求，返回{@link Observable}对象
         * @Author: zg
         * @Time: 2016/1/16 15:56
         */
//        @GET("/msi/login.php")
//        Observable<User> loginObs(@Query("login_name") String name, @Query("login_pass") String pass);

        /**
         * @Desc: post请求，返回{@link Observable}对象
         * @Author: zg
         * @Time: 2016/1/16 15:56
         */
//        @FormUrlEncoded
//        @POST("/msi/login.php")
//        Observable<User> loginObsPost(@Field("login_name") String name, @Field("login_pass") String pass);

        /**
         * @Desc: 以map的形式传递参数
         * @Author: zg
         * @Time: 2016/1/16 15:57
         */
        @FormUrlEncoded
        @POST("/msi/save_share.php")
        Observable<com.steptowin.core.common.BaseEntity> saveShare(@FieldMap Map<String, String> fields);


        /**
         * @Desc: 以map的形式传递参数（带文件，）
         * 经测试使用TypedByteArray无效
         * @Author: zg
         * @Time: 2016/1/16 15:57
         */
        @Multipart
        @POST("/msi/save_book.php")
        Observable<com.steptowin.core.common.BaseEntity> saveBook(@PartMap Map<String, Object> fields);

        /**
         * @Desc: 上传单个文件，参数定义：
         * TypedFile tile = new TypedFile(String mimetype,File file);
         * @Author: zg
         * @Time: 2016/2/21 14:19
         */
        @Multipart
        @POST("/msi/upload_file.php")
        Observable<UploadReponse> upload(@Part("Filedata") Object object);


    }
}
