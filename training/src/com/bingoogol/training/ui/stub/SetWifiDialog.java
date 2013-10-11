package com.bingoogol.training.ui.stub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bingoogol.training.App;
import com.bingoogol.training.R;
import com.bingoogol.training.util.Constants;

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
			break;
		case R.id.btn_set_wifi_exit:
			App app = (App) activity.getApplication();
			app.exit();
			break;
		default:
			break;
		}
		this.dismiss();
	}

}
