/*package com.gservfocus.qwc.net.impl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.bean.Weather;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class GetWheather {
	ArrayList<Weather> weathers;
	Context context;
	View view;

	

	public GetWheather(Context context, View view) {
		super();
		this.context = context;
		this.view = view;
	}



	public ArrayList<Weather> getWheather(){
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
				Toast.makeText(context,"数据加载失败" , 2000).show();
				super.onFailure(t, errorNo, strMsg);
				//"高温 35℃".replace("高温 ", "");

			}

			@Override
			public void onSuccess(String t) {
				
					try {
						JSONObject weatherObject = new JSONObject(t);
						if (weatherObject.getString("desc").equals("OK") && weatherObject.getString("status").equals("1000")){
							JSONObject weatherobj1 = weatherObject.getJSONObject("data");
							if (weatherobj1 != null && weatherobj1.length() > 0) {
								JSONArray weatherArray = weatherobj1.getJSONArray("forecast");
								JSONObject weathArr1 = null;
								weathers = new ArrayList<Weather>();
								Weather weather = null;
								for(int i = 0;i<weatherArray.length();i++){
									weather = new Weather();
									weathArr1 = weatherArray.getJSONObject(i);
									weather.setTempnow(weatherobj1.getString("wendu")+"℃");
									weather.setType(weathArr1.getString("type"));
									weather.setTemp(weathArr1.getString("high").replace("高温 ", "")+"/"+weathArr1.getString("low").replace("低温 ", ""));
									weathers.add(weather);
								}
								
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		return weathers;
	}
}
*/