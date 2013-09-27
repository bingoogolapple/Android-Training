package com.bingoogol.mymoment.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * 图片内存缓存
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class ImageCache {
	private static HashMap<String, SoftReference<Bitmap>> imgCache = new HashMap<String, SoftReference<Bitmap>>();

	private ImageCache() {
	}

	public static boolean isExistsInMemory(String key) {
		return imgCache.containsKey(key) && imgCache.get(key) != null && imgCache.get(key).get() != null;
	}

	public static Bitmap get(String key) {
		return imgCache.get(key).get();
	}

	public static void put(String key, Bitmap bitmap) {
		imgCache.put(key, new SoftReference<Bitmap>(bitmap));
	}
}
