package com.bingoogol.mymoment.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bingoogol.mymoment.R;

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
	
	public static void makeCustomToast(Context context, int resId) {
		makeCustomToast(context, context.getResources().getText(resId));
	}
	
	public static void makeCustomToast(Context context, CharSequence text) {
		View view = View.inflate(context, R.layout.my_toast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_my_toast);
		tv.setText(text);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}
}
