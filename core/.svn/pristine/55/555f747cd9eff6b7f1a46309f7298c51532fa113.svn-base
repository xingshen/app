package com.steptowin.core.common.callback;

/**
 * @Desc: 回调接口,代替随意写的接口作为回调
 * @Author: zg
 * @Time: 2016/3/8 17:30
 */
public interface ICallback<T> {

    void start();

    /**
     * Successful response.
     */
    void success(T t);

    /**
     * Unsuccessful response , or unexpected
     * exception.
     */
    void failure(Throwable throwable);
}
