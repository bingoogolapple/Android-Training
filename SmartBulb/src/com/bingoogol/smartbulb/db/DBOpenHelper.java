package com.bingoogol.smartbulb.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bingoogol.smartbulb.util.Constants;

public class DBOpenHelper extends SQLiteOpenHelper {
	public DBOpenHelper(Context context) {
		super(context, Constants.db.DB_NAME, null, Constants.db.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTemplate(db);
		createLightAttr(db);
	}

	private void createTemplate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + Constants.db.template.TABLE_NAME + " (");
		sql.append(Constants.db.template.COLUMN_NAME_ID + " integer primary key autoincrement,");
		sql.append(Constants.db.template.COLUMN_NAME_NAME + " varchar(50) null,");
		sql.append(Constants.db.template.COLUMN_NAME_IMG_PATH + " varchar(100) null");
		sql.append(")");
		db.execSQL(sql.toString());
	}

	private void createLightAttr(SQLiteDatabase db) {
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

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 关闭数据链接
	 * 
	 * @param db
	 * @param cursor
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
