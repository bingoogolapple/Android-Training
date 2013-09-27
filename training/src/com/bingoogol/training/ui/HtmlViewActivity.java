package com.bingoogol.training.ui;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bingoogol.training.GenericActivity;
import com.bingoogol.training.R;
import com.bingoogol.training.util.ConnectivityUtil;
import com.bingoogol.training.util.NetUtil;

public class HtmlViewActivity extends GenericActivity {

	private Button requestBtn = null;
	private EditText urlEt = null;
	private TextView showTv = null;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_html_view);
	}

	@Override
	protected void findViewById() {
		requestBtn = (Button) this.findViewById(R.id.request_btn);
		urlEt = (EditText) this.findViewById(R.id.url_et);
		showTv = (TextView) this.findViewById(R.id.show_tv);
	}

	@Override
	protected void setListener() {
		requestBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.request_btn:
			request();
			break;

		default:
			break;
		}
	}

	private void request() {
		String url = urlEt.getText().toString().trim();
		if (ConnectivityUtil.isOnline(context)) {
			if (url != null) {
				new AsyncTask<String, Void, String>() {

					@Override
					protected String doInBackground(String... params) {
						return NetUtil.getByUrlConnection(params[0]);
					}

					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						showTv.setText(result);
					}
				}.execute(url);
			} else {
				//提示url不能为空
			}
		} else {
			// 弹出对话框让用户选择是否到系统网络设置界面
		}
	}

	@Override
	protected void processLogic() {

	}
}
