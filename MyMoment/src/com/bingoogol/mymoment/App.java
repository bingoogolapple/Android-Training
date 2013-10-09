package com.bingoogol.mymoment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class App extends Application {
	private List<Activity> activities;
	
	@Override
	public void onCreate() {
		super.onCreate();
		activities = new ArrayList<Activity>();
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
}
