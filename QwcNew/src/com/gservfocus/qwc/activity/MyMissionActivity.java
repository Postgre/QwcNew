package com.gservfocus.qwc.activity;

import java.util.ArrayList;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class MyMissionActivity extends BaseActivity {

	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> addscore = new ArrayList<String>();
	ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetitleonelistview);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((ImageView) findViewById(R.id.search)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.title)).setText("我的任务");
		listview = (ListView) findViewById(R.id.listview);
		/*View headerView = LayoutInflater.from(this).inflate(
				R.layout.header_mission, null);*/
		name.add("完成注册");
		name.add("填写问卷");
		name.add("每次扫一扫");
		name.add("每次分享");
		name.add("每次评论");
		addscore.add("+100积分");
		addscore.add("+20积分");
		addscore.add("+20积分");
		addscore.add("+10积分");
		addscore.add("+5积分");
		//listview.addHeaderView(headerView);
		listview.setAdapter(new Myadapter());
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		}
	}

	class Myadapter extends BaseAdapter {

		private LayoutInflater mInflater;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return name.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			mInflater = LayoutInflater.from(MyMissionActivity.this);
			convertView = mInflater.inflate(R.layout.mymission_adapterlist,
					null);
			((TextView)convertView.findViewById(R.id.missionName)).setText(name.get(position));
			((TextView)convertView.findViewById(R.id.addScore)).setText(addscore.get(position));
			/*if(position == 0){
				((TextView)convertView.findViewById(R.id.completeType)).setText("已完成");
				((TextView)convertView.findViewById(R.id.completeType)).setTextColor(Color.GREEN);
			}else{
				((TextView)convertView.findViewById(R.id.completeType)).setText("未完成");
			}*/
			return convertView;
		}
	}

}
