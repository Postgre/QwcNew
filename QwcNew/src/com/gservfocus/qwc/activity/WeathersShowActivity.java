package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.AirQuality;
import com.gservfocus.qwc.bean.Weather;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class WeathersShowActivity extends BaseActivity {

	GridView nextfourday;
	int month;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weathershow);
		nextfourday = (GridView) findViewById(R.id.nextfourday);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		Intent intent = getIntent();
		Log.i("rzh", Constants.weathers.get(0).getType());
		Log.i("rzh", Constants.weathers.get(0).getTemp());
		Log.i("rzh", Constants.weathers.get(0).getDate());
		Log.i("rzh", Constants.weathers.get(0).getWeek());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		month = cal.get(Calendar.MONTH) + 1;
		((TextView) findViewById(R.id.weathertypetext))
				.setText(Constants.weathers.get(0).getType());
		WeathersShowActivity.this.getWindow().setBackgroundDrawableResource(
				selectImageByTypeforbg(Constants.weathers.get(0).getType()));
		((TextView) findViewById(R.id.weathertemptext))
				.setText(Constants.weathers.get(0).getTemp());

		((TextView) findViewById(R.id.date))
				.setText((month > 10 ? month : "0" + month)
						+ "‘¬"
						+ (Constants.weathers.get(0).getDate().length() > 1 ? Constants.weathers
								.get(0).getDate() : "0"
								+ Constants.weathers.get(0).getDate()) + "»’");
		((TextView) findViewById(R.id.week)).setText("–«∆⁄"
				+ Constants.weathers.get(0).getWeek());
		((ImageView) findViewById(R.id.weathertypeimage))
				.setImageResource(selectImageByType(Constants.weathers.get(0)
						.getType()));
		nextfourday.setAdapter(new MyAdapter(this, Constants.weathers));
		this.doAsync(null, new Callable<AirQuality>() {

			@Override
			public AirQuality call() throws Exception {
				try {
					return new QwcApiImpl().getAirqualityInfo();
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<AirQuality>() {

			@Override
			public void onCallback(AirQuality pCallbackValue) {
				if (pCallbackValue != null) {

					((TextView) findViewById(R.id.pm25)).setText("PM2.5£∫    "
							+ pCallbackValue.getPm());
					((TextView) findViewById(R.id.fuyanglizi))
							.setText("∏∫—ı¿Î◊”£∫    " + pCallbackValue.getOxygen());

				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);

		if (view.getId() == R.id.back) {
			finish();
		}
	}

	public int selectImageByType(String type) {
		if (type.indexOf("«Á") != -1) {
			return R.drawable.weather_sun;
		} else if (type.indexOf("‘∆") != -1) {
			return R.drawable.weather_cloudy;
		} else if (type.indexOf("”Í") != -1) {
			return R.drawable.weather_rain;
		} else if (type.indexOf("—©") != -1) {
			return R.drawable.weather_snow;
		} else if (type.indexOf("“ı") != -1) {
			return R.drawable.weather_na;
		} else {
			return R.drawable.weather_na;
		}
	}

	private int selectImageByTypeforbg(String type) {

		if (type.indexOf("«Á") != -1) {
			return R.drawable.sun_bg;
		} else if (type.indexOf("‘∆") != -1) {
			return R.drawable.cloudy_bg;
		} else if (type.indexOf("”Í") != -1) {
			return R.drawable.rain_bg;
		} else if (type.indexOf("—©") != -1) {
			return R.drawable.snow_bg;
		} else if (type.indexOf("“ı") != -1) {
			return R.drawable.cloudy_bg;
		} else {
			return R.drawable.sun_bg;
		}

	}

	class MyAdapter extends BaseAdapter {

		Context c;
		private LayoutInflater mInflater;
		private ArrayList<Weather> weathers;

		public MyAdapter(Context c, ArrayList<Weather> weathers) {
			super();
			this.c = c;
			this.weathers = weathers;
		}

		@Override
		public int getCount() {

			return weathers.size() - 3;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {

			mInflater = LayoutInflater.from(c);
			convertView = mInflater.inflate(R.layout.weathershow_adapterlist,
					null);
			((TextView) convertView.findViewById(R.id.weeknext)).setText("–«∆⁄"
					+ weathers.get(position + 1).getWeek());
			((TextView) convertView.findViewById(R.id.datenext)).setText("0"
					+ month + "‘¬" + "0" + weathers.get(position + 1).getDate()
					+ "»’");
			((TextView) convertView.findViewById(R.id.tempnext))
					.setText(weathers.get(position + 1).getTemp());
			((TextView) convertView.findViewById(R.id.typenext))
					.setText(weathers.get(position + 1).getType());
			((ImageView) convertView.findViewById(R.id.imagenext))
					.setImageResource(selectImageByType(weathers.get(
							position + 1).getType()));

			return convertView;
		}

	}

}
