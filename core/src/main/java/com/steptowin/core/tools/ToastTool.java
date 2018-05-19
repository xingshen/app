package com.steptowin.core.tools;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 
 * @Description 解决toast重复弹出的问题
 * @author zg
 * @date 2013-12-24 上午8:56:28
 * @record 修改记录：2015-6-16 对Toast提示进行封装，添加只有debug模式下才有的提示showDebugToast
 */
public class ToastTool {
	private static Toast mToast;
	private static boolean closed = false;// 一些debug模式下的toast不需要提示

	private static Handler mhandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		};
	};

	public static void closeToast() {
		closed = true;
	}

	public static void openToast() {
		closed = false;
	}

	public static void showToast(Context context, String text, int duration) {
		mhandler.removeCallbacks(r);
		if (null != mToast) {
			mToast.setText(text);
		} else {
			mToast = Toast.makeText(context, text, duration);
		}
		mhandler.postDelayed(r, 5000);
		mToast.show();
	}

	public static void showToast(Context context, int strId, int duration) {
		showToast(context, context.getString(strId), duration);
	}

	public static void showLongToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG);
	}

	public static void showShortToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}

	/**
	 * 只有在debug模式下才有的toast提示
	 * edit by zg
	 * @param context
	 * @param text    
	 * void
	 */
	public static void showDebugToast(Context context, String text) {
		if (!closed) {
			showToast(context, text, Toast.LENGTH_LONG);
		}
	}
}
