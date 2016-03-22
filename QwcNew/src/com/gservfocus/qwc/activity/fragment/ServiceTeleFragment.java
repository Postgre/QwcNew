package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gservfocus.qwc.R;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceTeleFragment extends Fragment {

	private ExpandableListView servicetele;
	private LayoutInflater mInflater;
	Intent intent;
	// 子视图显示文字
	private String[][] phonenum = new String[][] {
			{ "021-59649211", "021-59649261", "021-59649003" },
			{ "021-59649300", "021-59649132" }, { "021-59649007" },
			{ "021-59649007" } };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.servicetelephone, null);
		servicetele = (ExpandableListView) v.findViewById(R.id.list);
		mInflater = LayoutInflater.from(getActivity());
		servicetele.setAdapter(new MyExpandableListAdapter());
		servicetele.expandGroup(0);
		servicetele.expandGroup(1);
		servicetele.expandGroup(2);
		servicetele.expandGroup(3);
		return v;
	}

	class MyExpandableListAdapter extends BaseExpandableListAdapter {

		// 设置组视图的显示文字
		private String[] phoneTypes = new String[] { "热线电话", "投诉电话", "救援电话 ",
				"医疗服务电话" };

		@Override
		public Object getChild(int arg0, int arg1) {
			return phonenum[arg0][arg1];
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			return arg1;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View son,
				ViewGroup arg4) {
			final String tele = phonenum[arg0][arg1];
			son = mInflater.inflate(R.layout.phonenum_adapterlist, null);
			((TextView) son.findViewById(R.id.telephonenum)).setText(tele);
			son.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intent = new Intent();

					// 系统默认的action，用来打开默认的电话界面
					intent.setAction(Intent.ACTION_DIAL);

					// 需要拨打的号码

					intent.setData(Uri.parse("tel:" + tele));
					MobclickAgent.onEvent(getActivity(), "service_hot_line");
					// Log.i("rzh","tel:"+tele);
					startActivity(intent);

				}
			});
			return son;
		}

		@Override
		public int getChildrenCount(int arg0) {

			return phonenum[arg0].length;
		}

		@Override
		public Object getGroup(int arg0) {

			return phoneTypes[arg0];
		}

		@Override
		public int getGroupCount() {
			return phoneTypes.length;
		}

		@Override
		public long getGroupId(int arg0) {
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View father,
				ViewGroup arg3) {

			father = mInflater.inflate(R.layout.phonetype_adapterlist, null);
			((TextView) father.findViewById(R.id.telephonetype))
					.setText(phoneTypes[arg0]);
			return father;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {

			return false;
		}

	}

}
