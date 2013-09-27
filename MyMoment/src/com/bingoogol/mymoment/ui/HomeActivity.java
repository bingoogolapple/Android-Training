package com.bingoogol.mymoment.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bingoogol.mymoment.R;
import com.bingoogol.mymoment.adapter.HomeMomentAdapter;
import com.bingoogol.mymoment.domain.Moment;
import com.bingoogol.mymoment.service.MomentService;
import com.bingoogol.mymoment.util.DateUtil;
import com.bingoogol.mymoment.util.Logger;
import com.bingoogol.mymoment.util.ToastUtil;

public class HomeActivity extends GenericActivity {
	private Button writeBtn;
	private TextView dateTv;
	private TextView timeTv;
	private ListView momentLv;
	private HomeMomentAdapter adapter;
	private MomentService momentService;
	private int offset = 0;
	private int maxResult = 6;
	private boolean isLoading = false;
	private ProgressDialog pd;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			refresh();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_write_btn:
			Intent writeIntent = new Intent(context, WriteActivity.class);
			startActivityForResult(writeIntent, 0);
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			break;
		case R.id.refresh_btn:
			refresh();
			break;

		default:
			break;
		}
	}

	private void refresh() {
		adapter = null;
		offset = 0;
		fillList();
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_home);
	}

	@Override
	protected void findViewById() {
		writeBtn = (Button) this.findViewById(R.id.home_write_btn);
		momentLv = (ListView) this.findViewById(R.id.home_moment_lv);
		View headerView = LayoutInflater.from(context).inflate(R.layout.moment_header, null);
		momentLv.addHeaderView(headerView);
		momentLv.setDivider(null);
		dateTv = (TextView) headerView.findViewById(R.id.home_date_tv);
		timeTv = (TextView) headerView.findViewById(R.id.home_time_tv);
	}

	@Override
	protected void setListener() {
		writeBtn.setOnClickListener(this);
		momentLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 如果当前滚动状态为静止状态，并且listview里面最后一个用户可见的条目等于listview适配器里的最后一个条目
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 如果不是正在加载数据才去加载数据
					if (!isLoading) {
						// 从1开始
						int position = view.getLastVisiblePosition();
						int count = adapter.getCount();
						if (position == count) {
							if (offset + maxResult < momentService.getCount()) {
								offset += maxResult;
								fillList();
							} else {
								ToastUtil.makeText(context, "没有更多数据了");
							}
						}
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
	}

	// 加载对话框
	public void showDialog() {
		pd = ProgressDialog.show(HomeActivity.this, "提示", "加载数据中...");
		pd.setCancelable(false);
	}

	// 关闭对话框
	public void closeDialog() {
		pd.dismiss();
	}

	@Override
	protected void processLogic() {
		momentService = new MomentService(context);
		setDateTime();
		fillList();
	}

	private Handler dateTimeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dateTv.setText(DateUtil.getDateString());
			timeTv.setText(DateUtil.getTimeString());
		};
	};

	private void setDateTime() {
		dateTv.setText(DateUtil.getDateString());
		timeTv.setText(DateUtil.getTimeString());
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
						dateTimeHandler.sendMessage(new Message());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void fillList() {
		new AsyncTask<Void, Void, List<Moment>>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				isLoading = true;
				showDialog();
			}

			@Override
			protected void onPostExecute(List<Moment> result) {				
				if (result != null) {
					if (adapter == null) {
						adapter = new HomeMomentAdapter(HomeActivity.this, momentLv, result);
						momentLv.setAdapter(adapter);
					} else {
						// 把获取到的数据添加到数据适配器里
						adapter.addMoreMoment(result);
						adapter.notifyDataSetChanged();
					}
				} else {
					Logger.i("bingo", "result is null");
				}
				closeDialog();
				isLoading = false;
				super.onPostExecute(result);
			}

			@Override
			protected List<Moment> doInBackground(Void... params) {
				if (momentService == null) {
					momentService = new MomentService(context);
				}
				return momentService.getScrollData(offset, maxResult);
			}

		}.execute();
	}

}
