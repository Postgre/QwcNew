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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.activity.util.FragmentBaseActivity;
import com.gservfocus.qwc.adapter.DetailSectionsPagerAdapter;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.camera.CameraCallback;
import com.gservfocus.qwc.db.ExpandDatabaseImpl;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.ApkUpdaterApiImpl;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.util.DateUtils;

public class MainActivity extends FragmentBaseActivity {

	// 进入我的信息界面广播
	private MyProfileBroadcastReceiver myProfileReceiver = new MyProfileBroadcastReceiver();
	public static final String MY_PROFILE_ACTION = "my_profile_action";
	public static final String MY_PROFILE_ACTION1 = "my_profile_action1";
	private ViewPager tabViewPager;
	public CameraCallback cameraCallback = new CameraCallback(this);
	public Boolean isUploadPhoto = false;
	// 操作参数
	String filePath = null;
	// tab menu
	public static final int TABBAR_MENU_COUNT = 4;
	// 切换菜单
	TextView[] tabbarMenu = new TextView[TABBAR_MENU_COUNT];
	Integer[] tabBars = { R.drawable.home, R.drawable.take_photo,
			R.drawable.qcode, R.drawable.more };
	Integer[] tabBarsSelect = { R.drawable.home_d, R.drawable.take_photo,
			R.drawable.qcode, R.drawable.more_d };
	// adapter
	DetailSectionsPagerAdapter mSectionsPagerAdapter;
	CheckUpdateBroadcastReceiver checkReceiver = new CheckUpdateBroadcastReceiver();
	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new ApkUpdaterApiImpl(this).setCheckUpdate();

		initFace();
		registerReceiver(myProfileReceiver, new IntentFilter(MY_PROFILE_ACTION));
		registerReceiver(myProfileReceiver,
				new IntentFilter(MY_PROFILE_ACTION1));
		// 注册更新版本事件广播
		registerReceiver(checkReceiver, new IntentFilter(
				CheckUpdateBroadcastReceiver.UPDATE_ACTON));
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((System.currentTimeMillis() - mExitTime) > 2000) {

				Object mHelperUtils;

				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

				mExitTime = System.currentTimeMillis();

			} else {

				finish();

			}

			return true;

		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myProfileReceiver);
		unregisterReceiver(checkReceiver);
	}

	/**
	 * init main face
	 * */
	private void initFace() {

		tabViewPager = (ViewPager) this.findViewById(R.id.tabViewPager);
		mSectionsPagerAdapter = new DetailSectionsPagerAdapter(
				this.getSupportFragmentManager());
		tabViewPager.setAdapter(mSectionsPagerAdapter);

		// bottom bar
		tabbarMenu[0] = (TextView) findViewById(R.id.bottomMenuHome);
		tabbarMenu[1] = (TextView) findViewById(R.id.bottomMenuPhoto);
		tabbarMenu[2] = (TextView) findViewById(R.id.bottomMenuQcode);
		tabbarMenu[3] = (TextView) findViewById(R.id.bottomMenuMore);
		for (int i = 0; i < 4; i++) {
			tabbarMenu[i].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.bottomMenuHome:
			setMenuSelected(0);
			tabViewPager.setCurrentItem(0, true);
			break;
		case R.id.bottomMenuPhoto:
			// setMenuSelected(1);
			cameraCallback.camera2Pic();
			break;
		case R.id.bottomMenuQcode:
			if (Constants.mAccount == null) {
				startActivity(new Intent(this, SaoYiSaoTeacherActivity.class));
			} else {
				startActivity(new Intent(this, CaptureActivity.class));
			}
			break;
		case R.id.bottomMenuMore:
			if (Constants.mAccount == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			} else {
				setMenuSelected(3);
				tabViewPager.setCurrentItem(1, true);
				break;
			}
		default:
			break;
		}
	}

	private void setMenuSelected(int index) {
		Drawable imgD = null;
		for (int i = 0; i < tabbarMenu.length; i++) {
			if (i == index) {
				imgD = getResources().getDrawable(this.tabBarsSelect[i]);
				imgD.setBounds(0, 0, imgD.getMinimumWidth(),
						imgD.getMinimumHeight());
				tabbarMenu[i].setCompoundDrawables(null, imgD, null, null);
				tabbarMenu[i].setTextAppearance(this,
						R.style.AverageWidthMacthTextStyle_d);
			} else {
				imgD = getResources().getDrawable(this.tabBars[i]);
				imgD.setBounds(0, 0, imgD.getMinimumWidth(),
						imgD.getMinimumHeight());
				tabbarMenu[i].setCompoundDrawables(null, imgD, null, null);
				tabbarMenu[i].setTextAppearance(this,
						R.style.AverageWidthMacthTextStyle);
			}
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
					return new QwcApiImpl().userImageUpload(
							Constants.mAccount.getMobile(), infoString);
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

	class MyProfileBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(MY_PROFILE_ACTION)) {
				setMenuSelected(3);
				tabViewPager.setCurrentItem(1, true);
			} else if (intent.getAction().equals(MY_PROFILE_ACTION1)) {
				setMenuSelected(0);
				tabViewPager.setCurrentItem(0, true);
			}

		}

	}

	public class CheckUpdateBroadcastReceiver extends BroadcastReceiver {
		// 更新UI action
		public static final String UPDATE_ACTON = "android.intent.action.update_version";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// showToast("已经是最新版本！");
		}

	}
}
