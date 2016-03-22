package com.gservfocus.qwc.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.ShareModel;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.media.UMImage;

public class AboutQwcActivity extends BaseActivity {

	EditText suggestET;
	TextView t2;
	ShareModel mShare = new ShareModel(this);
	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		suggestET = (EditText) findViewById(R.id.suggestET);
		t2 = (TextView) findViewById(R.id.t2);
		t2.setOnClickListener(this);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((ImageView) findViewById(R.id.about_share)).setOnClickListener(this);
		((TextView) findViewById(R.id.sendSuggest)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.about_share:
			Constants.isShareWhat = "isShareAbout";
			String url = Constants.About_Qwc;
			InputStream is = getResources().openRawResource(
					R.drawable.qwciconshare);
			Bitmap bp = BitmapFactory.decodeStream(is);
			UMImage ui = new UMImage(this, bp);
			mShare.SetController("伴您拥有独一无二的“清肺”之旅，尽情享受最为“纯朴”的农家快乐！",
					"【前卫村】互动寻览APP", url, ui);
			mPopupWindow = mShare.initPopuptWindow();
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.sendSuggest:
			sandData();
			break;
		case R.id.t2:
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("hotelname", "巨服科技");
			map.put("type", "click");
			MobclickAgent.onEvent(this, "company_tele", map);
			break;
		}
	}

	private void sandData() {
		// TODO Auto-generated method stub
		if (!(suggestET.getText() + "").equals("")) {
			this.doAsync("数据提交中，请稍候...", new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					try {
						return new QwcApiImpl().addSuggestion(
								(Constants.mAccount.getMobile() != null ? Constants.mAccount
										.getMobile() : Constants.mAccount
										.getUserId()), suggestET.getText() + "");
					} catch (WSError e) {
						e.printStackTrace();
					}
					return null;
				}
			}, new Callback<Boolean>() {

				@Override
				public void onCallback(Boolean pCallbackValue) {
					if (pCallbackValue) {
						showToast("提交成功！");
						suggestET.setText("");
					} else {
						showToast("提交失败！");
					}
				}
			});
		} else {
			showToast("建议不能为空！");
		}
	}
}
