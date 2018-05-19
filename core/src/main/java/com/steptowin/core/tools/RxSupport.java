package com.steptowin.core.tools;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Desc: 为rxjava提供支持的抽象类，负责生成{@link Observable}实例
 * @Author: zg
 * @Time: 2016/3/9 17:49
 */
public abstract class RxSupport {
    public static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();

    public abstract Observable createObservable(Func1 func1);

    public static boolean hasRxJavaOnClasspath() {
        try {
            Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
