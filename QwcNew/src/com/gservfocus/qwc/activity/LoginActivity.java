package com.gservfocus.qwc.activity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class LoginActivity extends BaseActivity {

	private boolean flag = true;
	private ViewSwitcher profileSwitcher;
	private EditText phonenum, pwd;
	private ImageView QQ_login, WX_login;
	private IWXAPI api;
	public static String WXLogin = "WX_LOGIN";
	MyBroadcastReceiver loginB = new MyBroadcastReceiver();
	UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.login");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WXLogin);
		registerReceiver(loginB, filter);
		String appId = "1103195825";
		String appKey = "JoZpZieCxpDwO47c";

		String WXappID = "wx8ef4eb6be6b68675";
		String WXSecret = "bd4d1d98d62868623ed4c164b00b6b98";
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this,
				appId, appKey);
		qqSsoHandler.addToSocialSDK();
		UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this, WXappID,
				WXSecret);
		wxHandler.addToSocialSDK();
		api = WXAPIFactory.createWXAPI(this, WXappID, true);
		api.registerApp(WXappID);
		((TextView) findViewById(R.id.title)).setText("账户");
		((TextView) findViewById(R.id.register)).setOnClickListener(this);
		((TextView) findViewById(R.id.forgetpwd)).setOnClickListener(this);
		((ImageView) findViewById(R.id.imagereturn1)).setOnClickListener(this);
		((TextView) findViewById(R.id.login)).setOnClickListener(this);
		QQ_login = (ImageView) findViewById(R.id.QQ_login);
		WX_login = (ImageView) findViewById(R.id.WX_login);
		QQ_login.setOnClickListener(this);
		WX_login.setOnClickListener(this);
		profileSwitcher = ((ViewSwitcher) findViewById(R.id.profileSwitcher));
		pwd = (EditText) findViewById(R.id.password);
		phonenum = (EditText) findViewById(R.id.phonenum);
		profileSwitcher.setOnClickListener(this);
	}

	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(loginB);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.forgetpwd:
			startActivity(new Intent(LoginActivity.this,
					ForgetPwdActivity.class));
			break;
		case R.id.imagereturn1:
			finish();
			break;
		case R.id.login:
			login(phonenum.getText().toString(), pwd.getText().toString());
			break;
		case R.id.profileSwitcher:
			if (flag) {
				profileSwitcher.showPrevious();
				pwd.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
				flag = false;
				break;
			} else {
				profileSwitcher.showNext();
				// 设置密码为隐藏的
				pwd.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
				flag = true;
				break;
			}
		case R.id.QQ_login:
			QQAndWXLogin(SHARE_MEDIA.QQ);
			break;
		case R.id.WX_login:
			Constants.isWxLogin = 1;
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test";
			api.sendReq(req);
			// QQAndWXLogin(SHARE_MEDIA.WEIXIN);
			break;
		}

	}

	private void QQAndWXLogin(final SHARE_MEDIA platform) {
		// TODO Auto-generated method stub
		mController.doOauthVerify(LoginActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						showToast("授权开始");
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						showToast("授权失败");
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// 获取uid
						String uid = value.getString("uid");
						if (!TextUtils.isEmpty(uid)) {
							// uid不为空，获取用户信息
							getUserInfo(platform);
						} else {
							showToast("授权失败...");
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						showToast("授权取消");
					}
				});
	}

	/**
	 * 获取用户信息
	 * 
	 * @param platform
	 */
	private void getUserInfo(final SHARE_MEDIA platform) {
		mController.getPlatformInfo(LoginActivity.this, platform,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {

						if (info != null) {
							if (platform == SHARE_MEDIA.QQ) {
								loginForOther(info.get("uid").toString(), info
										.get("screen_name").toString(), info
										.get("profile_image_url").toString());
							} else if (platform == SHARE_MEDIA.WEIXIN) {
								showToast(info.toString() + "微信信息");
							}
						}
					}
				});
	}

	private void loginForOther(final String Uid, final String UserName,
			final String imageUrl) {

		this.doAsync("用户登录中...", new Callable<Account>() {

			@Override
			public Account call() throws Exception {
				try {
					return new QwcApiImpl().verifyForOther(Uid, UserName);
				} catch (WSError e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new Callback<Account>() {

			@Override
			public void onCallback(Account pCallbackValue) {
				if (pCallbackValue != null) {
					pCallbackValue.setImageUrl(imageUrl);
					QwcApplication.getInstance().saveAccount(pCallbackValue);
					Constants.mAccount = pCallbackValue;
					showToast("用户登录成功");
					sendBroadcast(new Intent(MoreFragment.PHOTO_UPDATE_ACTION));
					finish();
				} else {
					showToast("用户登录失败");
				}
			}
		});
	}

	private void login(final String mobile, final String password) {

		if (isMobileNO(mobile)) {
			this.doAsync("用户登录中...", new Callable<Account>() {

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
						showToast("用户登录成功");
						sendBroadcast(new Intent(
								MoreFragment.PHOTO_UPDATE_ACTION));
						finish();
					} else {
						showToast("请输入正确账号密码...");
					}
				}
			});
		} else {
			showToast("请输入正确手机号");
			phonenum.setError("请输入正确手机号");
		}
	}

	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			loginForOther(Constants.OpenId, Constants.NickName,
					Constants.UserImage);
		}

	}

	// 如果有使用任一平台的SSO授权, 则必须在对应的activity中实现onActivityResult方法, 并添加如下代码
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				resultCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
