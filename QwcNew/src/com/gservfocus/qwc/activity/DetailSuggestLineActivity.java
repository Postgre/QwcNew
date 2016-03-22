package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.RecommendRoute;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.gservfocus.qwc.other.parallax.ParallaxListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailSuggestLineActivity extends BaseActivity {

	ImageView image;
	ParallaxListView parallax;
	MyAdapter adapter;
	private DisplayImageOptions options;
	Intent intent;
	RecommendRoute mcall;
	TextView ifNoNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailsuggestline);
		findView();
	}

	private void findView() {
		ifNoNet = ((TextView) findViewById(R.id.ifNoNet));
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
				.showImageOnLoading(R.drawable.white).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		parallax = (ParallaxListView) findViewById(R.id.recommendRouteListView);
		adapter = new MyAdapter(this);
		image = new ImageView(this);
		image.setImageResource(R.drawable.me_bg);

		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources()
						.getDimension(R.dimen.more_bg_height));

		image.setLayoutParams(params);

		LinearLayout layout = new LinearLayout(this);
		layout.setPadding(0, 0, 0, Constants.dip2px(this, 10f));
		layout.addView(image);

		parallax.addHeaderView(layout, null, false);
		parallax.setImageViewToParallax(image);
		parallax.setOnItemClickListener(this);
		parallax.setAdapter(adapter);
		if (!Constants.isNetworkConnected(this)) {
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		} else {
			getData();
		}
	}

	private void getData() {
		// TODO Auto-generated method stub
		this.doAsync("数据加载中，请稍候...", new Callable<RecommendRoute>() {

			@Override
			public RecommendRoute call() throws Exception {
				try {
					return new QwcApiImpl().getRecommend(getIntent()
							.getStringExtra("id"));
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<RecommendRoute>() {

			@Override
			public void onCallback(RecommendRoute pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					((TextView) findViewById(R.id.title))
							.setText(pCallbackValue.getRouteName());
					//fb.display(image, pCallbackValue.getRouteIconUrl());
					ImageLoader.getInstance().displayImage(
							pCallbackValue.getRouteIconUrl(), image, options);
					adapter.setRecommendRoute(pCallbackValue);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		super.onItemClick(adapterView, view, arg2, arg3);
		intent = new Intent(DetailSuggestLineActivity.this,
				DetailScenicIntroActivity.class);
		intent.putExtra("id", mcall.getPaths().get(arg2 - 1).getScenic()
				.getId());
		startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
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

		RecommendRoute mcall;
		Context context;
		// FinalBitmap fb;
		private LayoutInflater mInflater;
		private ViewHolder vh = null;
		private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

		public MyAdapter(Context context) {
			this.context = context;
			// fb = FinalBitmap.create(context);
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
					// .showImageOnLoading(R.drawable.icon)
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
					.build();
		}

		public void setRecommendRoute(RecommendRoute mcall) {
			this.mcall = mcall;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (mcall == null ? 0 : mcall.getPaths().size());
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mcall.getPaths().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				mInflater = LayoutInflater.from(context);
				convertView = mInflater
						.inflate(R.layout.suggestline_item, null);
				vh = new ViewHolder();

				vh.suggestscenicimage = (ImageView) convertView
						.findViewById(R.id.suggestscenicimage);
				vh.suggestscenicintro = (TextView) convertView
						.findViewById(R.id.suggestscenicintro);
				vh.suggestscenicname = (TextView) convertView
						.findViewById(R.id.suggestscenicname);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			/*
			 * fb.display(vh.suggestscenicimage, mcall.getPaths().get(position)
			 * .getScenic().getImageUrl());
			 */

			ImageLoader.getInstance().displayImage(
					mcall.getPaths().get(position).getScenic().getImageUrl(),
					vh.suggestscenicimage, options);
			vh.suggestscenicintro.setText(mcall.getPaths().get(position)
					.getScenic().getAbstracts());
			vh.suggestscenicname.setText(mcall.getPaths().get(position)
					.getScenic().getName());
			return convertView;
		}

		class ViewHolder {
			ImageView suggestscenicimage;
			TextView suggestscenicintro;
			TextView suggestscenicname;
		}
	}
}
