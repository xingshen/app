package com.steptowin.core.db.client;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Subscription;

/**
 * @Desc: 对基于rxjava机制的数据库请求进行管理
 * @Author: zg
 * @Time: 2016/1/19 16:49
 */
public class DBManager {
    static final String DEFAULT_TAG = "DBManager";
    private static HashMap<Context, List<Subscription>> contextSubscriptions = new HashMap<>();
    private static HashMap<String, List<Subscription>> tagSubscriptions = new HashMap<>();

    public static void addDBWork(Context context, Subscription subscription) {
        if (null == context) {
            addDBWork(DEFAULT_TAG, subscription);
            return;
        }
        if (!contextSubscriptions.containsKey(context)) {
            List<Subscription> subscriptions = new ArrayList<>();
            contextSubscriptions.put(context, subscriptions);
        }
        contextSubscriptions.get(context).add(subscription);
    }

    public static void addDBWork(String tag, Subscription subscription) {
        if (!tagSubscriptions.containsKey(tag)) {
            List<Subscription> subscriptions = new ArrayList<>();
            tagSubscriptions.put(tag, subscriptions);
        }
        tagSubscriptions.get(tag).add(subscription);
    }

    public static void unsubscribeAll() {
        Set<Map.Entry<Context, List<Subscription>>> contextSet = contextSubscriptions.entrySet();
        for (Map.Entry<Context, List<Subscription>> entry : contextSet) {
            unscbscribeByContext(entry.getKey());
        }
        contextSubscriptions.clear();

        Set<Map.Entry<String, List<Subscription>>> tagtSet = tagSubscriptions.entrySet();
        for (Map.Entry<String, List<Subscription>> entry : tagtSet) {
            unscbscribeByTag(entry.getKey());
        }
        tagSubscriptions.clear();
    }

    public static void unscbscribeByContext(Context context) {
        List<Subscription> subscriptions = contextSubscriptions.get(context);
        if (null == subscriptions)
            return;
        for (Subscription subscription : subscriptions) {
            unscbscribe(subscription);
        }
        subscriptions.clear();
    }

    public static void unscbscribeByTag(String tag) {
        List<Subscription> subscriptions = tagSubscriptions.get(tag);
        if (null == subscriptions)
            return;
        for (Subscription subscription : subscriptions) {
            unscbscribe(subscription);
        }
        subscriptions.clear();
    }

    private static void unscbscribe(Subscription subscription) {
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
