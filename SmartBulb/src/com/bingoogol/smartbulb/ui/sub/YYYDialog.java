package com.bingoogol.smartbulb.ui.sub;

import java.util.List;
import java.util.Random;

import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.db.dao.TemplateDao;
import com.bingoogol.smartbulb.domain.Template;
import com.bingoogol.smartbulb.ui.MainActivity;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.Logger;
import com.bingoogol.smartbulb.util.StorageUtil;

/**
 * 摇一摇对话框
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class YYYDialog extends Dialog {
	protected static final String TAG = "YYYDialog";
	private TemplateDao templateDao;
	private TextView nameTv;
	private ImageView iconIv;
	private MainActivity activity;
	private SensorManager sensorManager;
	private Vibrator vibrator;
	private List<Template> templatesYyy;
	
	public YYYDialog(MainActivity activity) {
		super(activity, R.style.DialogTheme);
		this.activity = activity;
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yyy_dialog);
		templateDao = new TemplateDao(activity);
		nameTv = (TextView) this.findViewById(R.id.tv_name_yyy);
		iconIv = (ImageView) this.findViewById(R.id.iv_icon_yyy);
		sensorManager = (SensorManager)activity.getSystemService(activity.SENSOR_SERVICE);
		vibrator = (Vibrator) activity.getSystemService(activity.VIBRATOR_SERVICE);
		sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		Logger.i(TAG, "注册监听器");
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(sensorEventListener);
		Logger.i(TAG, "取消监听器");
		super.onStop();
	}
	
	private Handler yyyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.activity.YYY:
				long count = templateDao.getCount();
				int offsetYyy = new Random().nextInt((int) count);
				templatesYyy = templateDao.getTemplateScrollData(offsetYyy, 1);
				if (templatesYyy.size() == 1) {
					Template template = templatesYyy.get(0);
					nameTv.setText(template.getName());
					iconIv.setImageBitmap(StorageUtil.getBitmapFromLocal(template.getImgPath(), 300, 240));
					activity.adapter.setTemplate(template.getId());
				}
				break;
			}
		}

	};
	
	/**
	 * 重力感应监听
	 */
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; // x轴方向的重力加速度，向右为正
			float y = values[1]; // y轴方向的重力加速度，向前为正
			float z = values[2]; // z轴方向的重力加速度，向上为正
			int medumValue = 19;
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
				vibrator.vibrate(200);
				Message msg = new Message();
				msg.what = Constants.activity.YYY;
				yyyHandler.sendMessage(msg);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
}
