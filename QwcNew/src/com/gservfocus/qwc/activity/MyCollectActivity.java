package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.fragment.UserAgricoalFragment;
import com.gservfocus.qwc.activity.fragment.UserScenicFragment;
import com.gservfocus.qwc.activity.fragment.UserSpecialtyFragment;
import com.gservfocus.qwc.activity.util.FragmentBaseActivity;

public class MyCollectActivity extends FragmentBaseActivity{

	// ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。
	// android-support-v4.jar
	private ViewPager mPager;//页卡内容
	private List<Fragment> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t, t1 , t2 , t3;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private String TITLE = "我的收藏";
	private String Ltitle1 = "景点";
	private String Ltitle2 = "农家乐";
	private String Ltitle3 = "农特产";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_3p);
       
        	InitImageView();
    		InitTextView();
    		InitViewPager();     
	}
	
	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch(view.getId()){
			case R.id.imagereturn1:
				finish();
				break;
			/*case R.id.search:
				break;*/
		}
	}
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0, 1, 1, "Exit");
			menu.add(0,2,1,"About");
			return super.onCreateOptionsMenu(menu);
		}


		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int menuId=item.getItemId();
			switch(menuId){
				case 1:finish();break;
				case 2:Toast.makeText(this, "about", Toast.LENGTH_LONG).show();break;
			}
			return super.onOptionsItemSelected(item);
		}
		
		/**
		 * 初始化头标
		 */
		private void InitTextView() {
			findViewById(R.id.imagereturn1).setOnClickListener(this);
			//findViewById(R.id.search).setOnClickListener(this);
			t = (TextView) findViewById(R.id.title);
			t1 = (TextView) findViewById(R.id.test1);
			t2 = (TextView) findViewById(R.id.test2);
			t3 = (TextView) findViewById(R.id.test3);
			t.setText(TITLE);
			t1.setText(Ltitle1);
			t2.setText(Ltitle2);
			t3.setText(Ltitle3);
			t1.setOnClickListener(new MyOnClickListener(0));
			t2.setOnClickListener(new MyOnClickListener(1));
			t3.setOnClickListener(new MyOnClickListener(2));
			
		}

		/**
		 * 初始化ViewPager
		 */
		private void InitViewPager() {
			mPager = (ViewPager) findViewById(R.id.vPager);
			listViews = new ArrayList<Fragment>();
			
			listViews.add(new UserScenicFragment());
			listViews.add(new UserAgricoalFragment());
			listViews.add(new UserSpecialtyFragment());

			mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
			mPager.setCurrentItem(0);
			mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		}

		/**
		 * 初始化动画
		 */
		private void InitImageView() {
			cursor = (ImageView) findViewById(R.id.cursor);
			bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
					.getWidth();// 获取图片宽度
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int screenW = dm.widthPixels;// 获取分辨率宽度
			offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
			Matrix matrix = new Matrix();
			matrix.postTranslate(offset, 0);
			cursor.setImageMatrix(matrix);// 设置动画初始位置
		}

		

		/**
		 * 头标点击监听
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
		 * 页卡切换监听
		 **/
		public class MyOnPageChangeListener implements OnPageChangeListener {

			int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
			int two = one * 2;// 页卡1 -> 页卡3 偏移量

			@Override
			public void onPageSelected(int arg0) {
				Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
				currIndex = arg0;
				animation.setFillAfter(true);// True:图片停在动画结束位置
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
		 * ViewPager适配器
		 */
		class MyFragmentPagerAdapter extends FragmentPagerAdapter{
		
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
