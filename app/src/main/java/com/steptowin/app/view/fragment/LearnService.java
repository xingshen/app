package com.steptowin.app.view.fragment;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * desc:
 * author：zg
 * date:17/2/8
 * time:下午5:10
 */
public class LearnService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("zhou","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("zhou","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("zhou","onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("zhou","onUnbind");
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("zhou","onBind");
        String binder = intent.getStringExtra("binder");
        if(TextUtils.equals(binder,"1")){
            return new Binder1();
        }

        if(TextUtils.equals(binder,"2")){
            return new Binder2();
        }

        return null;
    }

    public static class Binder1 extends Binder{

    }

    public static class Binder2 extends Binder{

    }
}
