package com.steptowin.audio;

import android.text.TextUtils;

import java.io.File;

public class FileUtil {

    /**
     * 根据路径删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {

        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        return file.delete();

    }

}
