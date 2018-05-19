package com.steptowin.core.http.common;

import android.content.Context;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;

/**
 * @Desc: 从持久层获取cookie
 * @Author: zg
 * @Time: 2016/1/16 17:44
 */
public class CookieHelper {

    public static List<HttpCookie> getCookies(Context context) {
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
        return persistentCookieStore.getCookies();
    }

    public static List<Cookie> getOkHttp3Cookies(Context context) {
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
        List<HttpCookie> cookies = persistentCookieStore.getCookies();
        List<Cookie> okHttpCookies = new ArrayList<>();
        for (HttpCookie cookie :
                cookies) {
            okHttpCookies.add(httpCookie2OkHttpCookie(cookie));
        }
        return okHttpCookies;
    }


    public static String getCookie(Context context, String cookieName) {
        List<HttpCookie> cookies = getCookies(context);
        for (HttpCookie httpCookie : cookies) {
            if (httpCookie.getName().equals(cookieName))
                return httpCookie.getValue();
        }
        return "";
    }

    private static Cookie httpCookie2OkHttpCookie(HttpCookie httpCookie) {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name(httpCookie.getName());
        builder.value(httpCookie.getValue());
        builder.domain(httpCookie.getDomain());
        builder.path(httpCookie.getPath());
        return builder.build();
    }
}
