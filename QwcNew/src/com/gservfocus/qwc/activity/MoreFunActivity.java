package com.gservfocus.qwc.activity;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class MoreFunActivity extends BaseActivity {

	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morefun);
		((TextView) findViewById(R.id.ver)).setText(getVersionCode()+".0.0");
		((TextView) findViewById(R.id.logout)).setOnClickListener(this);
		((TextView) findViewById(R.id.about)).setOnClickListener(this);
		((TextView) findViewById(R.id.Question_Survey)).setOnClickListener(this);
		((TextView) findViewById(R.id.title)).setText("更多");
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);

	}

	public int getVersionCode() {
		int verCode = -1;
		String verPack = "";
		try {
			verPack = this.getPackageName();
			verCode = this.getPackageManager().getPackageInfo(verPack, 0).versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
		return verCode;
	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.alert_dialog_logout,
				null);
		((Button) popupWindow.findViewById(R.id.logout_sure))
				.setOnClickListener(this);
		((Button) popupWindow.findViewById(R.id.logout_cancel))
				.setOnClickListener(this);
		// 创建一个PopupWindow
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		mPopupWindow = new PopupWindow(popupWindow,
				WindowManager.LayoutParams.MATCH_PARENT, Constants.dip2px(this,
						210));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.logout:
			initPopuptWindow();
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.logout_sure:
			QwcApplication.getInstance().clearAccount();
			Constants.mAccount = null;
			sendBroadcast(new Intent(MainActivity.MY_PROFILE_ACTION1));
			startActivity(new Intent(MoreFunActivity.this, MainActivity.class));
			break;
		case R.id.logout_cancel:
			mPopupWindow.dismiss();
			break;
		case R.id.about:
			startActivity(new Intent(MoreFunActivity.this,
					AboutQwcActivity.class));
			break;
		case R.id.Question_Survey:
			startActivity(new Intent(MoreFunActivity.this,
					QuestionSurveyActivity.class));
			break;
		}
	}

	/*
	 * public void showDialog(Context context) {
	 * 
	 * LayoutInflater inflater = LayoutInflater.from(this); final View v =
	 * inflater.inflate(R.layout.dialog_logout, null); ((Button)
	 * v.findViewById(R.id.button1)) .setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub QwcApplication.getInstance().clearAccount(); Constants.mAccount =
	 * null; sendBroadcast(new Intent( MainActivity.MY_PROFILE_ACTION1));
	 * startActivity(new Intent(MoreFunActivity.this, MainActivity.class)); }
	 * }); ((Button) v.findViewById(R.id.button2)) .setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub finish(); } }); AlertDialog.Builder builder = new
	 * AlertDialog.Builder(context); builder.setView(v);
	 * builder.setCancelable(true); builder.show(); }
	 */

}
