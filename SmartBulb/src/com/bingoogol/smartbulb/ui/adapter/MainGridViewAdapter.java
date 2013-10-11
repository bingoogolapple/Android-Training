package com.bingoogol.smartbulb.ui.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bingoogol.smartbulb.App;
import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.db.dao.TemplateDao;
import com.bingoogol.smartbulb.domain.LightAttr;
import com.bingoogol.smartbulb.domain.Template;
import com.bingoogol.smartbulb.domain.http.LightEntry;
import com.bingoogol.smartbulb.domain.http.State;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.engine.LightsController;
import com.bingoogol.smartbulb.ui.EditTemplateActivity;
import com.bingoogol.smartbulb.ui.MainActivity;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.StorageUtil;
import com.bingoogol.smartbulb.util.ToastUtil;

public class MainGridViewAdapter extends BaseAdapter implements OnItemClickListener, OnItemLongClickListener {
	private static final String TAG = "MainGridViewAdapter";
	private LayoutInflater layoutInflater;
	private List<Template> datas;
	private MainActivity activity;
	private LightsController lightsController = new LightsController();
	/**
	 * 当前桥接器链接的所有灯的信息
	 */
	private List<LightEntry> lightEntries;
	private TemplateDao templateDao;
	private Typeface typeface;
	/**
	 * 設置模板的flag
	 */
	private int setTemplateFlag = 0;
	private LightCallback setTemplateCallback = new LightCallback() {

		@Override
		public void onSuccess(Object obj) {
			setTemplateFlag++;
			if (setTemplateFlag == 3) {
				Logger.i(TAG, "成功设置所有灯泡属性");
				setTemplateFlag = 0;
				activity.closeProgressDialog();
			}
		}

		@Override
		public void onFailure() {
			Logger.e(TAG, "设置灯泡属性失败");
			setTemplateFlag = 0;
			activity.closeProgressDialog();
			App app = (App) activity.getApplication();
			app.addSp("username", "");
			activity.openSplashActivity();
		}

		@Override
		public void wifiError() {
			Logger.e(TAG, "wifi链接不对");
			activity.closeProgressDialog();
			App app = (App) activity.getApplication();
			app.addSp("username", "");
			activity.openSplashActivity();
		}

		@Override
		public void unauthorized() {
			Logger.e(TAG, "用户名失效");
			activity.closeProgressDialog();
			App app = (App) activity.getApplication();
			app.addSp("username", "");
			activity.openSplashActivity();
		}

		@Override
		public void pressLinkBtn() {
			Logger.w(TAG, "请按下按钮");
			App app = (App) activity.getApplication();
			app.addSp("username", "");
			activity.openSplashActivity();
		}

		@Override
		public void closeDialog() {
			
		}

	};

	public MainGridViewAdapter(MainActivity activity, List<Template> datas, List<LightEntry> lightEntries) {
		this.layoutInflater = LayoutInflater.from(activity);
		this.datas = datas;
		this.activity = activity;
		this.lightEntries = lightEntries;
		templateDao = new TemplateDao(activity);
		// 字体
		typeface = Typeface.createFromAsset(activity.getAssets(), "font.ttf");
	}
	
	/**
	 * 設置指定模板
	 * @param id
	 */
	public void setTemplate(long id) {
		List<LightAttr> lightAttrs = templateDao.getLightAttrListByTid((int) id);
		LightAttr lightAttr;
		setTemplateFlag = 0;
		for (int i = 0; i < lightAttrs.size(); i++) {
			lightAttr = lightAttrs.get(i);
			State state = new State();
			state.setHue(lightAttr.getHue());
			state.setOn(lightAttr.getState() == 1 ? true : false);
			state.setBri(lightAttr.getBri());
			state.setSat(lightAttr.getSat());
			Logger.i(TAG, "设置模板 >> bri:" + state.getBri() + "   hue:" + state.getHue() + "   sat:" + state.getSat() + " on:" + state.isOn());
//			state.setAlert(lightAttr.getAlert());
//			state.setColormode(lightAttr.getColormode());
//			state.setCt(lightAttr.getCt());
//			state.setEffect(lightAttr.getEffect());
//			state.setTransitiontime(lightAttr.getTransitiontime());
//			state.setXy(lightAttr.getXy_x(), lightAttr.getXy_y());
			lightsController.setLightState(lightEntries.get(i).getId() + "", state, setTemplateCallback);
		}
	}
	

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return datas.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.main_item, null);
			viewHolder = new ViewHolder();
			viewHolder.idTv = (TextView) convertView.findViewById(R.id.tv_id_main_item);
			viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.iv_icom_main_item);
			viewHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_name_main_item);
			viewHolder.nameTv.setTypeface(typeface);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Template template = datas.get(position);
		viewHolder.idTv.setText(template.getId() + "");
		if (position == 0) {
			viewHolder.iconIv.setImageResource(R.drawable.alloff);
		} else {
			viewHolder.iconIv.setImageBitmap(StorageUtil.getBitmapFromLocal(template.getImgPath(), 120, 90));
		}
		viewHolder.nameTv.setText(template.getName());
		return convertView;
	}

	private class ViewHolder {
		private TextView idTv;
		private ImageView iconIv;
		private TextView nameTv;
	}

	public void addMoreMoment(List<Template> templates) {
		datas.addAll(templates);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Logger.i(TAG, "长按");
		if(id != -1) {
			showLongClickDialog((int)id);
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		activity.openProgressDialog("正在设置灯泡属性");
		if (id == -1) {
			Logger.i(TAG, "关闭全部燈泡");
			State state = new State();
			state.setBri(0);
			state.setHue(0);
			state.setSat(0);
			state.setOn(false);
			setTemplateFlag = 0;
			for (final LightEntry lightEntry : lightEntries) {
				lightsController.setLightState(lightEntry.getId(), state, setTemplateCallback);
			}
		} else {
			Logger.i(TAG, "设置模板");
			setTemplate(id);
		}
	}

	private void showLongClickDialog(final int id) {
		final Dialog dialog = new Dialog(activity, R.style.DialogTheme);
		View view = View.inflate(activity, R.layout.item_long_click_dialog, null);
		Button updateBtn = (Button) view.findViewById(R.id.item_dialog_update);
		updateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i("HomeMomentAdapter", "要修改的模板id >> " + id);
				Intent intent = new Intent(activity,EditTemplateActivity.class);
				intent.putExtra("id", id);
				activity.startActivityForResult(intent, 0);
				activity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
				dialog.dismiss();
			}
		});
		Button deleteBtn = (Button) view.findViewById(R.id.item_dialog_delete);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i("HomeMomentAdapter", "要删除的模板id >> " + id);
				if (templateDao.deleteTemplete(id)) {
					if(datas.remove(getDeleteTemplate(id))) {
						activity.refresh();
					}
				} else {
					ToastUtil.makeText(activity, "删除失败");
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	/**
	 * 从数据源中获取要删除的Template
	 * 
	 * @param id
	 * @return
	 */
	public Template getDeleteTemplate(int id) {
		for (Template template : datas) {
			if (template.getId() == id) {
				return template;
			}
		}
		return null;
	}

}
