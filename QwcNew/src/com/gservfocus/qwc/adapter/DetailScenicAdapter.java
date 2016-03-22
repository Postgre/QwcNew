package com.gservfocus.qwc.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.DetailScenicIntroActivity;
import com.gservfocus.qwc.activity.view.RoundedImageView;
import com.gservfocus.qwc.bean.Scenic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailScenicAdapter extends BaseAdapter {

	//private FinalBitmap fb;
	private LayoutInflater mInflater;
	Context c;
	private ViewHolder vh = null;
	private Scenic mcallbackvalue;
	private int count;
	Intent intent;
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public DetailScenicAdapter(Context c) {
		super();
		this.c = c;
		//fb = FinalBitmap.create(c);
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
				options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
						//.showImageOnLoading(R.drawable.icon)
						.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
						.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
						//.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
						.build();
	}

	public Scenic getMcallbackvalue() {
		return mcallbackvalue;
	}

	public void setMcallbackvalue(Scenic mcallbackvalue) {
		this.mcallbackvalue = mcallbackvalue;
	}

	public DetailScenicAdapter(Context c, Scenic mcallbackvalue) {
		super();
		this.c = c;
		this.mcallbackvalue = mcallbackvalue;
		//fb = FinalBitmap.create(c);
	}

	public DetailScenicAdapter() {
		super();
		//fb = FinalBitmap.create(c);
	}

	@Override
	public int getCount() {
		if(mcallbackvalue != null){
			if(mcallbackvalue.getChild()!=null){
				count = mcallbackvalue.getChild().size();
				int row = count / 3;
				int mod = count % 3;
				if (mod > 0)
					row++;
				return row;
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Log.i("rzh","position->"+position);
		if (convertView == null) {
			mInflater = LayoutInflater.from(c);
			convertView = mInflater.inflate(R.layout.detailsonscenicintro_item,
					null);
			vh = new ViewHolder();
			vh.childrenLayout1 = (FrameLayout) convertView
					.findViewById(R.id.scenicDetailChildItem1);
			vh.childrenLayout2 = (FrameLayout) convertView
					.findViewById(R.id.scenicDetailChildItem2);
			vh.childrenLayout3 = (FrameLayout) convertView
					.findViewById(R.id.scenicDetailChildItem3);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
				// 设置第1项
				/*fb.display(vh.childrenLayout1.getChildAt(0),mcallbackvalue.getChild().get(position*3)
						 .getChildImageUrl());*/
				ImageLoader.getInstance().displayImage(mcallbackvalue.getChild().get(position*3)
						 .getChildImageUrl(), (RoundedImageView) vh.childrenLayout1.getChildAt(0), options);
				vh.childrenLayout1.setTag(position*3);
				((TextView)vh.childrenLayout1.getChildAt(1)).setText(mcallbackvalue.getChild().get(position*3).getChildName());
			// 设置第2项
			if ((position*3+1) < count) {
				/*fb.display(vh.childrenLayout2.getChildAt(0),mcallbackvalue.getChild().get(position*3+1)
						 .getChildImageUrl());*/
				ImageLoader.getInstance().displayImage(mcallbackvalue.getChild().get(position*3+1)
						 .getChildImageUrl(), (RoundedImageView) vh.childrenLayout2.getChildAt(0), options);
				vh.childrenLayout2.setTag(position*3+1);
				((TextView)vh.childrenLayout2.getChildAt(1)).setText(mcallbackvalue.getChild().get(position*3+1).getChildName());
				vh.childrenLayout2.setVisibility(View.VISIBLE);
			}else{
				
				vh.childrenLayout2.setVisibility(View.INVISIBLE);
			}
			// 设置第3项
			if ((position*3+2) < count) {
				/*fb.display(vh.childrenLayout3.getChildAt(0),mcallbackvalue.getChild().get(position*3+2)
						 .getChildImageUrl());*/
				ImageLoader.getInstance().displayImage(mcallbackvalue.getChild().get(position*3+2)
						 .getChildImageUrl(), (RoundedImageView) vh.childrenLayout3.getChildAt(0), options);
				vh.childrenLayout3.setTag(position*3+2);
				((TextView)vh.childrenLayout3.getChildAt(1)).setText(mcallbackvalue.getChild().get(position*3+2).getChildName());
				vh.childrenLayout3.setVisibility(View.VISIBLE);
			}else{
				
				vh.childrenLayout3.setVisibility(View.INVISIBLE);
			}
		vh.childrenLayout1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				intent = new Intent(c,DetailScenicIntroActivity.class);
				int i = Integer.parseInt(view.getTag().toString());
				intent.putExtra("id", mcallbackvalue.getChild().get(i).getChildID());
				c.startActivity(intent);
			}
		});
		vh.childrenLayout2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						intent = new Intent(c,DetailScenicIntroActivity.class);
						int i = Integer.parseInt(view.getTag().toString());
						intent.putExtra("id", mcallbackvalue.getChild().get(i).getChildID());
						Log.i("rzh",mcallbackvalue.getChild().get(i).getChildName());
						c.startActivity(intent);
					}
				});
		vh.childrenLayout3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				intent = new Intent(c,DetailScenicIntroActivity.class);
				int i = Integer.parseInt(view.getTag().toString());
				intent.putExtra("id", mcallbackvalue.getChild().get(i).getChildID());
				Log.i("rzh",mcallbackvalue.getChild().get(i).getChildName());
				c.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		FrameLayout childrenLayout1;
		FrameLayout childrenLayout2;
		FrameLayout childrenLayout3;
	}
}
