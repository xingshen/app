package com.steptowin.common.factory;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class BroadCastFactory {
	public static final String ACTION_GLOBAL="action.steptowin.com";

	/**
	 * MEDIA_REMVOED:
	 * MEDIA_MOUNTED:
	 * MEDIA_EJECT:
	 * MEDIA_BUTTON:
	 * @return
	 */
	public static IntentFilter getSDCardReceiver(){
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addDataScheme("file"); 
        return intentFilter;
	}
	/**
	 * 电池
	 * @return
	 */
	public static IntentFilter getBatteryReceiver(){
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
		intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
		return intentFilter;
	}
	/**
	 * 网络
	 * @return
	 */
	public static IntentFilter getNetWorkReceiver(){
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		return intentFilter;
	}
	/**
	 *注册全局广播时使用
	 * @return
	 */
	public static IntentFilter getConfigReceiver(){
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(ACTION_GLOBAL);
		return intentFilter;
	}

}
