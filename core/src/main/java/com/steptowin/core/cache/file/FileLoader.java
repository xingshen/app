package com.steptowin.core.cache.file;

import android.content.Context;
import android.support.annotation.NonNull;

import com.steptowin.core.http.okhttp.OkHttpRequest;
import com.steptowin.core.http.okhttp.download.FileDownloadCallback;
import com.steptowin.core.tools.StringTool;

import java.io.File;

/**
 * @Desc: 文件加载器
 * @Author: zg
 * @Time:
 */
public class FileLoader {

    //配置加载对象，提高扩展性

    public static void load(final Configuration configuration) {
        if (null == configuration)
            return;
        final File file = new File(FileLoaderHelper.generateFilePath(configuration));
        if (file.exists()) {
            if (null != configuration.loadingListener) {
                configuration.loadingListener.onLoadingStart();
                configuration.loadingListener.onLoading(100);
                configuration.loadingListener.onLoadingComplete(file);
            }
        } else if (!StringTool.isEmpty(configuration.url)) {
            OkHttpRequest.download(configuration.url, file, new FileDownloadCallback() {
                @Override
                public void onStart() {
                    if (null != configuration.loadingListener)
                        configuration.loadingListener.onLoadingStart();
                }

                @Override
                public void onProgress(int progress, long networkSpeed) {
                    if (null != configuration.loadingListener)
                        configuration.loadingListener.onLoading(progress);
                }

                @Override
                public void onDone() {
                    if (null != configuration.loadingListener)
                        configuration.loadingListener.onLoadingComplete(file);
                }

                @Override
                public void onFailure() {
                    if (null != configuration.loadingListener)
                        configuration.loadingListener.onLoadingFail();
                }
            });
        } else {
            if (null != configuration.loadingListener) {
                configuration.loadingListener.onLoadingStart();
                configuration.loadingListener.onLoadingFail();
            }
        }

    }

    public static void load(String url) {
        load(url, null);
    }

    public static void load(String url, Context context) {
        load(url, context, null);
    }


    public static void load(String url, Context context, String target) {
        load(url, context, target, true);
    }

    public static void load(String url, Context context, String target, boolean cache) {
        load(url, context, target, cache, null);
    }

    public static void load(@NonNull String url, Context context, String target, boolean cache, FileLoadingListener listener) {
        Configuration configuration = Configuration.create(null);
        configuration.context = context;
        configuration.url = url;
        configuration.target = target;
        configuration.cache = cache;
        configuration.loadingListener = listener;
        load(configuration);
    }

    /**
     * @Desc: 文件加载时的配置信息
     * @Author: zg
     * @Time: 2016/2/19 15:49
     */
    public static class Configuration {
        /**
         * 程序上下文，用于获取app的文件根目录
         */
        public Context context;
        public String url = "";

        /**
         * 文件要存储的路径，如果不配置，则默认为app文件存储根目录
         */
        public String target;

        /**
         * 是否优先使用缓存文件，每次都下载可以置为false
         */
        public boolean cache = true;
        public FileLoadingListener loadingListener;

        private Configuration(Context context1) {
            context = context1;
        }

        public static Configuration create(Context context) {
            return new Configuration(context);
        }
    }

}
