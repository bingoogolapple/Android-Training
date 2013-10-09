package com.bingoogol.smartbulb.ui.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
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

import com.bingoogol.smartbulb.db.dao.TemplateDao;
import com.bingoogol.smartbulb.domain.LightAttr;
import com.bingoogol.smartbulb.domain.Template;
import com.bingoogol.smartbulb.domain.http.LightEntry;
import com.bingoogol.smartbulb.domain.http.State;
import com.bingoogol.smartbulb.engine.LightsController;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.ui.EditTemplateActivity;
import com.bingoogol.smartbulb.ui.MainActivity;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.ToastUtil;
import com.bingoogol.smarthue.R;

public class MainGridViewAdapter extends BaseAdapter implements OnItemClickListener, OnItemLongClickListener {
	private LayoutInflater layoutInflater;
	private List<Template> datas;
	private MainActivity activity;
	private LightsController lightsController;
	private boolean setLightAttributesAndState = true;
	private List<LightEntry> lightEntries;
	private TemplateDao templateDao;
	private int flag = 0;

	public MainGridViewAdapter(MainActivity activity, List<Template> datas, List<LightEntry> lightEntries) {
		this.layoutInflater = LayoutInflater.from(activity);
		this.datas = datas;
		this.activity = activity;
		this.lightEntries = lightEntries;
		templateDao = new TemplateDao(activity);
		lightsController = new LightsController();
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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Template template = datas.get(position);
		viewHolder.idTv.setText(template.getId() + "");
		if (position == 0) {
			viewHolder.iconIv.setImageResource(R.drawable.alloff);
		} else {
			viewHolder.iconIv.setImageBitmap(BitmapFactory.decodeFile(template.getImgPath()));
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
		Logger.i(Constants.TAG, "长按");
		if(id != -1) {
			showLongClickDialog((int)id);
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		activity.openProgressDialog("正在获取灯泡属性");
		if (id == -1) {
			Logger.i(Constants.TAG, "关闭全部");
			State state = new State();
			state.setBri(0);
			state.setHue(0);
			state.setSat(0);
			state.setOn(false);
			for (final LightEntry lightEntry : lightEntries) {
				lightsController.setLightState(lightEntry.getId(), state, new SetLightCallback());
			}
		} else {
			Logger.i(Constants.TAG, "设置模板");
			List<LightAttr> lightAttrs = templateDao.getLightAttrListByTid((int) id);
			LightAttr lightAttr;
			for (int i = 0; i < lightAttrs.size(); i++) {
				lightAttr = lightAttrs.get(i);
				State state = new State();
				state.setAlert(lightAttr.getAlert());
				state.setBri(lightAttr.getBri());
				state.setColormode(lightAttr.getColormode());
				state.setCt(lightAttr.getCt());
				state.setEffect(lightAttr.getEffect());
				state.setHue(lightAttr.getHue());
				state.setOn(lightAttr.getState() == 1 ? true : false);
				state.setSat(lightAttr.getSat());
				state.setTransitiontime(lightAttr.getTransitiontime());
				state.setXy(lightAttr.getXy_x(), lightAttr.getXy_y());
				lightsController.setLightState(lightEntries.get(i).getId() + "", state, new SetLightCallback());
			}
		}
		new Thread() {
			public void run() {
				while (true) {
					if (flag == 3) {
						Logger.i(Constants.TAG, "成功设置所有灯泡属性");
						flag = 0;
						activity.closeProgressDialog();
						break;
					}
					if (!setLightAttributesAndState) {
						Logger.e(Constants.TAG, "设置灯泡属性失败");
						flag = 0;
						activity.closeProgressDialog();
						break;
					}
				}
			};
		}.start();
	}

	private void showLongClickDialog(final int id) {
		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		View view = View.inflate(activity, R.layout.item_long_click_dialog, null);
		Button updateBtn = (Button) view.findViewById(R.id.item_dialog_update);
		updateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i("HomeMomentAdapter", "修改" + id);
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
				Logger.i("HomeMomentAdapter", "删除" + id);
				if (templateDao.deleteTemplete(id)) {
					datas.remove(getDeleteTemplate(id));
					MainGridViewAdapter.this.notifyDataSetChanged();
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

	private class SetLightCallback implements LightCallback {

		@Override
		public void onSuccess(Object obj) {
			flag++;
		}

		@Override
		public void onFailure(Object obj) {
			Logger.e(Constants.TAG, "设置灯泡属性失败");
			setLightAttributesAndState = false;
		}

		@Override
		public void wifiError() {
			Logger.e(Constants.TAG, "wifi链接不对");
			activity.closeProgressDialog();
			activity.openSplashActivity();
		}

		@Override
		public void unauthorized() {
			Logger.e(Constants.TAG, "用户名失效");
			activity.closeProgressDialog();
			Editor editor = activity.sp.edit();
			editor.putString("username", "");
			editor.commit();
			activity.openSplashActivity();
		}

		@Override
		public void pressLinkBtn() {
			Logger.i(Constants.TAG, "按钮");
		}

	}

}
