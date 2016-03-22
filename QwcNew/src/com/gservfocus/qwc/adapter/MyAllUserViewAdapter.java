package com.gservfocus.qwc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.bean.Scenic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyAllUserViewAdapter extends BaseAdapter {

	//private FinalBitmap fb;
	private LayoutInflater mInflater;
	private ArrayList<Scenic> mcallbackvalue;
	private Context context;
	private ViewHolder vh = null;
	private DisplayImageOptions options; // DisplayImageOptions����������ͼƬ��ʾ����

	public ArrayList<Scenic> getMcallbackvalue() {
		return mcallbackvalue;
	}

	public void setMcallbackvalue(ArrayList<Scenic> mcallbackvalue) {
		this.mcallbackvalue = mcallbackvalue;
	}

	public MyAllUserViewAdapter(Context context) {
		super();
		this.context = context;
		//fb = FinalBitmap.create(context);
		// ʹ��DisplayImageOptions.Builder()����DisplayImageOptions
		options = new DisplayImageOptions.Builder() // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageOnLoading(R.drawable.white)
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisk(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				// .displayer(new RoundedBitmapDisplayer(20)) // ���ó�Բ��ͼƬ
				.build();
	}

	public MyAllUserViewAdapter(ArrayList<Scenic> mcallbackvalue,
			Context context) {
		super();
		this.mcallbackvalue = mcallbackvalue;
		this.context = context;
		//fb = FinalBitmap.create(context);
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
			convertView = mInflater.inflate(R.layout.allview_adapterlist, null);
			vh = new ViewHolder();
			vh.allviewimage = (ImageView) convertView
					.findViewById(R.id.allviewimage);
			vh.allviewname = (TextView) convertView
					.findViewById(R.id.allviewname);
			vh.allviewlike = (TextView) convertView
					.findViewById(R.id.allviewlike);
			vh.allviewshare = (TextView) convertView
					.findViewById(R.id.allviewshare);
			vh.allviewintro = (TextView) convertView
					.findViewById(R.id.allviewintro);
			vh.allviewintromore = (TextView) convertView
					.findViewById(R.id.allviewintromore);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (mcallbackvalue.get(position).getImageUrl() != null) {
			String url = mcallbackvalue.get(position).getImageUrl();
			// fb.display(vh.allviewimage, url);
			ImageLoader.getInstance().displayImage(url, vh.allviewimage,
					options);
		}
		vh.allviewname.setText(mcallbackvalue.get(position).getName());
		vh.allviewintro.setText(mcallbackvalue.get(position).getAbstracts() + "...����");
		vh.allviewshare.setVisibility(View.GONE);
		vh.allviewlike.setVisibility(View.GONE);
		vh.allviewintromore.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		ImageView allviewimage;
		TextView allviewname;
		TextView allviewlike;
		TextView allviewshare;
		TextView allviewintro;
		TextView allviewintromore;
	}

}
