package com.bingoogol.training.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bingoogol.training.GenericActivity;
import com.bingoogol.training.R;
import com.bingoogol.training.util.ToastUtil;

public class FirstActivity extends GenericActivity {

	private Button sendBtn = null;
	private Button saveBtn = null;
	private Button readBtn = null;
	private EditText messageEt = null;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_first);
	}

	@Override
	protected void findViewById() {
		sendBtn = (Button) this.findViewById(R.id.first_send_btn);
		saveBtn = (Button) this.findViewById(R.id.first_save_btn);
		readBtn = (Button) this.findViewById(R.id.first_read_btn);
		messageEt = (EditText) this.findViewById(R.id.first_message_et);
	}

	@Override
	protected void setListener() {
		sendBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		readBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences sp = getSharedPreferences("training", Context.MODE_PRIVATE);
		String message = messageEt.getText().toString();
		switch (v.getId()) {
		case R.id.first_send_btn:
			if (TextUtils.isEmpty(message)) {
				Toast.makeText(context, "请输入消息", Toast.LENGTH_SHORT).show();
			} else {
				Intent secondIntent = new Intent(context, SecondActivity.class);
				secondIntent.putExtra("message", message);
				startActivity(secondIntent);
				finish();
			}
			break;
		case R.id.first_save_btn:
			Editor editor = sp.edit();
			editor.putString("msg", message);
			if (editor.commit()) {
				ToastUtil.makeText(context, "保存成功");
			} else {
				ToastUtil.makeText(context, "保存失败");
			}
			break;
		case R.id.first_read_btn:
			ToastUtil.makeText(context, sp.getString("msg", ""));
			break;
		default:
			break;
		}
	}

	@Override
	protected void processLogic() {

	}
}
