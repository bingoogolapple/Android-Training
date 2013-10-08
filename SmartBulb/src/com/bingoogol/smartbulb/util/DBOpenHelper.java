package com.bingoogol.smartbulb.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	public DBOpenHelper(Context context) {
		super(context, Constants.db.DB_NAME, null, Constants.db.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTemplate(db);
		createLightAttr(db);

		//initTempate(db);
		//initLightAttr(db);
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

	private void initLightAttr(SQLiteDatabase db) {

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('1','1','50','0.99855','0.99785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('1','1','50','0.14855','0.35785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('1','1','50','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('2','1','80','0.99855','0.99785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('2','1','50','0.14855','0.35785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('2','1','130','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('3','1','34','0.68655','0.69985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('3','1','87','0.14855','0.15785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('3','1','150','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('4','1','200','0.38655','0.49985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('4','1','59','0.54855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('4','1','150','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('5','1','25','0.78655','0.79985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('5','1','59','0.54855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('5','1','15','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('6','1','89','0.78655','0.79985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('6','1','129','0.54855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('6','1','215','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('7','1','45','0.48655','0.79985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('7','1','39','0.34855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('7','1','35','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('8','1','68','0.48655','0.79985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('8','1','65','0.54855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('8','1','45','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('9','1','25','0.78655','0.79985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('9','1','25','0.54855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('9','1','30','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('10','1','125','0.56554','0.49985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('10','1','39','0.44855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('10','1','39','0.64855','0.55785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('11','1','25','0.78655','0.79985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('11','1','59','0.54855','0.65785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('11','1','15','0.44855','0.45785');");

		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('12','1','15','0.48655','0.39985');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('12','1','52','0.34855','0.25785');");
		db.execSQL("INSERT INTO " + Constants.db.lightattr.TABLE_NAME + "('" + Constants.db.lightattr.COLUMN_NAME_TID + "','" + Constants.db.lightattr.COLUMN_NAME_STATE + "','" + Constants.db.lightattr.COLUMN_NAME_BRI + "','" + Constants.db.lightattr.COLUMN_NAME_XY_X + "','" + Constants.db.lightattr.COLUMN_NAME_XY_Y + "') VALUES('12','1','15','0.24855','0.55785');");

	}

	private void initTempate(SQLiteDatabase db) {
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('beach','beach.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('colorpencils','colorpencils.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('deepsea','deepsea.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('greece','greece.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('hammock','hammock.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('kathy','kathy.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('layla','layla.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('rainbow','rainbow.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('layla','layla.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('ski','ski.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('socks','socks.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('sunset','sunset.jpg');");
		db.execSQL("INSERT INTO Template('" + Constants.db.template.COLUMN_NAME_NAME + "','" + Constants.db.template.COLUMN_NAME_IMG_PATH + "') values('taj','taj.jpg');");
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
