/*
 * Copyright (C) 2015 彭建波(pengjianbo@finalteam.cn), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.steptowin.core.http.okhttp;

import android.content.Context;

import com.steptowin.core.common.Constants;
import com.steptowin.core.http.common.CookieHelper;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @Desc:
 * @Author: zg
 * @Time: 2016/2/18 17:26
 */
public class OkHttpFinalConfiguration {

    private long timeout = Constants.Http.REQ_TIMEOUT;
    private CookieJar cookieJar;
    private boolean retryOnConnectionFailure;

    private OkHttpFinalConfiguration(final Builder builder) {
        this.timeout = builder.timeout;
        this.cookieJar = builder.cookieJar;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
    }

    public static class Builder {
        private long timeout;
        private CookieJar cookieJar = CookieJar.NO_COOKIES;
        private boolean retryOnConnectionFailure;

        public Builder() {
            retryOnConnectionFailure = true;
        }

        /**
         * 设置timeout
         *
         * @param timeout
         * @return
         */
        public Builder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 设置cookie jar
         *
         * @param cookieJar
         * @return
         */
        public Builder setCookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        public Builder setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public Builder defaultCookieJar(final Context context) {
            CookieJar cookieJar = new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    return CookieHelper.getOkHttp3Cookies(context);
                }
            };
            setCookieJar(cookieJar);
            return this;
        }

        public OkHttpFinalConfiguration build() {
            return new OkHttpFinalConfiguration(this);
        }
    }

    public long getTimeout() {
        return timeout;
    }


    public CookieJar getCookieJar() {
        return cookieJar;
    }


    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }
}
