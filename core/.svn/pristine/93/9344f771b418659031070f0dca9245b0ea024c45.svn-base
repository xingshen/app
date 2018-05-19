package com.steptowin.core.common.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * @Desc: Executor that runs tasks on Android's main thread.
 * @Author: zg
 * @Time:
 */
public class MainThreadExecutor implements Executor{
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void execute(Runnable r) {
        handler.post(r);
    }
}
