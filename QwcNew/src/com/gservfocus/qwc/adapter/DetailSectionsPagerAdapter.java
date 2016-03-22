package com.gservfocus.qwc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.gservfocus.qwc.activity.fragment.HomeFragment;
import com.gservfocus.qwc.activity.fragment.MoreFragment;

public class DetailSectionsPagerAdapter extends FragmentStatePagerAdapter {
	
	public static final int ARG_SECTION_COUNT = 2;
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	public DetailSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = null;
		switch (arg0) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new MoreFragment();
			break;
		default:
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return ARG_SECTION_COUNT;
	}

}
