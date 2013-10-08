package com.bingoogol.smartbulb.util;

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 文件操作工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class StorageUtil {

	private StorageUtil() {
	}

	/**
	 * 判断外存储是否可写
	 * 
	 * @return
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * 判断外存储是否可读
	 * 
	 * @return
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	/**
	 * 获取当前app文件存储路径
	 * 
	 * @return
	 */
	public static File getAppDir() {
		File appDir = null;
		if (isExternalStorageWritable()) {
			appDir = new File(Environment.getExternalStorageDirectory(), Constants.file.DIR_ROOT);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
		} else {
			Logger.e(Constants.TAG, "外部存储器不可写入");
		}
		return appDir;
	}

	/**
	 * 获取当前app图片存储路径
	 * 
	 * @param context
	 * @return
	 */
	public static File getImageDir() {
		File imageDir = null;
		if (isExternalStorageWritable()) {
			imageDir = new File(Environment.getExternalStorageDirectory(), Constants.file.DIR_IMAGE);
			if (!imageDir.exists()) {
				imageDir.mkdirs();
			}
		} else {
			Logger.e(Constants.TAG, "外部存储器不可写入");
		}
		return imageDir;
	}

	/**
	 * 从本地读取图片
	 * 
	 * @param imgRealPath
	 * @return
	 */
	public static Bitmap getBitmapFromLocal(String imgRealPath) {
		Bitmap bitmap = null;
		if (isExternalStorageReadable()) {
			bitmap = BitmapFactory.decodeFile(imgRealPath);
		} else {
			Logger.e(Constants.TAG, "外部存储器不可读");
		}
		return bitmap;
	}

	/**
	 * 从本地获取压缩后的图片
	 * 
	 * @param imgRealPath
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap getBitmapFromLocal(String imgRealPath, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		if (isExternalStorageReadable()) {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imgRealPath, opts);
			opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(imgRealPath, opts);
		} else {
			Logger.e(Constants.TAG, "外部存储器不可读");
		}
		return bitmap;
	}

	/**
	 * 计算图片inSampleSize缩放比
	 * 
	 * @param opts
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(Options opts, int reqWidth, int reqHeight) {
		int width = opts.outWidth;
		int height = opts.outHeight;
		int inSampleSize = 1;
		if (width > reqWidth || height > reqHeight) {
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) height / (float) reqHeight);
			inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
			int totalPixels = width * height;
			int totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * 根据Uri获取文件的真实路径
	 * 
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getRealPathByUri(Context context, Uri uri) {
		ContentResolver resolver = context.getContentResolver();
		String[] proj = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = MediaStore.Images.Media.query(resolver, uri, proj);
		String realPath = null;
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				realPath = cursor.getString(columnIndex);
			}
		}
		return realPath;
	}

	/**
	 * 根据真实路径判断文件是否存在
	 * 
	 * @param realPath
	 * @return
	 */
	public static boolean isExistsInLocal(String realPath) {
		if (isExternalStorageReadable()) {
			File file = new File(realPath);
			return file.exists();
		} else {
			Logger.e(Constants.TAG, "外部存储器不可读");
			return false;
		}
	}
}
