package com.gservfocus.qwc.activity.fragment;

import com.gservfocus.qwc.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutQwcFragment extends Fragment {
private ImageView imageView1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.aboutqwc_viewintro, null);
		imageView1 = (ImageView) v.findViewById(R.id.imageView1);
		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.about_qwc,
				imageView1);
		return v;
	}
	
}
