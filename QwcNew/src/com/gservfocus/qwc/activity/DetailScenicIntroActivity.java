package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.ShareModel;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.BaseMenuActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.activity.view.MenuItemView;
import com.gservfocus.qwc.activity.view.MyAnimations;
import com.gservfocus.qwc.adapter.DetailScenicAdapter;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.bean.Teacher;
import com.gservfocus.qwc.camera.CameraCallback;
import com.gservfocus.qwc.db.ExpandDatabaseImpl;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.other.parallax.ParallaxListView;
import com.gservfocus.qwc.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;

@SuppressLint("ResourceAsColor")
public class DetailScenicIntroActivity extends BaseMenuActivity {

	Intent intent;
	private DisplayImageOptions options;
	Scenic mcall;
	String id = null;
	String code = null;
	Context context;
	private PopupWindow mPopupWindow;
	// 景点图片
	private TextView titleTv, intro, fromwhere, t1, t2, ishot, allviewlike,
			allviewshare, sceIn;
	private ImageView moreintroIV;
	private ParallaxListView scenicDetailsLV;
	private MenuItemView myViewRT;
	private ImageView allviewintromore, kawayijump;
	UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	DetailScenicAdapter adapter = new DetailScenicAdapter(
			DetailScenicIntroActivity.this);
	ShareModel mShare = new ShareModel(this);
	TextView ifNoNet;
	int likeNum = 0;
	int shareNum = 0;
	AboutShareReceiver aboutShareReceiver = new AboutShareReceiver();
	public static String UPDATESHARENUM = "updatesharenum";

