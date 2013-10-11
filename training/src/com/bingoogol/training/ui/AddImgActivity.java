package com.bingoogol.training.ui;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bingoogol.training.GenericActivity;
import com.bingoogol.training.R;
import com.bingoogol.training.ui.stub.SelectImgDialog;
import com.bingoogol.training.ui.stub.SetWifiDialog;
import com.bingoogol.training.util.Constants;

public class AddImgActivity extends GenericActivity {
	private ImageView imgIv;
	private Button addImgBtn;
	public String imgRealPath = "";
	private SelectImgDialog selectImgDialog;

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_add_img);
	}

	@Override
	protected void findViewById() {
		imgIv = (ImageView) this.findViewById(R.id.iv_img);
		addImgBtn = (Button) this.findViewById(R.id.btn_add_img);
		selectImgDialog = new SelectImgDialog(this);
	}

	@Override
	protected void setListener() {
		addImgBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		openInsertImgDialog();
//		openSetWifiDialog();
	}

	@Override
	protected void processLogic() {

	}
	
	private void openSetWifiDialog() {
		new SetWifiDialog(this).show();
	}

	private void openInsertImgDialog() {
		SelectImgDialog selectImgDialog = new SelectImgDialog(this);
		selectImgDialog.show();
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

}
