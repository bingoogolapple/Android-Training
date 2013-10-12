package com.bingoogol.smartbulb.db.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.bingoogol.smartbulb.db.DBOpenHelper;
import com.bingoogol.smartbulb.domain.LightAttr;
import com.bingoogol.smartbulb.domain.Template;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;

/**
 * 模板数据访问Dao
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class TemplateDao {
	private static final String TAG = "TemplateDao";
	private DBOpenHelper dbOpenHelper;

	/**
	 * 创建一个TemplateDao,用于对模板数据进行增删改查
	 * 
	 * @param context 应用程序上下文
	 */
	public TemplateDao(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}

	/**
	 * 添加模板
	 * 
	 * @param template
	 *            模板
	 * @param lightAttrs
	 *            灯泡属性列表
	 * @param bitmap
	 *            模板图片Bitmap
	 * @return 添加成功返回true，否则返回false
	 */
	public boolean addTemplete(Template template, ArrayList<LightAttr> lightAttrs, Bitmap bitmap) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 1.开起事务
		db.beginTransaction();
		FileOutputStream fos = null;
		boolean result = false;
		try {
			ContentValues templateValues = new ContentValues();
			templateValues.put(Constants.db.template.COLUMN_NAME_NAME, template.getName());
			templateValues.put(Constants.db.template.COLUMN_NAME_IMG_PATH, template.getImgPath());
			// 2.添加模板
			long tid = db.insert(Constants.db.template.TABLE_NAME, null, templateValues);
			// 3.添加灯泡属性
			for (LightAttr lightAttr : lightAttrs) {
				ContentValues lightAttrValues = new ContentValues();
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_TID, tid);
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_TYPE, lightAttr.getType());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_NAME, lightAttr.getName());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_MODELID, lightAttr.getModelid());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_SWVERSION, lightAttr.getSwversion());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_STATE, lightAttr.getState());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_BRI, lightAttr.getBri());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_HUE, lightAttr.getHue());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_SAT, lightAttr.getSat());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_XY_X, lightAttr.getXy_x());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_XY_Y, lightAttr.getXy_y());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_CT, lightAttr.getCt());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_ALERT, lightAttr.getAlert());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_EFFECT, lightAttr.getEffect());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_TRANSITIONTIME, lightAttr.getTransitiontime());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_COLORMODE, lightAttr.getColormode());
				db.insert(Constants.db.lightattr.TABLE_NAME, null, lightAttrValues);
				Logger.i(TAG, "添加 >> bri:" + lightAttr.getBri() + "   hue:" + lightAttr.getHue() + "   sat:" + lightAttr.getSat() + " state:" + lightAttr.getState());
			}
			// 4.保存图片文件
			File imgFile = new File(template.getImgPath());
			if (!imgFile.getParentFile().exists()) {
				imgFile.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(imgFile);
			bitmap.compress(CompressFormat.PNG, 80, fos);
			// 5.如果能执行到这一步，说明前面的操作没出错，设置状态为success
			db.setTransactionSuccessful();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			// 6.结束事务
			/*
			 * 结束事务，有两种情况：commit，rollback 事务的提交或回滚是由事务的标志决定
			 * 如果事务的标志为True，事务就会提交，否则回滚，默认情况下事务的标志为False
			 */
			db.endTransaction();
			DBOpenHelper.close(db, null);
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 刪除指定模板
	 * 
	 * @param id
	 *            模板id
	 * @return 刪除成功返回true，否则返回false
	 */
	public boolean deleteTemplete(int id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		db.beginTransaction();
		boolean result = false;
		try {
			db.delete(Constants.db.lightattr.TABLE_NAME, Constants.db.lightattr.COLUMN_NAME_TID + "=?", new String[] { id + "" });
			db.delete(Constants.db.template.TABLE_NAME, Constants.db.template.COLUMN_NAME_ID + "=?", new String[] { id + "" });
			db.setTransactionSuccessful();
			result = true;
		} finally {
			db.endTransaction();
			DBOpenHelper.close(db, null);
		}
		return result;
	}

	/**
	 * 修改模板
	 * 
	 * @param template
	 *            模板
	 * @return 修改成功返回true，否则返回false
	 */
	public boolean updateTemplete(Template template, ArrayList<LightAttr> lightAttrs, Bitmap bitmap) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.beginTransaction();
		FileOutputStream fos = null;
		boolean result = false;
		try {
			db = dbOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Constants.db.template.COLUMN_NAME_NAME, template.getName());
			values.put(Constants.db.template.COLUMN_NAME_IMG_PATH, template.getImgPath());
			db.update(Constants.db.template.TABLE_NAME, values, Constants.db.template.COLUMN_NAME_ID + "=?", new String[] { template.getId() + "" });
			for (LightAttr lightAttr : lightAttrs) {
				ContentValues lightAttrValues = new ContentValues();
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_TID, lightAttr.getTid());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_TYPE, lightAttr.getType());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_NAME, lightAttr.getName());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_MODELID, lightAttr.getModelid());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_SWVERSION, lightAttr.getSwversion());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_STATE, lightAttr.getState());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_BRI, lightAttr.getBri());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_HUE, lightAttr.getHue());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_SAT, lightAttr.getSat());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_XY_X, lightAttr.getXy_x());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_XY_Y, lightAttr.getXy_y());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_CT, lightAttr.getCt());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_ALERT, lightAttr.getAlert());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_EFFECT, lightAttr.getEffect());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_TRANSITIONTIME, lightAttr.getTransitiontime());
				lightAttrValues.put(Constants.db.lightattr.COLUMN_NAME_COLORMODE, lightAttr.getColormode());
				db.update(Constants.db.lightattr.TABLE_NAME, lightAttrValues, Constants.db.lightattr.COLUMN_NAME_ID + "=?", new String[] { lightAttr.getId() + "" });
				Logger.i(TAG, "修改 >> bri:" + lightAttr.getBri() + "   hue:" + lightAttr.getHue() + "   sat:" + lightAttr.getSat() + " state:" + lightAttr.getState());
			}
			File imgFile = new File(template.getImgPath());
			if (!imgFile.getParentFile().exists()) {
				imgFile.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(imgFile);
			bitmap.compress(CompressFormat.PNG, 80, fos);
			db.setTransactionSuccessful();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			DBOpenHelper.close(db, null);
		}
		return result;
	}

	/**
	 * 获取指定模板
	 * 
	 * @param id
	 *            模板id
	 * @return 模板
	 */
	public Template getTemplete(int id) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Template template = null;
		try {
			db = dbOpenHelper.getWritableDatabase();
			cursor = db.query(Constants.db.template.TABLE_NAME, null, Constants.db.template.COLUMN_NAME_ID + "=?", new String[] { id + "" }, null, null, null);
			if (cursor.moveToFirst()) {
				String name = cursor.getString(cursor.getColumnIndex(Constants.db.template.COLUMN_NAME_NAME));
				String imgPath = cursor.getString(cursor.getColumnIndex(Constants.db.template.COLUMN_NAME_IMG_PATH));
				template = new Template(id, name, imgPath);
			}
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return template;
	}

	/**
	 * 分页获取模板数据
	 * 
	 * @param offset
	 *            偏移量
	 * @param maxResult
	 *            每页显示的数量
	 * @return 模板分页数据
	 */
	public List<Template> getTemplateScrollData(int offset, int maxResult) {
		List<Template> datas = new ArrayList<Template>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.query(Constants.db.template.TABLE_NAME, null, null, null, null, null, null, offset + "," + maxResult);
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(Constants.db.template.COLUMN_NAME_ID));
				String name = cursor.getString(cursor.getColumnIndex(Constants.db.template.COLUMN_NAME_NAME));
				String imgPath = cursor.getString(cursor.getColumnIndex(Constants.db.template.COLUMN_NAME_IMG_PATH));
				datas.add(new Template(id, name, imgPath));
			}
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return datas;
	}

	/**
	 * 根据模板id获取灯泡属性列表
	 * 
	 * @param tid
	 *            模板id
	 * @return 灯泡属性列表
	 */
	public ArrayList<LightAttr> getLightAttrListByTid(int tid) {
		ArrayList<LightAttr> datas = new ArrayList<LightAttr>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.query(Constants.db.lightattr.TABLE_NAME, null, Constants.db.lightattr.COLUMN_NAME_TID + "=?", new String[] { tid + "" }, null, null, null, null);
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_ID));
				String type = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_TYPE));
				String name = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_NAME));
				String modelid = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_MODELID));
				String swversion = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_SWVERSION));
				int state = cursor.getInt(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_STATE));
				int bri = cursor.getInt(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_BRI));
				int hue = cursor.getInt(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_HUE));
				int sat = cursor.getInt(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_SAT));
				double xy_x = cursor.getDouble(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_XY_X));
				double xy_y = cursor.getDouble(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_XY_Y));
				int ct = cursor.getInt(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_CT));
				String alert = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_ALERT));
				String effect = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_EFFECT));
				long transitiontime = cursor.getLong(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_TRANSITIONTIME));
				String colormode = cursor.getString(cursor.getColumnIndex(Constants.db.lightattr.COLUMN_NAME_COLORMODE));
				datas.add(new LightAttr(id, tid, type, name, modelid, swversion, state, bri, hue, sat, xy_x, xy_y, ct, alert, effect, transitiontime, colormode));
			}
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return datas;
	}

	/**
	 * 获取模板数量
	 * 
	 * @return 模板总数
	 */
	public long getCount() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		long result = 0L;
		try {
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.rawQuery("select count(*) from " + Constants.db.template.TABLE_NAME, null);
			cursor.moveToFirst();
			result = cursor.getLong(0);
		} finally {
			DBOpenHelper.close(db, cursor);
		}
		return result;
	}

}
