package com.steptowin.core.test;

import android.test.InstrumentationTestCase;

import com.steptowin.core.db.common.DBHelper;
import com.steptowin.core.db.dao.DaoImp;
import com.steptowin.core.http.okhttp.ApiExample;
import com.steptowin.core.parser.Person;
import com.steptowin.core.parser.Team;
import com.steptowin.core.parser.xing.XingXmlParser;
import com.steptowin.core.test.db.User;
import com.steptowin.core.tools.MD5_Tool;
import com.steptowin.core.tools.WLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/1/1.
 */
public class ExampleTest extends InstrumentationTestCase {
    public final static String ROOT = "root";

    public void testC() {
        Object object = anString();
        WLog.info("info","type="+object.getClass());
    }

    private int anInt(){
        return (int)a(1);
    }

    private String anString(){
        return (String)a("abc");
    }

    private <T,R>R a(T t){
//        if(t.getClass() == Integer.class){
//            Integer a = 0;
//            return (R)a;
//        } else if(t.getClass() == String.class){
//            return (R)"a";
//        }
        return (R)t;
    }

    private void db() {
        DBHelper.getHelper(getInstrumentation().getContext());
        DaoImp daoImp = new DaoImp(User.class);
        User user = new User();
        user.setName("user1");
        daoImp.insert(user);
    }

    private void xmlParser() {
        try {
            InputStream inputStream = getInstrumentation().getContext().getAssets().open("testXml.txt");
            XingXmlParser parser = new XingXmlParser();
//            User user = parser.parse(inputStream, User.class);
//            user.getUser_names();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void okhttpFinal() {
        ApiExample example = new ApiExample();
        example.login();
    }

    private void testRetrofit() {
        //        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://weixue.steptowin.com").build();
//
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<User> call = apiService.login("18158179499", "c4ca4238a0b923820dcc509a6f75849b");
//        User user = apiService.loginSync("18158179499", "c4ca4238a0b923820dcc509a6f75849b");
    }


    public interface ApiService {

        //zg135246   2256f36aa64ff69c86020ad3a5e0fc7b
        //1          c4ca4238a0b923820dcc509a6f75849b
        @GET("/msi/login.php")
        void login(@Query("login_name") String name, @Query("login_pass") String pass, retrofit2.Callback<User> callback);

        @FormUrlEncoded
        @POST("/msi/login.php")
        void loginPost(@Field("login_name") String name, @Field("login_pass") String pass, retrofit2.Callback<User> callback);

        @GET("/msi/login.php")
        Observable<User> loginObs(@Query("login_name") String name, @Query("login_pass") String pass);

        @FormUrlEncoded
        @POST("/msi/login.php")
        Observable<User> loginObsPost(@Field("login_name") String name, @Field("login_pass") String pass);

        //以map的形式传递参数
        @FormUrlEncoded
        @POST("/msi/save_share.php")
        Observable<BaseEntity> saveShare(@FieldMap Map<String, String> fields);

        //以map的形式传递参数（带文件，可以是File、流对象、Bitmap对象）
        @Multipart
        @POST("/msi/save_book.php")
        Observable<BaseEntity> saveBook(@PartMap Map<String, Object> fields);

        @GET("/msi/view_book.php")
        Observable<BaseEntity> viewBook(@Query("book_id") String id);

//        @POST("/msi/save_share.php")
//        Observable<BaseEntity> saveShare(@Body SaveShare share);

//        @FormUrlEncoded
//        @POST("/msi/save_share.php")
//        Observable<BaseEntity> saveShare(@Field("content") String content, @Field("type") String type);


    }

    private String md5(String s) {
        return MD5_Tool.encryptmd5(s);
    }

    private <T> T getC(Class<T> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {

        }
        return null;
    }


    private void thread() {
        System.out.print("hi thread");
        new Thread() {
            @Override
            public void run() {
                int a = 1;
                System.out.print(a + "");
            }
        }.start();
    }

    private void okhttp() {
//        RequestBody requestBody = null;
//        MultipartBuilder multipartBuilder = new MultipartBuilder();
//        multipartBuilder.type(MultipartBuilder.FORM);
//        FormEncodingBuilder paramBuilder = new FormEncodingBuilder();
//        multipartBuilder.addFormDataPart("login_name", "18158179499");
//        multipartBuilder.addFormDataPart("login_pass", "c4ca4238a0b923820dcc509a6f75849b");
//
//        multipartBuilder.addPart(paramBuilder.build());
//        requestBody = multipartBuilder.build();
//
//
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//
//        Request request = new Request.Builder().url("http://weixue.steptowin.com/msi/login.php").post(requestBody)
//                .build();
//        com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//                         @Override
//                         public void onResponse(Response response) throws IOException {
//                             String result = response.body().string();
//                             System.out.print(result);
//                         }
//
//                         @Override
//                         public void onFailure(Request request, IOException e) {
//                             Log.w("w", "onFailure:" + e);
//                             e.printStackTrace();
//                             Object[] objArr = new Object[3];
//                         }
//                     }
//
//        );
    }

    private void bean2Xml() {
        Person p = new Person("zg1", "25");
        Person p1 = new Person("zg2", "24");
        Team team = new Team("xing");
        team.add(p);
        team.add(p1);
//        XStream stream = new XStream(new DomDriver());
//        String xml = stream.toXML(team);
////        Person p1 = (Person) stream.fromXML("<?xml version=\"1.0\" ?> "+xml);
//        System.out.print("xml:" + xml);
    }
}
