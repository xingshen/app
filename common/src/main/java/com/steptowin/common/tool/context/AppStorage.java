package com.steptowin.common.tool.context;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * App的存储位置
 * 
 * @author zhujun
 * 
 */
public class AppStorage {

	protected static String STORE_ROOT = "";
	protected static String STORE_FACE = "face/";
	protected static String STORE_IMAGE = "image/";
	protected static String STORE_AUDIO = "audio/";
	protected static String STORE_TMP = "tmp/";
	protected static String STORE_FILE = "file/";

	public static String CACHE_FACE = "F";
	public static String CACHE_IMAGE = "I";

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static void setRootFilePath(Context context) {
		// /mnt/sdcard/Android/data/com.hztbc.android.activities/files
		// /data/data/com.hztbc.android.activities/files
		if (hasSDCard()) {
			if (null != context.getExternalFilesDir(null))
				STORE_ROOT = context.getExternalFilesDir(null)
						.getAbsolutePath() + "/";
		} else {
			STORE_ROOT = context.getFilesDir().getAbsolutePath() + "/";
		}
		// 创建必要的目录
		_mkdir(getFacePath());
		_mkdir(getImagePath());
		_mkdir(getAudioPath());
		_mkdir(getTmpPath());
		_mkdir(getFilePath());
	}

	public static String getRootFilePath() {
		return STORE_ROOT;
	}

	/**
	 * 获得图片缓存的存储路径
	 * 
	 * @return
	 */
	public static String getImageCachePath(String type) {
		if (CACHE_FACE.equals(type))
			return getFacePath();
		else
			return getImagePath();
	}

	/**
	 * 获得头像的存储路径
	 * 
	 * @return
	 */
	public static String getFacePath() {
		return getRootFilePath() + STORE_FACE;
	}

	/**
	 * 获得图片的存储路径
	 * 
	 * @return
	 */
	public static String getImagePath() {
		return getRootFilePath() + STORE_IMAGE;
	}

	/**
	 * 获得声音的存储路径
	 * 
	 * @return
	 */
	public static String getAudioPath() {
		return getRootFilePath() + STORE_AUDIO;
	}

	/**
	 * 获得附件的存储路径
	 * 
	 * @return
	 */
	public static String getFilePath() {
		return getRootFilePath() + STORE_FILE;
	}

	/**
	 * 获得零时文件的存储路径
	 * 
	 * @return
	 */
	public static String getTmpPath() {
		return getRootFilePath() + STORE_TMP;
	}

	protected static void _mkdir(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
}
