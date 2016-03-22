package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class FindPwdActivity extends BaseActivity {

	EditText mailbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findpwd);
		((TextView) findViewById(R.id.title)).setText("�һ�����");
		mailbox = (EditText) findViewById(R.id.mailbox);
		((TextView) findViewById(R.id.findpwd)).setOnClickListener(this);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
	}

	// �ж�email��ʽ�Ƿ���ȷ
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.findpwd:
			findPwd(mailbox.getText().toString());
			break;
		case R.id.imagereturn1:
			finish();
			break;
		}

	}

	public void findPwd(final String email) {
		if (isEmail(email)) {
			this.doAsync("�����ύ��...", new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					try {
						return new QwcApiImpl().findPwd(email);
					} catch (WSError e) {
						e.printStackTrace();
					}
					return null;
				}
			}, new Callback<Integer>() {

				@Override
				public void onCallback(Integer pCallbackValue) {
					if (pCallbackValue == 0) {
						showToast("�����ѷ��͵��������䣬��ע�����");
						finish();
					} else {
						showToast("�һ�ʧ�ܣ�");
					}
				}
			});
		} else {
			showToast("��������ȷ�������ַ");
			mailbox.setError("��������ȷ�������ַ");
		}
	}
}
