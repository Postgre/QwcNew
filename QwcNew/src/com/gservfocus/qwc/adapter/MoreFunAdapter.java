package com.gservfocus.qwc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.bean.MoreFun;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;

public class MoreFunAdapter extends BaseAdapter {
	private ArrayList<MoreFun> items = null;
	private Context mContext;

	public MoreFunAdapter(Context context) {
		mContext = context;
	}

	public void setItems(ArrayList<MoreFun> items) {
		this.items = items;
	}

	public ArrayList<MoreFun> getItems() {
		return this.items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {

			
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.more_fun_item, null);

			holder = new ViewHolder();

			holder.moreTextItem = (TextView) convertView
					.findViewById(R.id.moreItemText);
			holder.moreImageView = (ImageView) convertView
					.findViewById(R.id.moreItemImageView);
			holder.moreLineView = (View) convertView
					.findViewById(R.id.moreItemLineView);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}
		if (items.get(position).getName() != null) {
			holder.moreTextItem.setText(items.get(position).getName());
			holder.moreImageView.setImageResource(items.get(position)
					.getDrawableIconId());

			Drawable arrowImgD = mContext.getResources().getDrawable(
					R.drawable.arrow_gray);
			arrowImgD.setBounds(0, 0, arrowImgD.getMinimumWidth(),
					arrowImgD.getMinimumHeight());
			holder.moreTextItem.setCompoundDrawables(null, null, arrowImgD,
					null);
			
			holder.moreTextItem.setVisibility(View.VISIBLE);
			holder.moreImageView.setVisibility(View.VISIBLE);
			holder.moreLineView.setVisibility(View.VISIBLE);
		}else{
			holder.moreTextItem.setVisibility(View.INVISIBLE);
			holder.moreImageView.setVisibility(View.GONE);
			holder.moreLineView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}

	class ViewHolder {
		ImageView moreImageView;
		TextView moreTextItem;
		View moreLineView;
	}
}
