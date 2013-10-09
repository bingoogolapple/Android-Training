package com.bingoogol.smartbulb.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.bingoogol.smartbulb.App;

public abstract class GenericActivity extends Activity implements OnClickListener {
	
	protected App app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		app = (App) getApplicationContext();
		app.addActivity(this);
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		loadViewLayout();
		findViewById();
		setListener();
		processLogic();
	}

	protected abstract void loadViewLayout();

	protected abstract void findViewById();

	protected abstract void setListener();

	protected abstract void processLogic();
	
	@Override
	protected void onDestroy() {
		app.removeActivity(this);
		super.onDestroy();
	}
}
