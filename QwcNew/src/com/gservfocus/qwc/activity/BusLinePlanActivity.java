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

	//�������
    RoutePlanSearch mSearch = null;    // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
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
		((TextView)findViewById(R.id.title)).setText("����");
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
		        //��ȡ������·�滮���
		    }  
		    public void onGetTransitRouteResult(TransitRouteResult result) {  
		        //��ȡ��������·���滮���  
		    	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            Toast.makeText(BusLinePlanActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
		            //result.getSuggestAddrInfo()
		            return;
		        }
		        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
		        	for(int j = 0;j<result.getRouteLines().size();j++){
		        		linenum.add("·��"+(j+1));
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
		        //��ȡ�ݳ���·�滮���  
		    	 
		    }  
		};
		mSearch.setOnGetRoutePlanResultListener(listener);
		LatLng me = new LatLng(x, y);
		//�������յ���Ϣ������tranist search ��˵��������������
        PlanNode stNode = PlanNode.withLocation(me);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("������", "ǰ����̬��");
        mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode)
                .city("�Ϻ�")
                .to(enNode));
	}
	
	class MyExpandableListAdapter extends BaseExpandableListAdapter{

		 //��������ͼ����ʾ����
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
