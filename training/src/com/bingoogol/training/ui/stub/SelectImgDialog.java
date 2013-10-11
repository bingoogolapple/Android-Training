package com.bingoogol.training.ui.stub;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bingoogol.training.R;
import com.bingoogol.training.ui.AddImgActivity;
import com.bingoogol.training.util.Constants;
import com.bingoogol.training.util.StorageUtil;

public class SelectImgDialog extends Dialog implements OnClickListener {
	private Button galleryBtn;
	private Button cameraBtn;
	private AddImgActivity activity;

	public SelectImgDialog(AddImgActivity activity) {
		super(activity, R.style.DialogTheme);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_img);
		galleryBtn = (Button) this.findViewById(R.id.btn_add_img_gallery);
		cameraBtn = (Button) this.findViewById(R.id.btn_add_img_camera);
		galleryBtn.setOnClickListener(this);
		cameraBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_img_camera:
			getFromCamera();
			break;
		case R.id.btn_add_img_gallery:
			getFromGallery();
			break;

		default:
			break;
		}
		this.dismiss();
	}

	private void getFromCamera() {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		activity.imgRealPath = StorageUtil.getImageDir() + File.separator + "IMG_" + timeStamp + ".png";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(activity.imgRealPath)));
		activity.startActivityForResult(intent, Constants.activity.GET_FROM_CAMERA);
		activity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	private void getFromGallery() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		// 设置特定环境下才能执行的参数，例如机顶盒，车载模式等
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		// 当同时有data和type时应该用intent.setDataAndType(data, type)
		intent.setType("image/*");
		activity.startActivityForResult(intent, Constants.activity.GET_FROM_GALLERY);
		activity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
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
		activity.startActivityForResult(intent, Constants.activity.GET_FROM_CROP);
	}

}
