package com.bingoogol.mymoment.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bingoogol.mymoment.domain.Moment;
import com.bingoogol.mymoment.util.Constants;
import com.bingoogol.mymoment.util.DBOpenHelper;

public class MomentService {
	private DBOpenHelper dbOpenHelper;

	public MomentService(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}

	public boolean addMoment(Moment moment) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Constants.db.COLUMN_NAME_CONTENT, moment.getContent());
			values.put(Constants.db.COLUMN_NAME_IMG_PATH, moment.getImgPath());
			values.put(Constants.db.COLUMN_NAME_PUBLISH_TIME, moment.getPublishTime());
			result = db.insert(Constants.db.TABLE_NAME, null, values) == -1 ? false : true;
		} finally {
			close(db, null);
		}
		return result;
	}

	public boolean deleteMoment(int id) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getReadableDatabase();
			int count = db.delete(Constants.db.TABLE_NAME, Constants.db.COLUMN_NAME_ID + "=?", new String[] { id + "" });
			if (count == 1) {
				result = true;
			}
		} finally {
			close(db, null);
		}
		return result;
	}

	public boolean updateMoment(Moment moment) {
		SQLiteDatabase db = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Constants.db.COLUMN_NAME_CONTENT, moment.getContent());
			values.put(Constants.db.COLUMN_NAME_IMG_PATH, moment.getImgPath());
			values.put(Constants.db.COLUMN_NAME_PUBLISH_TIME, moment.getPublishTime());
			int count = db.update(Constants.db.TABLE_NAME, values, Constants.db.COLUMN_NAME_ID + "=?", new String[] { moment.getId() + "" });
			if (count == 1) {
				result = true;
			}
		} finally {
			close(db, null);
		}
		return result;
	}

	public Moment getMoment(int id) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Moment result = null;
		try {
			db = dbOpenHelper.getWritableDatabase();
			cursor = db.rawQuery("select * from " + Constants.db.TABLE_NAME + " where " + Constants.db.COLUMN_NAME_ID + "=?", new String[] { id + "" });
			if (cursor.moveToFirst()) {
				String content = cursor.getString(cursor.getColumnIndex(Constants.db.COLUMN_NAME_CONTENT));
				String imgPath = cursor.getString(cursor.getColumnIndex(Constants.db.COLUMN_NAME_IMG_PATH));
				String publishTime = cursor.getString(cursor.getColumnIndex(Constants.db.COLUMN_NAME_PUBLISH_TIME));
				result = new Moment(id, content, imgPath, publishTime);
			}
		} finally {
			close(db, cursor);
		}
		return result;
	}

	public List<Moment> getScrollData(int offset, int maxResult) {
		List<Moment> datas = new ArrayList<Moment>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.query(Constants.db.TABLE_NAME, null, null, null, null, null, null, offset + "," + maxResult);
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(Constants.db.COLUMN_NAME_ID));
				String content = cursor.getString(cursor.getColumnIndex(Constants.db.COLUMN_NAME_CONTENT));
				String imgPath = cursor.getString(cursor.getColumnIndex(Constants.db.COLUMN_NAME_IMG_PATH));
				String publishTime = cursor.getString(cursor.getColumnIndex(Constants.db.COLUMN_NAME_PUBLISH_TIME));
				datas.add(new Moment(id, content, imgPath, publishTime));
			}
		} finally {
			close(db, cursor);
		}
		return datas;
	}

	public long getCount() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		long result = 0L;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.rawQuery("select count(*) from " + Constants.db.TABLE_NAME, null);
			cursor.moveToFirst();
			result = cursor.getLong(0);
		} finally {
			close(db, cursor);
		}
		return result;
	}

	public void close(SQLiteDatabase db, Cursor cursor) {
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
