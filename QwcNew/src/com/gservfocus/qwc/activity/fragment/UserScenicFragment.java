package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.DetailScenicIntroActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.adapter.MyAllUserViewAdapter;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class UserScenicFragment extends BaseFragment {

	private ListView allviewlist;
	private View v;
	private Intent intent;
	ArrayList<Scenic> mcall = new ArrayList<Scenic>();
	TextView ifNoNet;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = getActivity().getLayoutInflater().inflate(R.layout.allview_hotview,
				null);
		ifNoNet = (TextView) v.findViewById(R.id.ifNoNet);
		allviewlist = (ListView) v.findViewById(R.id.allviewlist);
		/*
		 * final MyAllUserViewAdapter ma = new
		 * MyAllUserViewAdapter(getActivity()); allviewlist.setAdapter(ma);
		 */
		allviewlist.setOnItemClickListener(this);
		if (!Constants.isNetworkConnected(getActivity())) {
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		} else {
			getData();
		}
		return v;
	}

	private void getData() {
		// TODO Auto-generated method stub
		this.doAsync("数据加载中，请稍候...", new Callable<ArrayList<Scenic>>() {

			@Override
			public ArrayList<Scenic> call() throws Exception {
				try {
					return new QwcApiImpl()
							.getUserScenicList((Constants.mAccount.getMobile() != "" ? Constants.mAccount
									.getMobile() : Constants.mAccount
									.getUserId()));
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<Scenic>>() {

			@Override
			public void onCallback(ArrayList<Scenic> pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					/*
					 * ma.setMcallbackvalue(pCallbackValue);
					 * ((BaseAdapter)(allviewlist
					 * .getAdapter())).notifyDataSetChanged();
					 */
					allviewlist.setAdapter(new MyAllUserViewAdapter(
							pCallbackValue, getActivity()));
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);

		intent = new Intent(getActivity(), DetailScenicIntroActivity.class);
		intent.putExtra("id", mcall.get(arg2).getId());
		intent.putExtra("name", mcall.get(arg2).getName());
		intent.putExtra("url", mcall.get(arg2).getImageUrl());
		startActivity(intent);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.ifNoNet:
			if (Constants.isNetworkConnected(getActivity())) {
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}
	}

}
