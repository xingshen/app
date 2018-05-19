package com.steptowin.common.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Desc:
 * @Author: zg
 * @Time: 2015/12/24 14:51
 */
public class NetworkTool {

    /**
     * @Desc: 当前网络是否打开
     * @Author: zg
     * @Time: 2015/12/24 14:54
     */
    public static boolean isNetworkOpen(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @Desc: 当前网络是否为wifi状态
     * @Author: zg
     * @Time: 2015/12/24 14:51
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return true;
            }
        }
        return false;
    }
}
