package com.gservfocus.qwc.activity;

import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.bean.Weather;
import com.gservfocus.qwc.net.CityWeatherService;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class DefaultActivity extends BaseActivity {
	private ImageView bgImageView;
	private static final int DURATION = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default);
		bgImageView = (ImageView) findViewById(R.id.default_bg_iv);

		// 动画效果
		AnimatorSet set = new AnimatorSet();
		// 执行动画队列
		set.playTogether(ObjectAnimator.ofFloat(bgImageView, "alpha", 1f, 1f)
				.setDuration(DURATION),
				ObjectAnimator.ofFloat(bgImageView, "scaleX", 1f, 1.0f)
						.setDuration(DURATION),
				ObjectAnimator.ofFloat(bgImageView, "scaleY", 1f, 1.0f)
						.setDuration(DURATION));
		// 动画事件监听
		set.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DefaultActivity.this,
						MainActivity.class));
				DefaultActivity.this.finish();
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});
		set.start();
		getWeather();
		// 发送广播
		Intent intent = new Intent();
		intent.setAction(CityWeatherService.BROADCASTACTION);
		if (Constants.weathers != null) {
			intent.putExtra("isOK", "OK");
		} else {
			intent.putExtra("isOK", "NO");
		}
		sendBroadcast(intent);
		// QwcApplication.getInstance().clearAccount();
		Constants.mAccount = QwcApplication.getInstance().getAccount();
		// Constants.mTeacher = QwcApplication.getInstance().getTeacher();
		// Teacher.getTeacher(this);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		Constants.ScreenWidth = dm.widthPixels;
		Constants.ScreenHeight = dm.heightPixels;
		Constants.ScreenDensity = dm.density;
	}

	public void getWeather() {

		FinalHttp fh = new FinalHttp();
		fh.get(Constants.GET_WEATHER_URL, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}

			@Override
			public void onSuccess(String t) {

				try {
					JSONObject weatherObject = new JSONObject(t);
					if (weatherObject.getString("desc").equals("OK")
							&& weatherObject.getString("status").equals("1000")) {
						JSONObject weatherobj1 = weatherObject
								.getJSONObject("data");
						if (weatherobj1 != null && weatherobj1.length() > 0) {
							JSONArray weatherArray = weatherobj1
									.getJSONArray("forecast");
							JSONObject weathArr1 = null;
							Constants.weathers = new ArrayList<Weather>();
							Weather weather = null;
							for (int i = 0; i < weatherArray.length(); i++) {
								weather = new Weather();
								weathArr1 = weatherArray.getJSONObject(i);
								weather.setType(weathArr1.getString("type"));
								weather.setTemp(weathArr1.getString("high")
										.replace("高温 ", "")
										+ "/"
										+ weathArr1.getString("low").replace(
												"低温 ", ""));
								weather.setDate(weathArr1.getString("date")
										.replaceAll("\\D+", ""));
								weather.setWeek(weathArr1.getString("date")
										.substring(
												weathArr1.getString("date")
														.length() - 1,
												weathArr1.getString("date")
														.length()));
								Constants.weathers.add(weather);
							}
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
}
