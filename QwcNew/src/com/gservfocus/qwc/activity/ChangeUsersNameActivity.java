package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class ChangeUsersNameActivity extends BaseActivity {
	private EditText newUserName;
	private TextView changeUserName, title;
	private ImageView imagereturn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeusersname);
		newUserName = (EditText) findViewById(R.id.newUserName);
		changeUserName = (TextView) findViewById(R.id.changeUserName);
		imagereturn1 = (ImageView) findViewById(R.id.imagereturn1);
		title = (TextView) findViewById(R.id.title);
		title.setText("�޸��û���");
		imagereturn1.setOnClickListener(this);
		changeUserName.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.changeUserName:
			if (newUserName.length() >= 1) {
				ChangeUsersName(Constants.mAccount.getMobile(), newUserName
						.getText().toString());
			} else {
				showToast("���û�������Ϊ�գ�");
			}
			break;
		case R.id.imagereturn1:
			finish();
			break;
		}
	}

	private void ChangeUsersName(final String mobile, final String newName) {
		// TODO Auto-generated method stub
		doAsync("�����ύ�У����Ժ�...", new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				try {
					return new QwcApiImpl().changeUsersName(mobile, newName);
				} catch (WSError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean pCallbackValue) {
				// TODO Auto-generated method stub
				if (pCallbackValue) {
					Constants.mAccount.setAccount(newName);
					QwcApplication.getInstance()
							.saveAccount(Constants.mAccount);

					showToast("�û����޸ĳɹ���");
					sendBroadcast(new Intent(BindMobileActivity.UPDATE_MOBILE));
					sendBroadcast(new Intent(MoreFragment.PHOTO_UPDATE_ACTION));
					finish();
				} else {
					showToast("�û����޸�ʧ�ܣ�");
				}
			}
		});
	}

}
