package com.gservfocus.qwc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.bean.Comment;

public class MyCommendAdapter extends BaseAdapter {

	private ArrayList<Comment> commentsArrayList;
	Context c;
	private LayoutInflater mInflater;
	private ViewHolder vh = null;
	
	
	
	public ArrayList<Comment> getCommentsArrayList() {
		return commentsArrayList;
	}

	public void setCommentsArrayList(ArrayList<Comment> commentsArrayList) {
		this.commentsArrayList = commentsArrayList;
	}

	public MyCommendAdapter(ArrayList<Comment> commentsArrayList, Context c) {
		super();
		this.commentsArrayList = commentsArrayList;
		this.c = c;
	}

	public MyCommendAdapter() {
		super();
	}
	

	public MyCommendAdapter(Context c) {
		super();
		this.c = c;
	}

	@Override
	public int getCount() {
		if(commentsArrayList == null){
			return 0;
		}
		return commentsArrayList.size();
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
			mInflater = LayoutInflater.from(c);
			convertView = mInflater.inflate(R.layout.agricoalcommend_adapterlist,
					null);
			vh = new ViewHolder();
			vh.content = (TextView) convertView.findViewById(R.id.content);
			vh.username = (TextView) convertView.findViewById(R.id.username);
			vh.create_date = (TextView) convertView.findViewById(R.id.create_date);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.content.setText(commentsArrayList.get(position).getContent());
		vh.username.setText(commentsArrayList.get(position).getUserID());
		vh.create_date.setText(commentsArrayList.get(position).getC_Date());
		return convertView;
	}
	
	class ViewHolder{
		TextView content , username , create_date;
	}

}
