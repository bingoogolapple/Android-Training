package com.bingoogol.smartbulb.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bingoogol.smartbulb.model.Template;
import com.bingoogol.smarthue.R;

public class MainGridViewAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Template> datas;

	public MainGridViewAdapter(Context context, List<Template> datas) {
		this.layoutInflater = LayoutInflater.from(context);
		this.datas = datas;
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
		if(position == 0) {
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

}
