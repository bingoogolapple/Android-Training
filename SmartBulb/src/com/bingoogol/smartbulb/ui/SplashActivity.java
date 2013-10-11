package com.bingoogol.smartbulb.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.engine.Config;
import com.bingoogol.smartbulb.engine.HueRestClient;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.ui.sub.LinkButtonDialog;
import com.bingoogol.smartbulb.ui.sub.SetWifiDialog;
import com.bingoogol.smartbulb.util.ConnectivityUtil;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.ToastUtil;

/**
 * 欢迎界面，在这个界面认证用户
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class SplashActivity extends GenericActivity {
	protected static final String TAG = "SplashActivity";
	private Config config = new Config();
	private ProgressDialog pd;
	private LightCallback lightCallback = new LightCallback() {

		@Override
		public void pressLinkBtn() {
			Logger.i(TAG, "打开按钮对话框");
			new LinkButtonDialog(SplashActivity.this).show();
		}

		@Override
		public void wifiError() {
			Logger.i(TAG, "打开wifi对话框");
			new SetWifiDialog(SplashActivity.this).show();
		}

		@Override
		public void onSuccess(Object obj) {
			app.addSp("username", (String) obj);
			openMainActivity();
		}

		@Override
		public void onFailure() {
			ToastUtil.makeText(app, R.string.auth_failure);
		}

		@Override
		public void unauthorized() {

		}
		
		@Override
		public void closeDialog() {
			pd.dismiss();
			Logger.i(TAG, "关闭进度条");
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.activity.OPEN_WIFI_SETTINGS) {
			processLogic();
		}
	};
	
	@Override
	public void onBackPressed() {
		app.exit();
	}

	private void openMainActivity() {
		HueRestClient.getInstance().setUserName(app.getSp("username", ""));
		Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.finish();
		startActivity(homeIntent);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_splash);
	}

	/**
	 * 认证用户
	 */
	public void auth() {
		pd = ProgressDialog.show(this, getResources().getString(R.string.prompt), getResources().getString(R.string.authing));
		pd.setCancelable(false);
		config.createUser(lightCallback);
	}

	@Override
	protected void processLogic() {
		if (ConnectivityUtil.isWifiConnected(SplashActivity.this)) {
			String username = app.getSp("username", "");
			Logger.i(TAG, "用户名:" + username);
			if ("".equals(username)) {
				auth();
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						openMainActivity();
					}
				}, 1500);
			}
		} else {
			new SetWifiDialog(this).show();
		}

	}

	@Override
	protected void findViewById() {
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void setListener() {
	}

}
