package com.gservfocus.qwc.activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class BindMobileActivity extends BaseActivity {

	private EditText phonenum, checknum;
	private LinearLayout text_toMobile;
	private TextView getCheckNum, toMobile, sendagain, complete, sendwait;
	private String mMobile;
	private String mCodeNum;
	private int recLen = 60;
	Timer timer = new Timer();
	public static final String UPDATE_MOBILE = "update_mobile";

	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					sendwait.setText("重新发送(" + recLen + ")");
					if (recLen < 0 && complete.getVisibility() == View.GONE) {
						timer.cancel();
						sendwait.setVisibility(View.GONE);
						sendagain.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindmobile);

		((TextView) findViewById(R.id.title)).setText("重新绑定手机号");
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		text_toMobile = (LinearLayout) findViewById(R.id.text_toMobile);
		toMobile = (TextView) findViewById(R.id.toMobile);
		getCheckNum = (TextView) findViewById(R.id.getCheckNum);
		sendagain = (TextView) findViewById(R.id.sendagain);
		sendwait = (TextView) findViewById(R.id.sendwait);
		complete = (TextView) findViewById(R.id.complete);
		phonenum = (EditText) findViewById(R.id.phonenum);
		checknum = (EditText) findViewById(R.id.checknum);
		checknum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if ((checknum.getText().toString()).length() == 6) {
					sendwait.setVisibility(View.GONE);
					complete.setVisibility(View.VISIBLE);
				} else {
					sendwait.setVisibility(View.VISIBLE);
					complete.setVisibility(View.GONE);
				}
			}
		});
		getCheckNum.setOnClickListener(this);
		complete.setOnClickListener(this);
		sendagain.setOnClickListener(this);
		sendwait.setOnClickListener(this);
	}

	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 判断email格式是否正确
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
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.getCheckNum:
			mMobile = phonenum.getText().toString();
			getCheckNum(mMobile);
			break;
		case R.id.complete:
			mCodeNum = checknum.getText().toString();
			begainBind(Constants.mAccount.getUserId(), mMobile, mCodeNum);
			break;
		case R.id.sendagain:
			getCheckNum(mMobile);
			break;
		}

	}

	private void getCheckNum(final String mobile) {
		// TODO Auto-generated method stub
		if (isMobileNO(mobile)) {
			this.doAsync("数据提交中...", new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					try {
						return new QwcApiImpl().CreateCheckCode(mobile);
					} catch (WSError e) {
						e.printStackTrace();
					}
					return null;
				}
			}, new Callback<Integer>() {

				@Override
				public void onCallback(Integer pCallbackValue) {
					switch (pCallbackValue) {
					case 0:
						showToast("消息已发送成功");
						phonenum.setVisibility(View.GONE);
						getCheckNum.setVisibility(View.GONE);
						text_toMobile.setVisibility(View.VISIBLE);
						toMobile.setText("发送到  " + mMobile);
						checknum.setVisibility(View.VISIBLE);
						sendwait.setVisibility(View.VISIBLE);
						timer.schedule(task, 1000, 1000); // timeTask
						break;
					default:
						showToast("消息发送失败");
						break;
					}
				}
			});
		} else {
			showToast("请输入正确的手机号");
		}
	}

	private void checkCodeIsTrue(final String moblie, final String codeNum) {
		// TODO Auto-generated method stub
		this.doAsync("数据提交中...", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				try {
					return new QwcApiImpl().CheckIsTrue(moblie, codeNum);
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Integer>() {

			@Override
			public void onCallback(Integer pCallbackValue) {
				switch (pCallbackValue) {
				case 0:
					showToast("验证成功");
					text_toMobile.setVisibility(View.GONE);
					checknum.setVisibility(View.GONE);
					complete.setVisibility(View.GONE);
					break;
				default:
					showToast("验证失败");
					break;
				}
			}
		});
	}

	public void begainBind(final String UID, final String mobile,
			final String codeNum) {

		this.doAsync("数据提交中...", new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				try {
					return new QwcApiImpl().BindMobile(UID, mobile, codeNum);
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean pCallbackValue) {
				if (pCallbackValue) {
					showToast("绑定成功！");
					Constants.mAccount.setMobile(mMobile);
					QwcApplication.getInstance().saveAccount(Constants.mAccount);
					sendBroadcast(new Intent(UPDATE_MOBILE));
					finish();
				} else {
					showToast("绑定失败！");
				}
			}
		});
	}
}
