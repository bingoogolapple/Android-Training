package com.bingoogol.smartbulb.ui.sub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bingoogol.smartbulb.App;
import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.util.Constants;

/**
 * 提示设置wifi的自定义对话框
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class SetWifiDialog extends Dialog implements OnClickListener {
	private Button commitBtn;
	private Button exitBtn;
	private Activity activity;

	public SetWifiDialog(Activity activity) {
		super(activity, R.style.DialogTheme);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_set_wifi);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);

		commitBtn = (Button) this.findViewById(R.id.btn_set_wifi_commit);
		exitBtn = (Button) this.findViewById(R.id.btn_set_wifi_exit);
		commitBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_set_wifi_commit:
			Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
			activity.startActivityForResult(intent, Constants.activity.OPEN_WIFI_SETTINGS);
			activity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			this.dismiss();
			break;
		case R.id.btn_set_wifi_exit:
			this.dismiss();
			App app = (App) activity.getApplication();
			app.exit();
			break;
		default:
			break;
		}
		this.dismiss();
	}

}
