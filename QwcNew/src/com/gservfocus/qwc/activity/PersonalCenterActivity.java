package com.gservfocus.qwc.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.activity.view.RoundedImageView;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.camera.CameraCallback;
import com.gservfocus.qwc.db.ExpandDatabaseImpl;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.util.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonalCenterActivity extends BaseActivity {

	private FrameLayout changeUserImage, changeUserName, changeUserPhone,
			changeUserPwd;
	private RoundedImageView UserImageView;
	private ImageView imagereturn1;
	private TextView UserName, UserPhone, UserPwd, title;
	private PopupWindow mPopupWindow;
	CameraCallback cameraCallback = new CameraCallback(this);
	public Boolean isUploadPhoto = false;
	private PhotoUpdateBroadcastReceiver photoBroadcastReceiver = new PhotoUpdateBroadcastReceiver();
	private MobileUpdateBroadcastReceiver mobileUpdateBroadcastReceiver = new MobileUpdateBroadcastReceiver();
	// 操作参数
	String filePath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		this.registerReceiver(photoBroadcastReceiver, new IntentFilter(
				MoreFragment.PHOTO_UPDATE_ACTION));
		this.registerReceiver(mobileUpdateBroadcastReceiver, new IntentFilter(
				BindMobileActivity.UPDATE_MOBILE));
		findView();
		intoView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(photoBroadcastReceiver);
		this.unregisterReceiver(mobileUpdateBroadcastReceiver);
	}

	private void findView() {
		// TODO Auto-generated method stub
		changeUserImage = (FrameLayout) findViewById(R.id.changeUserImage);
		changeUserName = (FrameLayout) findViewById(R.id.changeUserName);
		changeUserPhone = (FrameLayout) findViewById(R.id.changeUserPhone);
		changeUserPwd = (FrameLayout) findViewById(R.id.changeUserPwd);
		UserImageView = (RoundedImageView) findViewById(R.id.UserImageView);
		setPhotoInfo();
		imagereturn1 = (ImageView) findViewById(R.id.imagereturn1);
		title = (TextView) findViewById(R.id.title);
		UserName = (TextView) findViewById(R.id.UserName);
		UserPhone = (TextView) findViewById(R.id.UserPhone);
		UserPwd = (TextView) findViewById(R.id.UserPwd);
	}

	private void intoView() {
		// TODO Auto-generated method stub
		title.setText("个人信息");
		UserName.setText(Constants.mAccount.getAccount());
		UserPhone
				.setText(Constants.mAccount.getMobile() != "" ? Constants.mAccount
						.getMobile() : "未绑定");
		if (Constants.mAccount.getPassword() != "") {
			changeUserPwd.setVisibility(View.VISIBLE);
		} else {
			changeUserPwd.setVisibility(View.GONE);
		}
		changeUserImage.setOnClickListener(this);
		changeUserName.setOnClickListener(this);
		changeUserPhone.setOnClickListener(this);
		changeUserPwd.setOnClickListener(this);
		imagereturn1.setOnClickListener(this);
	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(
				R.layout.alert_dialog_changeuserimage, null);
		((Button) popupWindow.findViewById(R.id.selectfromimage))
				.setOnClickListener(this);
		((Button) popupWindow.findViewById(R.id.makeimage))
				.setOnClickListener(this);
		((Button) popupWindow.findViewById(R.id.cancel))
				.setOnClickListener(this);
		// 创建一个PopupWindow
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		mPopupWindow = new PopupWindow(popupWindow,
				WindowManager.LayoutParams.MATCH_PARENT, Constants.dip2px(this,
						210));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.changeUserImage:
			initPopuptWindow();
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.changeUserName:
			startActivity(new Intent(this, ChangeUsersNameActivity.class));
			break;
		case R.id.changeUserPhone:
			startActivity(new Intent(this, BindMobileActivity.class));
			break;
		case R.id.changeUserPwd:
			startActivity(new Intent(this, ChangePwdActivity.class));
			break;
		case R.id.selectfromimage:
			cameraCallback.tuku2Pictrue();
			break;
		case R.id.makeimage:
			isUploadPhoto = true;
			cameraCallback.camera2Pic();
			break;
		case R.id.cancel:
			mPopupWindow.dismiss();
			break;
		case R.id.imagereturn1:
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		byte[] b = null;
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {

			cameraCallback
					.setCallBackParameter(requestCode, resultCode, intent);
			filePath = cameraCallback.getFilePath();
			cameraCallback.copySmallImage(filePath);
			if (requestCode == CameraCallback.SELECT_PICTURE) { // 相册中选择头像上传
				b = getLocalImage2ByteArray(filePath);
				uploadImage(b);
				// showToast("相册中选择头像上传");
			} else if (requestCode == CameraCallback.CAMERA_RESULT
					&& isUploadPhoto) { // 头像拍照上传
				b = getLocalImage2ByteArray(filePath);
				uploadImage(b);
				// 处理不同需求
				// showToast("头像拍照上传");
			} else if (requestCode == CameraCallback.CAMERA_RESULT
					&& !isUploadPhoto) { // 拍照保存到sdcrad
				// 处理不同需求
				savePhoto(filePath);
			}
		}
		isUploadPhoto = false;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] getLocalImage2ByteArray(String filePath) {
		byte[] data = null;
		File file = null;
		FileInputStream fis = null;
		try {
			file = new File(filePath);
			if (file != null) {
				fis = new FileInputStream(file);
				data = new byte[fis.available()];
				fis.read(data);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return data;
	}
	public void uploadImage(byte[] b) {
		final String infoString = Base64.encodeToString(b, Base64.DEFAULT);
		this.doAsync("头像上传中...", new Callable<String>() {

			@Override
			public String call() throws Exception {
				try {
					return new QwcApiImpl().userImageUpload((Constants.mAccount
							.getMobile() != "" ? Constants.mAccount.getMobile()
							: Constants.mAccount.getUserId()), infoString);
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<String>() {

			@Override
			public void onCallback(String pCallbackValue) {
				if (pCallbackValue != null) {
					Constants.mAccount.setImageUrl(pCallbackValue);
					QwcApplication.getInstance()
							.saveAccount(Constants.mAccount);
					sendBroadcast(new Intent(MoreFragment.PHOTO_UPDATE_ACTION));
					showToast("上传成功！");
				} else {
					showToast("上传失败！");
				}

			}
		});

	}

	private void savePhoto(final String filePath) {
		this.doAsync("保存图片中...", new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				Photo photoT = new Photo();
				photoT.setDate(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
				photoT.setPath(filePath);

				return new ExpandDatabaseImpl(ExpandDatabaseImpl.DB_PATH)
						.savePhoto(photoT);
			}
		}, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean pCallbackValue) {
				// TODO Auto-generated method stub
				if (pCallbackValue) {

					showToast("保存图片成功");
				}
			}
		});
	}

	private void setPhotoInfo() {
		if (Constants.mAccount != null) {
			UserImageView.setScaleType(ScaleType.CENTER_CROP);
			ImageLoader.getInstance().displayImage(
					Constants.mAccount.getImageUrl(), UserImageView);
		}
	}

	class PhotoUpdateBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			setPhotoInfo();
		}

	}

	class MobileUpdateBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			UserPhone.setText(Constants.mAccount.getMobile());
			UserName.setText(Constants.mAccount.getAccount());
		}

	}
}
