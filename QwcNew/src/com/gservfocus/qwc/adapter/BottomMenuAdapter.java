package com.gservfocus.qwc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;

public class BottomMenuAdapter extends BaseAdapter {

	private Integer[] menuImgs = { R.drawable.hot_scenic, R.drawable.farm_stay,
			R.drawable.recommend_route, R.drawable.speciality };
	private Context mContext;
	public BottomMenuAdapter(Context context){
		this.mContext = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuImgs.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return menuImgs[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView; 
        if (convertView == null) { 
            imageView = new ImageView(mContext); 
            imageView.setLayoutParams(new GridView.LayoutParams(Constants.ScreenWidth/2-Constants.dip2px(mContext, 25), LayoutParams.WRAP_CONTENT));//设置ImageView对象布局 
            imageView.setAdjustViewBounds(true);//设置边界对齐 
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);//设置刻度的类型 
        }  
        else { 
            imageView = (ImageView) convertView; 
        } 
        imageView.setImageResource(menuImgs[position]);//为ImageView设置图片资源 
        return imageView; 
	}

}
