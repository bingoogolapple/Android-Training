package com.bingoogol.smartbulb.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络链接状态检查工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class ConnectivityUtil {
	/**
	 * 判断当前是否链接网络
	 * 
	 * @param context
	 *            应用程序上下文
	 * @return 如果当前网络处于链接状态则返回true，否则返回false
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * 判断当前是否具有Wifi链接
	 * 
	 * @param context
	 *            应用程序上下文
	 * @return 如果wifi处于链接状态则返回true，否则返回false
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * 判断当前是否具有移动数据链接
	 * 
	 * @param context
	 *            应用程序上下文
	 * @return 如果移动网络处于链接状态则返回true，否则返回false
	 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkInfo != null && networkInfo.isConnected();
	}
}
