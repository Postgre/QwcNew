package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class ChangePwdActivity extends BaseActivity {

	TextView changePwd;
	EditText nowPwd, newPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepwd);
		changePwd = (TextView) findViewById(R.id.changePwd);
		nowPwd = (EditText) findViewById(R.id.nowPwd);
		newPwd = (EditText) findViewById(R.id.newPwd);
		((TextView) findViewById(R.id.title)).setText("修改密码");
		((ImageView)findViewById(R.id.imagereturn1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		changePwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!(nowPwd.getText().toString()).equals(Constants.mAccount
						.getPassword())) {
					showToast("你输入的密码不正确");
					nowPwd.setError("请输入正确密码");
				} else {
					beginChange();
				}
			}
		});
	}

	public void beginChange() {
		this.doAsync("数据提交中，请稍后...", new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				try {
					return new QwcApiImpl().changePwd(Constants.mAccount
							.getMobile(), newPwd.getText().toString(), nowPwd
							.getText().toString());
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean pCallbackValue) {
				if (pCallbackValue) {
					showToast("密码修改成功");
					Constants.mAccount.setPassword(newPwd.getText().toString());
					QwcApplication.getInstance()
							.saveAccount(Constants.mAccount);
					finish();
				} else {
					showToast("密码修改失败");
				}
			}
		});
	}

}
