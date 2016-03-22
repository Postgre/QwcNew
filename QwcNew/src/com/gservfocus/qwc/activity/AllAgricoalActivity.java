package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.SearchModel;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.adapter.MyAllAgricoalAdapter;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class AllAgricoalActivity extends BaseActivity {

	private ListView agricoallist;
	private String AGRICOAL = "农家乐";
	ArrayList<Agricola> mcall;
	ArrayList<Agricola> tempArrays = null;
	Intent intent;
	private MyAllAgricoalAdapter adapter;
	boolean flag = true;
	EditText edsearch;
	private ViewSwitcher searchSwitcher;
	TextView ifNoNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetitleonelistview);
		getAndSetPost();
		if(!Constants.isNetworkConnected(this)){
			ifNoNet.setVisibility(View.VISIBLE);
			ifNoNet.setOnClickListener(this);
		}else{
			getData();
		}
	}
	
	private void getData(){
		this.doAsync("数据加载中，请稍候...", new Callable<ArrayList<Agricola>>() {

			@Override
			public ArrayList<Agricola> call() throws Exception {
				try {
					return new QwcApiImpl().getAllAgricolaList();
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
					Log.i("rzh", pCallbackValue.get(1).getImage_url());
					adapter.setMcallbackvalue(pCallbackValue);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void getAndSetPost() {
		ifNoNet = ((TextView)findViewById(R.id.ifNoNet));
		findViewById(R.id.imagereturn1).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		agricoallist = (ListView) findViewById(R.id.listview);
		agricoallist.setOnItemClickListener(this);
		adapter = new MyAllAgricoalAdapter(
				AllAgricoalActivity.this);
		adapter.setMcallbackvalue(new ArrayList<Agricola>());
		agricoallist.setAdapter(adapter);
		
		((TextView) findViewById(R.id.title)).setText(AGRICOAL);
		edsearch = (EditText) findViewById(R.id.edsearch);
		edsearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				tempArrays = SearchModel.getSearchList(mcall, new String[]{"name","abstracts"},edsearch.getText().toString());
				adapter.setMcallbackvalue(tempArrays);
				adapter.notifyDataSetChanged();
			}
		});
		searchSwitcher = ((ViewSwitcher) findViewById(R.id.searchSwitcher));
		((TextView)findViewById(R.id.btreturn)).setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		intent = new Intent(AllAgricoalActivity.this,
				DetailAgricoalActivity.class);
		if(tempArrays == null){
			intent.putExtra("id", mcall.get(arg2).getId());
			intent.putExtra("avgservice", mcall.get(arg2).getService());
			intent.putExtra("avgtaste", mcall.get(arg2).getTaste());
			intent.putExtra("avgcondition", mcall.get(arg2).getCondition());
			startActivity(intent);
		}else{
			edsearch.setText("");
			searchSwitcher.showNext();
			intent.putExtra("id", tempArrays.get(arg2).getId());
			intent.putExtra("avgservice", tempArrays.get(arg2).getService());
			intent.putExtra("avgtaste", tempArrays.get(arg2).getTaste());
			intent.putExtra("avgcondition", tempArrays.get(arg2).getCondition());
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.search:
			searchSwitcher.showPrevious();

			flag = false;
			break;

		case R.id.btreturn:
			edsearch.setText("");
			searchSwitcher.showNext();

			flag = true;
			break;
		case R.id.ifNoNet:
			if(Constants.isNetworkConnected(this)){
				ifNoNet.setVisibility(View.INVISIBLE);
				getData();
			}
			break;
		}
	}
}
