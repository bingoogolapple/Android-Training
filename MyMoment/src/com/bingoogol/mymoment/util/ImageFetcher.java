package com.bingoogol.mymoment.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * 图片读取器
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class ImageFetcher {
	private ImageFetcher() {
	}

	public static void loadBitmap(final String imgRealPath, final int reqWidth, final int reqHeight, final ImgCallback imgCallback) {

		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if (imgCallback != null) {
					imgCallback.imgLoaded((Bitmap) message.obj, imgRealPath);
				}
			}
		};
		if (StorageUtil.isExistsInLocal(imgRealPath)) {
			new Thread() {
				@Override
				public void run() {
					Bitmap bitmap = StorageUtil.getBitmapFromLocal(imgRealPath, reqWidth, reqHeight);
					ImageCache.put(imgRealPath, bitmap);
					Message message = handler.obtainMessage(0, bitmap);
					handler.sendMessage(message);
				}
			}.start();
		} else {
			// TODO 从网络获取图片
		}
	}

	public interface ImgCallback {
		public void imgLoaded(Bitmap bitmap, String imageUrl);
	}
}
