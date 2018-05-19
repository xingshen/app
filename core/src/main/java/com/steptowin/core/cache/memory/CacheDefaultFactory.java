package com.steptowin.core.cache.memory;

/**
 * 类描述：内存分配的默认工厂，单例模式<br>
 * 创建日期 2015年9月2日 上午11:04:37 <br>
 * 修改记录： <br>
 *
 * @author zg <br>
 */
public class CacheDefaultFactory {
    private static CacheDefaultFactory instance;
    static final int DEFAULT_MAX_STRING_SIZE = 2 * 1000;//2KB

    private static MemoryCache mLruMemoryCache;
    private static MemoryCacheAware mLruMemoryCacheString;

    private CacheDefaultFactory() {
        mLruMemoryCache = new LruMemoryCache((int) (Runtime.getRuntime()
                .maxMemory() / 8L));// 默认内存占app内存1/8
        mLruMemoryCacheString = new LruMemoryCacheString(DEFAULT_MAX_STRING_SIZE);
    }

    public static CacheDefaultFactory getInstance() {
        if (null == instance) {
            instance = new CacheDefaultFactory();
        }
        return instance;
    }

    public MemoryCache getDefaultMemoryCache() {
        return mLruMemoryCache;
    }

    public MemoryCacheAware<String, String> getDefaultMemoryCacheString() {
        return mLruMemoryCacheString;
    }

}
