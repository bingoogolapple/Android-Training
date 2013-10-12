package com.bingoogol.smartbulb.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐丝工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class ToastUtil {
	private ToastUtil() {
	}

	/**
	 * 根据文本打印吐丝
	 * 
	 * @param context
	 *            应用程序上下文
	 * @param text
	 *            要显示的文本
	 */
	public static void makeText(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 根据资源id打印吐丝
	 * 
	 * @param context
	 *            应用程序上下文
	 * @param resId
	 *            资源id
	 */
	public static void makeText(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
}
