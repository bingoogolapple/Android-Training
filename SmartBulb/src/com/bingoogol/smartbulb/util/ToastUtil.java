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

	public static void makeText(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void makeText(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
}
