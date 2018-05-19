package com.steptowin.common.tool.context;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.steptowin.common.factory.IntentFactory;

import java.util.List;

/**
 * 提供Activity的一些工具方法。
 * 
 * @author zg
 * 
 */
public class ActivityTool {
	private Activity mActivity;

	private DialogTool dialogTool;

	public ActivityTool(Activity activity) {
		mActivity = activity;

		dialogTool = new DialogTool(activity);
	}

	/**
	 * 获取系统当前的亮度值
	 * 
	 * @return
	 */
	public int getBrightness() {
		try {
			return Settings.System.getInt(mActivity.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 锁住为竖屏状态
	 */
	public void lockPortrait() {
		mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 锁住为横屏状态
	 */
	public void lockLandscape() {
		mActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 解锁屏幕,允许感应
	 */
	public void unLockOrientation() {
		mActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	/**
	 * 锁定系统休眠。使用<use-permission android:name="WAKE_LOCK">也可以达到锁定系统休眠。<br>
	 * 但是背光会变暗，wakeLock方法通过操纵Window的LayoutParams来改善锁定休眠的背光问题。
	 */
	public void wakeLock() {
		mActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}




}
