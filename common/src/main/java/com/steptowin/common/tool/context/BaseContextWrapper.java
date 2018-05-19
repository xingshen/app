package com.steptowin.common.tool.context;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

public interface BaseContextWrapper {

	/**
	 * 获取当前程序中的本地目标
	 * 
	 * @param localIntent
	 * @return
	 */
	public Intent getLocalIntent(Class<? extends Context> localIntent);

	/**
	 * 以无参数的模式启动Activity。
	 * 
	 * @param activityClass
	 */
	public void startActivity(Class<? extends Activity> activityClass);

	/**
	 * 以无参数的模式启动service
	 */
	public void startService(Class<? extends Service> serviceClass);

	/**
	 * 以无参数的模式关闭service。<br>
	 * <strong>对于bind启动的服务，需要释放所有的ServiceConnection才可关闭。</strong>
	 * 
	 * @param serviceClass
	 */
	public void stopService(Class<? extends Service> serviceClass);

	public boolean containsActivity(Intent intent);

	public boolean isAppInstalled(String uri);

	public boolean isDebugMode();

	public String getAppVersion(String uri);
}
