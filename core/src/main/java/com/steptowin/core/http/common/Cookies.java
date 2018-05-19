package com.steptowin.core.http.common;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc: 对cookie集合的操作
 * @Author: zg
 * @Time: 2016/3/1 18:19
 */
public class Cookies {

    private final HashMap<String, ConcurrentHashMap<String, HttpCookie>> cookies;

    public Cookies() {
        cookies = new HashMap<String, ConcurrentHashMap<String, HttpCookie>>();
    }

    public void add(String host, HttpCookie httpCookie) {
        String name = getCookieToken(host, httpCookie);
        if (!httpCookie.hasExpired()) {//有效期大于0
            if (!cookies.containsKey(host))
                cookies.put(host,
                        new ConcurrentHashMap<String, HttpCookie>());
            cookies.get(host).put(name, httpCookie);
        } else {
            if (cookies.containsKey(host))
                cookies.get(host).remove(name);
        }
    }

    /**
     * cookie的唯一标识
     * @param host
     * @param cookie
     * @return
     */
    public String getCookieToken(String host, HttpCookie cookie) {
        return cookie.getName() + cookie.getDomain() + cookie.getVersion();
    }

    public List<HttpCookie> get(String host) {
        ArrayList<HttpCookie> ret = new ArrayList<HttpCookie>();
        if (cookies.containsKey(host))
            ret.addAll(cookies.get(host).values());
        return ret;
    }

    public List<HttpCookie> getAll() {
        ArrayList<HttpCookie> ret = new ArrayList<HttpCookie>();
        for (String host : cookies.keySet()) {
            ret.addAll(cookies.get(host).values());
        }
        return ret;
    }

    public boolean remove(String host, HttpCookie cookie) {
        String name = getCookieToken(host, cookie);
        if (cookies.containsKey(host)
                && cookies.get(host).containsKey(name)) {
            cookies.get(host).remove(name);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAll() {
        cookies.clear();
        return true;
    }

    public List<URI> getURIs() {
        ArrayList<URI> ret = new ArrayList<URI>();
        for (String key : cookies.keySet())
            try {
                ret.add(new URI(key));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        return ret;
    }
}
