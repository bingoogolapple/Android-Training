package com.bingoogol.mymoment.adapter;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bingoogol.mymoment.R;
import com.bingoogol.mymoment.domain.Moment;
import com.bingoogol.mymoment.service.MomentService;
import com.bingoogol.mymoment.ui.WriteActivity;
import com.bingoogol.mymoment.util.ImageCache;
import com.bingoogol.mymoment.util.ImageFetcher;
import com.bingoogol.mymoment.util.ImageFetcher.ImgCallback;
import com.bingoogol.mymoment.util.Logger;
import com.bingoogol.mymoment.util.ToastUtil;

public class HomeMomentAdapter extends BaseAdapter implements OnLongClickListener {
	private List<Moment> datas;
	private LayoutInflater layoutInflater;
	private ListView listView;
	private Activity context;
	
	public HomeMomentAdapter(Activity context, ListView listView, List<Moment> datas) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.datas = datas;
		this.listView = listView;
		this.context = context;
	}

	public void addMoreMoment(List<Moment> moments) {
		datas.addAll(moments);
	}

	@Override
	public int getCount() {
		return (datas.size() + 1) / 2;
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
		// 0 0 1
		// 1 2 3
		// 2 4 5
		// 3 6 7
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.moment_item, null);
			viewHolder = new ViewHolder();
			viewHolder.idLeftTv = (TextView) convertView.findViewById(R.id.id_left_tv);
			viewHolder.contentLeftTv = (TextView) convertView.findViewById(R.id.content_left_tv);
			viewHolder.imgLeftIv = (ImageView) convertView.findViewById(R.id.img_left_iv);
			viewHolder.publishTimeLeftTv = (TextView) convertView.findViewById(R.id.publish_time_left_tv);
			viewHolder.itemLeftLl = (LinearLayout) convertView.findViewById(R.id.item_left_ll);
			viewHolder.idRightTv = (TextView) convertView.findViewById(R.id.id_right_tv);
			viewHolder.contentRightTv = (TextView) convertView.findViewById(R.id.content_right_tv);
			viewHolder.imgRightIv = (ImageView) convertView.findViewById(R.id.img_right_iv);
			viewHolder.publishTimeRightTv = (TextView) convertView.findViewById(R.id.publish_time_right_tv);
			viewHolder.itemRightLl = (LinearLayout) convertView.findViewById(R.id.item_right_ll);
			convertView.setTag(viewHolder);

			viewHolder.itemLeftLl.setOnLongClickListener(this);
			viewHolder.itemRightLl.setOnLongClickListener(this);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.itemRightLl.setVisibility(View.VISIBLE);
			viewHolder.publishTimeRightTv.setVisibility(View.VISIBLE);
		}
		// 加载左边部分
		Moment leftMoment = datas.get(position * 2);
		viewHolder.idLeftTv.setText(leftMoment.getId() + "");
		viewHolder.publishTimeLeftTv.setText(leftMoment.getPublishTime().substring(11));
		viewHolder.contentLeftTv.setText(leftMoment.getContent());
		String leftImgRealPath = leftMoment.getImgPath();
		if (ImageCache.isExistsInMemory(leftImgRealPath)) {
			viewHolder.imgLeftIv.setImageBitmap(ImageCache.get(leftImgRealPath));
		} else {
			viewHolder.imgLeftIv.setImageResource(R.drawable.ic_launcher);
			viewHolder.imgLeftIv.setTag(leftImgRealPath);
			ImageFetcher.loadBitmap(leftImgRealPath, 100, 100, new ImgCallback() {
				@Override
				public void imgLoaded(Bitmap bitmap, String imageUrl) {
					ImageView imgIvByTag = (ImageView) listView.findViewWithTag(imageUrl);
					if (imgIvByTag != null && bitmap != null) {
						imgIvByTag.setImageBitmap(bitmap);
					}
				}
			});
		}
		// 如果右边部分有才加载
		if (position * 2 + 1 < datas.size()) {
			Moment rightMoment = datas.get(position * 2 + 1);
			viewHolder.idRightTv.setText(rightMoment.getId() + "");
			viewHolder.publishTimeRightTv.setText(rightMoment.getPublishTime().substring(11));
			viewHolder.contentRightTv.setText(rightMoment.getContent());
			String rightImgRealPath = rightMoment.getImgPath();
			if (ImageCache.isExistsInMemory(rightImgRealPath)) {
				viewHolder.imgRightIv.setImageBitmap(ImageCache.get(rightImgRealPath));
			} else {
				viewHolder.imgRightIv.setImageResource(R.drawable.ic_launcher);
				viewHolder.imgRightIv.setTag(rightImgRealPath);
				ImageFetcher.loadBitmap(rightImgRealPath, 100, 100, new ImgCallback() {
					@Override
					public void imgLoaded(Bitmap bitmap, String imageUrl) {
						ImageView imgIvByTag = (ImageView) listView.findViewWithTag(imageUrl);
						if (imgIvByTag != null && bitmap != null) {
							imgIvByTag.setImageBitmap(bitmap);
						}
					}

				});
			}
		} else {
			viewHolder.publishTimeRightTv.setVisibility(View.INVISIBLE);
			viewHolder.itemRightLl.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView idLeftTv;
		private TextView contentLeftTv;
		private TextView publishTimeLeftTv;
		private ImageView imgLeftIv;
		private TextView idRightTv;
		private TextView contentRightTv;
		private TextView publishTimeRightTv;
		private ImageView imgRightIv;
		private LinearLayout itemLeftLl;
		private LinearLayout itemRightLl;
	}

	@Override
	public boolean onLongClick(View v) {
		TextView idTv = null;
		String id = null;
		switch (v.getId()) {
		case R.id.item_left_ll:
			idTv = (TextView) v.findViewById(R.id.id_left_tv);
			id = idTv.getText().toString().trim();
			break;
		case R.id.item_right_ll:
			idTv = (TextView) v.findViewById(R.id.id_right_tv);
			id = idTv.getText().toString().trim();
			break;
		}
		showDialog(Integer.parseInt(id));
		return false;
	}
	
	
	private void showDialog(final int id) {
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		View view = View.inflate(context, R.layout.item_long_click_dialog, null);
		Button updateBtn = (Button) view.findViewById(R.id.item_dialog_update);
		updateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i("HomeMomentAdapter", "修改" + id);
				Intent intent = new Intent(context,WriteActivity.class);
				intent.putExtra("id", id);
				context.startActivityForResult(intent, 0);
				context.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
				dialog.dismiss();
			}
		});
		Button deleteBtn = (Button) view.findViewById(R.id.item_dialog_delete);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i("HomeMomentAdapter", "删除" + id);
				MomentService momentService = new MomentService(context);
				if(momentService.deleteMoment(id)) {
					datas.remove(getDeleteMoment(id));
					HomeMomentAdapter.this.notifyDataSetChanged();
					Logger.i("HomeMomentAdapter", "size=" + datas.size());
					dialog.dismiss();
				} else {
					ToastUtil.makeCustomToast(context, "删除失败");
				}
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}
	
	/**
	 * 从数据源中获取要删除的Moment
	 * @param id
	 * @return
	 */
	public Moment getDeleteMoment(int id) {
		for(Moment moment : datas) {
			if(moment.getId() == id) {
				return moment;
			}
		}
		return null;
	}
}
