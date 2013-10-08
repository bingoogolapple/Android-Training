package com.bingoogol.smartbulb.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bingoogol.smartbulb.api.LightsController;
import com.bingoogol.smartbulb.api.LightHandler.LightCallback;
import com.bingoogol.smartbulb.httpmodel.LightEntry;
import com.bingoogol.smartbulb.httpmodel.State;
import com.bingoogol.smartbulb.model.LightAttr;
import com.bingoogol.smartbulb.model.ModelAccess;
import com.bingoogol.smartbulb.model.Template;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.StorageUtil;
import com.bingoogol.smarthue.R;

public class AddTemplateActivity extends GenericActivity implements OnTouchListener {

	private ImageView picker1;
	private ImageView picker2;
	private ImageView picker3;
	private int index = 0;
	private int saveFlag = 0;
	private ImageView imgIv;
	private SeekBar sbar;
	private EditText nameEt;
	private Button saveBtn;
	private Button selectImgBtn;
	private TextView bulbNameTv;
	private ProgressDialog pd;
	private LightsController lightsController = new LightsController();
	private List<ImageView> pickersList = new ArrayList<ImageView>();
	private List<LightEntry> lightEntries;
	private boolean getLightAttributesAndState = true;

	private void showAddImgMethodDialog() {
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		View view = View.inflate(this, R.layout.add_img_dialog, null);
		Button updateBtn = (Button) view.findViewById(R.id.camera_btn);
		updateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFromCamera();
				dialog.dismiss();
			}
		});
		Button deleteBtn = (Button) view.findViewById(R.id.gallery_btn);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFromGallery();
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	private void getFromCamera() {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imgRealPath = StorageUtil.getImageDir() + File.separator + "IMG_" + timeStamp + ".png";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imgRealPath)));
		startActivityForResult(intent, Constants.activity.GET_FROM_CAMERA);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	private void getFromGallery() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		// 设置特定环境下才能执行的参数，例如机顶盒，车载模式等
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		// 当同时有data和type时应该用intent.setDataAndType(data, type)
		intent.setType("image/*");
		startActivityForResult(intent, Constants.activity.GET_FROM_GALLERY);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 180);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constants.activity.GET_FROM_CROP);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constants.activity.GET_FROM_GALLERY:
				startPhotoZoom(data.getData());
				break;
			case Constants.activity.GET_FROM_CAMERA:
				startPhotoZoom(Uri.fromFile(new File(data.getData().getPath())));
				break;
			case Constants.activity.GET_FROM_CROP:
				if (data != null) {
					Bitmap bitmap = data.getExtras().getParcelable("data");
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
			break;
		case R.id.picker2:
			index = 1;
			bulbNameTv.setText(lightEntries.get(index).getName());
			break;
		case R.id.picker3:
			index = 2;
			bulbNameTv.setText(lightEntries.get(index).getName());
			break;
		case R.id.btn_select_img:
			showAddImgMethodDialog();
			break;
		case R.id.btn_save:
			final String name = nameEt.getText().toString().trim();
			if (name.length() == 0) {
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				nameEt.startAnimation(shake);
			} else {
				openProgressDialog("正在获取灯泡属性");
				for (final LightEntry lightEntry : lightEntries) {
					lightsController.getLightAttributesAndState(lightEntry.getId(), new LightCallback() {

						@Override
						public void onSuccess(Object obj) {
							int index = lightEntries.indexOf(lightEntry);
							Logger.i(Constants.TAG, "index >> " + index);
							lightEntries.set(index, (LightEntry) obj);
							saveFlag++;
						}

						@Override
						public void onFailure(Object obj) {
							Logger.e(Constants.TAG, "获取灯泡属性失败");
							getLightAttributesAndState = false;
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
				}
				new Thread() {
					public void run() {
						while (true) {
							if (saveFlag == 3) {
								Logger.i(Constants.TAG, "成功获取所有灯泡属性");
								closeProgressDialog();
								ModelAccess modelAccess = new ModelAccess(getApplicationContext());
								Template template = new Template();
								template.setName(name);
								String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
								String imgPath = StorageUtil.getImageDir() + File.separator + "IMG_" + timeStamp + ".png";
								template.setImgPath(imgPath);
								Bitmap bitmap = imgIv.getDrawingCache();
								ArrayList<LightAttr> lightAttrs = new ArrayList<LightAttr>();
								for (int i = 0; i < 3; i++) {
									LightEntry lightEntry = lightEntries.get(i);
									LightAttr lightAttr = new LightAttr();
									lightAttr.setType(lightEntry.getType());
									lightAttr.setName(lightEntry.getName());
									lightAttr.setModelid(lightEntry.getModelid());
									lightAttr.setSwversion(lightEntry.getSwversion());
									lightAttr.setState(lightEntry.getState().isOn() == false ? 0 : 1);
									lightAttr.setBri(lightEntry.getState().getBri());
									lightAttr.setHue(lightEntry.getState().getHue());
									lightAttr.setSat(lightEntry.getState().getSat());
									lightAttr.setXy_x(lightEntry.getState().getXy()[0]);
									lightAttr.setXy_y(lightEntry.getState().getXy()[1]);
									lightAttr.setCt(lightEntry.getState().getCt());
									lightAttr.setAlert(lightEntry.getState().getAlert());
									lightAttr.setEffect(lightEntry.getState().getEffect());
									lightAttr.setTransitiontime(lightEntry.getState().getTransitiontime());
									lightAttr.setColormode(lightEntry.getState().getColormode());
									lightAttrs.add(lightAttr);
								}
								if (modelAccess.addTemplete(template, lightAttrs, bitmap)) {
									Logger.i(Constants.TAG, "添加成功");
									setResult(RESULT_OK);
									finish();
									overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
								} else {
									Logger.e(Constants.TAG, "添加失败");
								}
								break;
							}
							if (!getLightAttributesAndState) {
								Logger.e(Constants.TAG, "获取灯泡属性失败");
								closeProgressDialog();
								break;
							}
						}
					};
				}.start();
			}
			break;

		default:
			break;
		}
	}

	private void openProgressDialog(String msg) {
		pd = ProgressDialog.show(AddTemplateActivity.this, "提示", msg);
		pd.setCancelable(false);
	}

	private void closeProgressDialog() {
		pd.dismiss();
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_add_template);
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
				openProgressDialog("正在设置灯泡亮度");
				lightsController.setLightState(lightEntries.get(index).getId(), state, new LightCallback() {
					@Override
					public void onSuccess(Object obj) {
						closeProgressDialog();
						Logger.i(Constants.TAG, "修改亮度成功");
					}

					@Override
					public void onFailure(Object obj) {
						closeProgressDialog();
						Logger.e(Constants.TAG, "修改亮度失败");
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
		openProgressDialog("正在获取灯泡列表");
		lightsController.getAllLights(new LightCallback() {

			@Override
			public void onSuccess(Object obj) {
				lightEntries = (List<LightEntry>) obj;
				Collections.sort(lightEntries);
				bulbNameTv.setText(lightEntries.get(index).getName());
				closeProgressDialog();
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
				setPickerColor(color);
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
				openProgressDialog("正在设置灯泡颜色");
				lightsController.setLightState(String.valueOf(index + 1), state, new LightCallback() {
					@Override
					public void onSuccess(Object obj) {
						closeProgressDialog();
						Logger.i(Constants.TAG, "修改LightAttr成功");
					}

					@Override
					public void onFailure(Object obj) {
						closeProgressDialog();
						Logger.e(Constants.TAG, "修改LightAttr失败");
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
				break;
			default:
				break;
			}
		}
		return true;
	}

	public void openSplashActivity() {
		Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
		startActivity(intent);
		this.finish();
	}

	private void setPickerColor(int color) {
		ImageView picker = pickersList.get(index);
		Bitmap alterBitmap = Bitmap.createBitmap(picker.getWidth(), picker.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(alterBitmap);
		Paint paint = new Paint();
		paint.setColor(color);
		canvas.drawCircle(picker.getWidth() / 2, picker.getWidth() / 2, picker.getWidth() / 2, paint);
		picker.setImageBitmap(alterBitmap);
	}
}
