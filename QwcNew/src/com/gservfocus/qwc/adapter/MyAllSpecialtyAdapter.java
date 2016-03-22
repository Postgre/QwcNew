package com.gservfocus.qwc.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.bean.Specialty;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MyAllSpecialtyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Specialty> mcallbackvalue;
	private Context context;
	private ViewHolder vh = null;
	private DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����

	public void setMcallbackvalue(ArrayList<Specialty> mcallbackvalue) {
		this.mcallbackvalue = mcallbackvalue;
	}

	public MyAllSpecialtyAdapter(Context context,
			ArrayList<Specialty> mcallbackvalue) {
		this(context);
		this.mcallbackvalue = mcallbackvalue;

	}

	public MyAllSpecialtyAdapter(Context context) {
		super();
		this.context = context;
		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
		options = new DisplayImageOptions.Builder() // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageOnLoading(R.drawable.white)
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				//.displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
				.build();
	}

	@Override
	public int getCount() {
		if (mcallbackvalue.size() == 0) {
			return 0;
		}
		return mcallbackvalue.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mcallbackvalue.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.specialty_adapterlist,
					null);
			vh = new ViewHolder();
			vh.specialtyimage = (ImageView) convertView
					.findViewById(R.id.specialtyimage);
			vh.specialtyname = (TextView) convertView
					.findViewById(R.id.specialtyname);
			vh.specialtyintro = (TextView) convertView
					.findViewById(R.id.specialtyintro);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (mcallbackvalue.get(position).getImageUrl() != null) {
			/*
			 * fb.display(vh.specialtyimage, mcallbackvalue.get(position)
			 * .getImageUrl());
			 */
			ImageLoader.getInstance().displayImage(
					mcallbackvalue.get(position).getImageUrl(),
					vh.specialtyimage, options);
		}
		vh.specialtyname.setText(mcallbackvalue.get(position).getName());
		vh.specialtyintro.setText(mcallbackvalue.get(position).getIntro());
		return convertView;
	}

	class ViewHolder {
		ImageView specialtyimage;
		TextView specialtyname;
		TextView specialtyintro;
	}


}
