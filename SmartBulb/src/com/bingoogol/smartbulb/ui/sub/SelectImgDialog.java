package com.bingoogol.smartbulb.ui.sub;

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

import com.bingoogol.smartbulb.R;
import com.bingoogol.smartbulb.ui.EditTemplateActivity;
import com.bingoogol.smartbulb.util.Constants;
import com.bingoogol.smartbulb.util.StorageUtil;

/**
 * 选择插入图片的自定义对话框
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class SelectImgDialog extends Dialog implements OnClickListener {
	private Button galleryBtn;
	private Button cameraBtn;
	private EditTemplateActivity activity;

	public SelectImgDialog(EditTemplateActivity activity) {
		super(activity, R.style.DialogTheme);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_img);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
		galleryBtn = (Button) this.findViewById(R.id.btn_select_img_gallery);
		cameraBtn = (Button) this.findViewById(R.id.btn_select_img_camera);
		galleryBtn.setOnClickListener(this);
		cameraBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_select_img_camera:
			this.dismiss();
			getFromCamera();
			break;
		case R.id.btn_select_img_gallery:
			this.dismiss();
			getFromGallery();
			break;
		}
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

}
