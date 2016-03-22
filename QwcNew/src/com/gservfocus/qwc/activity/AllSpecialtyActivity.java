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
import com.gservfocus.qwc.adapter.MyAllSpecialtyAdapter;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.Specialty;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class AllSpecialtyActivity extends BaseActivity {

	private ListView specialtylist;
	private String SPECIALTY = "农特产";
	ArrayList<Specialty> mcall;
	Intent intent;
	EditText edsearch;
	private ViewSwitcher searchSwitcher;
	ArrayList<Specialty> tempArrays = null;
	boolean flag = true;
	MyAllSpecialtyAdapter adapter = new MyAllSpecialtyAdapter(this);
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
		this.doAsync("数据加载中，请稍候...", new Callable<ArrayList<Specialty>>() {

			@Override
			public ArrayList<Specialty> call() throws Exception {
				try {
					return new QwcApiImpl().getAllSpecialtyList();
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
		specialtylist = (ListView) findViewById(R.id.listview);
		adapter.setMcallbackvalue(new ArrayList<Specialty>());
		specialtylist.setAdapter(adapter);
		specialtylist.setOnItemClickListener(this);
		((TextView) findViewById(R.id.title)).setText(SPECIALTY);
		searchSwitcher = ((ViewSwitcher) findViewById(R.id.searchSwitcher));
		((TextView) findViewById(R.id.btreturn)).setOnClickListener(this);
		edsearch = (EditText) findViewById(R.id.edsearch);
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
						"name", "intro" }, edsearch.getText().toString());
				adapter.setMcallbackvalue(tempArrays);
				adapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		intent = new Intent(AllSpecialtyActivity.this,
				DetailSpecialtyActivity.class);
		if (tempArrays != null) {
			intent.putExtra("id", tempArrays.get(arg2).getId());
			startActivity(intent);
			edsearch.setText("");
			searchSwitcher.showNext();
		} else {
			intent.putExtra("id", mcall.get(arg2).getId());
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
