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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class RegisterActivity extends BaseActivity {

	private boolean flag = true;
	private ViewSwitcher profileSwitcher;
	private EditText password, phonenum, checknum;
	private LinearLayout text_toMobile;
	private FrameLayout frame_password;
	private TextView getCheckNum, next, register, toMobile, sendagain,
			sendwait;
	private String mMobile;
	private String mCodeNum;
	private int recLen = 60;
	Timer timer = new Timer();

	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					sendwait.setText("���·���("+recLen+")");
					if (recLen < 0 && next.getVisibility() == View.GONE) {
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
		setContentView(R.layout.register);
		
		((TextView) findViewById(R.id.title)).setText("ע��");
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		text_toMobile = (LinearLayout) findViewById(R.id.text_toMobile);
		toMobile = (TextView) findViewById(R.id.toMobile);
		frame_password = (FrameLayout) findViewById(R.id.frame_password);
		profileSwitcher = ((ViewSwitcher) findViewById(R.id.profileSwitcher));
		getCheckNum = (TextView) findViewById(R.id.getCheckNum);
		sendagain = (TextView) findViewById(R.id.sendagain);
		sendwait = (TextView) findViewById(R.id.sendwait);
		next = (TextView) findViewById(R.id.next);
		register = (TextView) findViewById(R.id.register);
		password = (EditText) findViewById(R.id.password);
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
					next.setVisibility(View.VISIBLE);
				} else {
					sendwait.setVisibility(View.VISIBLE);
					next.setVisibility(View.GONE);
				}
			}
		});
		profileSwitcher.setOnClickListener(this);
		getCheckNum.setOnClickListener(this);
		next.setOnClickListener(this);
		register.setOnClickListener(this);
		sendagain.setOnClickListener(this);
		sendwait.setOnClickListener(this);
	}

	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
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
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.profileSwitcher:
			if (flag) {
				profileSwitcher.showPrevious();
				password.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
				flag = false;
				break;
			} else {
				profileSwitcher.showNext();
				// ��������Ϊ���ص�
				password.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				flag = true;
				break;
			}
		case R.id.getCheckNum:
			mMobile = phonenum.getText().toString();
			getCheckNum(mMobile);
			break;
		case R.id.next:
			mCodeNum = checknum.getText().toString();
			checkCodeIsTrue(mMobile, mCodeNum);
			break;
		case R.id.register:
			register(mMobile, password.getText().toString());
			break;
		case R.id.sendagain:
			getCheckNum(mMobile);
			break;
		}

	}

	private void getCheckNum(final String mobile) {
		// TODO Auto-generated method stub
		if (isMobileNO(mobile)) {
			this.doAsync("�����ύ��...", new Callable<Integer>() {

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
						showToast("��Ϣ�ѷ��ͳɹ�");
						phonenum.setVisibility(View.GONE);
						getCheckNum.setVisibility(View.GONE);
						text_toMobile.setVisibility(View.VISIBLE);
						toMobile.setText("���͵�  " + mMobile);
						checknum.setVisibility(View.VISIBLE);
						sendwait.setVisibility(View.VISIBLE);
						timer.schedule(task, 1000, 1000); // timeTask
						break;
					default:
						showToast("��Ϣ����ʧ��");
						break;
					}
				}
			});
		} else {
			showToast("��������ȷ���ֻ���");
		}
	}

	private void checkCodeIsTrue(final String moblie, final String codeNum) {
		// TODO Auto-generated method stub
		this.doAsync("�����ύ��...", new Callable<Integer>() {

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
					showToast("��֤�ɹ�");
					text_toMobile.setVisibility(View.GONE);
					checknum.setVisibility(View.GONE);
					next.setVisibility(View.GONE);
					frame_password.setVisibility(View.VISIBLE);
					register.setVisibility(View.VISIBLE);
					break;
				default:
					showToast("��֤ʧ��");
					break;
				}
			}
		});
	}

	public void register(final String mobile, final String pwd) {

		this.doAsync("�����ύ��...", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				try {
					return new QwcApiImpl().RegisterNew(mobile, pwd);
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
					showToast("ע��ɹ���");
					login(phonenum.getText().toString(), password.getText()
							.toString());
					finish();
					break;
				case 1:
					showToast("�ֻ����Ѵ��ڣ�");
					break;
				case 2:
					showToast("�û����Ѵ��ڣ�");
					break;
				case 3:
					showToast("�����ַ�Ѵ��ڣ�");
					break;
				default:
					showToast("ע��ʧ�ܣ�");
					break;
				}
			}
		});
	}

	private void login(final String mobile, final String password) {

		if (isMobileNO(mobile)) {
			this.doAsync("�û���½��...", new Callable<Account>() {

				@Override
				public Account call() throws Exception {
					try {
						return new QwcApiImpl().verify(mobile, password);
					} catch (WSError e) {
						e.printStackTrace();
					}
					return null;
				}
			}, new Callback<Account>() {

				@Override
				public void onCallback(Account pCallbackValue) {
					if (pCallbackValue != null) {
						pCallbackValue.setPassword(password);
						QwcApplication.getInstance()
								.saveAccount(pCallbackValue);
						Constants.mAccount = pCallbackValue;
						showToast("�û���½�ɹ�");
						sendBroadcast(new Intent(
								MoreFragment.PHOTO_UPDATE_ACTION));

						startActivity(new Intent(RegisterActivity.this,
								MainActivity.class));
					} else {
						showToast("��������ȷ�˺�����...");
					}
				}
			});
		} else {
			showToast("��������ȷ�ֻ���");
			phonenum.setError("��������ȷ�ֻ���");
		}
	}

}
