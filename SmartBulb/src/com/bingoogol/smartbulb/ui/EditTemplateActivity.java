package com.bingoogol.smartbulb.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.db.dao.TemplateDao;
import com.bingoogol.smartbulb.domain.LightAttr;
import com.bingoogol.smartbulb.domain.Template;
import com.bingoogol.smartbulb.domain.http.LightEntry;
import com.bingoogol.smartbulb.domain.http.State;
import com.bingoogol.smartbulb.engine.LightHandler.LightCallback;
import com.bingoogol.smartbulb.engine.LightsController;
import com.bingoogol.smartbulb.ui.sub.SelectImgDialog;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.StorageUtil;
import com.bingoogol.smartbulb.util.ToastUtil;

/**
 * 编辑模板
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class EditTemplateActivity extends GenericActivity implements OnTouchListener {

	private static final String TAG = "EditTemplateActivity";
	private ImageView picker1;
	private ImageView picker2;
	private ImageView picker3;
	private int index = 0;
	private ImageView imgIv;
	private SeekBar sbar;
	private EditText nameEt;
	private Button saveBtn;
	private Button selectImgBtn;
	private TextView bulbNameTv;
	private TextView titleTv;
	private ProgressDialog pd;
	private TemplateDao templateDao;
	private LightsController lightsController = new LightsController();
	private ArrayList<ImageView> pickersList = new ArrayList<ImageView>();
	private ArrayList<LightEntry> lightEntries;
	private ArrayList<LightAttr> lightAttrs;
	private boolean isAdd = true;
	private int id;
	public String imgRealPath = "";
	private SelectImgDialog selectImgDialog;
	private Typeface typeface;

	private int setTemplateFlag = 0;
	private LightCallback setTemplateCallback = new LightCallback() {

		@Override
		public void onSuccess(Object obj) {
			setTemplateFlag++;
			if (setTemplateFlag == 3) {
				Logger.i(TAG, "成功设置所有灯泡属性");
				initColor();
				pd.dismiss();
			}
		}

		@Override
		public void onFailure() {
			Logger.e(TAG, "设置灯泡属性失败");
			setTemplateFlag = 0;
			pd.dismiss();
			app.exit();
			openSplashActivity();
		}

		@Override
		public void wifiError() {
			Logger.e(TAG, "wifi链接不对");
			pd.dismiss();
			app.exit();
			openSplashActivity();
		}

		@Override
		public void unauthorized() {
			Logger.e(TAG, "用户名失效");
			pd.dismiss();
			app.addSp("username", "");
			app.exit();
			openSplashActivity();
		}

		@Override
		public void pressLinkBtn() {
			Logger.i(TAG, "按钮");
			pd.dismiss();
			app.exit();
			openSplashActivity();
		}

		@Override
		public void closeDialog() {

		}

	};

	private void setTemplate(long id) {
		openProgressDialog(R.string.setting_lightattr);
		lightAttrs = templateDao.getLightAttrListByTid((int) id);
		LightAttr lightAttr;
		setTemplateFlag = 0;
		for (int i = 0; i < lightAttrs.size(); i++) {
			lightAttr = lightAttrs.get(i);
			State state = lightEntries.get(i).getState();
			state.setBri(lightAttr.getBri());
			state.setHue(lightAttr.getHue());
			state.setOn(lightAttr.getState() == 1 ? true : false);
			state.setSat(lightAttr.getSat());
//			state.setAlert(lightAttr.getAlert());
//			state.setColormode(lightAttr.getColormode());
//			state.setCt(lightAttr.getCt());
//			state.setEffect(lightAttr.getEffect());
//			state.setTransitiontime(lightAttr.getTransitiontime());
//			state.setXy(lightAttr.getXy_x(), lightAttr.getXy_y());
			lightsController.setLightState(lightEntries.get(i).getId() + "", state, setTemplateCallback);
		}
	}

	protected void initColor() {
		sbar.setProgress(lightEntries.get(index).getState().getBri());
		setPickerColor(1, 0);
		for(int i = 0; i < lightAttrs.size(); i++) {
			float[] hsv = new float[3];
			hsv[0] = lightAttrs.get(i).getHue() * 360/65535;
			hsv[1] = lightAttrs.get(i).getSat() * 255;
			hsv[2] = lightAttrs.get(i).getBri() * 255;
			int color = Color.HSVToColor(hsv);
			setPickerColor(color, i);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constants.activity.GET_FROM_GALLERY:
				selectImgDialog.startPhotoZoom(data.getData());
				break;
			case Constants.activity.GET_FROM_CAMERA:
				selectImgDialog.startPhotoZoom(Uri.fromFile(new File(imgRealPath)));
				break;
			case Constants.activity.GET_FROM_CROP:
				if (data != null) {
					Bitmap bitmap = data.getExtras().getParcelable("data");
					imgIv.setDrawingCacheEnabled(false);
					imgIv.setImageBitmap(bitmap);
				}
				break;
			default:
				break;
			}
		} else {
			// TODO
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.picker1:
			index = 0;
			bulbNameTv.setText(lightEntries.get(index).getName());
			sbar.setProgress(lightEntries.get(index).getState().getBri());
			break;
		case R.id.picker2:
			index = 1;
			bulbNameTv.setText(lightEntries.get(index).getName());
			sbar.setProgress(lightEntries.get(index).getState().getBri());
			break;
		case R.id.picker3:
			index = 2;
			bulbNameTv.setText(lightEntries.get(index).getName());
			sbar.setProgress(lightEntries.get(index).getState().getBri());
			break;
		case R.id.btn_select_img:
			selectImgDialog.show();
			break;
		case R.id.btn_save:
			final String name = nameEt.getText().toString().trim();
			if (name.length() == 0) {
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				nameEt.startAnimation(shake);
			} else {
				Template template = new Template();
				template.setName(name);
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				String imgPath = StorageUtil.getImageDir() + File.separator + "IMG_" + timeStamp + ".png";
				template.setImgPath(imgPath);
				imgIv.setDrawingCacheEnabled(true);
				Bitmap bitmap = imgIv.getDrawingCache();
				for (int i = 0; i < 3; i++) {
					LightEntry lightEntry = lightEntries.get(i);
					LightAttr lightAttr = lightAttrs.get(i);
//					lightAttr.setType(lightEntry.getType());
//					lightAttr.setName(lightEntry.getName());
//					lightAttr.setModelid(lightEntry.getModelid());
//					lightAttr.setSwversion(lightEntry.getSwversion());
					lightAttr.setState(lightEntry.getState().isOn() == false ? 0 : 1);
					lightAttr.setBri(lightEntry.getState().getBri());
					lightAttr.setHue(lightEntry.getState().getHue());
					lightAttr.setSat(lightEntry.getState().getSat());
//					lightAttr.setXy_x(lightEntry.getState().getXy()[0]);
//					lightAttr.setXy_y(lightEntry.getState().getXy()[1]);
//					lightAttr.setCt(lightEntry.getState().getCt());
//					lightAttr.setAlert(lightEntry.getState().getAlert());
//					lightAttr.setEffect(lightEntry.getState().getEffect());
//					lightAttr.setTransitiontime(lightEntry.getState().getTransitiontime());
//					lightAttr.setColormode(lightEntry.getState().getColormode());
				}
				if (isAdd) {
					if (templateDao.addTemplete(template, lightAttrs, bitmap)) {
						Logger.i(TAG, "添加成功");
						setResult(RESULT_OK);
						finish();
						overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
					} else {
						ToastUtil.makeText(EditTemplateActivity.this, "添加失败");
					}
				} else {
					template.setId(id);
					if (templateDao.updateTemplete(template, lightAttrs, bitmap)) {
						Logger.i(TAG, "修改成功");
						setResult(RESULT_OK);
						finish();
						overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
					} else {
						ToastUtil.makeText(EditTemplateActivity.this, "修改失败");
					}
				}
			}
			break;

		default:
			break;
		}
	}

	private void openProgressDialog(int resId) {
		pd = ProgressDialog.show(this, getResources().getString(R.string.prompt), getResources().getString(resId));
		pd.setCancelable(false);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_edit_template);
	}

	@Override
	protected void findViewById() {
		picker1 = (ImageView) this.findViewById(R.id.picker1);
		picker2 = (ImageView) this.findViewById(R.id.picker2);
		picker3 = (ImageView) this.findViewById(R.id.picker3);
		selectImgBtn = (Button) this.findViewById(R.id.btn_select_img);
		pickersList.add(picker1);
		pickersList.add(picker2);
		pickersList.add(picker3);

		imgIv = (ImageView) this.findViewById(R.id.iv_img_add_template);
		sbar = (SeekBar) findViewById(R.id.edit_sb_bri);
		nameEt = (EditText) this.findViewById(R.id.et_name);
		saveBtn = (Button) this.findViewById(R.id.btn_save);
		bulbNameTv = (TextView) this.findViewById(R.id.tv_bulb_name);
		titleTv = (TextView) this.findViewById(R.id.tv_title_edit_template);
		selectImgDialog = new SelectImgDialog(this);
		
		typeface = Typeface.createFromAsset(getAssets(), "font.ttf");
		nameEt.setTypeface(typeface);
		saveBtn.setTypeface(typeface);
		bulbNameTv.setTypeface(typeface);
		titleTv.setTypeface(typeface);
	}

	@Override
	protected void setListener() {
		picker1.setOnClickListener(this);
		picker2.setOnClickListener(this);
		picker3.setOnClickListener(this);
		selectImgBtn.setOnClickListener(this);
		imgIv.setOnTouchListener(this);
		saveBtn.setOnClickListener(this);
		sbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				State state = lightEntries.get(index).getState();
				state.setBri(seekBar.getProgress());
				setLightState(state);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			}
		});
	}

	@Override
	protected void processLogic() {
		templateDao = new TemplateDao(EditTemplateActivity.this);
		openProgressDialog(R.string.getting_lights);
		lightsController.getAllLights(new LightCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object obj) {
				lightEntries = (ArrayList<LightEntry>) obj;
				Collections.sort(lightEntries);
				bulbNameTv.setText(lightEntries.get(index).getName());
				Bundle bundle = getIntent().getExtras();
				if (bundle != null) {
					isAdd = false;
					saveBtn.setText("修改");
					titleTv.setText("修改模板");
					id = bundle.getInt("id");
					Template template = templateDao.getTemplete(id);
					imgIv.setImageBitmap(BitmapFactory.decodeFile(template.getImgPath()));
					nameEt.setText(template.getName());
					setTemplate(id);
				} else {
					lightAttrs = new ArrayList<LightAttr>();
					openProgressDialog(R.string.setting_lightattr);
					for(int i = 0; i < 3;i++) {
						LightAttr lightAttr = new LightAttr(1, 135, 24775, 232);
						State state = lightEntries.get(i).getState();
						state.setBri(lightAttr.getBri());
						state.setHue(lightAttr.getHue());
						state.setOn(lightAttr.getState() == 1 ? true : false);
						state.setSat(lightAttr.getSat());
						lightAttrs.add(lightAttr);
						lightsController.setLightState(lightEntries.get(i).getId() + "", state, setTemplateCallback);
					}
				}
			}

			@Override
			public void onFailure() {
				Logger.e(TAG, "获取灯泡列表失败");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void wifiError() {
				Logger.e(TAG, "wifi链接不对");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void unauthorized() {
				Logger.e(TAG, "用户名失效");
				app.addSp("username", "");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void pressLinkBtn() {
				Logger.i(TAG, "按钮");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void closeDialog() {
				pd.dismiss();
			}

		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 获得触控的坐标
		int x = (int) event.getX();
		int y = (int) event.getY();
		// 设置绘制缓存
		imgIv.setDrawingCacheEnabled(true);
		Bitmap bm = imgIv.getDrawingCache();
		float width = bm.getWidth();
		float height = bm.getHeight();
		final int color = bm.getPixel(x, y);
		// 获得触摸点的颜色
		if ((x > 0 && x < width) && (y > 0 && y < height)) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				setPickerColor(color, index);
				break;
			case MotionEvent.ACTION_UP:
				// RGB转HSV
				int red = Color.red(color);
				int green = Color.green(color);
				int blue = Color.blue(color);
				// hsv[0]:hue,hsv[1]:sat hsv[2]:bri
				float[] hsv = new float[3];
				Color.RGBToHSV(red, green, blue, hsv);
				int hue = (int) (hsv[0] * 65535.0 / 360.0);
				int sat = (int) (hsv[1] * 255.0);
				int bri = (int) (hsv[2] * 255.0);

				State state = lightEntries.get(index).getState();
				state.setOn(true);
				state.setHue(hue);
				state.setSat(sat);
				state.setBri(bri);
				Logger.i(TAG, "设置灯泡属性 >> hue:" + hue + " sat:" + sat + " bri:" + bri);
				setLightState(state);
				break;
			default:
				break;
			}
		}
		return true;
	}

	private void setLightState(State state) {
		openProgressDialog(R.string.setting_lightattr);
		lightsController.setLightState(lightEntries.get(index).getId(), state, new LightCallback() {

			@Override
			public void wifiError() {
				app.exit();
				openSplashActivity();
			}

			@Override
			public void unauthorized() {
				Logger.e(TAG, "用户名失效");
				app.addSp("username", "");
				app.exit();
				openSplashActivity();
			}

			@Override
			public void pressLinkBtn() {
				app.exit();
				openSplashActivity();
			}

			@Override
			public void onSuccess(Object obj) {
				sbar.setProgress(lightEntries.get(index).getState().getBri());
				Logger.i(TAG, "成功设置灯泡属性");
			}

			@Override
			public void onFailure() {
				app.exit();
				openSplashActivity();
			}

			@Override
			public void closeDialog() {
				pd.dismiss();
			}
		});
	}

	public void openSplashActivity() {
		Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
		startActivity(intent);
		this.finish();
	}

	private void setPickerColor(int color, int position) {
		ImageView picker = pickersList.get(position);
		Bitmap alterBitmap = Bitmap.createBitmap(picker.getWidth(), picker.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(alterBitmap);
		Paint paint = new Paint();
		paint.setColor(color);
		canvas.drawCircle(picker.getWidth() / 2, picker.getWidth() / 2, picker.getWidth() / 2, paint);
		picker.setImageBitmap(alterBitmap);
	}

}
