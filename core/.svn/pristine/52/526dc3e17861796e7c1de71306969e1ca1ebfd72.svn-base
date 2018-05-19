package com.steptowin.core.cache.file;

import android.support.annotation.NonNull;

import com.steptowin.core.tools.AppTool;
import com.steptowin.core.tools.StringTool;

import org.apache.commons.lang3.StringUtils;

/**
 * @Desc: 提供帮助文件加载的静态方法
 * @Author: zg
 * @Time:
 */
public class FileLoaderHelper {

    /**
     * @Desc: 获取文件本地存储路径
     * @Author: zg
     * @Time: 2016/2/19 14:19
     */
    public static String generateFilePath(@NonNull FileLoader.Configuration configuration) {
        String targetPath = "";

        if (!StringTool.isEmpty(configuration.target))
            return configuration.target;

        String parentPath = "";
        if (null == configuration.context)
            parentPath = AppTool.getExternalStoragePath();
        else
            parentPath = AppTool.getAppFileRootDir(configuration.context);

        if (!StringUtils.isEmpty(parentPath))
            targetPath = parentPath + generateFileNameFromUrl(configuration.url);
        return targetPath;
    }

    public static String generateFileNameFromUrl(String url) {
        if (StringTool.isEmpty(url))
            return "";
        url.replace("\\", "/");
        String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
        return filename;
    }
}
