package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.Message;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OnSellActionActivity extends BaseActivity {

	private ListView messagelist;
	private String onsellaction = "折扣活动";
	ArrayList<Message> mcall;
	Intent intent;
	TextView ifNoNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetitleonelistview);
		getAndSetPost();
		if (!Constants.isNetworkConnected(this)) {
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		} else {
			getData();
		}
	}

	private void getData() {
		// TODO Auto-generated method stub
		this.doAsync("加载中...", new Callable<ArrayList<Message>>() {

			@Override
			public ArrayList<Message> call() throws Exception {
				try {
					return new QwcApiImpl().getMessageList();
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<Message>>() {

			@Override
			public void onCallback(ArrayList<Message> pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					messagelist.setAdapter(new Myadapter(
							OnSellActionActivity.this, pCallbackValue));
				}
			}
		});
	}

	private void getAndSetPost() {
		ifNoNet = ((TextView) findViewById(R.id.ifNoNet));
		findViewById(R.id.imagereturn1).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		messagelist = (ListView) findViewById(R.id.listview);
		messagelist.setDivider(new ColorDrawable());
		messagelist.setDividerHeight(20);
		messagelist.setOnItemClickListener(this);
		((TextView) findViewById(R.id.title)).setText(onsellaction);
		((ImageView) findViewById(R.id.search)).setVisibility(View.INVISIBLE);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		intent = new Intent(this, DetailOnSellActionActivity.class);
		intent.putExtra("id", mcall.get(arg2).getId());
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.search:
			Log.i("rzh", "touch search");
			break;
		case R.id.ifNoNet:
			if (Constants.isNetworkConnected(this)) {
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}

	}

	class Myadapter extends BaseAdapter {

		// private FinalBitmap fb;
		private LayoutInflater mInflater;
		private ArrayList<Message> mcallbackvalue;
		private Context context;
		private ViewHolder vh = null;
		private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

		public Myadapter(Context context, ArrayList<Message> mcallbackvalue) {
			super();
			this.context = context;
			this.mcallbackvalue = mcallbackvalue;
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
					// .showImageOnLoading(R.drawable.icon)
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
					.build();
			// fb = FinalBitmap.create(context);
		}

		@Override
		public int getCount() {
			return mcallbackvalue.size();
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
			if (convertView == null) {
				mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(
						R.layout.onsellaction_adapterlist, null);
				vh = new ViewHolder();
				vh.actionphoto = (ImageView) convertView
						.findViewById(R.id.actionphoto);
				vh.actiontitle = (TextView) convertView
						.findViewById(R.id.actiontitle);
				vh.actiontime = (TextView) convertView
						.findViewById(R.id.actiontime);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			if (mcallbackvalue.get(position).getImageUrl() != null) {
				// fb.display(vh.actionphoto,mcallbackvalue.get(position).getImageUrl());
				ImageLoader.getInstance().displayImage(
						mcallbackvalue.get(position).getImageUrl(),
						vh.actionphoto, options);
			}
			vh.actiontitle.setText(mcallbackvalue.get(position).getTitle());
			vh.actiontime.setText("发布时间："+mcallbackvalue.get(position).getDate());

			return convertView;
		}

	}

	class ViewHolder {
		ImageView actionphoto;
		TextView actiontitle;
		TextView actiontime;
	}

}
