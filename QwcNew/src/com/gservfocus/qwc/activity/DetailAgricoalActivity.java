package com.gservfocus.qwc.activity;

import java.util.HashMap;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseMenuActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.activity.view.MenuItemView;
import com.gservfocus.qwc.activity.view.MyAnimations;
import com.gservfocus.qwc.adapter.MyCommendAdapter;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.camera.CameraCallback;
import com.gservfocus.qwc.db.ExpandDatabaseImpl;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.other.parallax.ParallaxListView;
import com.gservfocus.qwc.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class DetailAgricoalActivity extends BaseMenuActivity {

	Intent intent;
	TextView commentTV;
	private DisplayImageOptions options;
	ParallaxListView agricoalCommendLV;
	ImageView hotelimage;
	RatingBar hotelrating;
	TextView score, hotelintro, hoteladdress, hoteltele, hoteltele1,
			hoteltele2;
	String id = null;
	private MenuItemView myViewRT;
	private ImageView allviewintromore;
	Agricola mcall;
	MyCommendAdapter adapter = new MyCommendAdapter(DetailAgricoalActivity.this);
	TextView ifNoNet;
	int likeNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailagricoal);
		ifNoNet = ((TextView) findViewById(R.id.ifNoNet));
		commentTV = (TextView) findViewById(R.id.comment);
		commentTV.setOnClickListener(this);
		myViewRT = (MenuItemView) findViewById(R.id.myViewRB);
		myViewRT.setPosition(MenuItemView.POSITION_RIGHT_BOTTOM);
		myViewRT.setRadius(100);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((TextView) findViewById(R.id.allviewlike)).setOnClickListener(this);
		allviewintromore = (ImageView) findViewById(R.id.allviewintromore);
		findViewById(R.id.relLayRB).setOnClickListener(this);
		setMenuItemView();

		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
				.showImageOnLoading(R.drawable.white).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		// fb = FinalBitmap.create(this);
		// /fb.configLoadingImage(R.drawable.white);//设置加载图片
		// list view header
		View headerView = LayoutInflater.from(this).inflate(
				R.layout.header_agricoal_details, null);
		agricoalCommendLV = (ParallaxListView) findViewById(R.id.agricoalCommendLV);
		hotelimage = (ImageView) headerView.findViewById(R.id.hotelimage);
		hotelrating = (RatingBar) headerView.findViewById(R.id.hotelrating);
		score = (TextView) headerView.findViewById(R.id.score);
		hotelintro = (TextView) headerView.findViewById(R.id.hotelintro);
		hoteladdress = (TextView) headerView.findViewById(R.id.hoteladdress);
		hoteltele = (TextView) headerView.findViewById(R.id.hoteltele);
		hoteltele1 = (TextView) headerView.findViewById(R.id.hoteltele1);
		hoteltele2 = (TextView) headerView.findViewById(R.id.hoteltele2);
		hoteltele.setOnClickListener(this);
		hoteltele1.setOnClickListener(this);
		hoteltele2.setOnClickListener(this);
		agricoalCommendLV.addHeaderView(headerView);
		// footer
		View footerView = new View(this);
		footerView.setMinimumHeight(Constants.dip2px(this, 60));
		agricoalCommendLV.addFooterView(footerView);
		agricoalCommendLV.setImageViewToParallax(hotelimage);
		agricoalCommendLV.setAdapter(adapter);
		intent = getIntent();
		id = intent.getStringExtra("id");
		if (!Constants.isNetworkConnected(this)) {
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		} else {
			getData();
		}
	}

	private void getData() {
		// TODO Auto-generated method stub
		this.doAsync("获取数据中，请稍候...", new Callable<Agricola>() {

			@Override
			public Agricola call() throws Exception {
				try {
					return new QwcApiImpl().getAgricolaById(id);
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Agricola>() {

			@Override
			public void onCallback(Agricola pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					((TextView) findViewById(R.id.title))
							.setText(pCallbackValue.getName());
					ImageLoader.getInstance().displayImage(
							pCallbackValue.getImage_url(), hotelimage, options);
					// fb.display(hotelimage, pCallbackValue.getImage_url());
					hotelrating.setProgress(Integer.parseInt(pCallbackValue
							.getLevel()));
					hotelintro.setText(pCallbackValue.getAbstracts());
					hoteladdress.setText(pCallbackValue.getAddress());
					hoteltele.setText(pCallbackValue.getPhone());
					((TextView) findViewById(R.id.allviewlike))
							.setText(pCallbackValue.getLikeNum());
					likeNum = Integer.parseInt(pCallbackValue.getLikeNum()) + 1;
					if (pCallbackValue.getCommentsArrayList() != null) {
						((TextView) findViewById(R.id.comment))
								.setText(pCallbackValue.getCommentsArrayList()
										.size() + "");
					} else {
						((TextView) findViewById(R.id.comment)).setText("0");
					}
					score.setText("服务 " + intent.getStringExtra("avgservice")
							+ "  口味 " + intent.getStringExtra("avgtaste")
							+ "  环境 " + intent.getStringExtra("avgcondition"));
					if (pCallbackValue.getPhone1().length() != 0) {
						hoteltele1.setText(pCallbackValue.getPhone1());
						hoteltele1.setVisibility(View.VISIBLE);
					} else {
						hoteltele1.setVisibility(View.GONE);
					}
					if (pCallbackValue.getPhone2().length() != 0) {
						hoteltele2.setText(pCallbackValue.getPhone2());
						hoteltele2.setVisibility(View.VISIBLE);
					} else {
						hoteltele1.setVisibility(View.GONE);
					}
					adapter.setCommentsArrayList(pCallbackValue
							.getCommentsArrayList());
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

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.comment: {
			if (Constants.mAccount == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else {
				Intent commentIntent = new Intent(this, CommentActivity.class);
				commentIntent.putExtra("agicolaID", id);
				startActivity(commentIntent);
			}
			break;
		}
		case R.id.relLayRB:
			MyAnimations.getRotateAnimation(allviewintromore, 0f, 270f, 300);
			MyAnimations.startAnimations(DetailAgricoalActivity.this, myViewRT,
					300);
			break;
		case R.id.allviewlike:

			if (Constants.mAccount == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else {
				this.doAsync("数据提交中，请稍后...", new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try {
							return new QwcApiImpl().likeAgrRecord(
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
							showToast("此农家乐已收藏！");
						} else {
							showToast("收藏失败！");
						}
					}
				});
			}
			break;
		case R.id.hoteltele:
			call(hoteltele);
			break;
		case R.id.hoteltele1:
			call(hoteltele1);
			break;
		case R.id.hoteltele2:
			call(hoteltele2);
			break;
		case R.id.ifNoNet:
			if (Constants.isNetworkConnected(this)) {
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}
	}

	public void call(TextView tv) {
		intent = new Intent();

		// 系统默认的action，用来打开默认的电话界面
		intent.setAction(Intent.ACTION_DIAL);

		// 需要拨打的号码

		intent.setData(Uri.parse("tel:" + tv.getText()));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("hotelname", mcall.getName());
		map.put("type", "click");
		MobclickAgent.onEvent(this, "nongjiale_tele", map);
		// Log.i("rzh","tel:"+tele);
		startActivity(intent);
	}

	private CameraCallback cameraCallback = new CameraCallback(this);

	@Override
	public void onclick(int item) {
		// Toast.makeText(DetailScenicIntroActivity.this, "" + item, 1).show();
		switch (item) {
		case 0:
			intent = new Intent(DetailAgricoalActivity.this, MainActivity.class);
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
				intent = new Intent(DetailAgricoalActivity.this,
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
				intent = new Intent(DetailAgricoalActivity.this,
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

}
