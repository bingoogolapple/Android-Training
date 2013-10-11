package com.bingoogol.smartbulb.engine;

import android.os.Handler;
import android.os.Message;

import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

/**
 * 操作燈泡的handler
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class LightHandler extends Handler {
	private static final String TAG = "LightHandler";
	private LightCallback lightCallback;

	public LightHandler(LightCallback lightCallback) {
		this.lightCallback = lightCallback;
	}

	public void handleMessage(Message msg) {
		lightCallback.closeDialog();
		switch (msg.what) {
		case Constants.what.SUCCESS:
			lightCallback.onSuccess(msg.obj);
			break;
		case Constants.what.FAILURE:
			lightCallback.onFailure();
			break;
		case Constants.what.UNAUTHORIZED:
			lightCallback.unauthorized();
			break;
		case Constants.what.LINKBUTTON:
			Logger.e(TAG, "按钮");
			lightCallback.pressLinkBtn();
			break;
		case Constants.what.WIFIERROR:
			lightCallback.wifiError();
			break;
		}
	}

	/**
	 * 操作灯泡的回调接口
	 * 
	 * @author 王浩 bingoogol@sina.com
	 */
	public interface LightCallback {
		public void onSuccess(Object obj);

		public void onFailure();

		public void wifiError();

		public void unauthorized();

		public void pressLinkBtn();

		public void closeDialog();
	}
}
