package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.AllAgricoalActivity;
import com.gservfocus.qwc.activity.DetailAgricoalActivity;
import com.gservfocus.qwc.activity.DetailScenicIntroActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.adapter.MyAllAgricoalAdapter;
import com.gservfocus.qwc.adapter.MyAllViewAdapter;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class UserAgricoalFragment extends BaseFragment {

	private ListView useragricoallist;
	ArrayList<Agricola> mcall;
	Intent intent;
	View v;
	TextView ifNoNet;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = getActivity().getLayoutInflater().inflate(R.layout.useragricoal,
				null);
		ifNoNet = (TextView) v.findViewById(R.id.ifNoNet);
		useragricoallist = (ListView) v.findViewById(R.id.useragricoallist);
		// final MyAllAgricoalAdapter ma = new
		// MyAllAgricoalAdapter(getActivity());
		// useragricoallist.setAdapter(ma);
		useragricoallist.setOnItemClickListener(this);
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
		this.doAsync("数据加载中，请稍候...", new Callable<ArrayList<Agricola>>() {

			@Override
			public ArrayList<Agricola> call() throws Exception {
				try {
					return new QwcApiImpl()
							.getUserAgricolaList((Constants.mAccount
									.getMobile() != "" ? Constants.mAccount
									.getMobile() : Constants.mAccount
									.getUserId()));
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<Agricola>>() {

			@Override
			public void onCallback(ArrayList<Agricola> pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					Log.i("rzh", pCallbackValue.get(0).getName());
					// ma.setMcallbackvalue(pCallbackValue);
					// ((BaseAdapter)(useragricoallist.getAdapter())).notifyDataSetChanged();
					useragricoallist.setAdapter(new MyAllAgricoalAdapter(
							getActivity(), pCallbackValue));
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);

		intent = new Intent(getActivity(), DetailAgricoalActivity.class);
		intent.putExtra("id", mcall.get(arg2).getId());
		intent.putExtra("avgservice", mcall.get(arg2).getService());
		intent.putExtra("avgtaste", mcall.get(arg2).getTaste());
		intent.putExtra("avgcondition", mcall.get(arg2).getCondition());
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
