package com.bingoogol.smartbulb.ui;

import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bingoogol.smartbulb.adapter.MainGridViewAdapter;
import com.bingoogol.smartbulb.api.LightsController;
import com.bingoogol.smartbulb.api.LightHandler.LightCallback;
import com.bingoogol.smartbulb.httpmodel.LightEntry;
import com.bingoogol.smartbulb.httpmodel.State;
import com.bingoogol.smartbulb.model.LightAttr;
import com.bingoogol.smartbulb.model.ModelAccess;
import com.bingoogol.smartbulb.model.Template;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.MyAnimations;
import com.bingoogol.smarthue.R;

public class MainActivity extends GenericActivity implements OnItemClickListener, OnItemLongClickListener {
	private GridView mainGv;
	private MainGridViewAdapter adapter;
	private ModelAccess modelAccess;
	private boolean isFunctionsShowing;
	private RelativeLayout functionRl;
	private ImageButton functionIb;

	private ImageButton addIb;

	private LightsController lightsController;
	private List<LightEntry> lightsList;
	private ProgressDialog pd;

	private int offset = 0;
	private int maxResult = 12;
	private boolean isLoading = false;

	private int flag = 0;

	private List<LightEntry> lightEntries;

	private boolean setLightAttributesAndState = true;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			refresh();
		}
	}

	private void refresh() {
		Logger.i(Constants.TAG, "刷新");
		adapter = null;
		offset = 0;
		fillGridView();
	}

	private void openProgressDialog(String msg) {
		pd = ProgressDialog.show(MainActivity.this, "提示", msg);
		pd.setCancelable(false);
	}

	private void closeProgressDialog() {
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
					if (adapter == null) {
						Template template = new Template();
						template.setId(-1);
						template.setName("ALL OFF");
						datas.add(0, template);
						adapter = new MainGridViewAdapter(MainActivity.this, datas);
						mainGv.setAdapter(adapter);
					} else {
						// 把获取到的数据添加到数据适配器里
						adapter.addMoreMoment(datas);
						adapter.notifyDataSetChanged();
					}
				} else {
					Logger.i(Constants.TAG, "datas is null");
				}
				closeProgressDialog();
				isLoading = false;
				super.onPostExecute(datas);
			}

			@Override
			protected List<Template> doInBackground(Void... params) {
				if (modelAccess == null) {
					modelAccess = new ModelAccess(getApplicationContext());
				}
				return modelAccess.getTemplateScrollData(offset, maxResult);
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
			Intent addTemplate = new Intent(getApplicationContext(), AddTemplateActivity.class);
			startActivityForResult(addTemplate, 0);
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
		mainGv.setOnItemClickListener(this);
		mainGv.setOnItemLongClickListener(this);
		functionIb.setOnClickListener(this);
		addIb.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		lightsController = new LightsController();
		openProgressDialog("正在获取灯泡列表");
		lightsController.getAllLights(new LightCallback() {

			@Override
			public void onSuccess(Object obj) {
				lightEntries = (List<LightEntry>) obj;
				Collections.sort(lightEntries);
				closeProgressDialog();
				fillGridView();
			}

			@Override
			public void onFailure(Object obj) {
				closeProgressDialog();
				Logger.e(Constants.TAG, "获取灯泡列表失败");
			}

			@Override
			public void wifiError() {
				Logger.e(Constants.TAG, "wifi链接不对");
				closeProgressDialog();
				openSplashActivity();
			}

			@Override
			public void unauthorized() {
				Logger.e(Constants.TAG, "用户名失效");
				closeProgressDialog();
				Editor editor = sp.edit();
				editor.putString("username", "");
				editor.commit();
				openSplashActivity();
			}

			@Override
			public void pressLinkBtn() {
				Logger.i(Constants.TAG, "按钮");
			}

		});
		functionIb.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));
	}

	public void openSplashActivity() {
		Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Logger.i(Constants.TAG, "长按");
		// TODO 悬浮删除模板
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		openProgressDialog("正在获取灯泡属性");
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
			List<LightAttr> lightAttrs = modelAccess.getLightAttrListByTid((int) id);
			LightAttr lightAttr;
			for(int i = 0;i < lightAttrs.size(); i++) {
				lightAttr = lightAttrs.get(i);
				State state = new State();
				state.setAlert(lightAttr.getAlert());
				state.setBri(lightAttr.getBri());
				state.setColormode(lightAttr.getColormode());
				state.setCt(lightAttr.getCt());
				state.setEffect(lightAttr.getEffect());
				state.setHue(lightAttr.getHue());
				state.setOn(lightAttr.getState() == 1?true:false);
				state.setSat(lightAttr.getSat());
				state.setTransitiontime(lightAttr.getTransitiontime());
				state.setXy(lightAttr.getXy_x(), lightAttr.getXy_y());
				lightsController.setLightState(lightAttr.getId() + "", state, new SetLightCallback());
			}
		}
		new Thread() {
			public void run() {
				while (true) {
					if (flag == 3) {
						Logger.i(Constants.TAG, "成功设置所有灯泡属性");
						flag = 0;
						closeProgressDialog();
						break;
					}
					if (!setLightAttributesAndState) {
						Logger.e(Constants.TAG, "设置灯泡属性失败");
						flag = 0;
						closeProgressDialog();
						break;
					}
				}
			};
		}.start();
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
			closeProgressDialog();
			openSplashActivity();
		}

		@Override
		public void unauthorized() {
			Logger.e(Constants.TAG, "用户名失效");
			closeProgressDialog();
			Editor editor = sp.edit();
			editor.putString("username", "");
			editor.commit();
			openSplashActivity();
		}

		@Override
		public void pressLinkBtn() {
			Logger.i(Constants.TAG, "按钮");
		}

	}

}