	/*
	 * DisplayMetrics metric = new DisplayMetrics(); int width =
	 * metric.widthPixels;
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailscenicintro);
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATESHARENUM);
		registerReceiver(aboutShareReceiver, filter);
		ifNoNet = (TextView) findViewById(R.id.ifNoNet);
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
		titleTv = (TextView) findViewById(R.id.title);
		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
				.showImageOnLoading(R.drawable.white).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		context = this;
		scenicDetailsLV = (ParallaxListView) findViewById(R.id.scenicDetailsLV);
		// list view header
		View headerView = LayoutInflater.from(this).inflate(
				R.layout.header_scenic_details, null);
		kawayijump = (ImageView) headerView.findViewById(R.id.kawayijump);
		kawayijump.setOnClickListener(this);
		moreintroIV = (ImageView) headerView.findViewById(R.id.moreintroimage);
		moreintroIV.setDrawingCacheEnabled(true);
		intro = (TextView) headerView.findViewById(R.id.text);
		fromwhere = (TextView) headerView.findViewById(R.id.parentscenicname);
		sceIn = (TextView) headerView.findViewById(R.id.sceIn);
		t1 = (TextView) headerView.findViewById(R.id.t1);
		t2 = (TextView) headerView.findViewById(R.id.t2);
		ishot = (TextView) headerView.findViewById(R.id.ishot);
		scenicDetailsLV.addHeaderView(headerView, null, false);
		scenicDetailsLV.setImageViewToParallax(moreintroIV);
		scenicDetailsLV.setAdapter(adapter);
		// footer
		View footerView = new View(this);
		footerView.setMinimumHeight(Constants.dip2px(this, 60));
		scenicDetailsLV.addFooterView(footerView);
		// info
		intent = getIntent();
		id = intent.getStringExtra("id");
		code = intent.getStringExtra("code");
		if (!Constants.isNetworkConnected(this)) {
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		} else {
			if (id != null)
				getData(id, true);
			else if (code != null)
				getData(code, false);
		}

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

	private void getData(final String info, final Boolean isID) {

		this.doAsync("获取数据中，请稍候...", new Callable<Scenic>() {

			@Override
			public Scenic call() throws Exception {
				try {
					if (isID) {
						return new QwcApiImpl().getScenicById(id);
					} else {
						return new QwcApiImpl().getScenicByQR(
								code,
								(Constants.mAccount.getMobile() != "" ? Constants.mAccount
										.getMobile() : Constants.mAccount
										.getUserId()));
					}
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Scenic>() {

			@Override
			public void onCallback(final Scenic pCallbackValue) {
				if (pCallbackValue != null) {
					titleTv.setText(pCallbackValue.getName());
					// fb.display(moreintroIV, pCallbackValue.getImageUrl());
					ImageLoader.getInstance().displayImage(
							pCallbackValue.getImageUrl(), moreintroIV, options);
					intro.setText(pCallbackValue.getAbstracts());
					allviewlike.setText(pCallbackValue.getLikeNum());
					likeNum = Integer.parseInt(pCallbackValue.getLikeNum()) + 1;
					allviewshare.setText(pCallbackValue.getShareNum());
					shareNum = Integer.parseInt(pCallbackValue.getShareNum()) + 1;
					if (pCallbackValue.getParentName() == null) {
						fromwhere.setVisibility(View.INVISIBLE);
						sceIn.setVisibility(View.INVISIBLE);
					} else {
						fromwhere.setTextColor(R.color.scenic_link_color);
						fromwhere.setText(pCallbackValue.getParentName());
						fromwhere.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								intent = new Intent(
										DetailScenicIntroActivity.this,
										DetailScenicIntroActivity.class);
								intent.putExtra("id",
										pCallbackValue.getParentID());
								startActivity(intent);
							}
						});
					}
					if (pCallbackValue.getParentName() == null) {
						t1.setVisibility(View.VISIBLE);
						t2.setVisibility(View.VISIBLE);
						kawayijump.setVisibility(View.VISIBLE);
						if (Teacher.getTeacher(DetailScenicIntroActivity.this)
								.isBoy()) {
							kawayijump
									.setImageResource(R.drawable.scenic_pointer_boy);
						} else {
							kawayijump
									.setImageResource(R.drawable.scenic_pointer_gril);
						}
					}
					/*
					 * if (pCallbackValue.getIsHot() != null &&
					 * pCallbackValue.getIsHot().equals("true")) {
					 * ishot.setVisibility(View.VISIBLE); }
					 */
					mcall = pCallbackValue;
					adapter.setMcallbackvalue(pCallbackValue);
					adapter.notifyDataSetChanged();
					if (!pCallbackValue.getQrScore().equals("0")) {
						showToast("您当前的积分为" + pCallbackValue.getQrScore());
						Constants.mAccount.setIntegral(pCallbackValue
								.getQrScore());
						intent = new Intent();
						intent.setAction(MoreFragment.PHOTO_UPDATE_ACTION);
						sendBroadcast(intent);
					}

				} else {
					showToast("请扫描景点二维码，谢谢");
					finish();
				}
			}
		}, new Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				// 加载错误时...
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);

		intent = new Intent(context, DetailScenicIntroActivity.class);
		intent.putExtra("id", mcall.getChild().get(arg2).getChildID());
		intent.putExtra("name", mcall.getChild().get(arg2).getChildName());
		intent.putExtra("url", mcall.getChild().get(arg2).getChildImageUrl());
		startActivity(intent);
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
			MyAnimations.startAnimations(DetailScenicIntroActivity.this,
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
							return new QwcApiImpl().likeScenicRecord(
									(Constants.mAccount.getMobile() != "" ? Constants.mAccount
											.getMobile() : Constants.mAccount
											.getUserId()), mcall.getId(), 0);
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
							allviewlike.setText(likeNum + "");
						} else if (pCallbackValue == 2) {
							showToast("此景点已收藏！");
						} else {
							showToast("收藏失败！");
						}
					}
				});
			}
			break;
		case R.id.allviewshare: {
			String url = Constants.SceUrl + mcall.getId();
			Bitmap bp = moreintroIV.getDrawingCache();
			UMImage ui = new UMImage(this, bp);
			Constants.isShareWhatID = mcall.getId();
			Constants.isShareWhat = "isShareScenic";
			mShare.SetController(mcall.getAbstracts(), mcall.getName(), url, ui);
			mPopupWindow = mShare.initPopuptWindow();
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		}
		case R.id.ifNoNet:
			if (Constants.isNetworkConnected(this)) {
				ifNoNet.setVisibility(View.INVISIBLE);
				if (id != null)
					getData(id, true);
				else if (code != null)
					getData(code, false);
			}
			break;
		case R.id.kawayijump:
			intent = new Intent(this, KawayiMapActivity.class);
			intent.putExtra("jump", mcall.getName());
			startActivity(intent);
			break;
		}

	}

	private CameraCallback cameraCallback = new CameraCallback(this);

	@Override
	public void onclick(int item) {
		// Toast.makeText(DetailScenicIntroActivity.this, "" + item, 1).show();
		switch (item) {
		case 0:
			intent = new Intent(DetailScenicIntroActivity.this,
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
				intent = new Intent(DetailScenicIntroActivity.this,
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
				intent = new Intent(DetailScenicIntroActivity.this,
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(aboutShareReceiver);
	}
}
