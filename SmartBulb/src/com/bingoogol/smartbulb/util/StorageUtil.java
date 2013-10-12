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

	private static final String TAG = "StorageUtil";

	private StorageUtil() {
	}

	/**
	 * 判断外存储是否可写
	 * 
	 * @return 如果外存储可写则返回ture，否则返回false
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * 判断外存储是否可读
	 * 
	 * @return 如果外存储可读则返回ture，否则返回false
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	/**
	 * 获取当前app文件存储路径
	 * 
	 * @return 当前app文件的根文件夹
	 */
	public static File getAppDir() {
		File appDir = null;
		if (isExternalStorageWritable()) {
			appDir = new File(Environment.getExternalStorageDirectory(), Constants.file.DIR_ROOT);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
		} else {
			Logger.e(TAG, "外部存储器不可写入");
		}
		return appDir;
	}

	/**
	 * 获取当前app图片存储路径
	 * 
	 * @param context
	 *            应用程序上下文
	 * @return 存放该应用程序图片的文件夹
	 */
	public static File getImageDir() {
		File imageDir = null;
		if (isExternalStorageWritable()) {
			imageDir = new File(Environment.getExternalStorageDirectory(), Constants.file.DIR_IMAGE);
			if (!imageDir.exists()) {
				imageDir.mkdirs();
			}
		} else {
			Logger.e(TAG, "外部存储器不可写入");
		}
		return imageDir;
	}

	/**
	 * 从本地读取图片
	 * 
	 * @param imgRealPath
	 *            图片真实路径
	 * @return Bitmap对象
	 */
	public static Bitmap getBitmapFromLocal(String imgRealPath) {
		Bitmap bitmap = null;
		if (isExternalStorageReadable()) {
			bitmap = BitmapFactory.decodeFile(imgRealPath);
		} else {
			Logger.e(TAG, "外部存储器不可读");
		}
		return bitmap;
	}

	/**
	 * 从本地获取压缩后的图片
	 * 
	 * @param imgRealPath
	 *            图片真实路径
	 * @param reqWidth
	 *            宽度
	 * @param reqHeight
	 *            高度
	 * @return 压缩后的Bitmap对象
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
			Logger.e(TAG, "外部存储器不可读");
		}
		return bitmap;
	}

	/**
	 * 计算图片inSampleSize缩放比
	 * 
	 * @param opts
	 *            Options
	 * @param reqWidth
	 *            宽度
	 * @param reqHeight
	 *            高度
	 * @return 图片缩放比例
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
	 *            Uri
	 * @param context
	 *            应用程序上下文
	 * @return 文件的真实路径
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
	 *            文件真实路径
	 * @return 如果存在则返回true，否则返回false
	 */
	public static boolean isExistsInLocal(String realPath) {
		if (isExternalStorageReadable()) {
			File file = new File(realPath);
			return file.exists();
		} else {
			Logger.e(TAG, "外部存储器不可读");
			return false;
		}
	}
}
