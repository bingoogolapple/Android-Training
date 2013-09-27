package com.bingoogol.mymoment.util;

import java.io.File;

/**
 * 系统常量工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public final class Constants {
	private Constants() {
	}

	public static final class db {
		public static final String DB_NAME = "MyMoment.db";
		public static final int DB_VERSION = 2;
		public static final String TABLE_NAME = "Moment";
		public static final String COLUMN_NAME_ID = "_id";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_IMG_PATH = "imgPath";
		public static final String COLUMN_NAME_PUBLISH_TIME = "publishTime";
	}

	public static final class activity {
		public static final int GET_FROM_GALLERY = 1;
		public static final int GET_FROM_CAMERA = 2;
	}

	public static final class file {
		public static final String DIR_ROOT = "MyMoment";
		public static final String DIR_IMAGE = DIR_ROOT + File.separator + "images";
	}

	public static final class tag {
		public static final String STORAGE_ERROR = "StorageUtil";
	}
}
