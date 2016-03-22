package com.gservfocus.qwc.activity.fragment;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.DetailScenicIntroActivity;
import com.gservfocus.qwc.activity.util.BaseFragment;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.MultiItemApdater;
import com.huewu.pla.lib.internal.PLA_AdapterView;

public class HotSuggestFragment extends BaseFragment {

	//MultiColumnListView hotsuggest;
	GridView suggest;
	Intent intent;
	private ArrayList<Scenic> mcall;
	TextView ifNoNet;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.hotsuggest_hotview, null);
		ifNoNet = (TextView) v.findViewById(R.id.ifNoNet);
		//final MyHotSuggestAdapter mh = new MyHotSuggestAdapter(getActivity());
		//hotsuggest = (MultiColumnListView) v.findViewById(R.id.hotsuggest);
		suggest = (GridView) v.findViewById(R.id.suggest);
		/*hotsuggest.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				intent = new Intent(getActivity(),DetailScenicIntroActivity.class);
				intent.putExtra("id", mcall.get(position).getId());
				intent.putExtra("name",mcall.get(position).getName());
				intent.putExtra("intro",mcall.get(position).getAbstracts());
				intent.putExtra("url", mcall.get(position).getImageUrl());
				startActivity(intent);
			}
		});*/
		suggest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				intent = new Intent(getActivity(),DetailScenicIntroActivity.class);
				intent.putExtra("id", mcall.get(position).getId());
				intent.putExtra("name",mcall.get(position).getName());
				intent.putExtra("intro",mcall.get(position).getAbstracts());
				intent.putExtra("url", mcall.get(position).getImageUrl());
				startActivity(intent);
			}
		});
		if(!Constants.isNetworkConnected(getActivity())){
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		}else{
			getData();
		}
		return v;
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch(view.getId()){
		case R.id.ifNoNet:
			if(Constants.isNetworkConnected(getActivity())){
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}
	}
	
	private void getData() {
		// TODO Auto-generated method stub
		this.doAsync("数据加载中，请稍候...", new Callable<ArrayList<Scenic>>() {

			@Override
			public ArrayList<Scenic> call() throws Exception {
				try {
					return new QwcApiImpl().getHotScenicList();
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<Scenic>>() {

			@Override
			public void onCallback(ArrayList<Scenic> pCallbackValue) {
				if(pCallbackValue != null){
					mcall = pCallbackValue;
					//mh.setMcallbackvalue(pCallbackValue);
					//((BaseAdapter)(hotsuggest.getAdapter())).notifyDataSetChanged();
					MultiItemApdater adapter =new MultiItemApdater(getActivity());
					adapter.setList(pCallbackValue);
					//hotsuggest.setAdapter(adapter);
					suggest.setAdapter(adapter);
				}
			}
		});
	}

}
