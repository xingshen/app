package com.steptowin.core.http.common;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description 数据包
 * @author wjd
 * @date 2013-5-6 上午9:39:14
 */
public class PackageInfo {
	private Object[] result;// 返回结果
	private String type;// 发送标识
	private Activity activity;
	private String url;// 路径
	private Map<String, Object> map = new HashMap<String, Object>();// 存放携带的参数
	private RequestParams requestParams;
	public final static int CONNECTIONTIMEOUT = 5 * 1000; // 默认连接时间是10秒
	public final static int READTIMEOUT = 120 * 1000; // 默认读取时间是2分钟
	private int connectionTimeOut = CONNECTIONTIMEOUT; // 连接时间
	private int readTimeOut = READTIMEOUT; // 读取时间
	private Boolean isStop = false; // 是否需要停止连接网络，默认不需要
	private String className = ""; // 当前使用请求网络的类名
	private View watingView = null; // 加载等待的视图
	private long requestInterval;// 请求间隔，单位为毫秒。如果大于0，则表示一段间隔之后，重新请求网络
	private Boolean isWait = false; // 是否需要等待连接网络，默认不需要
	private RequestMethod requestMethod = RequestMethod.POST;

	public enum RequestMethod {
		GET, POST
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	/**
	 * @return the isWait
	 */
	public Boolean getIsWait() {
		return isWait;
	}

	/**
	 * @param isWait
	 *            the isWait to set
	 */
	public void setIsWait(Boolean isWait) {
		this.isWait = isWait;
	}

	/**
	 * @return the requestInterval
	 */
	public long getRequestInterval() {
		return requestInterval;
	}




	public View getWatingView() {
		return watingView;
	}

	/**
	 * 
	 * @Title: putRequestParam
	 * @Description: 追加请求参数
	 * @param key
	 * @param value
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo putRequestParam(String key, String value) {
		if (null != requestParams)
			requestParams.put(key, value);
		return this;
	}

	/**
	 * 
	 * @Title: putParam
	 * @Description: 用于存放携带的参数 便于回调使用(一般用不到)
	 * @param @param key
	 * @param @param value
	 * @param @return
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo putParam(String key, String value) {
		map.put(key, value);
		return this;
	}

	/**
	 * 
	 * @Title: putParam
	 * @Description: 用于存放携带的参数 便于回调使用(一般用不到)
	 * @param @param map
	 * @param @return
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo putParam(Map<String, Object> map) {
		this.map.putAll(map);
		return this;
	}

	/**
	 * 
	 * @Title: getParam
	 * @Description: 获取携带的参数
	 * @param @param key
	 * @param @return
	 * @return Object
	 * @throws
	 */
	public Object getParam(String key) {
		return map.get(key);
	}

	/**
	 * 
	 * @Title: getUrl
	 * @Description: 获取url
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @Title: addUrl
	 * @Description: 设置url
	 * @param @param url
	 * @param @return
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo addUrl(String url) {
		this.url = url;
		return this;
	}

	private String sessionId;

	/**
	 * 
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param type
	 *            数据包的标识 用于识别包
	 * @param url
	 *            访问地址
	 * @param requestParams
	 *            访问地址传递的参数
	 */
	public PackageInfo(String type, String url,
					   RequestParams requestParams) {
		this.type = type;
		this.url = url;
		this.requestParams = requestParams;
	}

	/**
	 * 
	 * @Title: getRequestParams
	 * @Description: 获取传递的参数
	 * @param @return
	 * @return RequestParams
	 * @throws
	 */
	public RequestParams getRequestParams() {
		return this.requestParams;
	}

	/**
	 * 
	 * @Title: addSession
	 * @Description: 添加sessionId
	 * @deprecated
	 * @param @param sessionId
	 * @param @return
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo addSession(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	/**
	 * @deprecated
	 * @Title: getSession
	 * @Description: 获取sessionId
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getSession() {
		return this.sessionId;
	}

	/**
	 * 
	 * @Title: addUICallBack
	 * @Description: 设置activity类 用于调用activity方法
	 * @param @param activity
	 * @param @return
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo addUICallBack(Activity activity) {// 如果想让MessageListener的doresult方法在主线程使用，需调用此方法
		this.activity = activity;
		if (activity != null) {
			watingView = activity.findViewById(activity.getResources()
					.getIdentifier("wating_dialog", "id",
							activity.getPackageName()));
		}
		return this;
	}

	/**
	 * 
	 * @Title: getUICallBack
	 * @Description: 得到 activity
	 * @param @return
	 * @return Activity
	 * @throws
	 */
	public Activity getUICallBack() {
		return activity;
	}

	/**
	 * 
	 * @Title: getResult
	 * @Description: 得到返回结果
	 * @param @return
	 * @return Object[]
	 * 
	 * @throws
	 */
	public Object[] getResult() {
		return this.result;
	}

	/**
	 * 
	 * @Title: getType
	 * @Description: 得到数据包标识
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 
	 * @Title: setResult
	 * @Description: 设置结果集合
	 * @param @param paramArrayOfObject
	 * @return void
	 * @throws
	 */
	public void setResult(Object[] paramArrayOfObject) {
		this.result = paramArrayOfObject;
	}

	/**
	 * 
	 * @Title: setType
	 * @Description: 设置标识
	 * @param @param paramString
	 * @param @return
	 * @return PackageInfo
	 * @throws
	 */
	public PackageInfo setType(String paramString) {
		this.type = paramString;
		return this;
	}

	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public Boolean getIsStop() {
		return isStop;
	}

	public void setIsStop(Boolean isStop) {
		this.isStop = isStop;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
