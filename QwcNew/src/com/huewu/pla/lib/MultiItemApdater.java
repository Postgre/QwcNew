package com.huewu.pla.lib;

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

public class MultiItemApdater extends BaseAdapter {
	private Context mContext;
	private ArrayList<Scenic>  scenicListArrays;
	private DisplayImageOptions options;
	public MultiItemApdater(Context context){
		mContext = context;
		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
		// .showImageOnLoading(R.drawable.icon)
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
		// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
		.build();
	}
	public void setList(ArrayList<Scenic>  scenicListArrays){
		this.scenicListArrays = scenicListArrays;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return scenicListArrays.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return scenicListArrays.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {

			
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.hotsuggest_item, null);

			holder = new ViewHolder();

			holder.itemTextItem = (TextView) convertView
					.findViewById(R.id.hotviewtitle);
			holder.itemImageView = (ImageView) convertView
					.findViewById(R.id.hotviewimage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//fb.display(holder.itemImageView, scenicListArrays.get(position).getImageUrl());
		ImageLoader.getInstance()
		.displayImage(scenicListArrays.get(position).getImageUrl(),
				(ImageView)holder.itemImageView,
				options);
		holder.itemTextItem.setText(scenicListArrays.get(position).getName());
		return convertView;
	}
	class ViewHolder {
		ImageView itemImageView;
		TextView itemTextItem;
	}
}
