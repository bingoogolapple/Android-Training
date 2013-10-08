package com.bingoogol.smartbulb.api;

import android.os.Handler;
import android.os.Message;

import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

public class LightHandler extends Handler {
	private LightCallback lightCallback;
	public LightHandler(LightCallback lightCallback) {
		this.lightCallback = lightCallback;
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.what.SUCCESS:
			lightCallback.onSuccess(msg.obj);
			break;
		case Constants.what.FAILURE:
			lightCallback.onFailure(null);
			break;
		case Constants.what.UNAUTHORIZED:
			lightCallback.unauthorized();
			break;
		case Constants.what.LINKBUTTON:
			Logger.e(Constants.TAG, "按钮");
			lightCallback.pressLinkBtn();
			break;
		case Constants.what.WIFIERROR:
			lightCallback.wifiError();
			break;
		}
	}
	
	public interface LightCallback {
		public void onSuccess(Object obj);

		public void onFailure(Object obj);

		public void wifiError();

		public void unauthorized();
		
		public void pressLinkBtn();
	}
}
