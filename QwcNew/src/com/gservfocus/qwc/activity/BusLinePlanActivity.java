package com.gservfocus.qwc.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.CarLinePlanActivity.MyExpandableListAdapter;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class BusLinePlanActivity extends BaseActivity {

	//搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    RouteLine route = null;
    Object step;
    private LayoutInflater mInflater;
    ArrayList<String> location;
    ExpandableListView busline;
    private ArrayList<ArrayList<String>> linenode = new ArrayList<ArrayList<String>>();
    private double x;
    private double y;
    Intent intent;
    ArrayList<String> linenum = new ArrayList<String>();
    Drawable imgD = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetitlesuperlistview);
		imgD = getResources().getDrawable(R.drawable.bus);
		imgD.setBounds(0, 0, imgD.getMinimumWidth(),
				imgD.getMinimumHeight());
		((TextView)findViewById(R.id.title)).setText("公交");
		((ImageView)findViewById(R.id.imagereturn1)).setOnClickListener(this);
		busline = (ExpandableListView) findViewById(R.id.list);
		//busline.setBackgroundResource(R.drawable.bigbee);
		intent = getIntent();
		x = Double.parseDouble(intent.getStringExtra("x"));
		y = Double.parseDouble(intent.getStringExtra("y"));
		mInflater = LayoutInflater.from(this);
		mSearch = RoutePlanSearch.newInstance();
		OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {  
		    public void onGetWalkingRouteResult(WalkingRouteResult result) {  
		        //获取步行线路规划结果
		    }  
		    public void onGetTransitRouteResult(TransitRouteResult result) {  
		        //获取公交换乘路径规划结果  
		    	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            Toast.makeText(BusLinePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
		            //result.getSuggestAddrInfo()
		            return;
		        }
		        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
		        	for(int j = 0;j<result.getRouteLines().size();j++){
		        		linenum.add("路线"+(j+1));
			            route = result.getRouteLines().get(j);
			            location = new ArrayList<String>();
			            for(int i = 0;i<route.getAllStep().size();i++){
			            	step = route.getAllStep().get(i);
			            	location.add(((TransitRouteLine.TransitStep) step).getInstructions());
			            }
			        linenode.add(location);
		        	}
			        Log.i("rzh",linenode.get(0).get(0));
			        busline.setAdapter(new MyExpandableListAdapter(linenum,linenode));
			        busline.expandGroup(0);
		        }
		    }  
		    public void onGetDrivingRouteResult(DrivingRouteResult result) {  
		        //获取驾车线路规划结果  
		    	 
		    }  
		};
		mSearch.setOnGetRoutePlanResultListener(listener);
		LatLng me = new LatLng(x, y);
		//设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withLocation(me);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("崇明县", "前卫生态村");
        mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode)
                .city("上海")
                .to(enNode));
	}
	
	class MyExpandableListAdapter extends BaseExpandableListAdapter{

		 //设置组视图的显示文字
       private ArrayList<String> linenum;
       private ArrayList<ArrayList<String>> linenode;
       
		public MyExpandableListAdapter(ArrayList<String> linenum,
			ArrayList<ArrayList<String>> linenode) {
			super();
			this.linenum = linenum;
			this.linenode = linenode;
		}

		@Override
		public Object getChild(int arg0, int arg1) {
			return linenode.get(arg0).get(arg1);
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			return arg1;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View son,
				ViewGroup arg4) {
			
			son = mInflater.inflate(R.layout.lineson_adapterlist, null);
			((TextView)son.findViewById(R.id.node)).setText(linenode.get(arg0).get(arg1));
			if(arg1 == 0){
				((ImageView)son.findViewById(R.id.leftline)).setImageResource(R.drawable.node_line_top);
			}else if(arg1 == (linenode.get(0).size())-1){
				((ImageView)son.findViewById(R.id.leftline)).setImageResource(R.drawable.node_line_bottom);
			}
			return son;
		}

		@Override
		public int getChildrenCount(int arg0) {
			
			return linenode.get(arg0).size();
		}

		@Override
		public Object getGroup(int arg0) {
			
			return linenum.get(arg0);
		}

		@Override
		public int getGroupCount() {
			return linenum.size();
		}

		@Override
		public long getGroupId(int arg0) {
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View father,
				ViewGroup arg3) {
			
			father = mInflater.inflate(R.layout.lineparent_adapterlist, null);
			((TextView)father.findViewById(R.id.linenum)).setText(linenum.get(arg0));
			((TextView)father.findViewById(R.id.linenum)).setCompoundDrawables(imgD, null, null, null);
			
			return father;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			
			
			return false;
		}
		
	}

	@Override
	public void onClick(View view) {

		if(view.getId() == R.id.imagereturn1){
			mSearch.destroy();
			finish();
		}
		super.onClick(view);
	}
	

	@Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }
}
