package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.AllSpecialtyActivity;
import com.gservfocus.qwc.activity.BaiduMapActivity;
import com.gservfocus.qwc.activity.HotViewActivity;
import com.gservfocus.qwc.activity.KawayiMapActivity;
import com.gservfocus.qwc.activity.SuggestLineActivity;
import com.gservfocus.qwc.activity.WeathersShowActivity;
import com.gservfocus.qwc.activity.WelcomeActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.adapter.BannerAdapter;
import com.gservfocus.qwc.adapter.BottomMenuAdapter;
import com.gservfocus.qwc.bean.Banner;
import com.gservfocus.qwc.bean.Teacher;
import com.gservfocus.qwc.net.CityWeatherService;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class HomeFragment extends BaseFragment {
	public static final String WEATHER_UPDATE_ACTION = "weather_update_action";
	private GridView bottomMenuGridView;
	private ViewPager bannerViewPager;
	private LinearLayout bannerViewPagerControl;

	private WeatherUpdateBroadcastReceiver weatherBroadcastReceiver = new WeatherUpdateBroadcastReceiver();

	Intent intent;
	Intent intentforService;
	View view;
	Bundle mBundle;
	TextView intro, navi, onsell, traffic;
	IntentFilter filter = new IntentFilter();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_home, container, false);
		intentforService = new Intent(getActivity(), CityWeatherService.class);
		getActivity().startService(intentforService);
		filter.addAction(CityWeatherService.BROADCASTACTION);
		getActivity().registerReceiver(weatherBroadcastReceiver, filter);
		getAndSetPost(view);
		findView(view);
		getActivity().registerReceiver(weatherBroadcastReceiver,
				new IntentFilter(WEATHER_UPDATE_ACTION));
		if (Teacher.getTeacher(getActivity()).isFirstLogin()) {
			startActivity(new Intent(getActivity(), WelcomeActivity.class));
		}
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(weatherBroadcastReceiver);
		getActivity().stopService(intentforService);
	}
	
	private void setWeatherInfo() {
		if (Constants.weathers != null && Constants.weathers.size() > 0) {
			setWeatherView();
		}
	}

	private void setWeatherView() {
		// TODO Auto-generated method stub
		((TextView) view.findViewById(R.id.navigationbar_weather_desc_tv))
				.setText(Constants.weathers.get(0).getType());
		((TextView) view.findViewById(R.id.navigationbar_weather_temp_tv))
				.setText(Constants.weathers
						.get(0)
						.getTemp()
						.substring(
								Constants.weathers.get(0).getTemp()
										.indexOf('/') + 1));
		if (Constants.weathers.get(0).getType().indexOf("晴") != -1) {
			((ImageView) view.findViewById(R.id.navigationbar_weather_icon_tv))
					.setImageResource(R.drawable.weather_sun);
		} else if (Constants.weathers.get(0).getType().indexOf("云") != -1) {
			((ImageView) view.findViewById(R.id.navigationbar_weather_icon_tv))
					.setImageResource(R.drawable.weather_cloudy);
		} else if (Constants.weathers.get(0).getType().indexOf("雨") != -1) {
			((ImageView) view.findViewById(R.id.navigationbar_weather_icon_tv))
					.setImageResource(R.drawable.weather_rain);
		} else if (Constants.weathers.get(0).getType().indexOf("雪") != -1) {
			((ImageView) view.findViewById(R.id.navigationbar_weather_icon_tv))
					.setImageResource(R.drawable.weather_snow);
		} else if (Constants.weathers.get(0).getType().indexOf("阴") != -1) {
			((ImageView) view.findViewById(R.id.navigationbar_weather_icon_tv))
					.setImageResource(R.drawable.weather_na);
		}
	}

	private void getAndSetPost(View view) {
		view.findViewById(R.id.navigationbar_weather_icon_tv)
				.setOnClickListener(this);
		intro = (TextView) view.findViewById(R.id.intro);
		intro.setOnClickListener(this);
		navi = (TextView) view.findViewById(R.id.navi);
		navi.setOnClickListener(this);
		onsell = (TextView) view.findViewById(R.id.onsell);
		onsell.setOnClickListener(this);
		traffic = (TextView) view.findViewById(R.id.traffic);
		traffic.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		navi.setEnabled(true);
		onsell.setEnabled(true);
		traffic.setEnabled(true);
		intro.setEnabled(true);
		/*
		 * final Animation scaleAnimation= new
		 * ScaleAnimation(0f,1f,0f,1f,Animation
		 * .RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		 * 
		 * scaleAnimation.setDuration(1000);
		 * bottomMenuGridView.setAnimation(scaleAnimation);
		 * scaleAnimation.startNow();
		 */
	}

	private void findView(View view) {

		bottomMenuGridView = (GridView) view
				.findViewById(R.id.bottomMenuGridView);
		bottomMenuGridView
				.setAdapter(new BottomMenuAdapter(this.getActivity()));
		bottomMenuGridView.setOnItemClickListener(this);

		// banner
		bannerViewPager = new ViewPager(this.getActivity());
		
		FrameLayout fl = (FrameLayout)view.findViewById(R.id.homeBannerLayout);
		fl.addView(bannerViewPager, 0, new FrameLayout.LayoutParams(Constants.ScreenWidth, Constants.ScreenWidth/2));
		
		bannerViewPagerControl = (LinearLayout) view
				.findViewById(R.id.bannerViewPagerControl);

		BannerAdapter bannerAdapter = new BannerAdapter(getActivity());
		ArrayList<Banner> banners = new ArrayList<Banner>();
		banners.add(new Banner(
				"http://qwcwebd.gservfocus.com/download/home1.jpg", "1"));
		banners.add(new Banner(
				"http://qwcwebd.gservfocus.com/download/home2.jpg", "2"));
		banners.add(new Banner(
				"http://qwcwebd.gservfocus.com/download/home3.jpg", "3"));
		banners.add(new Banner(
				"http://qwcwebd.gservfocus.com/download/home4.jpg", "4"));

		bannerAdapter.setBanners(banners);
		bannerViewPager.setAdapter(bannerAdapter);
		bannerViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						setBannerPointer(bannerViewPagerControl, arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

		addBannerPointer(bannerViewPagerControl, banners.size());
		setBannerPointer(bannerViewPagerControl, 0);
	}

	/**
	 * 添加指引点
	 * */
	private void addBannerPointer(LinearLayout group, int count) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 5, 0);
		ImageView pointIv = null;
		for (int i = 0; i < count; i++) {
			pointIv = new ImageView(getActivity());
			pointIv.setImageResource(R.drawable.dot);

			group.addView(pointIv, lp);
		}
	}

	/**
	 * 设置当前指引点
	 * */
	private void setBannerPointer(LinearLayout group, int index) {
		for (int i = 0; i < group.getChildCount(); i++) {
			if (i == index) {
				((ImageView) group.getChildAt(i))
						.setImageResource(R.drawable.dot_l);
				AnimatorSet set = new AnimatorSet();
				set.playTogether(
						ObjectAnimator.ofFloat(group.getChildAt(i), "scaleX",
								0.8f, 1.2f, 1.0f).setDuration(500),
						ObjectAnimator.ofFloat(group.getChildAt(i), "scaleY",
								0.8f, 1.2f, 1.0f).setDuration(500));
				set.start();
			} else
				((ImageView) group.getChildAt(i))
						.setImageResource(R.drawable.dot);

		}
	}

	@Override
	public void onClick(View v) {

		Intent intent;

		switch (v.getId()) {
		case (R.id.intro):
			navi.setEnabled(false);
			onsell.setEnabled(false);
			traffic.setEnabled(false);
			intent = new Intent("viewintroactivity");
			startActivity(intent);
			break;
		case (R.id.navi):
			intro.setEnabled(false);
			onsell.setEnabled(false);
			traffic.setEnabled(false);
			intent = new Intent(getActivity(), KawayiMapActivity.class);
			startActivity(intent);
			break;
		case (R.id.onsell):
			intro.setEnabled(false);
			navi.setEnabled(false);
			traffic.setEnabled(false);
			intent = new Intent("onsellactionactivity");
			startActivity(intent);
			break;
		case (R.id.traffic):
			navi.setEnabled(false);
			intro.setEnabled(false);
			onsell.setEnabled(false);
			intent = new Intent(getActivity(), BaiduMapActivity.class);
			startActivity(intent);
			break;
		case (R.id.navigationbar_weather_icon_tv): {
			if (Constants.weathers == null) {
				showToast("亲，您的网络不给力啊~");
			} else {
				intent = new Intent(getActivity(), WeathersShowActivity.class);
				startActivity(intent);
			}
			break;
		}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int post, long arg3) {

		switch (post) {
		case 0:
			startActivity(new Intent(getActivity(), HotViewActivity.class));
			break;
		case 1:
			startActivity(new Intent("agricoal"));
			break;
		case 2:
			startActivity(new Intent(getActivity(), SuggestLineActivity.class));
			break;
		case 3:
			startActivity(new Intent(getActivity(), AllSpecialtyActivity.class));
			break;
		}

	}

	class WeatherUpdateBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getStringExtra("isOK").equals("OK")) {
				setWeatherInfo();
			}
		}
	}
}
