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
import android.widget.ListView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.DetailAgricoalActivity;
import com.gservfocus.qwc.activity.DetailSpecialtyActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.adapter.MyAllAgricoalAdapter;
import com.gservfocus.qwc.adapter.MyAllSpecialtyAdapter;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.Specialty;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class UserSpecialtyFragment extends BaseFragment {

	private ListView userspecialtylist;
	ArrayList<Specialty> mcall;
	Intent intent;
	View v;
	TextView ifNoNet;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = getActivity().getLayoutInflater().inflate(R.layout.userspecialty,
				null);
		ifNoNet = (TextView) v.findViewById(R.id.ifNoNet);
		userspecialtylist = (ListView) v.findViewById(R.id.userspecialtylist);
		// final MyAllAgricoalAdapter ma = new
		// MyAllAgricoalAdapter(getActivity());
		// useragricoallist.setAdapter(ma);
		userspecialtylist.setOnItemClickListener(this);

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
		this.doAsync("数据加载中，请稍候...", new Callable<ArrayList<Specialty>>() {

			@Override
			public ArrayList<Specialty> call() throws Exception {
				try {
					return new QwcApiImpl()
							.getUserSpecialtyList((Constants.mAccount
									.getMobile() != "" ? Constants.mAccount
									.getMobile() : Constants.mAccount
									.getUserId()));
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<Specialty>>() {

			@Override
			public void onCallback(ArrayList<Specialty> pCallbackValue) {
				if (pCallbackValue != null) {
					mcall = pCallbackValue;
					Log.i("rzh", pCallbackValue.get(0).getName());
					// ma.setMcallbackvalue(pCallbackValue);
					// ((BaseAdapter)(useragricoallist.getAdapter())).notifyDataSetChanged();
					userspecialtylist.setAdapter(new MyAllSpecialtyAdapter(
							getActivity(), pCallbackValue));
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);

		intent = new Intent(getActivity(), DetailSpecialtyActivity.class);
		intent.putExtra("id", mcall.get(arg2).getId());
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
