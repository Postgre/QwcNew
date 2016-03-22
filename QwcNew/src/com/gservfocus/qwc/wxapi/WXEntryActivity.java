package com.gservfocus.qwc.wxapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.activity.DetailScenicIntroActivity;
import com.gservfocus.qwc.activity.DetailSpecialtyActivity;
import com.gservfocus.qwc.activity.LoginActivity;
import com.gservfocus.qwc.activity.fragment.MoreFragment;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private IWXAPI api;
	Intent intent;
	String Openid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		api = WXAPIFactory.createWXAPI(this, Constants.WXappID, false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	private void MyXline(final String code) {
		// TODO Auto-generated method stub
		this.doAsync("正在获取信息，请稍候...", new Callable<ArrayList<String>>() {

			@Override
			public ArrayList<String> call() throws Exception {
				ArrayList<String> codeArry = httpGetAccount(code);
				return getUserInfo(codeArry.get(0), codeArry.get(1));
			}
		}, new Callback<ArrayList<String>>() {

			@Override
			public void onCallback(ArrayList<String> pCallbackValue) {
				Log.i("rzh", "name=" + pCallbackValue.get(0) + "image="
						+ pCallbackValue.get(1));
				Constants.OpenId = Openid;
				Constants.NickName = pCallbackValue.get(0);
				Constants.UserImage = pCallbackValue.get(1);
				intent = new Intent();
				intent.setAction(LoginActivity.WXLogin);
				sendBroadcast(intent);
				finish();

			}
		});
	}

	private ArrayList<String> getUserInfo(String openid, String access_token) {
		// TODO Auto-generated method stub
		// 先将参数放入List，再对参数进行URL编码
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("openid", openid));
		Openid = openid;
		// 对参数编码
		String param = URLEncodedUtils.format(params, "UTF-8");
		// baseUrl
		String baseUrl = "https://api.weixin.qq.com/sns/userinfo";

		// 将URL与参数拼接
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);

		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

			String res = EntityUtils.toString(response.getEntity(), "utf-8");// 获取服务器响应内容
			Log.i("rzh", "openid=" + openid + "access_token=" + access_token);
			Log.i("rzh", res);
			JSONObject dataJson;
			ArrayList<String> list = new ArrayList<String>();
			try {
				dataJson = new JSONObject(res);
				list.add(dataJson.getString("nickname"));
				list.add(dataJson.getString("headimgurl"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private ArrayList<String> httpGetAccount(String code) {
		// TODO Auto-generated method stub
		// 先将参数放入List，再对参数进行URL编码
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Constants.WXappID));
		params.add(new BasicNameValuePair("secret", Constants.WXSecret));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));

		// 对参数编码
		String param = URLEncodedUtils.format(params, "UTF-8");
		// baseUrl
		String baseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

		// 将URL与参数拼接
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);

		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

			String res = EntityUtils.toString(response.getEntity(), "utf-8");// 获取服务器响应内容
			Log.i("rzh", res);
			Constants.isWxLogin = 0;
			JSONObject dataJson;
			ArrayList<String> list = new ArrayList<String>();
			try {
				dataJson = new JSONObject(res);
				list.add(dataJson.getString("openid"));
				list.add(dataJson.getString("access_token"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void sendBc(int a) {
		// TODO Auto-generated method stub
		intent = new Intent();
		intent.setAction(MoreFragment.PHOTO_UPDATE_ACTION);
		sendBroadcast(intent);
		if (a == 1) {
			intent.setAction(DetailScenicIntroActivity.UPDATESHARENUM);
			intent.putExtra("ss", "sce");
			sendBroadcast(intent);
		} else if (a == 2) {
			intent.setAction(DetailSpecialtyActivity.UPDATESHARENUM_SPE);
			intent.putExtra("ss", "sce");
			sendBroadcast(intent);
		}
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		// 分享成功
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:

			if (Constants.mAccount != null
					&& Constants.isShareWhat.equals("isShareScenic")) {
				this.doAsync("数据提交中，请稍后...", new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						try {
							return new QwcApiImpl().shareScenicRecord(
									(Constants.mAccount.getMobile() != "" ? Constants.mAccount
											.getMobile() : Constants.mAccount
											.getUserId()),
									Constants.isShareWhatID);
						} catch (WSError e) {
							e.printStackTrace();
						}
						return null;
					}
				}, new Callback<Boolean>() {

					@Override
					public void onCallback(Boolean pCallbackValue) {
						if (pCallbackValue) {
							showKwayiToast("分享成功", 10);
							Constants.mAccount.setIntegral((Integer
									.parseInt(Constants.mAccount.getIntegral()) + 10)
									+ "");
							QwcApplication.getInstance().saveAccount(
									Constants.mAccount);
							sendBc(1);
							finish();
						} else {
							showKwayiToast("分享成功", 0);
							finish();
						}
					}
				});
			} else if (Constants.mAccount != null
					&& Constants.isShareWhat.equals("isShareSpe")) {
				this.doAsync("数据提交中，请稍后...", new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						try {
							return new QwcApiImpl().shareSpeRecord(
									(Constants.mAccount.getMobile() != "" ? Constants.mAccount
											.getMobile() : Constants.mAccount
											.getUserId()),
									Constants.isShareWhatID);
						} catch (WSError e) {
							e.printStackTrace();
						}
						return null;
					}
				}, new Callback<Boolean>() {

					@Override
					public void onCallback(Boolean pCallbackValue) {
						if (pCallbackValue) {
							showKwayiToast("分享成功", 10);
							Constants.mAccount.setIntegral((Integer
									.parseInt(Constants.mAccount.getIntegral()) + 10)
									+ "");
							QwcApplication.getInstance().saveAccount(
									Constants.mAccount);
							sendBc(2);
							finish();
						} else {
							showKwayiToast("分享成功", 0);
							finish();
						}
					}
				});
			}/*
			 * else if(Constants.mAccount != null &&
			 * Constants.isShareWhat.equals("isShareAbout")){ showToast("分享成功");
			 * finish(); }
			 */else if (Constants.mAccount == null && Constants.isWxLogin == 0) {
				showToast("分享成功，登录后分享还能加积分哦~~");
				finish();
			} else if (Constants.mAccount == null && Constants.isWxLogin == 1) {
				String code = ((SendAuth.Resp) resp).code;
				//showToast("code=" + code);
				Log.i("rzh", code);
				MyXline(code);
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 分享取消
			showToast("分享取消");
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 分享拒绝
			showToast("分享拒绝");
			finish();
			break;
		}

	}

}
