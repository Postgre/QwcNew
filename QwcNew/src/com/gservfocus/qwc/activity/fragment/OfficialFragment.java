package com.gservfocus.qwc.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class OfficialFragment extends Fragment {

	private static final String APP_ID = "wx397097ba83604ec9";
	private IWXAPI api;
	private ImageView imageView1, imageView2, imageView3, imageView4;
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.officialwx_viewintro, null);
		/*options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片
		.showImageOnLoading(R.drawable.white).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
		// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
		.build();*/
		imageView1 = (ImageView) v.findViewById(R.id.imageView1);
		imageView2 = (ImageView) v.findViewById(R.id.imageView2);
		imageView3 = (ImageView) v.findViewById(R.id.imageView3);
		imageView4 = (ImageView) v.findViewById(R.id.imageView4);
		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.wx_step_1,
				imageView1);
		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.wx_step_2,
				imageView2);
		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.wx_step_3,
				imageView3);
		ImageLoader.getInstance().displayImage("drawable://" + R.drawable.wx_step_4,
				imageView4);
		((TextView) v.findViewById(R.id.openWx))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						regToWx();
						api.openWXApp();
					}
				});
		return v;
	}

	private void regToWx() {
		// TODO Auto-generated method stub
		api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);

		api.registerApp(APP_ID);
	}

}
