package com.bingoogol.smartbulb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bingoogol.smartbulb.util.Constants;

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
		sp = getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
	}
	
	public void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	public void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	
	public void exit() {
		for(Activity activity : activities) {
			activity.finish();
		}
	}
	
	public void addSp(String key,String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getSp(String key,String defValue) {
		return sp.getString(key, defValue);
	}
}