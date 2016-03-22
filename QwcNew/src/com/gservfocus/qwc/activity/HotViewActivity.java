package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.SearchModel;
import com.gservfocus.qwc.activity.fragment.AllViewFragment;
import com.gservfocus.qwc.activity.fragment.HotSuggestFragment;
import com.gservfocus.qwc.activity.util.FragmentBaseActivity;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.Scenic;

public class HotViewActivity extends FragmentBaseActivity {

	// ViewPager��google SDk���Դ���һ�����Ӱ���һ���࣬��������ʵ����Ļ����л���
	// android-support-v4.jar
	private ViewPager mPager;// ҳ������
	private List<Fragment> listViews; // Tabҳ���б�
	private ImageView cursor;// ����ͼƬ
	private TextView t, t1, t2;// ҳ��ͷ��
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private String TITLE = "���ž���";
	private String Ltitle1 = "�����Ƽ�";
	private String Ltitle2 = "ȫ������";
	boolean flag = true;
	public static ViewSwitcher searchSwitcher;
	AllViewFragment allviewfragment = new AllViewFragment();
	EditText edsearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_2p);

		InitImageView();
		InitTextView();
		InitViewPager();
	}
	
	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.search:
			searchSwitcher.showPrevious();

			flag = false;
			mPager.setCurrentItem(1);
			break;
		case R.id.btreturn:
			edsearch.setText("");
			searchSwitcher.showNext();

			flag = true;
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Exit");
		menu.add(0, 2, 1, "About");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int menuId = item.getItemId();
		switch (menuId) {
		case 1:
			finish();
			break;
		case 2:
			Toast.makeText(this, "about", Toast.LENGTH_LONG).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ʼ��ͷ��
	 */
	private void InitTextView() {
		findViewById(R.id.imagereturn1).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		searchSwitcher = ((ViewSwitcher) findViewById(R.id.searchSwitcher));
		((TextView) findViewById(R.id.btreturn)).setOnClickListener(this);
		edsearch = (EditText) findViewById(R.id.edsearch);
		allviewfragment.setEdsearch(edsearch);
		t = (TextView) findViewById(R.id.title);
		t1 = (TextView) findViewById(R.id.test1);
		t2 = (TextView) findViewById(R.id.test2);
		t.setText(TITLE);
		t1.setText(Ltitle1);
		t2.setText(Ltitle2);
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));

	}

	/**
	 * ��ʼ��ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<Fragment>();
		
		listViews.add(new HotSuggestFragment());
		listViews.add(allviewfragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager()));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ��ʼ������
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 2 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
	}

	/**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * ҳ���л�����
	 **/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * ViewPager������
	 */
	class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return listViews.get(arg0);
		}

		@Override
		public int getCount() {
			return listViews.size();
		}
	}

}
