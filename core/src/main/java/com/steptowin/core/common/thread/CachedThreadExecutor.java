package com.steptowin.core.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * @Desc: 可重复使用线程池
 * @Author: zg
 * @Time: 2016/3/8 16:06
 */
public class CachedThreadExecutor implements Executor {
    static final String THREAD_PREFIX = "Xing-";
    static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";
    Executor executor;

    public static CachedThreadExecutor create(){
        return new CachedThreadExecutor();
    }

    private CachedThreadExecutor(){
        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                }, IDLE_THREAD_NAME);
            }
        });
    }
    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
