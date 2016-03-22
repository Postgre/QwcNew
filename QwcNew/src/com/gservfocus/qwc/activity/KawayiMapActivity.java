package com.gservfocus.qwc.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.NaviMapObject;
import com.gservfocus.qwc.bean.Teacher;
import com.gservfocus.qwc.map.MapObjectContainer;
import com.gservfocus.qwc.map.MapObjectModel;
import com.gservfocus.qwc.map.TextPopup;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.ls.widgets.map.utils.PivotFactory.PivotPosition;

public class KawayiMapActivity extends BaseActivity implements
		MapEventsListener, OnMapTouchListener {
	// http://mapp.android-libraries.com/slicingtool/
	// map layout
	private RelativeLayout mapLayout;
	private static final String TAG = "HomeTabNaviFragment";

	private static final Integer LAYER1_ID = 0;
	// private static final Integer LAYER2_ID = 1;
	private static final int MAP_ID = 23;

	private MapObjectContainer model;
	private MapWidget map;
	private TextPopup mapObjectInfoPopup;
	String Traffic = "交通导览";
	Drawable drawable = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kawayimap);
		((TextView) findViewById(R.id.title)).setText(Traffic);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
		// setting navigation map
		mapLayout = (RelativeLayout) findViewById(R.id.kawayimap);
		model = new MapObjectContainer();
		// Remove LOGO
		removeLogoMethod();
		initMap(null);
		initMapObjectData(NaviMapObject.getAll());
		initMapObjects();
		initMapListeners();
		map.centerMap();
		refresh();

	}

	private void refresh() {

		this.doAsync(null, new Callable<ArrayList<NaviMapObject>>() {

			@Override
			public ArrayList<NaviMapObject> call() throws Exception {
				try {
					return new QwcApiImpl()
							.getScencParentByMobile((Constants.mAccount
									.getMobile() != "" ? Constants.mAccount
									.getMobile() : Constants.mAccount
									.getUserId()));
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<ArrayList<NaviMapObject>>() {

			@Override
			public void onCallback(ArrayList<NaviMapObject> pCallbackValue) {
				if (pCallbackValue != null) {

					NaviMapObject.getAll().clear();
					NaviMapObject.setAll(pCallbackValue);
					// 删除旧数据
					removeAllMapObject(map.getLayerById(LAYER1_ID));
					// 添加新数据
					initMapObjectData(pCallbackValue);
					// 添加新地图对象
					initMapObjects();
					String name = getIntent().getStringExtra("jump");
					if (name != null) {
						for (int i = 0; i < pCallbackValue.size(); i++) {
							if (name.equals(pCallbackValue.get(i).getName())) {
								map.scrollMapTo(new Point(pCallbackValue.get(i)
										.getX(), pCallbackValue.get(i).getY()));

								break;
							}
						}
					}
				}
			}
		}, new Callback<Exception>() {

			@Override
			public void onCallback(Exception pCallbackValue) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.button1:
			map.zoomOut();
			break;
		case R.id.button2:
			map.zoomIn();

			break;
		case R.id.imagereturn1:
			finish();
			break;
		}

	}

	/** Remove LOGO */
	private void removeLogoMethod() {
		Class<?> c = null;

		try {
			c = Class.forName("com.ls.widgets.map.utils.Resources");
			Object obj = c.newInstance();
			Field field = c.getDeclaredField("LOGO");
			field.setAccessible(true);

			field.set(obj, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("ResourceAsColor")
	private void initMap(Bundle savedInstanceState) {
		// In order to display the map on the screen you will need
		// to initialize widget and place it into layout.
		if (Constants.ScreenWidth > 480) {
			map = new MapWidget(savedInstanceState, this, "qwc", 11); // initial
		} else {
			map = new MapWidget(savedInstanceState, this, "qwc", 10);
		}

		// map = new MapWidget(savedInstanceState, this.getActivity(), new File(
		// Environment.getExternalStorageDirectory().getAbsolutePath()
		// + "/qwc"), 11);
		// zoom
		map.setMinZoomLevel(10);
		map.setMaxZoomLevel(12);
		// level

		map.setId(MAP_ID);

		OfflineMapConfig config = map.getConfig();
		config.setZoomBtnsVisible(false); // Sets embedded zoom buttons visible
		config.setPinchZoomEnabled(true); // Sets pinch gesture to zoom
		config.setFlingEnabled(true); // Sets inertial scrolling of the map
		config.setMapCenteringEnabled(true);
		// Configuration of GPS receiver
		// GPSConfig gpsConfig = config.getGpsConfig();
		// gpsConfig.setPassiveMode(false);
		// gpsConfig.setGPSUpdateInterval(500, 5);

		// Configuration of position marker
		MapGraphicsConfig graphicsConfig = config.getGraphicsConfig();
		graphicsConfig.setAccuracyAreaColor(R.color.white); // Blue with
															// transparency
		graphicsConfig.setAccuracyAreaBorderColor(R.color.white); // Blue
																	// without
																	// transparency
		// Adding the map to the layout
		mapLayout.addView(map, 0);
		mapLayout.setBackgroundColor(getResources().getColor(R.color.white));

		// Adding layers in order to put there some map objects
		map.createLayer(LAYER1_ID); // you will need layer id's in order to
									// access particular layer
		mapObjectInfoPopup = new TextPopup(this, mapLayout);
	}

	private void initMapObjectData(ArrayList<NaviMapObject> _naviMapObjects) {
		// Adding objects to the model
		// You may want to implement your own model
		ArrayList<NaviMapObject> naviMapObjects = _naviMapObjects;
		for (NaviMapObject naviMapObject : naviMapObjects) {
			MapObjectModel objectModel = new MapObjectModel(
					naviMapObject.getId(), naviMapObject.getX(),
					naviMapObject.getY(), naviMapObject.getName(),
					naviMapObject.isScan());
			model.addObject(objectModel);
		}

	}

	private void initMapObjects() {

		Layer layer1 = map.getLayerById(LAYER1_ID);

		for (int i = 0; i < model.size(); ++i) {
			addNotScalableMapObject(model.getObject(i), layer1, NaviMapObject
					.getAll().get(i));
		}

	}

	private void removeAllMapObject(Layer layer) {
		// for (int i = 0; i < model.size(); ++i) {
		// layer.removeMapObject(model.getObject(i));
		layer.clearAll();
		// }
		model.removeAllObject();
	}

	private void addNotScalableMapObject(MapObjectModel objectModel,
			Layer layer, NaviMapObject mCallbackValue) {

		if (objectModel.isScan()) {
			if (Teacher.getTeacher(this).isBoy()) {
				if (getIntent().getStringExtra("jump") != null
						&& getIntent().getStringExtra("jump").equals(
								mCallbackValue.getName())
						&& mCallbackValue != null) {
					drawable = getResources().getDrawable(
							R.drawable.scenic_pointer_boy_red);
				} else {
					drawable = getResources().getDrawable(
							R.drawable.scenic_pointer_boy);
				}
			} else {
				if (getIntent().getStringExtra("jump") != null
						&& getIntent().getStringExtra("jump").equals(
								mCallbackValue.getName())
						&& mCallbackValue != null) {
					drawable = getResources().getDrawable(
							R.drawable.scenic_pointer_girl_red);
				} else {
					drawable = getResources().getDrawable(
							R.drawable.scenic_pointer_gril);
				}
			}
		} else {
			if (getIntent().getStringExtra("jump") != null
					&& getIntent().getStringExtra("jump").equals(
							mCallbackValue.getName()) && mCallbackValue != null) {
				drawable = getResources().getDrawable(
						R.drawable.scenic_pointer_red);
			} else {
				drawable = getResources()
						.getDrawable(R.drawable.scenic_pointer);
			}
		}
		drawable.getIntrinsicHeight();
		// Creating the map object
		MapObject object1 = new MapObject(objectModel.getId(), drawable,
				new Point(objectModel.getX(), objectModel.getY()),
				PivotFactory.createPivotPoint(drawable,
						PivotPosition.PIVOT_CENTER), true, false);

		// Adding object to layer
		layer.addMapObject(object1);
	}

	private void initMapListeners() {
		// In order to receive MapObject touch events we need to set listener
		map.setOnMapTouchListener(this);

		// In order to receive pre and post zoom events we need to set
		// MapEventsListener
		map.addMapEventsListener(this);

		// In order to receive map scroll events we set OnMapScrollListener
		map.setOnMapScrolledListener(new OnMapScrollListener() {
			public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
				handleOnMapScroll(v, event);
			}
		});

	}

	private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) {
		// When user scrolls the map we receive scroll events
		// This is useful when need to move some object together with the map

		int dx = event.getDX(); // Number of pixels that user has scrolled
								// horizontally
		int dy = event.getDY(); // Number of pixels that user has scrolled
								// vertically

		if (mapObjectInfoPopup.isVisible()) {
			mapObjectInfoPopup.moveBy(dx, dy);
		}
	}

	@Override
	public void onPostZoomIn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostZoomOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreZoomIn() {
		Log.i(TAG, "onPreZoomIn()");

		if (mapObjectInfoPopup != null) {
			mapObjectInfoPopup.hide();
		}
	}

	@Override
	public void onPreZoomOut() {
		Log.i(TAG, "onPreZoomOut()");

		if (mapObjectInfoPopup != null) {
			mapObjectInfoPopup.hide();
		}
	}

	// * On map touch listener implemetnation *//
	@Override
	public void onTouch(MapWidget v, MapTouchedEvent event) {
		// Get touched object events from the MapTouchEvent
		ArrayList<ObjectTouchEvent> touchedObjs = event
				.getTouchedObjectEvents();

		if (touchedObjs.size() > 0) {

			int xInMapCoords = event.getMapX();
			int yInMapCoords = event.getMapY();
			int xInScreenCoords = event.getScreenX();
			int yInScreenCoords = event.getScreenY();

			ObjectTouchEvent objectTouchEvent = event.getTouchedObjectEvents()
					.get(0);

			// Due to a bug this is not actually the layer id, but index of the
			// layer in layers array.
			// Will be fixed in the next release.
			long layerId = objectTouchEvent.getLayerId();
			Integer objectId = (Integer) objectTouchEvent.getObjectId();
			// User has touched one or more map object
			// We will take the first one to show in the toast message.
			String message = "You touched the object with id: " + objectId
					+ " on layer: " + layerId + " mapX: " + xInMapCoords
					+ " mapY: " + yInMapCoords + " screenX: " + xInScreenCoords
					+ " screenY: " + yInScreenCoords;

			Log.d(TAG, message);

			MapObjectModel objectModel = model.getObjectById(objectId
					.intValue());

			if (objectModel != null) {

				showLocationsPopup(objectId, xInScreenCoords, yInScreenCoords,
						objectModel.getCaption());
			} else {
				// This is a case when we want to show popup where the user has
				// touched.
				showLocationsPopup(objectId, xInScreenCoords, yInScreenCoords,
						"Shows where user touched");
			}

			// Hint: If user touched more than one object you can show the
			// dialog in which ask
			// the user to select concrete object
		} else {
			if (mapObjectInfoPopup != null) {
				mapObjectInfoPopup.hide();
			}
		}
	}

	private void showLocationsPopup(final int objectId, int x, int y,
			String text) {

		if (mapObjectInfoPopup != null) {
			mapObjectInfoPopup.hide();
		}
		((TextPopup) mapObjectInfoPopup).setText(text);

		mapObjectInfoPopup.setOnClickListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (mapObjectInfoPopup != null) {

						Intent intent = new Intent(KawayiMapActivity.this,
								DetailScenicIntroActivity.class);
						intent.putExtra("id",
								NaviMapObject.findNaviMapObject(objectId)
										.getScenicID());
						startActivity(intent);

						mapObjectInfoPopup.hide();
					}
				}

				return false;
			}
		});

		((TextPopup) mapObjectInfoPopup).show(mapLayout, x, y);
	}

}
