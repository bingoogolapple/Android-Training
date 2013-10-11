package com.bingoogol.training.util;

import java.io.File;

/**
 * 系统常量工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public final class Constants {

	public static final class net {
		public static final int CONNECTTIMEOUT = 5000;
		public static final int READTIMEOUT = 10000;
		public static final String GET = "GET";
		public static final String POST = "POST";
	}
	
	public static final class file {
		public static final String DIR_ROOT = "traning";
		public static final String DIR_IMAGE = DIR_ROOT + File.separator + "images";
	}
	
	public static final class activity {
		public static final int OPEN_WIFI_SETTINGS = 1;
		public static final int GET_FROM_GALLERY = 2;
		public static final int GET_FROM_CAMERA = 3;
		public static final int GET_FROM_CROP = 4;
	}

	public static final String CHARSET = "UTF-8";
	public static final String APP_NAME = "training";
	public static final String TAG = APP_NAME;
}
