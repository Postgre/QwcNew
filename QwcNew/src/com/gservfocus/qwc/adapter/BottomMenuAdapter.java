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
            imageView.setLayoutParams(new GridView.LayoutParams(Constants.ScreenWidth/2-Constants.dip2px(mContext, 25), LayoutParams.WRAP_CONTENT));//����ImageView���󲼾� 
            imageView.setAdjustViewBounds(true);//���ñ߽���� 
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);//���ÿ̶ȵ����� 
        }  
        else { 
            imageView = (ImageView) convertView; 
        } 
        imageView.setImageResource(menuImgs[position]);//ΪImageView����ͼƬ��Դ 
        return imageView; 
	}

}
