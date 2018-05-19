/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.steptowin.core.http.okhttp;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;


/**
 * @Desc: {@link OkHttpClient}配置初始化
 * @Author: zg
 * @Time: 2016/2/18 17:26
 */
public class OkHttpFinal {

    private OkHttpClient okHttpClient;

    private static OkHttpFinal okHttpFinal;
    private OkHttpFinalConfiguration configuration;

    private OkHttpFinal() {
    }

    public synchronized void init(OkHttpFinalConfiguration configuration) {
        this.configuration = configuration;

        long timeout = configuration.getTimeout();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .followRedirects(true);

        CookieJar cookieJar = configuration.getCookieJar();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        builder.retryOnConnectionFailure(configuration.isRetryOnConnectionFailure());

        okHttpClient = builder.build();
    }

    public synchronized void defaultInit(Context context){
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        builder.defaultCookieJar(context);
        OkHttpFinalConfiguration configuration = builder.build();
        init(configuration);
    }

    public static OkHttpFinal getInstance() {
        if (okHttpFinal == null) {
            okHttpFinal = new OkHttpFinal();
        }
        return okHttpFinal;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public long getTimeout() {
        return configuration.getTimeout();
    }
}
