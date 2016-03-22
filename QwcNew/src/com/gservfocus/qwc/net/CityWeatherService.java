package com.gservfocus.qwc.net;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.bean.Weather;

public class CityWeatherService extends Service {

	public static final String BROADCASTACTION = "com.jone.broad";

	Timer timer;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// updateWeather();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				getWeather();
				// ·¢ËÍ¹ã²¥
				Intent intent = new Intent();
				intent.setAction(BROADCASTACTION);
				if (Constants.weathers != null) {
					intent.putExtra("isOK", "OK");
				}else{
					intent.putExtra("isOK", "NO");
				}
				sendBroadcast(intent);

			}
		}, 0, 20 * 1000);

		return super.onStartCommand(intent, flags, startId);
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
										.replace("¸ßÎÂ ", "")
										+ "/"
										+ weathArr1.getString("low").replace(
												"µÍÎÂ ", ""));
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
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

}
