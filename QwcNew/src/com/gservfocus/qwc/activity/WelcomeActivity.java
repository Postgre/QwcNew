package com.gservfocus.qwc.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class WelcomeActivity extends BaseActivity {

	ViewPager viewPager;
	LinearLayout help_item_indictor;
	int helpImgs[] = {R.drawable.eat,R.drawable.house,R.drawable.play,R.drawable.enter};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcometohere);
		viewPager = (ViewPager) findViewById(R.id.viewpager_help);
		help_item_indictor = (LinearLayout) findViewById(R.id.help_item_indictor);
		final List<View> views = new ArrayList<View>();
		ImageView view;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		for (int i = 0; i < helpImgs.length; i++) {
			view = new ImageView(this);
			view.setLayoutParams(params);
			view.setBackgroundResource(helpImgs[i]);
			views.add(view);
		}
		views.get(views.size()-1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WelcomeActivity.this, MyTeacherActivity.class));
				finish();
			}
		});
		// pagerview adapter
		PagerAdapter pageAdater = new PagerAdapter() {

			
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}

			
			public int getCount() {
				return views.size();
			}
		};
		viewPager.setAdapter(pageAdater);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				for (int i = 0; i < help_item_indictor.getChildCount(); i++) {
					if (arg0 == i)
						((ImageView) help_item_indictor.getChildAt(i))
								.setImageResource(R.drawable.round_point_gray);
					else
						((ImageView) help_item_indictor.getChildAt(i))
						.setImageResource(R.drawable.round_point);
				}
			}

			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
