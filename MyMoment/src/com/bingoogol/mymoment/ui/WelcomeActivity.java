package com.bingoogol.mymoment.ui;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.bingoogol.mymoment.R;

public class WelcomeActivity extends GenericActivity {

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void loadViewLayout() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
	}

	@Override
	protected void findViewById() {
	}

	@Override
	protected void setListener() {
	}

	@Override
	protected void fillData() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent homeIntent = new Intent(context,HomeActivity.class);
				startActivity(homeIntent);
				WelcomeActivity.this.finish();
			}
		}, 1500);
	}

}
