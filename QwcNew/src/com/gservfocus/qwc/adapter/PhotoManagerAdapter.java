package com.gservfocus.qwc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.ImagePagerActivity;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.PhotoDict;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoManagerAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private ArrayList<PhotoDict> photoDicts = null;
	private DisplayImageOptions options;
	private Boolean chooseMode = false;

	public PhotoManagerAdapter(Context context) {
		mContext = context;
		options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
		.showImageOnLoading(R.drawable.white)
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
		// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
		.build();
	}

	public void setPhotoList(ArrayList<PhotoDict> photoDicts) {
		this.photoDicts = photoDicts;
	}

	public ArrayList<PhotoDict> getPhotoList() {
		return this.photoDicts;
	}

	public void setChooseMode(Boolean mode) {
		chooseMode = mode;
	}

	public Boolean getChooseMode() {
		return chooseMode;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return photoDicts.get(groupPosition).getPhotos().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildViewHolder holder = null;
		if (convertView == null) {

			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_photo_manager_child,
					null);

			holder = new ChildViewHolder();
			holder.childItem1IV = (ImageView) convertView
					.findViewById(R.id.photoManagerChildItem1IV);
			holder.childItem2IV = (ImageView) convertView
					.findViewById(R.id.photoManagerChildItem2IV);
			holder.childItem3IV = (ImageView) convertView
					.findViewById(R.id.photoManagerChildItem3IV);
			holder.childItem4IV = (ImageView) convertView
					.findViewById(R.id.photoManagerChildItem4IV);

			holder.childItem1FGLayout = (FrameLayout) convertView
					.findViewById(R.id.photoManagerChildItemFG1Layout);
			holder.childItem2FGLayout = (FrameLayout) convertView
					.findViewById(R.id.photoManagerChildItemFG2Layout);
			holder.childItem3FGLayout = (FrameLayout) convertView
					.findViewById(R.id.photoManagerChildItemFG3Layout);
			holder.childItem4FGLayout = (FrameLayout) convertView
					.findViewById(R.id.photoManagerChildItemFG4Layout);

			convertView.setTag(holder);

		} else {

			holder = (ChildViewHolder) convertView.getTag();

		}
		ImageLoader.getInstance()
		.displayImage("file://"+photoDicts.get(groupPosition)
				.getPhotos().get(childPosition * 4).getPath(),
				holder.childItem1IV,
				options);
		// item1
		//fb.display(holder.childItem1IV, photoDicts.get(groupPosition)
		//		.getPhotos().get(childPosition * 4).getPath(),
		//		(Constants.ScreenWidth - Constants.dip2px(mContext, 3)) / 4,
		//		(Constants.ScreenWidth - Constants.dip2px(mContext, 3)) / 4);
		if (!chooseMode) {
			holder.childItem1FGLayout.getChildAt(1).setVisibility(
					View.INVISIBLE);
		}
		holder.childItem1FGLayout.setTag(groupPosition);
		holder.childItem1FGLayout.getChildAt(0).setTag(childPosition * 4);
		holder.childItem1FGLayout
				.setOnClickListener(new PhotoItemClickListener());

		// item2
		if (photoDicts.get(groupPosition).getPhotos().size() > childPosition * 4 + 1) {
			ImageLoader.getInstance()
			.displayImage("file://"+photoDicts.get(groupPosition)
							.getPhotos().get(childPosition * 4 + 1).getPath(),
					holder.childItem2IV,
					options);
			//fb.display(holder.childItem2IV, photoDicts.get(groupPosition)
			//		.getPhotos().get(childPosition * 4 + 1).getPath());
			holder.childItem2IV.setVisibility(View.VISIBLE);
			if (!chooseMode) {
				holder.childItem2FGLayout.getChildAt(1).setVisibility(
						View.INVISIBLE);
			}
		} else {
			holder.childItem2IV.setVisibility(View.INVISIBLE);
			holder.childItem2FGLayout.getChildAt(1).setVisibility(
					View.INVISIBLE);
		}
		holder.childItem2FGLayout.setTag(groupPosition);
		holder.childItem2FGLayout.getChildAt(0).setTag(childPosition * 4 + 1);
		holder.childItem2FGLayout
				.setOnClickListener(new PhotoItemClickListener());
		// item3
		if (photoDicts.get(groupPosition).getPhotos().size() > childPosition * 4 + 2) {
			//fb.display(holder.childItem3IV, photoDicts.get(groupPosition)
			//		.getPhotos().get(childPosition * 4 + 2).getPath());
			ImageLoader.getInstance()
			.displayImage("file://"+photoDicts.get(groupPosition)
							.getPhotos().get(childPosition * 4 + 2).getPath(),
					holder.childItem3IV,
					options);
			holder.childItem3IV.setVisibility(View.VISIBLE);
			if (!chooseMode) {
				holder.childItem3FGLayout.getChildAt(1).setVisibility(
						View.INVISIBLE);
			}
		} else {
			holder.childItem3IV.setVisibility(View.INVISIBLE);
			holder.childItem3FGLayout.getChildAt(1).setVisibility(
					View.INVISIBLE);
		}
		holder.childItem3FGLayout.setTag(groupPosition);
		holder.childItem3FGLayout.getChildAt(0).setTag(childPosition * 4 + 2);
		holder.childItem3FGLayout
				.setOnClickListener(new PhotoItemClickListener());
		// item4
		if (photoDicts.get(groupPosition).getPhotos().size() > childPosition * 4 + 3) {
			//fb.display(holder.childItem4IV, photoDicts.get(groupPosition)
			//		.getPhotos().get(childPosition * 4 + 3).getPath());
			ImageLoader.getInstance()
			.displayImage("file://"+photoDicts.get(groupPosition)
							.getPhotos().get(childPosition * 4 + 3).getPath(),
					holder.childItem4IV,
					options);
			holder.childItem4IV.setVisibility(View.VISIBLE);
			if (!chooseMode) {
				holder.childItem4FGLayout.getChildAt(1).setVisibility(
						View.INVISIBLE);
			}
		} else {
			holder.childItem4IV.setVisibility(View.INVISIBLE);
			holder.childItem4FGLayout.getChildAt(1).setVisibility(
					View.INVISIBLE);
		}
		holder.childItem4FGLayout.setTag(groupPosition);
		holder.childItem4FGLayout.getChildAt(0).setTag(childPosition * 4 + 3);
		holder.childItem4FGLayout
				.setOnClickListener(new PhotoItemClickListener());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if (photoDicts.size() > 0) {
			int row = photoDicts.get(groupPosition).getPhotos().size() / 4;
			if (photoDicts.get(groupPosition).getPhotos().size() % 4 > 0)
				row++;
			return row;
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return photoDicts.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return photoDicts.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ParentViewHolder holder = null;
		if (convertView == null) {

			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_photo_manager_group,
					null);

			holder = new ParentViewHolder();
			holder.titleTV = (TextView) convertView
					.findViewById(R.id.photoManagerParentListTitle);

			convertView.setTag(holder);
		} else {

			holder = (ParentViewHolder) convertView.getTag();

		}
		holder.titleTV.setText(photoDicts.get(groupPosition).getDate());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	class PhotoItemClickListener implements View.OnClickListener {

		@Override
		public void onClick(View childParent) {
			// TODO Auto-generated method stub
			int groupIndex = Integer.parseInt(childParent.getTag().toString());
			int childIndex = Integer.parseInt(((FrameLayout) childParent)
					.getChildAt(0).getTag().toString());
			if (chooseMode
					&& childIndex < photoDicts.get(groupIndex).getPhotos()
							.size()) {
				Photo photo = photoDicts.get(groupIndex).getPhotos()
						.get(childIndex);
				if (((FrameLayout) childParent).getChildAt(1).getVisibility() == View.VISIBLE) {
					((FrameLayout) childParent).getChildAt(1).setVisibility(
							View.INVISIBLE);
					photo.setIsChoose(false);
				} else {
					((FrameLayout) childParent).getChildAt(1).setVisibility(
							View.VISIBLE);
					photo.setIsChoose(true);
				}
			}else if(childIndex < photoDicts.get(groupIndex).getPhotos()
					.size()){	//点击事件处理
				Intent intent = new Intent(mContext, ImagePagerActivity.class);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, photoDicts.get(groupIndex).getPhotos());
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, childIndex);
				mContext.startActivity(intent);
			}
		}

	}

	class ParentViewHolder {
		TextView titleTV;
	}

	class ChildViewHolder {
		ImageView childItem1IV;
		ImageView childItem2IV;
		ImageView childItem3IV;
		ImageView childItem4IV;

		FrameLayout childItem1FGLayout;
		FrameLayout childItem2FGLayout;
		FrameLayout childItem3FGLayout;
		FrameLayout childItem4FGLayout;
	}
}
