package com.steptowin.core.tools;

import java.util.concurrent.Executor;

/**
 * @Desc:
 * @Author: zg
 * @Time:
 */
public class Tools {

    public static class SynchronousExecutor implements Executor {
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }



    private Tools() {
        // No instances.
    }
}
