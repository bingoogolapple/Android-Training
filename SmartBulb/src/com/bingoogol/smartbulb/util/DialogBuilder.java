package com.bingoogol.smartbulb.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bingoogol.smarthue.R;

public class DialogBuilder {

	public static Dialog createOperateDialog(Activity activity, int msgId, int leftId, int rightId, final OperateDialogCallBack operateDialogCallBack) {
		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		View view = View.inflate(activity, R.layout.operate_dialog, null);
		TextView msgTv = (TextView) view.findViewById(R.id.tv_msg_dialog);
		msgTv.setText(msgId);
		Button leftBtn = (Button) view.findViewById(R.id.btn_left_dialog);
		leftBtn.setText(activity.getResources().getString(leftId));
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operateDialogCallBack.onClickLeftBtn();
			}
		});
		Button rightBtn = (Button) view.findViewById(R.id.btn_right_dialog);
		rightBtn.setText(activity.getResources().getString(rightId));
		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				operateDialogCallBack.onClickRightBtn();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		return dialog;
	}

	public interface OperateDialogCallBack {
		public void onClickLeftBtn();

		public void onClickRightBtn();
	}
}
