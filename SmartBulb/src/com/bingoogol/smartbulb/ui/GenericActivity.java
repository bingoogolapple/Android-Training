package com.bingoogol.smartbulb.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.bingoogol.smartbulb.App;

/**
 * 通用activity，应用程序中所有的activity都继承自改activity，模板方法
 * 
 * @author 王浩 bingoogol@sina.com
 */
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
