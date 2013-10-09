package com.bingoogol.smartbulb.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.bingoogol.smartbulb.engine.Config;
import com.bingoogol.smartbulb.engine.HueRestClient;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.util.ConnectivityUtil;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.DialogBuilder;
import com.bingoogol.smartbulb.util.DialogBuilder.OperateDialogCallBack;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smarthue.R;

public class SplashActivity extends GenericActivity {
	private Config config = new Config();
	private ProgressDialog pd;
	private Dialog wifiDialog;
	private Dialog linkBtnDialog;
	private LightCallback lightCallback = new LightCallback() {

		@Override
		public void pressLinkBtn() {
			closeProgressDialog();
			openLinkBtnDialog();
		}

		@Override
		public void wifiError() {
			closeProgressDialog();
			openWifiDialog();
		}

		@Override
		public void onSuccess(Object obj) {
			closeProgressDialog();
			app.addSp("username", (String)obj);
			openMainActivity();
		}

		@Override
		public void onFailure(Object obj) {
			
		}

		@Override
		public void unauthorized() {
			
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.activity.OPEN_WIFI_SETTINGS) {
			processLogic();
		}
	};

	private void openWifiDialog() {
		wifiDialog = DialogBuilder.createOperateDialog(this, R.string.no_wifi, R.string.set_wifi, R.string.exit, new OperateDialogCallBack() {

			@Override
			public void onClickRightBtn() {
				closeWifiDialog();
				SplashActivity.this.finish();
			}

			@Override
			public void onClickLeftBtn() {
				closeWifiDialog();
				Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				startActivityForResult(intent, Constants.activity.OPEN_WIFI_SETTINGS);
			}
		});
		wifiDialog.show();
	}

	private void closeWifiDialog() {
		wifiDialog.dismiss();
	}

	private void openLinkBtnDialog() {
		linkBtnDialog = DialogBuilder.createOperateDialog(this, R.string.press_link_btn, R.string.retry, R.string.exit, new OperateDialogCallBack() {

			@Override
			public void onClickRightBtn() {
				SplashActivity.this.finish();
			}

			@Override
			public void onClickLeftBtn() {
				closeLinkBtnDialog();
				openProgressDialog();
				config.createUser(lightCallback);
			}
		});
		linkBtnDialog.show();
	}

	private void closeLinkBtnDialog() {
		linkBtnDialog.dismiss();
	}

	@Override
	public void onClick(View v) {

	}

	private void openMainActivity() {
		HueRestClient.getInstance().setUserName(app.getSp("username",""));
		Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.finish();
		startActivity(homeIntent);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void setListener() {

	}

	private void openProgressDialog() {
		pd = ProgressDialog.show(SplashActivity.this, "提示", "正在链接...");
		pd.setCancelable(false);
	}

	private void closeProgressDialog() {
		pd.dismiss();
	}

	@Override
	protected void processLogic() {
		if (ConnectivityUtil.isWifiConnected(SplashActivity.this)) {
			String username = app.getSp("username","");
			Logger.i(Constants.TAG, "用户名:" + username);
			if ("".equals(username)) {
				openProgressDialog();
				config.createUser(lightCallback);
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						openMainActivity();
					}
				}, 1500);
			}
		} else {
			openWifiDialog();
		}

	}

}
