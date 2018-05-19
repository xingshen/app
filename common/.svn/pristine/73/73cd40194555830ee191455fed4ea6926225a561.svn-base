package com.steptowin.common.tool.context;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.util.List;


public class BaseContext implements BaseContextWrapper{

	/**
	 * 指向本地引用
	 */
	private Context me;
	
	private ContextTool tool;
	
	public void infoLog(Object msg) {
		tool.infoLog(msg);
	}

	public void warnLog(Object msg) {
		tool.warnLog(msg);
	}

	public void verboseLog(Object msg) {
		tool.verboseLog(msg);
	}

	public BaseContext(Context base) {
		me=base;
		if(tool==null)
			tool=new ContextTool(base);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	/**
	 * 获取当前程序中的本地目标
	 * @param localIntent
	 * @return
	 */
	public Intent getLocalIntent(Class<?extends Context> localIntent)
	{
		//Intent.FLAG_ACTIVITY_CLEAR_TOP  启动该activity后 把在该activity启动的activity  clear
		//Intent.FLAG_ACTIVITY_REORDER_TO_FRONT 启动该activity 把该activity 置前
		return new Intent(me,localIntent).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	/**
	 * 获取当前程序中的本地目标
	 * @param localIntent
	 * @return
	 */
	public Intent getLocalIntent(Class<?extends Context> localIntent,int[] flag)
	{
		//Intent.FLAG_ACTIVITY_CLEAR_TOP  启动该activity后 把在该activity启动的activity  clear
		//Intent.FLAG_ACTIVITY_REORDER_TO_FRONT 启动该activity 把该activity 置前
		return new Intent(me,localIntent).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	
	/**
	 * 以无参数的模式启动Activity。
	 * @param activityClass
	 */
	public void startActivity(Class<? extends Activity> activityClass) {
		me.startActivity(
				getLocalIntent(activityClass));
	}
	
	
	/**
	 * 以无参数的模式启动service
	 */
	public void startService(Class<? extends Service> serviceClass) {
		me.startService(
				getLocalIntent(serviceClass));
	}
	
	/**
	 * 以无参数的模式关闭service。<br>
	 * <strong>对于bind启动的服务，需要释放所有的ServiceConnection才可关闭。</strong>
	 * @param serviceClass
	 */
	public void stopService(Class<? extends Service> serviceClass) {
		Intent intent=new Intent(me,serviceClass);
		me.stopService(intent);
	}

	/**
	 * @param msg
	 * @see practice.utils.ContextTool#showMessage(java.lang.Object)
	 */
	public void showMessage(Object msg) {
		tool.showMessage(msg);
	}

	/**
	 * @param msg
	 * @see practice.utils.ContextTool#showLongMessage(java.lang.Object)
	 */
	public void showLongMessage(Object msg) {
		tool.showLongMessage(msg);
	}

	/**
	 * @param msg
	 * @see practice.utils.ContextTool#debugLog(java.lang.Object)
	 */
	public void debugLog(Object msg) {
		tool.debugLog(msg);
	}

	/**
	 * @param msg
	 * @see practice.utils.ContextTool#errorLog(java.lang.Object)
	 */
	public void errorLog(Object msg) {
		tool.errorLog(msg);
	}

	/**
	 * @param msg
	 * @see practice.utils.ContextTool#testLog(java.lang.Object)
	 */
	public void testLog(Object msg) {
		tool.testLog(msg);
	}

	public boolean containsActivity(Intent intent) {
		return tool.containsActivity(intent);
	}

	public boolean isAppInstalled(String uri) {
		return tool.isAppInstalled(uri);
	}

	public boolean isDebugMode() {
		return tool.isDebugMode();
	}

	public String getAppVersion(String uri) {
		return tool.getAppVersion(uri);
	}

	/**
	 *
	 * @Title: isBackground
	 * @Description: 判断程序是否在后台运行
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	public void clear(){
		tool.clear();
		me=null;
	}
}
