package com.bingoogol.smartbulb.ui;

import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.db.dao.TemplateDao;
import com.bingoogol.smartbulb.domain.Template;
import com.bingoogol.smartbulb.domain.http.LightEntry;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.engine.LightsController;
import com.bingoogol.smartbulb.ui.adapter.MainGridViewAdapter;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.MyAnimations;
import com.bingoogol.smartbulb.util.ToastUtil;

public class MainActivity extends GenericActivity {
	protected static final String TAG = "MainActivity";
	private GridView mainGv;
	private MainGridViewAdapter adapter;
	private TemplateDao templateDao;
	private boolean isFunctionsShowing;
	private RelativeLayout functionRl;
	private ImageButton functionIb;

	private ImageButton addIb;

	private LightsController lightsController;
	private ProgressDialog pd;

	private int offset = 0;
	private int maxResult = 12;
	private boolean isLoading = false;

	private List<LightEntry> lightEntries;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			refresh();
		}
	}

	public void refresh() {
		Logger.i(Constants.TAG, "刷新");
		adapter = null;
		offset = 0;
		fillGridView();
	}

	public void openProgressDialog(String msg) {
		pd = ProgressDialog.show(this, "提示", msg);
		pd.setCancelable(false);
	}

	public void closeProgressDialog() {
		pd.dismiss();
	}

	private void fillGridView() {
		new AsyncTask<Void, Void, List<Template>>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				isLoading = true;
				openProgressDialog("正在加载数据");
			}

			@Override
			protected void onPostExecute(List<Template> datas) {
				if (datas != null) {
					Logger.i(TAG, "datas.size >> " + datas.size());
					if (adapter == null) {
						Logger.i(TAG, "第一次加载模板 >> " + datas.size());
						Template template = new Template();
						template.setId(-1);
						template.setName("ALL OFF");
						datas.add(0, template);
						adapter = new MainGridViewAdapter(MainActivity.this, datas, lightEntries);
						mainGv.setAdapter(adapter);
						mainGv.setOnItemClickListener(adapter);
						mainGv.setOnItemLongClickListener(adapter);
					} else {
						Logger.i(TAG, "加载更多模板 >> " + datas.size());
						// 把获取到的数据添加到数据适配器里
						adapter.addMoreMoment(datas);
						adapter.notifyDataSetChanged();
					}
				} else {
					Logger.i(TAG, "datas is null");
				}
				closeProgressDialog();
				isLoading = false;
				super.onPostExecute(datas);
			}

			@Override
			protected List<Template> doInBackground(Void... params) {
				if (templateDao == null) {
					templateDao = new TemplateDao(getApplicationContext());
				}
				return templateDao.getTemplateScrollData(offset, maxResult);
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_function_main:
			if (!isFunctionsShowing) {
				showFunction();
			} else {
				closeFunction();
			}
			break;

		case R.id.ib_add_main:
			Logger.i(Constants.TAG, "点击了添加按钮");
			closeFunction();
			Intent editTemplateIntent = new Intent(getApplicationContext(), EditTemplateActivity.class);
			startActivityForResult(editTemplateIntent, 0);
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			break;

		default:
			break;
		}
	}

	private void showFunction() {
		MyAnimations.startAnimationsIn(functionRl, 300);
		functionIb.startAnimation(MyAnimations.getRotateAnimation(0, -270, 300));
		isFunctionsShowing = true;
	}

	private void closeFunction() {
		MyAnimations.startAnimationsOut(functionRl, 300);
		functionIb.startAnimation(MyAnimations.getRotateAnimation(-270, 0, 300));
		isFunctionsShowing = false;
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_main);
		MyAnimations.initOffset(MainActivity.this);
	}

	@Override
	protected void findViewById() {
		mainGv = (GridView) this.findViewById(R.id.gv_main);
		functionRl = (RelativeLayout) this.findViewById(R.id.rl_function_main);
		functionIb = (ImageButton) this.findViewById(R.id.ib_function_main);
		addIb = (ImageButton) this.findViewById(R.id.ib_add_main);
	}

	@Override
	protected void setListener() {
		functionIb.setOnClickListener(this);
		addIb.setOnClickListener(this);
		mainGv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 如果当前滚动状态为静止状态，并且mainGv里面最后一个用户可见的条目等于mainGv适配器里的最后一个条目
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 如果不是正在加载数据才去加载数据
					if (!isLoading) {
						// 从0开始
						Logger.i(TAG, "view.getLastVisiblePosition():" + view.getLastVisiblePosition());
						int lastPosition = view.getLastVisiblePosition() + 1;
						int count = adapter.getCount();
						Logger.i(TAG, "lastPosition:" + lastPosition + "  count:" + count);
						if (lastPosition == count) {
							Logger.i(TAG, "offset:" + offset + "  maxResult:" + maxResult + "  templateDao.getCount():" + templateDao.getCount());
							if (offset + maxResult < templateDao.getCount()) {
								offset += maxResult;
								fillGridView();
							} else {
								ToastUtil.makeText(MainActivity.this, "没有更多数据了");
							}
						}
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		app.exit();
	}

	@Override
	protected void processLogic() {
		lightsController = new LightsController();
		openProgressDialog("正在获取灯泡列表");
		lightsController.getAllLights(new LightCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object obj) {
				lightEntries = (List<LightEntry>) obj;
				Collections.sort(lightEntries);
				// 成功获取到当前桥接器链接的所有灯的信息，为gridview填充数据
				fillGridView();
			}

			@Override
			public void onFailure() {
				Logger.e(Constants.TAG, "获取灯泡列表失败");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void wifiError() {
				Logger.e(Constants.TAG, "wifi链接不对");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void unauthorized() {
				Logger.e(Constants.TAG, "用户名失效");
				app.addSp("username", "");
				app.exit();
				openSplashActivity();

			}

			@Override
			public void pressLinkBtn() {
				Logger.i(Constants.TAG, "按钮");
			}

			@Override
			public void closeDialog() {
				closeProgressDialog();
			}

		});
		functionIb.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));
	}

	public void openSplashActivity() {
		Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
		startActivity(intent);
		this.finish();
	}

}
