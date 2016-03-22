package com.gservfocus.qwc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.bean.Agricola;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyAllAgricoalAdapter extends BaseAdapter {

	// private FinalBitmap fb;
	private LayoutInflater mInflater;
	private ArrayList<Agricola> mcallbackvalue;
	private Context context;
	private ViewHolder vh = null;
	private DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����

	public MyAllAgricoalAdapter(Context context,
			ArrayList<Agricola> mcallbackvalue) {
		this(context);
		this.mcallbackvalue = mcallbackvalue;
	}

	public MyAllAgricoalAdapter(Context context) {
		this.context = context;
		// fb = FinalBitmap.create(context);
		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
		options = new DisplayImageOptions.Builder() // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageOnLoading(R.drawable.white)
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				// .displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
				.build();
	}

	public ArrayList<Agricola> getMcallbackvalue() {
		return mcallbackvalue;
	}

	public void setMcallbackvalue(ArrayList<Agricola> mcallbackvalue) {
		this.mcallbackvalue = mcallbackvalue;
	}

	@Override
	public int getCount() {
		if (mcallbackvalue == null) {
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
			convertView = mInflater
					.inflate(R.layout.agricoal_adapterlist, null);
			vh = new ViewHolder();
			vh.hotelphoto = (ImageView) convertView
					.findViewById(R.id.hotelphoto);
			vh.hotelrating = (RatingBar) convertView
					.findViewById(R.id.hotelrating);
			vh.hotelname = (TextView) convertView.findViewById(R.id.hotelname);
			vh.hoteltaste = (TextView) convertView
					.findViewById(R.id.hoteltaste);
			vh.hotelevo = (TextView) convertView.findViewById(R.id.hotelevo);
			vh.hotelser = (TextView) convertView.findViewById(R.id.hotelser);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (mcallbackvalue.get(position).getImage_url() != null) {
			// fb.display(vh.hotelphoto,mcallbackvalue.get(position).getImage_url());
			ImageLoader.getInstance().displayImage(
					mcallbackvalue.get(position).getImage_url(), vh.hotelphoto,
					options);
		}
		vh.hotelrating.setProgress(Integer.parseInt(mcallbackvalue
				.get(position).getLevel()));
		vh.hotelname.setText(mcallbackvalue.get(position).getName());
		vh.hoteltaste.setText("��ζ " + mcallbackvalue.get(position).getTaste());
		vh.hotelevo
				.setText("���� " + mcallbackvalue.get(position).getCondition());
		vh.hotelser.setText("���� " + mcallbackvalue.get(position).getService());
		return convertView;
	}

	class ViewHolder {
		ImageView hotelphoto;
		RatingBar hotelrating;
		TextView hoteltaste;
		TextView hotelevo;
		TextView hotelser;
		TextView hotelname;
	}
}