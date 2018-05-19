package com.steptowin.app;

import com.alibaba.android.arouter.launcher.ARouter;
import com.steptowin.albums.LocalImageHelper;
import com.steptowin.common.base.BaseApplication;
import com.steptowin.common.tool.ResTool;
import com.steptowin.core.cache.ImageLoader;
import com.steptowin.core.common.AppVariables;
import com.steptowin.core.common.Constants;
import com.steptowin.core.http.okhttp.OkHttpFinal;
import com.steptowin.core.http.retrofit.RetrofitClient;
import com.steptowin.core.tools.AppStorage;
import com.steptowin.minivideo.ffmpeg.FFmpegController;

import java.io.IOException;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Desc:
 * @Author: zg
 * @Time: 2015/12/25 18:52
 */
public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.getInstance().domain(AppVariables.getString(Constants.Http.KEY_DOMAIN))
                .addCookieManager(this).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //拦截添加参数
//                HttpUrl httpUrl = request.url().newBuilder().addQueryParameter("","").build();
//                request =  request.newBuilder().url(httpUrl).build();
                return chain.proceed(request);
            }
        }).build();

        OkHttpFinal.getInstance().defaultInit(this);
        AppStorage.setRootFilePath(this);
        FFmpegController.getInstance(this);
    }

    @Override
    protected void init() {
        super.init();
        //数据库初始化
        DBHelper helper = DBHelper.getHelper(this);

        LocalImageHelper.getHelper(this);
        ImageLoader.init(this);
        ResTool.getInstance(this);

//        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(Exception error) {
                // FFmpeg is not supported by device
            }
        });
    }

    @Override
    protected void initPre() {
        AppVariables.put(Constants.Http.KEY_DOMAIN, "http://192.168.10.141:8000/");
        AppVariables.put(Constants.KEY_DEBUG, true);
        AppVariables.put(Constants.KEY_IS_UMENG_PATH_OPEN, false);
    }

    @Override
    protected void initAfter() {

    }

}
