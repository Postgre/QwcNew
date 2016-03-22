package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.gservfocus.qwc.bean.RecommendRoute;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SuggestLineActivity extends BaseActivity {

	private String SUGGESTLINE = "�Ƽ�·��";
	ListView suggestline;
	ArrayList<RecommendRoute> mcall;
	Intent intent;
	TextView ifNoNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetitleonelistview);
		((TextView) findViewById(R.id.title)).setText(SUGGESTLINE);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((ImageView) findViewById(R.id.search)).setVisibility(View.INVISIBLE);
		ifNoNet = ((TextView)findViewById(R.id.ifNoNet));
		suggestline = (ListView) findViewById(R.id.listview);
		suggestline.setOnItemClickListener(this);
		if(!Constants.isNetworkConnected(this)){
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		}else{
			getData();
		}
	}
	
	private void getData() {
		// TODO Auto-generated method stub
		this.doAsync("���ݼ����У����Ժ�...", new Callable<ArrayList<RecommendRoute>>() {

			@Override
			public ArrayList<RecommendRoute> call() throws Exception {
				try {
					return new QwcApiImpl().getRecommendList();
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<RecommendRoute>>() {

			@Override
			public void onCallback(ArrayList<RecommendRoute> pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					suggestline.setAdapter(new MyAdapter(
							SuggestLineActivity.this, pCallbackValue));
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.ifNoNet:
			if(Constants.isNetworkConnected(this)){
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2,
			long arg3) {
		super.onItemClick(adapterView, view, arg2, arg3);
		// showToast(mcall.get(arg2).getRouteID());
		intent = new Intent(SuggestLineActivity.this,
				DetailSuggestLineActivity.class);
		intent.putExtra("id", mcall.get(arg2).getRouteID());
		startActivity(intent);
	}

	class MyAdapter extends BaseAdapter {

		Context context;
		ArrayList<RecommendRoute> mcall;
		private ViewHolder vh = null;
		// private FinalBitmap fb;
		private LayoutInflater mInflater;
		private DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����

		public MyAdapter(Context context, ArrayList<RecommendRoute> mcall) {
			super();
			this.context = context;
			this.mcall = mcall;
			// fb = FinalBitmap.create(context);
			// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
			options = new DisplayImageOptions.Builder() // ����ͼƬ�����ڼ���ʾ��ͼƬ
					// .showImageOnLoading(R.drawable.icon)
					.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
					.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
					// .displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
					.build();
		}

		@Override
		public int getCount() {
			return (mcall == null ? 0 : mcall.size());
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
						R.layout.suggestline_adapterlist, null);
				vh = new ViewHolder();
				vh.slimage = (ImageView) convertView.findViewById(R.id.slimage);
				vh.sltext = (TextView) convertView.findViewById(R.id.sltext);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// fb.display(vh.slimage,mcall.get(position).getRouteIconUrl());
			ImageLoader.getInstance().displayImage(mcall.get(position).getRouteIconUrl(), vh.slimage, options);
			vh.sltext.setText(mcall.get(position).getRouteName());
			return convertView;
		}

		class ViewHolder {
			ImageView slimage;
			TextView sltext;
		}
	}
}
