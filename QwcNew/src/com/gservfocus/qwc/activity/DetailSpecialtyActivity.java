package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.ShareModel;
import com.gservfocus.qwc.activity.util.BaseMenuActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.activity.view.MenuItemView;
import com.gservfocus.qwc.activity.view.MyAnimations;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.Specialty;
import com.gservfocus.qwc.camera.CameraCallback;
import com.gservfocus.qwc.db.ExpandDatabaseImpl;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.other.parallax.ParallaxListView;
import com.gservfocus.qwc.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.media.UMImage;

public class DetailSpecialtyActivity extends BaseMenuActivity {

	private Intent intent;
	private String id;
	Specialty mcall;
	private DisplayImageOptions options;
	ParallaxListView specialtyDetailsLV;
	ImageView detailspecialtyimage;
	TextView text, allviewlike, allviewshare;
	private MenuItemView myViewRT;
	private ImageView allviewintromore;
	private PopupWindow mPopupWindow;
	ShareModel mShare = new ShareModel(this);
	TextView ifNoNet;
	MyAdapter adapter = new MyAdapter();
	int likeNum = 0;
	int shareNum = 0;
	AboutShareReceiver aboutShareReceiver = new AboutShareReceiver();
	public static String UPDATESHARENUM_SPE = "updatesharenum_spe";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailspecialty);
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATESHARENUM_SPE);
		registerReceiver(aboutShareReceiver, filter);
		ifNoNet = ((TextView) findViewById(R.id.ifNoNet));
		ifNoNet.setOnClickListener(this);
		myViewRT = (MenuItemView) findViewById(R.id.myViewRB);
		myViewRT.setPosition(MenuItemView.POSITION_RIGHT_BOTTOM);
		myViewRT.setRadius(100);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		allviewlike = ((TextView) findViewById(R.id.allviewlike));
		allviewlike.setOnClickListener(this);
		allviewshare = ((TextView) findViewById(R.id.allviewshare));
		allviewshare.setOnClickListener(this);
		allviewintromore = (ImageView) findViewById(R.id.allviewintromore);
		findViewById(R.id.relLayRB).setOnClickListener(this);
		setMenuItemView();
		// list view header
		View headerView = LayoutInflater.from(this).inflate(
				R.layout.header_specialty_details, null);
		specialtyDetailsLV = (ParallaxListView) findViewById(R.id.specialtyDetailsLV);
		detailspecialtyimage = (ImageView) headerView
				.findViewById(R.id.detailspecialtyimage);
		detailspecialtyimage.setDrawingCacheEnabled(true);
		text = (TextView) headerView.findViewById(R.id.text);
		specialtyDetailsLV.addHeaderView(headerView, null, false);
		specialtyDetailsLV.setImageViewToParallax(detailspecialtyimage);
		specialtyDetailsLV.setAdapter(adapter);
		intent = getIntent();
		id = intent.getStringExtra("id");

		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
				.showImageOnLoading(R.drawable.white).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		if (!Constants.isNetworkConnected(this)) {
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.bringToFront();
			ifNoNet.setOnClickListener(this);
		} else {
			getData();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(aboutShareReceiver);
	}

	private void getData() {
		this.doAsync("获取数据中，请稍候...", new Callable<Specialty>() {

			@Override
			public Specialty call() throws Exception {
				try {
					return new QwcApiImpl().getSpecialtyById(id);
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Specialty>() {

			@Override
			public void onCallback(Specialty pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					((TextView) findViewById(R.id.title))
							.setText(pCallbackValue.getName());
					text.setText(pCallbackValue.getIntro());
					allviewlike.setText(pCallbackValue.getLikeNum());
					likeNum = Integer.parseInt(pCallbackValue.getLikeNum()) + 1;
					allviewshare.setText(pCallbackValue.getShareNum());
					shareNum = Integer.parseInt(pCallbackValue.getShareNum()) + 1;
					// fb.display(detailspecialtyimage,
					// pCallbackValue.getImageUrl());
					ImageLoader.getInstance().displayImage(
							pCallbackValue.getImageUrl(), detailspecialtyimage,
							options);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void setMenuItemView() {
		ImageButton imgBtn1 = new ImageButton(this);
		imgBtn1.setBackgroundResource(R.drawable.zhuye);
		ImageButton imgBtn2 = new ImageButton(this);
		imgBtn2.setBackgroundResource(R.drawable.xiangji);
		ImageButton imgBtn3 = new ImageButton(this);
		imgBtn3.setBackgroundResource(R.drawable.sys);
		ImageButton imgBtn4 = new ImageButton(this);
		imgBtn4.setBackgroundResource(R.drawable.wode);

		myViewRT.addView(imgBtn1);
		myViewRT.addView(imgBtn2);
		myViewRT.addView(imgBtn3);
		myViewRT.addView(imgBtn4);
	}

	/*
	 * class MyAsyncTask extends AsyncTask<Void, Void, Bitmap>{
	 * 
	 * Bitmap bm;
	 * 
	 * @Override protected Bitmap doInBackground(Void... arg0) {
	 * 
	 * detailspecialtyimage.buildDrawingCache(); while(true){ if((bm =
	 * detailspecialtyimage.getDrawingCache())!=null){ return bm; }else{
	 * continue; } } }
	 * 
	 * @Override protected void onPostExecute(Bitmap bm) {
	 * super.onPostExecute(bm); DisplayMetrics dm = new DisplayMetrics();
	 * getWindowManager().getDefaultDisplay().getMetrics(dm); int screenwidth =
	 * dm.widthPixels;//宽度 int screenheight = dm.heightPixels ;//高度 int
	 * width=bm.getWidth(); int height = (width/screenwidth)*screenheight;
	 * Log.i("rzh","width"+width+"height"+height);
	 * fb.display(detailspecialtyimage, mcall.getImageUrl() , width , height); }
	 * 
	 * }
	 */

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.relLayRB:
			MyAnimations.getRotateAnimation(allviewintromore, 0f, 270f, 300);
			MyAnimations.startAnimations(DetailSpecialtyActivity.this,
					myViewRT, 300);
			break;
		case R.id.allviewlike:

			if (Constants.mAccount == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else {
				this.doAsync("数据提交中，请稍后...", new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try {
							return new QwcApiImpl().likeSpeRecord(
									(Constants.mAccount.getMobile() != "" ? Constants.mAccount
											.getMobile() : Constants.mAccount
											.getUserId()), id, 0);
						} catch (WSError e) {
							e.printStackTrace();
						}
						return 1;
					}
				}, new Callback<Integer>() {

					@Override
					public void onCallback(Integer pCallbackValue) {
						if (pCallbackValue == 0) {
							showToast("收藏成功！");
							((TextView) findViewById(R.id.allviewlike))
									.setText(likeNum + "");
						} else if (pCallbackValue == 2) {
							showToast("此农特产已收藏！");
						} else {
							showToast("收藏失败！");
						}
					}
				});
			}
			break;
		case R.id.allviewshare:
			Constants.isShareWhat = "isShareSpe";
			Constants.isShareWhatID = mcall.getId();
			String url = Constants.SpeUrl + mcall.getId();
			Bitmap bp = detailspecialtyimage.getDrawingCache();
			UMImage ui = new UMImage(this, bp);
			mShare.SetController(mcall.getIntro(), mcall.getName(), url, ui);
			mPopupWindow = mShare.initPopuptWindow();
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.ifNoNet:
			if (Constants.isNetworkConnected(this)) {
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}
	}

	private CameraCallback cameraCallback = new CameraCallback(this);

	@Override
	public void onclick(int item) {
		switch (item) {
		case 0:
			intent = new Intent(DetailSpecialtyActivity.this,
					MainActivity.class);
			intent.putExtra("isProfile", false);
			startActivity(intent);
			break;
		case 1:
			cameraCallback.camera2Pic();
			break;
		case 2:
			if (Constants.mAccount == null) {
				startActivity(new Intent(this, SaoYiSaoTeacherActivity.class));
			} else {
				intent = new Intent(DetailSpecialtyActivity.this,
						CaptureActivity.class);
				startActivity(intent);
			}
			break;
		case 3:
			if (Constants.mAccount == null) {
				startActivity(new Intent(this, LoginActivity.class));
				break;
			} else {
				sendBroadcast(new Intent(MainActivity.MY_PROFILE_ACTION));
				intent = new Intent(DetailSpecialtyActivity.this,
						MainActivity.class);
				startActivity(intent);
				break;
			}
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK
				&& requestCode == CameraCallback.CAMERA_RESULT) {

			cameraCallback
					.setCallBackParameter(requestCode, resultCode, intent);
			String filePath = cameraCallback.getFilePath();
			cameraCallback.copySmallImage(filePath);
			// 处理不同需求
			savePhoto(filePath);
		}
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

	public class AboutShareReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getStringExtra("ss").equals("sce")) {
				allviewshare.setText(shareNum + "");
			}
		}
	}
}
