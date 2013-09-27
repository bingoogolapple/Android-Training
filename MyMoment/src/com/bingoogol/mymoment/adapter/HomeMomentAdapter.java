package com.bingoogol.mymoment.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bingoogol.mymoment.R;
import com.bingoogol.mymoment.domain.Moment;
import com.bingoogol.mymoment.util.ImageCache;
import com.bingoogol.mymoment.util.ImageFetcher;
import com.bingoogol.mymoment.util.ImageFetcher.ImgCallback;

public class HomeMomentAdapter extends BaseAdapter {
	private List<Moment> datas;
	private LayoutInflater layoutInflater;
	private ListView listView;

	public HomeMomentAdapter(Context context, ListView listView, List<Moment> datas) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.datas = datas;
		this.listView = listView;
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
			viewHolder.contentLeftTv = (TextView) convertView.findViewById(R.id.content_left_tv);
			viewHolder.imgLeftIv = (ImageView) convertView.findViewById(R.id.img_left_iv);
			viewHolder.publishTimeLeftTv = (TextView) convertView.findViewById(R.id.publish_time_left_tv);
			viewHolder.contentRightTv = (TextView) convertView.findViewById(R.id.content_right_tv);
			viewHolder.imgRightIv = (ImageView) convertView.findViewById(R.id.img_right_iv);
			viewHolder.publishTimeRightTv = (TextView) convertView.findViewById(R.id.publish_time_right_tv);
			viewHolder.itemRightLl = (LinearLayout) convertView.findViewById(R.id.item_right_ll);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.itemRightLl.setVisibility(View.VISIBLE);
			viewHolder.publishTimeRightTv.setVisibility(View.VISIBLE);
		}
		// 加载左边部分
		Moment leftMoment = datas.get(position * 2);
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
		private TextView contentLeftTv;
		private TextView publishTimeLeftTv;
		private ImageView imgLeftIv;
		private TextView contentRightTv;
		private TextView publishTimeRightTv;
		private ImageView imgRightIv;
		private LinearLayout itemRightLl;
	}
}
