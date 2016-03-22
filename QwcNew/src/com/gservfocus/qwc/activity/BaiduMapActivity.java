package com.gservfocus.qwc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class BaiduMapActivity extends BaseActivity {

	// ��λ���
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	MapView mMapView;
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	String Traffic = "��ͨ����";
	Marker mMarker;
	BitmapDescriptor bd;
	OverlayOptions ooA;
	LatLng ll;
	Intent intent;
	private ScrollView trafficDetailsLayout;
	private ImageView bigMapImageView;
	private PopupWindow mPopupWindow;

	// private String x;
	// private String y;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_location);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText(Traffic);

		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		bigMapImageView = ((ImageView) findViewById(R.id.bigmap));
		bigMapImageView.setOnClickListener(this);
		trafficDetailsLayout = (ScrollView) findViewById(R.id.trafficDetailsLayout);
		// �������ſؼ�
		int childCount = mMapView.getChildCount();
		View zoom = null;
		for (int i = 0; i < childCount; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				zoom = child;
				break;
			}
		}
		zoom.setVisibility(View.GONE);
		// ɾ���ٶȵ�ͼlogo
		mMapView.removeViewAt(1);
		mBaiduMap = mMapView.getMap();
		// mBaiduMap.
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// �����ͼ״̬
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(8.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {

				// ��ӵ������¼���Ӧ����
				if (mBaiduMap.getLocationData() != null) {

					System.out.println("latitude:"
							+ mBaiduMap.getLocationData().latitude
							+ ",longitude"
							+ mBaiduMap.getLocationData().longitude);
					initPopuptWindow();
					mPopupWindow.showAtLocation(mMapView, Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
				} else {

				}

				return false;
			}

		});

	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.btn_car:
			intent = new Intent(BaiduMapActivity.this,
					CarLinePlanActivity.class);
			intent.putExtra("x", "" + mBaiduMap.getLocationData().latitude);
			intent.putExtra("y", "" + mBaiduMap.getLocationData().longitude);
			startActivity(intent);
			break;
		case R.id.btn_bus:
			intent = new Intent(BaiduMapActivity.this,
					BusLinePlanActivity.class);
			intent.putExtra("x", "" + mBaiduMap.getLocationData().latitude);
			intent.putExtra("y", "" + mBaiduMap.getLocationData().longitude);
			startActivity(intent);
			break;
		case R.id.btn_cancel:
			mPopupWindow.dismiss();
			break;
		case R.id.bigmap:
			trafficDetailsLayout.setVisibility(trafficDetailsLayout
					.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
			bigMapImageView.setImageResource(trafficDetailsLayout
					.getVisibility() == View.GONE ? R.drawable.up_arrow_t
					: R.drawable.down_arrow_t);
			break;
		}
	}

	public void initOverlay() {

		// add marker overlay
		bd = BitmapDescriptorFactory.fromResource(R.drawable.qwc_location);
		ll = new LatLng(31.723234, 121.5162);

		ooA = new MarkerOptions().position(ll).icon(bd);
		mMarker = (Marker) (mBaiduMap.addOverlay(ooA));

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.setMapStatus(u);
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			if (location.getLatitude() != 4.9E-324) {
				mBaiduMap.setMyLocationData(locData);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/*
	 * ����PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.alert_dialog, null);
		((Button) popupWindow.findViewById(R.id.btn_car))
				.setOnClickListener(this);
		((Button) popupWindow.findViewById(R.id.btn_bus))
				.setOnClickListener(this);
		((Button) popupWindow.findViewById(R.id.btn_cancel))
				.setOnClickListener(this);
		// ����һ��PopupWindow
		// ����1��contentView ָ��PopupWindow������
		// ����2��width ָ��PopupWindow��width
		// ����3��height ָ��PopupWindow��height
		mPopupWindow = new PopupWindow(popupWindow,
				WindowManager.LayoutParams.MATCH_PARENT, Constants.dip2px(this,
						210));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		mLocClient.start();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		bd.recycle();
		super.onDestroy();
	}
}
