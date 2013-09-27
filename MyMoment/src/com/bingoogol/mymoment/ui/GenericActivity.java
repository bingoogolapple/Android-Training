package com.bingoogol.mymoment.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.Window;

public abstract class GenericActivity extends Activity implements OnClickListener {
	protected Context context;
	protected String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = getApplicationContext();
		tag = getClass().getSimpleName();
		initView();
	}

	private void initView() {
		loadViewLayout();
		findViewById();
		setListener();
		fillData();
	}

	protected abstract void loadViewLayout();

	protected abstract void findViewById();

	protected abstract void setListener();

	protected abstract void fillData();
}
