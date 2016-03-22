package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.Message;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.other.parallax.ParallaxListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailOnSellActionActivity extends BaseActivity {

	ParallaxListView onsellDetailLV;
	ImageView detailOnsellImage;
	private DisplayImageOptions options;
	TextView ifNoNet, title, onsellactionintro;
	MyAdapter adapter = new MyAdapter();
	View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailonsellaction);
		ifNoNet = ((TextView)findViewById(R.id.ifNoNet));
		title = ((TextView)findViewById(R.id.title));
		
		options = new DisplayImageOptions.Builder() // ����ͼƬ�����ڼ���ʾ��ͼƬ
		// .showImageOnLoading(R.drawable.icon)
		.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
		.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
		// .displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
		.build();
		
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		onsellDetailLV = (ParallaxListView) findViewById(R.id.onsellDetailLV);
		// list view header
		headerView = LayoutInflater.from(this).inflate(
				R.layout.header_onsell_details, null);
		
		onsellactionintro = ((TextView) headerView
				.findViewById(R.id.onsellactionintro));
		detailOnsellImage = (ImageView) headerView
				.findViewById(R.id.detailOnsellImage);
		onsellDetailLV.addHeaderView(headerView);
		onsellDetailLV.setImageViewToParallax(detailOnsellImage);
		onsellDetailLV.setAdapter(adapter);
		if(!Constants.isNetworkConnected(this)){
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		}else{
			getData();
		}
	}

	private void getData() {
		this.doAsync("������...", new Callable<Message>() {

			@Override
			public Message call() throws Exception {
				try {
					return new QwcApiImpl().getMessageById(getIntent()
							.getStringExtra("id"));
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Message>() {

			@Override
			public void onCallback(Message pCallbackValue) {
				if (pCallbackValue != null) {
					title.setText(pCallbackValue.getTitle());
					onsellactionintro.setText(pCallbackValue.getContent());
					//fb.display(detailOnsellImage, pCallbackValue.getImageUrl());
					ImageLoader.getInstance()
					.displayImage(pCallbackValue.getImageUrl(),
							detailOnsellImage,options);
					adapter.notifyDataSetChanged();
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
			if (Constants.isNetworkConnected(this)) {
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
