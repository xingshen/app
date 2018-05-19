package com.steptowin.core.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * @Desc: 对universalimageloader的再封装，提供最简洁的调用方式。
 * 如果都不合适，可用{@link #getImageLoader()}重新定义.
 * 注意:在程序程序时,调用{@link #init(Context)}初始化.
 * @Author: zg
 * @Time: 2016/1/26 21:04
 */
public class ImageLoader {
    static com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    static DisplayImageOptions defaultOptions;

    static {
        /**
         DisplayImageOptions options;
         options = new DisplayImageOptions.Builder()
         .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
         .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
         .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
         .cacheInMemory(true)//设置下载的图片是否缓存在内存中
         .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
         .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
         .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
         .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
         .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
         //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
         //设置图片加入缓存前，对bitmap进行设置
         //.preProcessor(BitmapProcessor preProcessor)
         .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
         .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
         .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
         .build();//构建完成
         */
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static void init(Context context) {
        /**
         *ImageLoaderConfiguration config = new ImageLoaderConfiguration
         .Builder(context)
         .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
         .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
         .threadPoolSize(3)//线程池内加载的数量
         .threadPriority(Thread.NORM_PRIORITY - 2)
         .denyCacheImageMultipleSizesInMemory()
         .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
         .memoryCacheSize(2 * 1024 * 1024)
         .discCacheSize(50 * 1024 * 1024)
         .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
         .tasksProcessingOrder(QueueProcessingType.LIFO)
         .discCacheFileCount(100) //缓存的文件数量
         .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
         .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
         .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
         .writeDebugLogs() // Remove for release app
         .build();//开始构建
         */
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheSizePercentage((int) (100.0f / 8))
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        init(configuration);
    }

    public static void init(ImageLoaderConfiguration configuration) {
        imageLoader.init(configuration);
    }

    public static com.nostra13.universalimageloader.core.ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void displayImage(ImageView imageView, int resId) {
        imageLoader.displayImage("drawable://"+resId, new ImageViewAware(imageView));
    }

    public static void displayImage(ImageView imageView, String uri) {
        imageLoader.displayImage(uri, new ImageViewAware(imageView));
    }

    public static void displayImage(ImageView imageView, String uri, int defaultImageOnLoading) {
        displayImage(imageView, uri, defaultImageOnLoading, 0);
    }

    public static void displayImage(ImageView imageView, String uri, int defaultImageOnLoading, int defaultImageForEmpty) {
        displayImage(imageView, uri, defaultImageOnLoading, defaultImageForEmpty, 0);
    }

    public static void displayImage(ImageView imageView, String uri, int defaultImageOnLoading, int defaultImageForEmpty, int defaultImageOnFail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cloneFrom(defaultOptions).showImageOnLoading(defaultImageOnLoading).showImageForEmptyUri(defaultImageForEmpty).showImageOnFail(defaultImageOnFail).build();
        imageLoader.displayImage(uri, new ImageViewAware(imageView), options);
    }

    public static void displayImage(ImageView imageView, String uri, ImageLoadingListener listener) {
        imageLoader.displayImage(uri, new ImageViewAware(imageView), defaultOptions, listener);
    }

    public static void displayImage(ImageView imageView, String uri, ImageLoadingProgressListener listener) {
        imageLoader.displayImage(uri, new ImageViewAware(imageView), defaultOptions, null, listener);
    }
}
