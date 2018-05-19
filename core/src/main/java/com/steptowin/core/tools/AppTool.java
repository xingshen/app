package com.steptowin.core.tools;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2016/1/21.
 */
public class AppTool {

    public static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    /**
     * @Desc: 获取外部存储根目录
     * Traditionally this is an SD
     * card, but it may also be implemented as built-in storage in a device that
     * is distinct from the protected internal storage and can be mounted as a
     * filesystem on a computer.
     * @Author: zg
     * @Time: 2016/2/19 15:58
     */
    public static String getExternalStoragePath() {
        if (isExternalStorageAvailable()) {
            File sdDir = Environment.getExternalStorageDirectory();
            return sdDir.getAbsolutePath()+"/";
        }
        return "";
    }


    /**
     * @Desc: 获取app文件存储根目录
     * @Author: zg
     * @Time: 2016/2/18 18:47
     */
    public static String getAppFileRootDir(Context context) {
        String STORE_ROOT = "";
        if (hasSDCard()) {
            if (null != context.getExternalFilesDir(null))
                STORE_ROOT = context.getExternalFilesDir(null)
                        .getAbsolutePath() + "/";
        } else {
            STORE_ROOT = context.getFilesDir().getAbsolutePath() + "/";
        }
        return STORE_ROOT;
    }

    /**
     * 外部存储是否可用 (存在且具有读写权限)
     *
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return
     */
    static public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部空间大小
     *
     * @return
     */
    static public long getTotalInternalMemorySize() {
        File file = Environment.getDataDirectory();// Gets the Android data
        // directory
        StatFs stat = new StatFs(file.getPath());
        long blockSize = stat.getBlockSize(); // 每个block 占字节数
        long totalBlocks = stat.getBlockCount(); // block总数
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机外部可用空间大小
     *
     * @return
     */
    static public long getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();// 获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取手机外部总空间大小
     *
     * @return
     */
    static public long getTotalExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory(); // 获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return 0;
        }
    }
}
