package com.bingoogol.smartbulb.ui.sub;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bingoogol.smartbulb.App;
import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.ui.SplashActivity;

/**
 * 提示按下桥接器上按钮的自定义对话框
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class LinkButtonDialog extends Dialog implements OnClickListener {
	private Button commitBtn;
	private Button exitBtn;
	private SplashActivity activity;

	public LinkButtonDialog(SplashActivity activity) {
		super(activity, R.style.DialogTheme);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_linkbutton);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		commitBtn = (Button) this.findViewById(R.id.btn_linkbutton_commit);
		exitBtn = (Button) this.findViewById(R.id.btn_linkbutton_exit);
		commitBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_linkbutton_commit:
			this.dismiss();
			activity.auth();
			break;
		case R.id.btn_linkbutton_exit:
			this.dismiss();
			App app = (App) activity.getApplication();
			app.exit();
			break;
		}
		this.dismiss();
	}

}
