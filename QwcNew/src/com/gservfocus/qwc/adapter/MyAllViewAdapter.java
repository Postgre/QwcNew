package com.gservfocus.qwc.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.ShareModel;
import com.gservfocus.qwc.activity.fragment.AllViewFragment;
import com.gservfocus.qwc.bean.Scenic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.media.UMImage;

public class MyAllViewAdapter extends BaseAdapter {

	// private FinalBitmap fb;
	private LayoutInflater mInflater;
	private ArrayList<Scenic> mcallbackvalue;
	private Context context;
	private ViewHolder vh = null;
	AllViewFragment allViewFragment;
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private PopupWindow mPopupWindow;
	ShareModel mShare;
	int likeNum = 0;

	public AllViewFragment getAllViewFragment() {
		return allViewFragment;
	}

	public void setAllViewFragment(AllViewFragment allViewFragment) {
		this.allViewFragment = allViewFragment;
	}

	public ArrayList<Scenic> getMcallbackvalue() {
		return mcallbackvalue;
	}

	public void setMcallbackvalue(ArrayList<Scenic> mcallbackvalue) {
		this.mcallbackvalue = mcallbackvalue;
	}

	public MyAllViewAdapter(Activity context) {
		super();
		this.context = context;
		mShare = new ShareModel(context);
		// fb = FinalBitmap.create(context);
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
				// .showImageOnLoading(R.drawable.icon)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.allview_adapterlist, null);
			vh = new ViewHolder();
			vh.allviewimage = (ImageView) convertView
					.findViewById(R.id.allviewimage);
			vh.allviewimage.setDrawingCacheEnabled(true);
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
		vh.allviewimage.setTag(position);
		if (mcallbackvalue.get(position).getImageUrl() != null) {
			String url = mcallbackvalue.get(position).getImageUrl();
			// fb.display(vh.allviewimage, url);
			ImageLoader.getInstance().displayImage(url, vh.allviewimage,
					options);
		}
		vh.allviewname.setText(mcallbackvalue.get(position).getName());
		vh.allviewintro.setText(mcallbackvalue.get(position).getAbstracts()
				+ "...更多");
		vh.allviewshare.setText(mcallbackvalue.get(position).getShareNum());
		vh.allviewshare.setTag(vh.allviewimage);
		vh.allviewlike.setText(mcallbackvalue.get(position).getLikeNum());
		likeNum = Integer.parseInt(mcallbackvalue.get(position).getLikeNum()) + 1;
		vh.allviewlike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				allViewFragment.collect(mcallbackvalue.get(position).getId(),
						likeNum, position);
			}
		});
		vh.allviewshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Constants.isShareWhat = "isShareScenic";
				Constants.isShareWhatID = mcallbackvalue.get(position).getId();
				ImageView selectedImageView = (ImageView) view.getTag();
				int currentPosition = (Integer) selectedImageView.getTag();
				Bitmap bp = selectedImageView.getDrawingCache();
				UMImage ui = new UMImage(context, bp);
				String url = Constants.SceUrl
						+ mcallbackvalue.get(currentPosition).getId();
				Constants.ScienicShareNum = Integer.parseInt(mcallbackvalue.get(
						currentPosition).getShareNum())+1;
				Constants.ScenicName = mcallbackvalue.get(currentPosition).getName();
				mShare.SetController(mcallbackvalue.get(currentPosition)
						.getAbstracts(), mcallbackvalue.get(currentPosition)
						.getName(), url, ui);
				mPopupWindow = mShare.initPopuptWindow();
				mPopupWindow.showAtLocation(view, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
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
