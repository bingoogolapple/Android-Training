package com.bingoogol.smartbulb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 保存应用程序全局状态的基类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class App extends Application {
	/**
	 * 存放应用程序中的activity，主要是用来实现完全退出应用程序功能
	 */
	private List<Activity> activities;
	private SharedPreferences sp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<Activity>();
		sp = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
	}
	
	/**
	 * 添加activity
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	/**
	 * 删除activity
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	
	/**
	 * 退出应用程序
	 */
	public void exit() {
		for(Activity activity : activities) {
			activity.finish();
		}
	}
	
	/**
	 * 向SharedPreferences中添加值
	 * @param key 键
	 * @param value 值
	 */
	public void addSp(String key,String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 从SharedPreferences中获取值
	 * @param key 键
	 * @param defValue 默认值
	 * @return 与key对应的alue
	 */
	public String getSp(String key,String defValue) {
		return sp.getString(key, defValue);
	}
}