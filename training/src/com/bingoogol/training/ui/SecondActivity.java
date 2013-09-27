package com.bingoogol.training.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bingoogol.training.GenericActivity;
import com.bingoogol.training.R;

public class SecondActivity extends GenericActivity {
	private TextView messageTv = null;
	
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_second);
	}

	@Override
	protected void findViewById() {
		messageTv = (TextView) this.findViewById(R.id.second_message_tv);
	}

	@Override
	protected void setListener() {
		
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void processLogic() {
		Intent intent = getIntent();
		String message = intent.getStringExtra("message");
		messageTv.setText(message);
	}
}
