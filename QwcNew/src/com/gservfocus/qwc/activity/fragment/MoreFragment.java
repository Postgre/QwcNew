package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.MainActivity;
import com.gservfocus.qwc.activity.MoreFunActivity;
import com.gservfocus.qwc.activity.MyCollectActivity;
import com.gservfocus.qwc.activity.MyMissionActivity;
import com.gservfocus.qwc.activity.OnSellActionActivity;
import com.gservfocus.qwc.activity.PersonalCenterActivity;
import com.gservfocus.qwc.activity.PhotoManagerActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.activity.view.RoundedImageView;
import com.gservfocus.qwc.adapter.MoreFunAdapter;
import com.gservfocus.qwc.bean.MoreFun;
import com.gservfocus.qwc.other.parallax.ParallaxListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreFragment extends BaseFragment {

	public static final String PHOTO_UPDATE_ACTION = "photo_update_action";
	private PhotoUpdateBroadcastReceiver photoBroadcastReceiver = new PhotoUpdateBroadcastReceiver();
	ImageView image;
	ParallaxListView parallax;
	RoundedImageView circleIV;
	private DisplayImageOptions options;
	private PopupWindow mPopupWindow;
	View view;
	public static final String MY_PROFILE_ACTION = "my_profile_action";
	Boolean flag = true;
	MoreFunAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_more, container, false);
		findView(view);
		getActivity().registerReceiver(photoBroadcastReceiver,
				new IntentFilter(PHOTO_UPDATE_ACTION));
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(photoBroadcastReceiver);
	}

	private void findView(final View view) {

		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
				// .showImageOnLoading(R.drawable.icon)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();

		// fb = FinalBitmap.create(getActivity());
		parallax = (ParallaxListView) view.findViewById(R.id.moreListView);
		circleIV = (RoundedImageView) view
				.findViewById(R.id.personPhotoImageView);
		/*
		 * circleIV.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { initPopuptWindow();
		 * mPopupWindow.showAtLocation(view, Gravity.BOTTOM |
		 * Gravity.CENTER_HORIZONTAL, 0, 0); } });
		 */
		image = new ImageView(this.getActivity());
		image.setImageResource(R.drawable.me_bg);

		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources()
						.getDimension(R.dimen.more_bg_height));

		image.setLayoutParams(params);

		LinearLayout layout = new LinearLayout(this.getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(0, 0, 0, Constants.dip2px(this.getActivity(), 30f));
		layout.addView(image);
		parallax.addHeaderView(layout, null, false);
		parallax.setImageViewToParallax(image);
		parallax.setOnItemClickListener(this);
		adapter = new MoreFunAdapter(this.getActivity());
		adapter.setItems(initTestData());
		parallax.setAdapter(adapter);
		// 设置头像
		setPhotoInfo(view);
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.setItems(initTestData());
		adapter.notifyDataSetChanged();
	}
	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
				WindowManager.LayoutParams.MATCH_PARENT, Constants.dip2px(
						getActivity(), 210));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.selectfromimage:
			((MainActivity) getActivity()).cameraCallback.tuku2Pictrue();
			break;
		case R.id.makeimage:
			((MainActivity) getActivity()).isUploadPhoto = true;
			((MainActivity) getActivity()).cameraCallback.camera2Pic();
			break;
		case R.id.cancel:
			mPopupWindow.dismiss();
			break;
		}
	}

	private void setPhotoInfo(View view) {
		if (Constants.mAccount != null) {
			circleIV.setScaleType(ScaleType.CENTER_CROP);
			ImageLoader.getInstance().displayImage(
					Constants.mAccount.getImageUrl(), circleIV);
			// fb.display(circleIV, Constants.mAccount.getImageUrl());

			((TextView) view.findViewById(R.id.personNameTxt))
					.setText(Constants.mAccount.getAccount());
			((TextView) view.findViewById(R.id.level)).setText("等级："
					+ Constants.mAccount.getRankName());
			((TextView) view.findViewById(R.id.score)).setText("积分："
					+ Constants.mAccount.getIntegral());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		super.onItemClick(arg0, arg1, position, arg3);
		switch (position) {
		case 1:
			startActivity(new Intent(getActivity(), MyMissionActivity.class));
			break;
		case 2:
			startActivity(new Intent(getActivity(), MyCollectActivity.class));
			break;
		case 3:
			startActivity(new Intent(getActivity(), PhotoManagerActivity.class));
			break;
		case 4:
			startActivity(new Intent(getActivity(), OnSellActionActivity.class));
			break;
		case 5:
			if (Constants.mAccount.getUserId() == "") {
				startActivity(new Intent(getActivity(),
						PersonalCenterActivity.class));
			} else {
				startActivity(new Intent(getActivity(), MoreFunActivity.class));
			}
			break;
		case 6:
			startActivity(new Intent(getActivity(), MoreFunActivity.class));
			break;
		}

	}

	/**
	 * Test data
	 * **/
	private ArrayList<MoreFun> initTestData() {

		ArrayList<MoreFun> mores = new ArrayList<MoreFun>();
		MoreFun more = new MoreFun();
		more.setDrawableIconId(R.drawable.my_task);
		more.setName("我的任务");
		mores.add(more);

		more = new MoreFun();
		more.setDrawableIconId(R.drawable.my_collection);
		more.setName("我的收藏");
		mores.add(more);

		more = new MoreFun();
		more.setDrawableIconId(R.drawable.my_photo);
		more.setName("我的照片");
		mores.add(more);

		more = new MoreFun();
		more.setDrawableIconId(R.drawable.my_message);
		more.setName("我的消息");
		mores.add(more);
		if(Constants.mAccount != null){
			if(Constants.mAccount.getUserId() == ""){
				more = new MoreFun();
				more.setDrawableIconId(R.drawable.personl);
				more.setName("个人信息");
				mores.add(more);
			}			
		}		
		more = new MoreFun();
		more.setDrawableIconId(R.drawable.my_more);
		more.setName("更多");
		mores.add(more);

		return mores;

	}

	class PhotoUpdateBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			setPhotoInfo(getView());
		}

	}
}
