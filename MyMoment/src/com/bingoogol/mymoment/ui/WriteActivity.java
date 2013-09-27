package com.bingoogol.mymoment.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bingoogol.mymoment.R;
import com.bingoogol.mymoment.domain.Moment;
import com.bingoogol.mymoment.service.MomentService;
import com.bingoogol.mymoment.util.Constants;
import com.bingoogol.mymoment.util.DateUtil;
import com.bingoogol.mymoment.util.Logger;
import com.bingoogol.mymoment.util.StorageUtil;
import com.bingoogol.mymoment.util.ToastUtil;

public class WriteActivity extends GenericActivity {

	private Button backBtn = null;
	private ImageView addImgIv = null;
	private Button publishBtn = null;
	private Resources resources = null;
	private EditText contentEt = null;
	private String imgRealPath = null;
	private MomentService momentService = null;
	private boolean isAdd = true;
	private int id;
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_write);
	}

	@Override
	protected void findViewById() {
		backBtn = (Button) this.findViewById(R.id.write_back_btn);
		addImgIv = (ImageView) this.findViewById(R.id.write_add_img_iv);
		publishBtn = (Button) this.findViewById(R.id.write_publish_btn);
		contentEt = (EditText) this.findViewById(R.id.write_content_et);
	}

	@Override
	protected void setListener() {
		backBtn.setOnClickListener(this);
		addImgIv.setOnClickListener(this);
		publishBtn.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		momentService = new MomentService(context);
		resources = context.getResources();
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			isAdd = false;
			id = bundle.getInt("id");
			publishBtn.setText(R.string.update);
			Moment moment = momentService.getMoment(id);
			contentEt.setText(moment.getContent());
			imgRealPath = moment.getImgPath();
			addImgIv.setImageBitmap(StorageUtil.getBitmapFromLocal(imgRealPath));
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.write_back_btn:
			finish();
			overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
			break;
		case R.id.write_add_img_iv:
			addImg();
			break;
		case R.id.write_publish_btn:
			publish();
			break;
		default:
			break;
		}
	}

	private void addImg() {
		String[] items = new String[] { resources.getString(R.string.camera), resources.getString(R.string.gallery) };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.add_img);
		builder.setItems(items, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					getFromCamera();
					break;
				case 1:
					getFromGallery();
					break;
				}
			}
		});
		builder.create().show();
	}

	private void getFromCamera() {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		imgRealPath = StorageUtil.getImageDir() + File.separator + "IMG_" + timeStamp + ".png";
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imgRealPath)));
		startActivityForResult(intent, Constants.activity.GET_FROM_CAMERA);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	private void getFromGallery() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		// 设置特定环境下才能执行的参数，例如机顶盒，车载模式等
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		// 当同时有data和type时应该用intent.setDataAndType(data, type)
		intent.setType("image/*");
		startActivityForResult(intent, Constants.activity.GET_FROM_GALLERY);
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	private void publish() {
		String content = contentEt.getText().toString().trim();
		if (content.length() == 0 || imgRealPath == null) {
			ToastUtil.makeText(context, "必须选择一张照片和说一些话");
		} else {
			Moment moment = new Moment();
			moment.setContent(content);
			moment.setImgPath(imgRealPath);
			moment.setPublishTime(DateUtil.getPublishTime());
			if(isAdd) {
				if (momentService.addMoment(moment)) {
					// mResultCode默认值为RESULT_CANCELED;
					setResult(RESULT_OK);
					Logger.i(tag, "add success");
					finish();
					overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
				} else {
					ToastUtil.makeText(context, "发布失败");
				}
			} else {
				moment.setId(id);
				if (momentService.updateMoment(moment)) {
					// mResultCode默认值为RESULT_CANCELED;
					setResult(RESULT_OK);
					Logger.i(tag, "update success");
					finish();
					overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
				} else {
					ToastUtil.makeText(context, "修改失败");
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			if (requestCode == Constants.activity.GET_FROM_GALLERY) {
				imgRealPath = StorageUtil.getRealPathByUri(context, intent.getData());
			} else {
				imgRealPath = intent.getData().getPath();
			}
			addImgIv.setImageBitmap(StorageUtil.getBitmapFromLocal(imgRealPath));
		} else {
			// TODO
		}
	}
}
