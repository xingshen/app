package com.steptowin.common.tool.context;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * 提供Activity的一些工具方法。
 * 
 * @author wjd
 * 
 */
public interface ActivityAction {


	/**
	 * 获取系统当前的亮度值
	 * 
	 * @return
	 */
	public int getBrightness() ;

	/**
	 * 是否当前活动处于全屏状态,全屏状态下将不显示状态栏
	 * 
	 * @return
	 */
	public boolean isFullScreen();

	/**
	 * 切换全屏状态
	 */
	public void toggleFullScreen() ;

	/**
	 * 锁住为竖屏状态
	 */
	public void lockPortrait() ;

	/**
	 * 锁住为横屏状态
	 */
	public void lockLandscape();

	/**
	 * 解锁屏幕,允许感应
	 */
	public void unLockOrientation() ;

	/**
	 * 设置亮度
	 * 
	 * @param brightness
	 *            亮度级别。0-255递增
	 */
	public void setBrightness(int brightness) ;

	/**
	 * 锁定系统休眠。使用<use-permission android:name="WAKE_LOCK">也可以达到锁定系统休眠。<br>
	 * 但是背光会变暗，wakeLock方法通过操纵Window的LayoutParams来改善锁定休眠的背光问题。
	 */
	public void wakeLock() ;
	/**
	 * 将与某个View关联的软键盘隐藏掉。
	 * 
	 * @param view
	 */
	public void hideInput(View editView) ;

	/**
	 * 为程序创建一个桌面的快捷方式.注意，该快捷方式只创建一次
	 * 
	 * @param startUpClass
	 *            必须为Category是Main,Action是Launcher的Class
	 */
	public void createShortCut(Class<? extends Activity> startUpClass) ;

	/**
	 * 简单的Spinner适配器实现
	 * 
	 * @param objects
	 * @return
	 */
	public ArrayAdapter<Object> getSpinnerAdapter(Object[] objects) ;

	/**
	 * 简单的Spinner适配器实现重载
	 * 
	 * @param list
	 * @return
	 */
	public ArrayAdapter<Object> getSpinnerAdapter(List<? extends Object> list) ;
	//public ViewTool getmViewTool() ;

	/**
	 * @param context
	 * @param app_name
	 * @return
	 */
	public boolean hasShortCut(Context context, int app_name);

}
