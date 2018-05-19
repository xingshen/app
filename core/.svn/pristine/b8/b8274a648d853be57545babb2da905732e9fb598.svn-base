package com.steptowin.core.http.okhttp;

import java.io.File;

/**
 *@desc: okhttp帮助
 *@author zg
 *@time 16/6/21
 */
public class OkHttpHelper {

    public static String generateDownloadCallKey(String url,File target){
        String filePath = null == target?"":target.getAbsolutePath();
        return url+"__"+filePath;
    }
}
