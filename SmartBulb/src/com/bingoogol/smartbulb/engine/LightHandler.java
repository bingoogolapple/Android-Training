package com.bingoogol.smartbulb.engine;

import android.os.Handler;
import android.os.Message;

import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

/**
 * 处理灯泡操作的handler
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class LightHandler extends Handler {
	private static final String TAG = "LightHandler";
	private LightCallback lightCallback;

	/**
	 * 
	 * @param lightCallback 操作灯泡的回调接口
	 */
	public LightHandler(LightCallback lightCallback) {
		this.lightCallback = lightCallback;
	}

	/**
	 * 根据msg中what值的不同，执行不同的回调方法
	 */
	public void handleMessage(Message msg) {
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
		/**
		 * 和服务器打交道成功时要执行的方法
		 * @param obj 操作成功返回的数据
		 */
		public void onSuccess(Object obj);

		/**
		 * 和服务器打交道失败时要执行的方法
		 */
		public void onFailure();

		/**
		 * 网络链接错误时要执行的方法
		 */
		public void wifiError();

		/**
		 * 用户过期时要执行的方法
		 */
		public void unauthorized();

		/**
		 * 提示按下桥接器按钮时要执行的方法
		 */
		public void pressLinkBtn();

	}
}
