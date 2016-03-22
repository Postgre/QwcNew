package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.SearchModel;
import com.gservfocus.qwc.activity.DetailScenicIntroActivity;
import com.gservfocus.qwc.activity.HotViewActivity;
import com.gservfocus.qwc.activity.LoginActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.adapter.MyAllViewAdapter;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class AllViewFragment extends BaseFragment {

	private ListView allviewlist;
	private View v;
	private Intent intent;
	ArrayList<Scenic> mcall = new ArrayList<Scenic>();
	EditText edsearch;
	ArrayList<Scenic> tempArrays = null;
	MyAllViewAdapter adapter = null;
	TextView ifNoNet;
	AboutShareReceiver aboutShareReceiver = new AboutShareReceiver();

	public EditText getEdsearch() {
		return edsearch;
	}

	public void setEdsearch(final EditText edsearch) {
		this.edsearch = edsearch;
		edsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				tempArrays = SearchModel.getSearchList(mcall, new String[] {
						"name", "abstracts" }, edsearch.getText().toString());
				adapter.setMcallbackvalue(tempArrays);
				adapter.notifyDataSetChanged();

			}
		});
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = getActivity().getLayoutInflater().inflate(R.layout.allview_hotview,
				null);
		IntentFilter filter = new IntentFilter();
		filter.addAction(DetailScenicIntroActivity.UPDATESHARENUM);
		getActivity().registerReceiver(aboutShareReceiver, filter);
		ifNoNet = (TextView) v.findViewById(R.id.ifNoNet);
		adapter = new MyAllViewAdapter(getActivity());
		allviewlist = (ListView) v.findViewById(R.id.allviewlist);
		adapter.setMcallbackvalue(new ArrayList<Scenic>());
		allviewlist.setAdapter(adapter);
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
					return new QwcApiImpl().getAllScenicList();
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
					adapter.setMcallbackvalue(pCallbackValue);
					adapter.setAllViewFragment(AllViewFragment.this);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		if (tempArrays == null) {
			intent = new Intent(getActivity(), DetailScenicIntroActivity.class);
			intent.putExtra("id", mcall.get(arg2).getId());
			intent.putExtra("name", mcall.get(arg2).getName());
			intent.putExtra("url", mcall.get(arg2).getImageUrl());
			startActivity(intent);
		} else {
			edsearch.setText("");
			HotViewActivity.searchSwitcher.showNext();
			intent = new Intent(getActivity(), DetailScenicIntroActivity.class);
			intent.putExtra("id", tempArrays.get(arg2).getId());
			intent.putExtra("name", tempArrays.get(arg2).getName());
			intent.putExtra("url", tempArrays.get(arg2).getImageUrl());
			startActivity(intent);
		}

	}

	public void collect(final String id, final int likeNum, final int position) {
		if (Constants.mAccount == null) {
			startActivity(new Intent(getActivity(), LoginActivity.class));
		} else {
			this.doAsync("数据提交中，请稍后...", new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					try {
						return new QwcApiImpl().likeScenicRecord(
								(Constants.mAccount.getMobile() != "" ? Constants.mAccount
										.getMobile() : Constants.mAccount
										.getUserId()), id, 0);
					} catch (WSError e) {
						e.printStackTrace();
					}
					return 1;
				}
			}, new Callback<Integer>() {

				@Override
				public void onCallback(Integer pCallbackValue) {
					if (pCallbackValue == 0) {
						showToast("收藏成功！");
						mcall.get(position).setLikeNum(likeNum + "");
						adapter.setMcallbackvalue(mcall);
						adapter.notifyDataSetChanged();
					} else if (pCallbackValue == 2) {
						showToast("此景点已收藏！");
					} else {
						showToast("收藏失败！");
					}
				}
			});
		}
	}

	public class AboutShareReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getStringExtra("ss").equals("sce")) {
				if (tempArrays == null) {
					mcall.get(searchScenic(mcall)).setShareNum(
							Constants.ScienicShareNum + "");
					adapter.setMcallbackvalue(mcall);
					adapter.notifyDataSetChanged();
				} else {
					tempArrays.get(searchScenic(tempArrays)).setShareNum(
							Constants.ScienicShareNum + "");
					adapter.setMcallbackvalue(tempArrays);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	private int searchScenic(ArrayList<Scenic> searchArray) {
		int i;
		for (i = 0; i < searchArray.size(); i++) {
			if (searchArray.get(i).getName().equals(Constants.ScenicName)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(aboutShareReceiver);
	}
}
