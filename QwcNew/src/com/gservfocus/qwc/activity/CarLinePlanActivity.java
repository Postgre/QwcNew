package com.gservfocus.qwc.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class CarLinePlanActivity extends BaseActivity{

	 //�������
    RoutePlanSearch mSearch = null;    // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
    RouteLine route = null;
    Object step;
    private LayoutInflater mInflater;
    ArrayList<String> location;
    ExpandableListView carline;
    private ArrayList<ArrayList<String>> linenode = new ArrayList<ArrayList<String>>();
    private double x;
    private double y;
    Intent intent;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetitlesuperlistview);
		((TextView)findViewById(R.id.title)).setText("�ݳ�");
		((ImageView)findViewById(R.id.imagereturn1)).setOnClickListener(this);
		carline = (ExpandableListView) findViewById(R.id.list);
		//carline.setBackgroundResource(R.drawable.bigbee2);
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
		    }  
		    public void onGetDrivingRouteResult(DrivingRouteResult result) {  
		        //��ȡ�ݳ���·�滮���  
		    	 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			            Toast.makeText(CarLinePlanActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
			        }
			        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
			            //result.getSuggestAddrInfo()
			            return;
			        }
			        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			            route = result.getRouteLines().get(0);
			            location = new ArrayList<String>();
			            for(int i = 0;i<route.getAllStep().size();i++){
			            	step = route.getAllStep().get(i);
						location.add(((DrivingRouteLine.DrivingStep) step)
								.getInstructions());
			            }
			            Log.i("rzh",location.get(0));
			            linenode.add(location);
			            carline.setAdapter(new MyExpandableListAdapter(linenode));
			            carline.expandGroup(0);
			        }
		    }  
		};
		mSearch.setOnGetRoutePlanResultListener(listener);
		LatLng me = new LatLng(x, y);
		//�������յ���Ϣ������tranist search ��˵��������������
        PlanNode stNode = PlanNode.withLocation(me);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("������", "ǰ����̬��");
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));
	}
	
	class MyExpandableListAdapter extends BaseExpandableListAdapter{

		 //��������ͼ����ʾ����
       private String[] linenum = new String[] {"·��1"};
       private ArrayList<ArrayList<String>> linenode;
       
       public MyExpandableListAdapter(ArrayList<ArrayList<String>> linenode) {
		super();
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
			
			return linenum[arg0];
		}

		@Override
		public int getGroupCount() {
			return linenum.length;
		}

		@Override
		public long getGroupId(int arg0) {
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View father,
				ViewGroup arg3) {
			father = mInflater.inflate(R.layout.lineparent_adapterlist, null);
			((TextView)father.findViewById(R.id.linenum)).setText(linenum[arg0]);
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
