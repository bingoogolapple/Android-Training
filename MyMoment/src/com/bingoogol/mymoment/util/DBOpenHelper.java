package com.bingoogol.mymoment.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	// 在SQLiteOpenHelper的子类当中，必须有该构造函数
	public DBOpenHelper(Context context) {
		// 必须通过调用父类当中的构造函数,null,采用系统默认游标工厂
		super(context, Constants.db.DB_NAME, null, Constants.db.DB_VERSION);
	}

	// 当第一次创建数据库时调用。表格的创建和初始化表格的个数在这里完成。
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + Constants.db.TABLE_NAME + " (");
		sql.append(Constants.db.COLUMN_NAME_ID + " integer primary key autoincrement,");
		sql.append(Constants.db.COLUMN_NAME_CONTENT + " varchar(50) null,");
		sql.append(Constants.db.COLUMN_NAME_IMG_PATH + " varchar(100) null,");
		sql.append(Constants.db.COLUMN_NAME_PUBLISH_TIME + " varchar(16) null");
		sql.append(")");
		db.execSQL(sql.toString());
	}

	// 数据库文件的版本号发生改变的时候调用的,如果目前数据库文件并不存在，这个方法不会被调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
