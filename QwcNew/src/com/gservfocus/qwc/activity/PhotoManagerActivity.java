package com.gservfocus.qwc.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.adapter.PhotoManagerAdapter;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.PhotoDict;
import com.gservfocus.qwc.db.ExpandDatabaseImpl;

public class PhotoManagerActivity extends BaseActivity {

	private ExpandableListView photoListView;
	private PhotoManagerAdapter adapter;
	private Button chooseBtn;
	private Button photoDeleteBtn;
	private FrameLayout photoManagerBottomMenuLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_manager);
		// choose mode
		chooseBtn = (Button) findViewById(R.id.photoChooseBtn);
		chooseBtn.setOnClickListener(this);
		// delete
		photoDeleteBtn = (Button) findViewById(R.id.photoDeleteBtn);
		photoDeleteBtn.setOnClickListener(this);
		((ImageButton) findViewById(R.id.photoBackIBtn))
				.setOnClickListener(this);
		// bottom munu
		photoManagerBottomMenuLayout = (FrameLayout) findViewById(R.id.photoManagerBottomMenuLayout);
		// listview
		photoListView = (ExpandableListView) findViewById(R.id.photoListView);
		photoListView.setGroupIndicator(null);
		adapter = new PhotoManagerAdapter(this);

		adapter.setPhotoList(new ArrayList<PhotoDict>());
		photoListView.setAdapter(adapter);

		getPhotos();
	}

	private void getPhotos() {
		this.doAsync("加载中...", new Callable<ArrayList<PhotoDict>>() {

			@Override
			public ArrayList<PhotoDict> call() throws Exception {
				// TODO Auto-generated method stub
				ArrayList<PhotoDict> photoDictArray = new ArrayList<PhotoDict>();

				ArrayList<Photo> photoArrays = new ExpandDatabaseImpl(
						ExpandDatabaseImpl.DB_PATH).allPhotos();

				Photo photo = null;
				ArrayList<Photo> photoDeleteArray = new ArrayList<Photo>();
				File file = null;
				while (photoArrays.size() > 0) {
					ArrayList<Photo> photoTempArrays = new ArrayList<Photo>();
					photo = photoArrays.get(0);
					for (Photo photoT : photoArrays) {
						file = new File(photoT.getPath());
						if (file.exists()) {
							if (photoT.getDate().contains(
									photo.getDate().substring(0, 10))) {
								photoTempArrays.add(photoT);

							}
						} else {
							photoDeleteArray.add(photoT);
						}
					}
					if (photoTempArrays.size() > 0) {
						photoDictArray.add(new PhotoDict(photo.getDate()
								.substring(0, 10), photoTempArrays));
						photoArrays.removeAll(photoTempArrays);
					}
					if(photoDeleteArray.size()>0){
						photoArrays.removeAll(photoDeleteArray);
					}
				}
				//从数据库中删除无效的图片信息
				new ExpandDatabaseImpl(ExpandDatabaseImpl.DB_PATH).removePhoto(photoDeleteArray);
				
				return photoDictArray;
			}
		}, new Callback<ArrayList<PhotoDict>>() {

			@Override
			public void onCallback(ArrayList<PhotoDict> pCallbackValue) {
				// TODO Auto-generated method stub
				adapter.setPhotoList(pCallbackValue);
				adapter.notifyDataSetChanged();
				for (int i = 0; i < pCallbackValue.size(); i++) {
					photoListView.expandGroup(i);
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
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.photoChooseBtn:
			adapter.setChooseMode(adapter.getChooseMode() ? false : true);
			chooseBtn
					.setText(adapter.getChooseMode() ? getString(R.string.cancel)
							: getString(R.string.choose));

			if (!adapter.getChooseMode()) {
				cancelChoose();
				photoManagerBottomMenuLayout.setVisibility(View.GONE);
			} else {
				photoManagerBottomMenuLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.photoDeleteBtn:
			if (adapter.getChooseMode()) {
				adapter.setChooseMode(adapter.getChooseMode() ? false : true);
				chooseBtn
						.setText(adapter.getChooseMode() ? getString(R.string.cancel)
								: getString(R.string.choose));
				deleteChoose();
				photoManagerBottomMenuLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.photoBackIBtn:
			finish();
			break;
		default:
			break;
		}
	}

	public void cancelChoose() {
		for (int i = 0; i < adapter.getPhotoList().size(); i++) {
			for (int j = 0; j < adapter.getPhotoList().get(i).getPhotos()
					.size(); j++) {
				adapter.getPhotoList().get(i).getPhotos().get(j)
						.setIsChoose(false);
			}
		}
		adapter.notifyDataSetChanged();
	}

	public void deleteChoose() {
		this.doAsync("删除中...", new Callable<ArrayList<PhotoDict>>() {

			@Override
			public ArrayList<PhotoDict> call() throws Exception {
				// TODO Auto-generated method stub
				ArrayList<PhotoDict> photoDeleteDictArray = new ArrayList<PhotoDict>();
				for (int i = 0; i < adapter.getPhotoList().size(); i++) {
					// 处理每一天的图片
					ArrayList<Photo> photoDeleteArray = new ArrayList<Photo>();
					ArrayList<Photo> photoTArray = adapter.getPhotoList()
							.get(i).getPhotos();
					for (int j = 0; j < photoTArray.size(); j++) {
						if (photoTArray.get(j).getIsChoose()) {
							File file = new File(photoTArray.get(j).getPath());
							if (file.exists()) { // 判断文件是否存在
								file.delete(); // delete()
							}
							photoDeleteArray.add(adapter.getPhotoList().get(i)
									.getPhotos().get(j));
						}
					}
					// 删除图片
					photoTArray.removeAll(photoDeleteArray);
					new ExpandDatabaseImpl(ExpandDatabaseImpl.DB_PATH)
							.removePhoto(photoDeleteArray);
					// 当图片组中的图片为0，则删除组
					if (photoTArray.size() == 0) {
						photoDeleteDictArray.add(adapter.getPhotoList().get(i));
					}
				}
				adapter.getPhotoList().removeAll(photoDeleteDictArray);
				return adapter.getPhotoList();
			}
		}, new Callback<ArrayList<PhotoDict>>() {

			@Override
			public void onCallback(ArrayList<PhotoDict> pCallbackValue) {
				// TODO Auto-generated method stub
				adapter.setPhotoList(pCallbackValue);
				adapter.notifyDataSetChanged();
			}
		}, new Callback<Exception>() {
			public void onCallback(Exception pCallbackValue) {
			}
		});
	}
}
