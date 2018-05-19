package com.steptowin.common.tool;

import android.app.Application;
import android.support.annotation.ColorRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

import com.steptowin.core.cache.memory.CacheDefaultFactory;
import com.steptowin.core.cache.memory.MemoryCacheAware;


/**
 * desc:使用application上下文对象，生成资源
 * 注意:需要在Application 或者BaseApplication里面执行初始化操作{@link #getInstance(Application)}
 * author：zg
 * date:2016/5/26 0026
 * time:下午 2:03
 */
public class ResTool {
    private static ResTool instance;
    static Application context;

    private static MemoryCacheAware<String,String> mLruMemoryCacheString = CacheDefaultFactory
            .getInstance().getDefaultMemoryCacheString();

    private ResTool(){}

    private ResTool(Application context){
        this.context = context;
    }

    public static ResTool getInstance(Application context){
        if(null == instance){
            instance = new ResTool(context);
        }
        return instance;
    }

    public static String getString(@StringRes int id){
        String key = ""+id;
        if(null == mLruMemoryCacheString.get(key))
            mLruMemoryCacheString.put(key,context.getString(id));
        return mLruMemoryCacheString.get(key);
    }

    public static int getInt(@IntegerRes int id){
        return context.getResources().getInteger(id);
    }

    public static int getColor(@ColorRes int id){
        return context.getResources().getColor(id);
    }

}
