package com.bingoogol.smartbulb.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

/**
 * 管理数据库的创建和版本
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBOpenHelper";

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context 应用程序上下文
	 */
	public DBOpenHelper(Context context) {
		/**
		 * 必须通过调用父类当中的构造函数，null,采用系统默认游标工厂
		 */
		super(context, Constants.db.DB_NAME, null, Constants.db.DB_VERSION);
	}

	/**
	 * 当第一次创建数据库时调用。表格的创建和初始化表格的个数在这里完成
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.i(TAG, "初始化数据库");
		createTemplate(db);
		createLightAttr(db);
	}

	/**
	 * 创建Template表
	 * 
	 * @param db
	 *            SQLiteDatabase对象
	 */
	private void createTemplate(SQLiteDatabase db) {
		Logger.i(TAG, "创建Template表");
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + Constants.db.template.TABLE_NAME + " (");
		sql.append(Constants.db.template.COLUMN_NAME_ID + " integer primary key autoincrement,");
		sql.append(Constants.db.template.COLUMN_NAME_NAME + " varchar(50) null,");
		sql.append(Constants.db.template.COLUMN_NAME_IMG_PATH + " varchar(100) null");
		sql.append(")");
		db.execSQL(sql.toString());
	}

	/**
	 * 创建LightAttr表
	 * 
	 * @param db
	 *            SQLiteDatabase对象
	 */
	private void createLightAttr(SQLiteDatabase db) {
		Logger.i(TAG, "创建LightAttr表");
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + Constants.db.lightattr.TABLE_NAME + " (");
		sql.append(Constants.db.lightattr.COLUMN_NAME_ID + " integer primary key autoincrement,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_TID + " integer,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_TYPE + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_NAME + " varchar(100) null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_MODELID + " varchar(500) null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_SWVERSION + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_STATE + " integer null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_BRI + " inteter null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_HUE + " integer null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_SAT + " integer null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_XY_X + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_XY_Y + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_CT + " integer null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_ALERT + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_EFFECT + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_TRANSITIONTIME + " varchar null,");
		sql.append(Constants.db.lightattr.COLUMN_NAME_COLORMODE + " varchar null");
		sql.append(")");
		db.execSQL(sql.toString());
	}

	/**
	 * 数据库文件的版本号发生改变的时候调用的 如果目前数据库文件并不存在，这个方法不会被调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.i(TAG, "升级数据库");
	}

	/**
	 * 关闭数据链接
	 * 
	 * @param db
	 *            SQLiteDatabase对象
	 * @param cursor
	 *            Cursor对象
	 */
	public static void close(SQLiteDatabase db, Cursor cursor) {
		try {
			if (cursor != null) {
				cursor.close();
			}
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
