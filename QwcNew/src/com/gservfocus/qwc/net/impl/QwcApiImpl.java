package com.gservfocus.qwc.net.impl;

import java.util.ArrayList;

import org.dom4j.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.text.TextUtils;
import android.util.Log;
import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.AirQuality;
import com.gservfocus.qwc.bean.ChildScenic;
import com.gservfocus.qwc.bean.Comment;
import com.gservfocus.qwc.bean.Message;
import com.gservfocus.qwc.bean.NaviMapObject;
import com.gservfocus.qwc.bean.Path;
import com.gservfocus.qwc.bean.RecommendRoute;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.bean.Specialty;
import com.gservfocus.qwc.bean.VersionInfo;
import com.gservfocus.qwc.net.QwcApi;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.util.Caller;
import com.gservfocus.qwc.net.util.WebServicesUtil;
import com.gservfocus.qwc.net.util.XMLUtil;

public class QwcApiImpl implements QwcApi {
	private static String POST_VERSION_API = "http://appios.gservfocus.com/common/mobile1.aspx";
	private static final String NAMESPACE = "http://qwc.gservfocus.com";
	private static final String DOWNLOAD_URL = "http://qwcweb.gservfocus.com/Download/";
	// 登录
	private static final String USER_URL = NAMESPACE
			+ "/StorageInfoServices.asmx";
	private static final String USER_ENTER_METHOD = "Login"; // 登录
	private static final String USER_REGISTER_METHOD = "Register"; // 注册
	private static final String USER_FINDPWD_METHOD = "FindPassword"; // 找回密码
	private static final String USER_COMMENTS = "Comment"; // 用户评论
	private static final String SURVEYANSWER = "SurveyAnswer"; // 调查问卷，code:72768181-647E-41F1-826A-16151BDAAA80
	private static final String USER_IMAGE_UPLOAD = "HeadPictureUpload"; // 头像上传，code:1573b98b-dc0d-497d-b5bc-a90233c63735
	private static final String GETUSERIMAGE_URL = "http://qwc.gservfocus.com/DownLoad/";
	// 景点
	private static final String SCENIC_URL = NAMESPACE
			+ "/GetInfoServices.asmx";
	private static final String AIR_QUALITY_METHOD = "GetAirqualityInfo"; // 获取PM2.5
	private static final String SCENIC_LIST_METHOD = "GetAllScenic"; // 获取景点列表
	private static final String HOTSCENIC_LIST_METHOD = "GetHotScenic"; // 获取热门景点列表
	private static final String USERSCENIC_LIST_METHOD = "UserScenic"; // 获取收藏景点列表
	private static final String USERAGRICOLA_LIST_METHOD = "UserAgr"; // 获取收藏农家乐列表
	private static final String USERSPECIALTY_LIST_METHOD = "UserSpe"; // 获取收藏农家乐列表
	private static final String SCENIC_METHOD = "GetScenicById"; // 获取指定景点
	private static final String SCENIC_QR_METHOD = "GetScenicByQr"; // 获取指定景点通过QR
	private static final String AGRICOLA_LIST_METHOD = "GetAllAgricola"; // 获取农家乐列表
	private static final String AGRICOLA_METHOD = "GetAgricolaById"; // 获取指定农家乐
	private static final String SPECIALTY_LIST_METHOD = "GetAllSpecialty"; // 获取农特产列表,code:7f978606-80c6-9496-001d-6c76f1803523
	private static final String SPECIALTY_METHOD = "GetSpecialtyById"; // 获取农特产详情,code:0d93f2d5-abb2-4ac5-9d39-3cfef06eec2c
	private static final String MESSAGE_LIST_METHOD = "GetAllMessage"; // 获取农特产列表,code:b02f88f6-2002-956d-8aa8-620873172bf4
	private static final String MESSAGE_METHOD = "GetMessageById"; // 获取农特产详情,code:246deeb9-2478-6e1b-23fb-2deb805b343c
	private static final String SCENIC_ISFLAG = "GetScencParentByMobile";// 获取父景点浏览记录
	private static final String RECOMMEND_LINE_LIST_METHOD = "GetRecommend"; // 获取推荐线路列表,65cdcb7e-7b70-948e-4083-9f09ed65224e
	private static final String RECOMMEND_LINE_METHOD = "GetRecommendInfo"; // 获取指定推荐线路,a2f10c5d-ad99-1bde-f6dc-b1571a689a85
	private static final String SCENIC_LIKE = "LikeScenicRecord";
	private static final String AGR_LIKE = "LikeAgrRecord";
	private static final String SPE_LIKE = "LikeSpeRecord";
	private static final String SCENIC_SHARE = "ShareScenicRecord";
	private static final String SPR_SHARE = "ShareSpeRecord";
	private static final String FINDPWD = "FindPasswordNew";
	private static final String CHANGEPWD = "ChangePassword";
	private static final String ADD_SUGGEST = "AddSuggestion";
	private static final String LOG_FOR_OTHER = "LoginForOther";
	private static final String GET_CHECKCODE = "CreateCheckCode";
	private static final String CHECKCODE_ISTRUE = "CheckIsTrue";
	private static final String REGISTER_NEW = "RegisterNew";
	private static final String BIND_MOBILE = "BindMobile";
	private static final String CHANGE_USER_NAME = "ChangeUserName";
	private static final String FRONT_URL = "http://qwcwebd.gservfocus.com/Download/";

	
	
	/**
	 * verify
	 * 
	 * @return 1 登录失败，0 登录成功
	 * */
	@Override
	public Account verify(String mobile, String password) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USER_ENTER_METHOD);
		// 设置参数
		rpc.addProperty("code", "a39d930a-c41d-019e-2f21-aebe7749da0f");
		rpc.addProperty("moblie", mobile);
		rpc.addProperty("psd", password);
		rpc.addProperty("status", "2");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		Account account = null;
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject = null;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONObject dataObject = rootJsonObject.getJSONArray("data")
							.getJSONObject(0);
					if (dataObject != null) {
						account = new Account();
						account.setAccount(dataObject.getString("name"));
						account.setMobile(dataObject.getString("account"));
						account.setIntegral(dataObject.getString("integral"));
						account.setRankName(dataObject.getString("rankName"));
						account.setImageUrl(GETUSERIMAGE_URL
								+ dataObject.getString("ImageUrl"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return account;
	}

	/**
	 * verifyforother
	 * 
	 * @return 1 登录失败，0 登录成功
	 * */
	@Override
	public Account verifyForOther(String Uid, String UserName) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, LOG_FOR_OTHER);
		// 设置参数
		rpc.addProperty("code", "e728d1eb-c271-4e80-8fa0-370aab92ebac");
		rpc.addProperty("Uid", Uid);
		rpc.addProperty("UserName", UserName);
		rpc.addProperty("status", "2");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		Account account = null;
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject = null;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONObject dataObject = rootJsonObject.getJSONArray("data")
							.getJSONObject(0);
					if (dataObject != null) {
						account = new Account();
						account.setUserId(dataObject.getString("Uid"));
						account.setAccount(dataObject.getString("name"));
						account.setMobile(dataObject.getString("account") != "null" ? dataObject
								.getString("account") : "");
						account.setIntegral(dataObject.getString("integral"));
						account.setRankName(dataObject.getString("rankName"));
						account.setImageUrl(GETUSERIMAGE_URL
								+ dataObject.getString("ImageUrl"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return account;
	}

	/**
	 * 获取验证码
	 * 
	 * @return 1 失败，0 成功
	 **/
	public Integer CreateCheckCode(String mobile) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, GET_CHECKCODE);
		// 设置参数
		rpc.addProperty("code", "aeddf535-0b33-4454-b1e7-e7534adecd83");
		rpc.addProperty("mobile", mobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		try {
			return Integer
					.valueOf(new JSONObject(response).getString("result"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * 效验验证码是否正确
	 * 
	 * @return 1失败，0成功
	 * 
	 * **/
	public Integer CheckIsTrue(String mobile, String codeNum) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, CHECKCODE_ISTRUE);
		// 设置参数
		rpc.addProperty("code", "d4a31a70-46ae-4ef8-94ef-1f7749fe7b4c");
		rpc.addProperty("mobile", mobile);
		rpc.addProperty("codeNum", codeNum);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		try {
			return Integer
					.valueOf(new JSONObject(response).getString("result"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * register
	 * 
	 * @return 1 注册失败，0 注册成功，-1 网络连接错误
	 * */
	@Override
	public Integer register(String account, String mobile, String email,
			String password) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USER_REGISTER_METHOD);
		// 设置参数
		rpc.addProperty("code", "28bef359-bd42-474c-06a7-254563031b0d");
		rpc.addProperty("info", account + "|" + mobile + "|" + password + "|"
				+ email);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		try {
			return Integer
					.valueOf(new JSONObject(response).getString("result"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}

	}

	/**
	 * 注册（新）
	 * 
	 * @return 1 注册失败，0 注册成功，-1 网络连接错误
	 * **/
	@Override
	public Integer RegisterNew(String mobile, String NewPass) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, REGISTER_NEW);
		// 设置参数
		rpc.addProperty("code", "28bef359-bd42-474c-06a7-254563031b0d");
		rpc.addProperty("mobile", mobile);
		rpc.addProperty("password", NewPass);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		try {
			return Integer
					.valueOf(new JSONObject(response).getString("result"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}

	}

	/**
	 * findPwd
	 * 
	 * @return 1 找回失败，0 找回成功
	 * */
	public Integer findPwd(String email) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USER_FINDPWD_METHOD);
		// 设置参数
		rpc.addProperty("code", "b8a78c6a-7ea5-e040-1aa0-960b410b421f");
		rpc.addProperty("email", email);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		System.out.println("register string:" + response);
		try {
			return Integer
					.valueOf(new JSONObject(response).getString("result"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 1;
		} catch (JSONException e) {
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	public ArrayList<Scenic> getAllScenicList() throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, SCENIC_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "da922a22-fd05-26d5-d62d-cec2cfdf8931");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		ArrayList<Scenic> scenics = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						scenics = new ArrayList<Scenic>();
						Scenic scenic = null;
						for (int i = 0; i < dataArray.length(); i++) {
							scenic = new Scenic();
							scenic.setId(dataArray.getJSONObject(i).getString(
									"id"));
							scenic.setName(dataArray.getJSONObject(i)
									.getString("name"));
							scenic.setImageUrl(FRONT_URL
									+ dataArray.getJSONObject(i).getString(
											"imageUrl"));
							scenic.setAbstracts(dataArray.getJSONObject(i)
									.getString("abstracts"));
							scenic.setShareNum(dataArray.getJSONObject(i)
									.getString("ShareNum"));
							scenic.setLikeNum(dataArray.getJSONObject(i)
									.getString("LikeNum"));

							scenics.add(scenic);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return scenics;
	}

	@Override
	public ArrayList<Scenic> getUserScenicList(String UserMobile)
			throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USERSCENIC_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "0a8609a7-ea23-4e9d-937a-4be0bb67b9e4");
		rpc.addProperty("UserMobile", UserMobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		Log.i("rzh", response);
		// init object
		ArrayList<Scenic> scenics = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						scenics = new ArrayList<Scenic>();
						Scenic scenic = null;
						for (int i = 0; i < dataArray.length(); i++) {
							scenic = new Scenic();
							scenic.setId(dataArray.getJSONObject(i).getString(
									"SC_ID"));
							scenic.setName(dataArray.getJSONObject(i)
									.getString("Name"));
							scenic.setImageUrl(FRONT_URL
									+ dataArray.getJSONObject(i).getString(
											"URL"));
							scenic.setAbstracts(dataArray.getJSONObject(i)
									.getString("Abstract"));
							scenic.setShareNum(dataArray.getJSONObject(i)
									.getString("ShareNum"));

							scenic.setLikeNum(dataArray.getJSONObject(i)
									.getString("LikeNum"));
							scenics.add(scenic);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return scenics;
	}

	@Override
	public ArrayList<Scenic> getHotScenicList() throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, HOTSCENIC_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "4314FB26-DC02-4360-BE76-3C66C08754F6");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		ArrayList<Scenic> scenics = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						scenics = new ArrayList<Scenic>();
						Scenic scenic = null;
						for (int i = 0; i < dataArray.length(); i++) {
							scenic = new Scenic();
							scenic.setId(dataArray.getJSONObject(i).getString(
									"id"));
							scenic.setName(dataArray.getJSONObject(i)
									.getString("name"));
							scenic.setImageUrl(FRONT_URL
									+ dataArray.getJSONObject(i).getString(
											"imageUrl"));
							scenic.setAbstracts(dataArray.getJSONObject(i)
									.getString("abstracts"));
							scenic.setShareNum(dataArray.getJSONObject(i)
									.getString("ShareNum"));

							scenic.setLikeNum(dataArray.getJSONObject(i)
									.getString("LikeNum"));
							scenics.add(scenic);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return scenics;
	}

	@Override
	public Scenic getScenicByQR(String qr, String mobile) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, SCENIC_QR_METHOD);
		// 设置参数
		rpc.addProperty("code", "2b7e8f5d-d35f-7756-f30b-b954607510ec");
		rpc.addProperty("qr", qr);
		rpc.addProperty("moblie", mobile);
		rpc.addProperty("status", "0");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null) {
						Scenic scenic = new Scenic();
						scenic.setQrScore(rootJsonObject.getString("Integral"));
						scenic.setId(dataArray.getJSONObject(0).getString("id"));
						scenic.setName(dataArray.getJSONObject(0).getString(
								"name"));
						scenic.setImageUrl(FRONT_URL
								+ dataArray.getJSONObject(0).getString(
										"imageUrl"));
						scenic.setAbstracts(dataArray.getJSONObject(0)
								.getString("abstracts"));
						scenic.setLikeNum(dataArray.getJSONObject(0).getString(
								"LikeNum"));
						scenic.setShareNum(dataArray.getJSONObject(0)
								.getString("ShareNum"));
						JSONArray parentArray = dataArray.getJSONObject(0)
								.getJSONArray("Parent");
						if (parentArray != null && parentArray.length() > 0) {
							scenic.setParentName(parentArray.getJSONObject(0)
									.getString("ParentName"));
							scenic.setParentID(parentArray.getJSONObject(0)
									.getString("ParentID"));
							scenic.setParentImageUrl(FRONT_URL
									+ parentArray.getJSONObject(0).getString(
											"ParentImageUrl"));
						}
						JSONArray childArray = dataArray.getJSONObject(0)
								.getJSONArray("Child");
						ArrayList<ChildScenic> children = null;
						if (childArray != null && childArray.length() > 0) {
							children = new ArrayList<ChildScenic>();
							ChildScenic child = null;
							Log.i("rzh", response);
							for (int i = 0; i < childArray.length(); i++) {
								child = new ChildScenic();
								child.setChildName(childArray.getJSONObject(i)
										.getString("ChildName"));
								child.setChildID(childArray.getJSONObject(i)
										.getString("ChildID"));
								child.setChildImageUrl(FRONT_URL
										+ childArray.getJSONObject(i)
												.getString("ChildImageUrl"));
								children.add(child);
							}
						}
						scenic.setChild(children);
						return scenic;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Scenic getScenicById(String id) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, SCENIC_METHOD);
		// 设置参数
		rpc.addProperty("code", "2551de8c-fbbe-be95-5d22-8dbbe749dfe3");
		rpc.addProperty("id", id);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null) {
						Scenic scenic = new Scenic();
						scenic.setId(dataArray.getJSONObject(0).getString("id"));
						scenic.setName(dataArray.getJSONObject(0).getString(
								"name"));
						scenic.setImageUrl(FRONT_URL
								+ dataArray.getJSONObject(0).getString(
										"imageUrl"));
						scenic.setAbstracts(dataArray.getJSONObject(0)
								.getString("abstracts"));
						scenic.setIsHot(dataArray.getJSONObject(0).getString(
								"IsHot"));
						scenic.setLikeNum(dataArray.getJSONObject(0).getString(
								"LikeNum"));
						scenic.setShareNum(dataArray.getJSONObject(0)
								.getString("ShareNum"));
						JSONArray parentArray = dataArray.getJSONObject(0)
								.getJSONArray("Parent");
						if (parentArray != null && parentArray.length() > 0) {
							scenic.setParentName(parentArray.getJSONObject(0)
									.getString("ParentName"));
							scenic.setParentID(parentArray.getJSONObject(0)
									.getString("ParentID"));
							scenic.setParentImageUrl(FRONT_URL
									+ parentArray.getJSONObject(0).getString(
											"ParentImageUrl"));
						}
						JSONArray childArray = dataArray.getJSONObject(0)
								.getJSONArray("Child");
						ArrayList<ChildScenic> children = null;
						if (childArray != null && childArray.length() > 0) {
							children = new ArrayList<ChildScenic>();
							ChildScenic child = null;
							Log.i("rzh", response);
							for (int i = 0; i < childArray.length(); i++) {
								child = new ChildScenic();
								child.setChildName(childArray.getJSONObject(i)
										.getString("ChildName"));
								child.setChildID(childArray.getJSONObject(i)
										.getString("ChildID"));
								child.setChildImageUrl(FRONT_URL
										+ childArray.getJSONObject(i)
												.getString("ChildImageUrl"));
								children.add(child);
							}
						}
						scenic.setChild(children);
						return scenic;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ArrayList<Agricola> getAllAgricolaList() throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, AGRICOLA_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "7b22af80-9e14-e976-b869-aa25c9798e0c");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		ArrayList<Agricola> agricolas = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						agricolas = new ArrayList<Agricola>();
						Agricola agricola = null;
						for (int i = 0; i < dataArray.length(); i++) {
							agricola = new Agricola();
							agricola.setId(dataArray.getJSONObject(i)
									.getString("id"));
							agricola.setName(dataArray.getJSONObject(i)
									.getString("name"));
							agricola.setAbstracts(dataArray.getJSONObject(i)
									.getString("abstracts"));
							agricola.setAddress(dataArray.getJSONObject(i)
									.getString("address"));
							agricola.setSort(dataArray.getJSONObject(i)
									.getString("sort"));
							agricola.setPhone(dataArray.getJSONObject(i)
									.getString("phone").replace("null", ""));
							agricola.setPhone1(dataArray.getJSONObject(i)
									.getString("phone1").replace("null", ""));
							agricola.setPhone2(dataArray.getJSONObject(i)
									.getString("phone2").replace("null", ""));
							agricola.setImage_url(DOWNLOAD_URL
									+ dataArray.getJSONObject(i).getString(
											"imageUrl"));
							agricola.setLevel(dataArray.getJSONObject(i)
									.getString("level").replace("null", "0"));
							agricola.setService(dataArray.getJSONObject(i)
									.getString("avgservice")
									.replace("null", "0.0").substring(0, 1));
							agricola.setTaste(dataArray.getJSONObject(i)
									.getString("avgtaste")
									.replace("null", "0.0").substring(0, 1));
							agricola.setCondition(dataArray.getJSONObject(i)
									.getString("avgcondition")
									.replace("null", "0.0").substring(0, 1));

							agricolas.add(agricola);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return agricolas;
	}

	@Override
	public ArrayList<Agricola> getUserAgricolaList(String UserMobile)
			throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USERAGRICOLA_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "d2780d4c-1024-41b0-8fd1-c8be78164c26");
		rpc.addProperty("UserMobile", UserMobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		Log.i("rzh", response);
		// init object
		ArrayList<Agricola> agricolas = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						agricolas = new ArrayList<Agricola>();
						Agricola agricola = null;
						for (int i = 0; i < dataArray.length(); i++) {
							agricola = new Agricola();
							agricola.setId(dataArray.getJSONObject(i)
									.getString("A_Id"));
							agricola.setName(dataArray.getJSONObject(i)
									.getString("Name"));
							/*
							 * agricola.setAbstracts(dataArray.getJSONObject(i)
							 * .getString("Abstracts"));
							 * agricola.setAddress(dataArray.getJSONObject(i)
							 * .getString("address"));
							 * agricola.setSort(dataArray.getJSONObject(i)
							 * .getString("sort"));
							 * agricola.setPhone(dataArray.getJSONObject(i)
							 * .getString("phone").replace("null", ""));
							 * agricola.setPhone1(dataArray.getJSONObject(i)
							 * .getString("phone1").replace("null", ""));
							 * agricola.setPhone2(dataArray.getJSONObject(i)
							 * .getString("phone2").replace("null", ""));
							 */
							agricola.setImage_url(DOWNLOAD_URL
									+ dataArray.getJSONObject(i).getString(
											"URL"));
							agricola.setLevel(dataArray.getJSONObject(i)
									.getString("Level").replace("null", "0"));
							agricola.setService(dataArray.getJSONObject(i)
									.getString("avgservice")
									.replace("null", "0.0").substring(0, 1));
							agricola.setTaste(dataArray.getJSONObject(i)
									.getString("avgtaste")
									.replace("null", "0.0").substring(0, 1));
							agricola.setCondition(dataArray.getJSONObject(i)
									.getString("avgcondition")
									.replace("null", "0.0").substring(0, 1));

							agricolas.add(agricola);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return agricolas;
	}

	@Override
	public Agricola getAgricolaById(String id) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, AGRICOLA_METHOD);
		// 设置参数
		rpc.addProperty("code", "f4d97d78-d5b3-44c5-5988-cb268eb39a97");
		rpc.addProperty("id", id);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		Agricola agricola = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						agricola = new Agricola();
						agricola.setId(dataArray.getJSONObject(0).getString(
								"id"));
						agricola.setName(dataArray.getJSONObject(0).getString(
								"name"));
						agricola.setAbstracts(dataArray.getJSONObject(0)
								.getString("abstracts"));
						agricola.setAddress(dataArray.getJSONObject(0)
								.getString("address"));
						agricola.setPhone(dataArray.getJSONObject(0).getString(
								"phone"));
						agricola.setPhone1(dataArray.getJSONObject(0)
								.getString("phone1").replace("null", ""));
						agricola.setPhone2(dataArray.getJSONObject(0)
								.getString("phone2").replace("null", ""));
						agricola.setImage_url(DOWNLOAD_URL
								+ dataArray.getJSONObject(0).getString(
										"imageUrl"));
						agricola.setLevel(dataArray.getJSONObject(0)
								.getString("level").replace("null", "0"));
						agricola.setService(dataArray.getJSONObject(0)
								.getString("avgservice").replace("null", "0.0")
								.substring(0, 1));
						agricola.setTaste(dataArray.getJSONObject(0)
								.getString("avgtaste").replace("null", "0.0")
								.substring(0, 1));
						agricola.setCondition(dataArray.getJSONObject(0)
								.getString("avgcondition")
								.replace("null", "0.0").substring(0, 1));
						agricola.setLikeNum(dataArray.getJSONObject(0)
								.getString("likeNum"));
						// 获取评论信息
						JSONArray commentArray = dataArray.getJSONObject(0)
								.getJSONArray("comment");
						ArrayList<Comment> comments = null;
						if (commentArray != null && commentArray.length() > 0) {
							comments = new ArrayList<Comment>();
							Comment comment = null;
							for (int i = 0; i < commentArray.length(); i++) {
								comment = new Comment();

								comment.setUserID(commentArray.getJSONObject(i)
										.getString("UserID"));
								comment.setContent(commentArray
										.getJSONObject(i).getString("content"));
								comment.setC_Date(commentArray.getJSONObject(i)
										.getString("C_Date"));

								comments.add(comment);
							}
						}
						agricola.setCommentsArrayList(comments);

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return agricola;
	}

	@Override
	public ArrayList<Specialty> getAllSpecialtyList() throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, SPECIALTY_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "7f978606-80c6-9496-001d-6c76f1803523");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		ArrayList<Specialty> specialtys = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						specialtys = new ArrayList<Specialty>();
						Specialty specialty = null;
						for (int i = 0; i < dataArray.length(); i++) {
							specialty = new Specialty();
							specialty.setId(dataArray.getJSONObject(i)
									.getString("id"));
							specialty.setName(dataArray.getJSONObject(i)
									.getString("name"));
							specialty.setImageUrl(DOWNLOAD_URL
									+ dataArray.getJSONObject(i).getString(
											"imageUrl"));
							specialty.setIntro(dataArray.getJSONObject(i)
									.getString("Abstract"));
							specialtys.add(specialty);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return specialtys;
	}

	@Override
	public ArrayList<Specialty> getUserSpecialtyList(String UserMobile)
			throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USERSPECIALTY_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "254aa096-57a4-4f07-b7dc-175748ea7133");
		rpc.addProperty("UserMobile", UserMobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		ArrayList<Specialty> specialtys = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						specialtys = new ArrayList<Specialty>();
						Specialty specialty = null;
						for (int i = 0; i < dataArray.length(); i++) {
							specialty = new Specialty();
							specialty.setId(dataArray.getJSONObject(i)
									.getString("SC_ID"));
							specialty.setName(dataArray.getJSONObject(i)
									.getString("Name"));
							specialty.setImageUrl(DOWNLOAD_URL
									+ dataArray.getJSONObject(i).getString(
											"URL"));
							specialty.setIntro(dataArray.getJSONObject(i)
									.getString("Abstract"));
							specialtys.add(specialty);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return specialtys;
	}

	@Override
	public Specialty getSpecialtyById(String id) throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, SPECIALTY_METHOD);
		// 设置参数
		rpc.addProperty("code", "0d93f2d5-abb2-4ac5-9d39-3cfef06eec2c");
		rpc.addProperty("id", id);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		Specialty specialty = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						specialty = new Specialty();
						specialty.setId(dataArray.getJSONObject(0).getString(
								"id"));
						specialty.setName(dataArray.getJSONObject(0).getString(
								"name"));
						specialty.setImageUrl(DOWNLOAD_URL
								+ dataArray.getJSONObject(0).getString(
										"imageUrl"));
						specialty.setIntro(dataArray.getJSONObject(0)
								.getString("Abstract"));
						specialty.setLikeNum(dataArray.getJSONObject(0)
								.getString("likeNum"));
						specialty.setShareNum(dataArray.getJSONObject(0)
								.getString("shareNum"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return specialty;
	}

	@Override
	public ArrayList<Message> getMessageList() throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, MESSAGE_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "b02f88f6-2002-956d-8aa8-620873172bf4");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		ArrayList<Message> messages = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						messages = new ArrayList<Message>();
						Message message = null;
						for (int i = 0; i < dataArray.length(); i++) {
							message = new Message();
							message.setId(dataArray.getJSONObject(i).getString(
									"id"));
							message.setTitle(dataArray.getJSONObject(i)
									.getString("title"));
							message.setContent(dataArray.getJSONObject(i)
									.getString("content"));
							message.setImageUrl(DOWNLOAD_URL
									+ dataArray.getJSONObject(i).getString(
											"imageUrl"));
							message.setType(dataArray.getJSONObject(i)
									.getString("typename"));
							message.setDate(dataArray.getJSONObject(i)
									.getString("time"));
							message.setCommentCount(dataArray.getJSONObject(i)
									.getString("count"));
							messages.add(message);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return messages;
	}

	@Override
	public Message getMessageById(String id) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, MESSAGE_METHOD);
		// 设置参数
		rpc.addProperty("code", "246deeb9-2478-6e1b-23fb-2deb805b343c");
		rpc.addProperty("id", id);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		// init object
		Message message = null;
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						message = new Message();
						message.setId(dataArray.getJSONObject(0)
								.getString("id"));
						message.setTitle(dataArray.getJSONObject(0).getString(
								"title"));
						message.setContent(dataArray.getJSONObject(0)
								.getString("content"));
						message.setImageUrl(DOWNLOAD_URL
								+ dataArray.getJSONObject(0)
										.getString("imageUrl")
										.replace("null", ""));
						message.setType(dataArray.getJSONObject(0).getString(
								"typename"));
						message.setDate(dataArray.getJSONObject(0).getString(
								"time"));
						// message.setCommentCount(dataArray.getJSONObject(0)
						// .getString("count").replace("null", "0"));

						// 获取评论信息
						JSONArray commentArray = dataArray.getJSONObject(0)
								.getJSONArray("comment");
						ArrayList<Comment> comments = null;
						if (commentArray != null && commentArray.length() > 0) {
							comments = new ArrayList<Comment>();
							Comment comment = null;
							for (int i = 0; i < commentArray.length(); i++) {
								comment = new Comment();

								comment.setUserID(commentArray.getJSONObject(i)
										.getString("UserID"));
								comment.setContent(commentArray
										.getJSONObject(i).getString("content"));
								comment.setC_Date(commentArray.getJSONObject(i)
										.getString("C_Date"));

								comments.add(comment);
							}
						}
						message.setCommentsArrayList(comments);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return message;
	}

	@Override
	public Boolean userComment(String id, String content, String mobile,
			String type, String service, String taste, String condition)
			throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, USER_COMMENTS);
		// 设置参数
		rpc.addProperty("code", "18fb1c98-ee58-86f8-0b52-32e45b64cca9");
		rpc.addProperty("info", id + "|" + content + "|" + mobile + "|" + type
				+ "|" + type + "|" + service + "|" + taste + "|" + condition);

		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	@Override
	public AirQuality getAirqualityInfo() throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, AIR_QUALITY_METHOD);
		// 设置参数
		rpc.addProperty("code", "9feb65ca-69e7-33c5-2503-33fa6670a041");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);

		AirQuality air = null;
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						air = new AirQuality();

						air.setPm(dataArray.getJSONObject(0).getString("pm"));
						air.setOxygen(dataArray.getJSONObject(0).getString(
								"air"));

						return air;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return air;
	}

	@Override
	public ArrayList<RecommendRoute> getRecommendList() throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, RECOMMEND_LINE_LIST_METHOD);
		// 设置参数
		rpc.addProperty("code", "65cdcb7e-7b70-948e-4083-9f09ed65224e");
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);

		ArrayList<RecommendRoute> routes = null;
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						routes = new ArrayList<RecommendRoute>();
						for (int i = 0; i < dataArray.length(); i++) {
							RecommendRoute route = new RecommendRoute();

							route.setRouteID(dataArray.getJSONObject(i)
									.getString("id"));
							route.setRouteName(dataArray.getJSONObject(i)
									.getString("name"));
							route.setRouteIconUrl(DOWNLOAD_URL
									+ dataArray.getJSONObject(i).getString(
											"imageUrl"));

							// node information
							JSONArray pathsArray = dataArray.getJSONObject(i)
									.getJSONArray("path");
							ArrayList<Path> pathsList = null;
							if (pathsArray != null && pathsArray.length() > 0) {
								pathsList = new ArrayList<Path>();
								Path mPath = null;
								Scenic scenic = null;
								for (int k = 0; k < pathsArray.length(); k++) {
									mPath = new Path();
									scenic = new Scenic();
									// 景点信息
									scenic.setName(pathsArray.getJSONObject(k)
											.getJSONArray("s_id")
											.getJSONObject(0).getString("name"));

									mPath.setScenic(scenic);

									pathsList.add(mPath);
								}
								route.setPaths(pathsList);
							}

							routes.add(route);
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return routes;
	}

	@Override
	public RecommendRoute getRecommend(String id) throws WSError {
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, RECOMMEND_LINE_METHOD);
		// 设置参数
		rpc.addProperty("code", "a2f10c5d-ad99-1bde-f6dc-b1571a689a85");
		rpc.addProperty("id", id);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		RecommendRoute route = null;
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null && dataArray.length() > 0) {
						route = new RecommendRoute();
						// basic information
						route.setRouteID(dataArray.getJSONObject(0).getString(
								"id"));
						route.setRouteName(dataArray.getJSONObject(0)
								.getString("name"));
						route.setRouteIconUrl(DOWNLOAD_URL
								+ dataArray.getJSONObject(0).getString(
										"imageUrl"));

						// node information
						JSONArray pathsArray = dataArray.getJSONObject(0)
								.getJSONArray("path");
						ArrayList<Path> pathsList = null;
						if (pathsArray != null && pathsArray.length() > 0) {
							pathsList = new ArrayList<Path>();
							Path mPath = null;
							Scenic scenic = null;
							for (int i = 0; i < pathsArray.length(); i++) {
								mPath = new Path();
								scenic = new Scenic();
								// 路径编号
								mPath.setId(pathsArray.getJSONObject(i)
										.getString("r_id"));
								// 排序编号
								mPath.setSort(pathsArray.getJSONObject(i)
										.getString("sort"));
								// 景点信息
								scenic.setId(pathsArray.getJSONObject(i)
										.getJSONArray("s_id").getJSONObject(0)
										.getString("id"));
								scenic.setName(pathsArray.getJSONObject(i)
										.getJSONArray("s_id").getJSONObject(0)
										.getString("name"));
								scenic.setAbstracts(pathsArray.getJSONObject(i)
										.getJSONArray("s_id").getJSONObject(0)
										.getString("Abstract"));
								scenic.setImageUrl(DOWNLOAD_URL
										+ pathsArray.getJSONObject(i)
												.getJSONArray("s_id")
												.getJSONObject(0)
												.getString("imageUrl"));

								mPath.setScenic(scenic);

								pathsList.add(mPath);
							}
							route.setPaths(pathsList);
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return route;
	}

	@Override
	public Boolean surveyAnswer(String info) throws WSError {
		// TODO Auto-generated method stub
		// 指定 WebService 的命名空间和调用方法
		SoapObject rpc = new SoapObject(NAMESPACE, SURVEYANSWER);
		// 设置参数
		rpc.addProperty("code", "72768181-647E-41F1-826A-16151BDAAA80");
		rpc.addProperty("info", info);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return false;
			}

		}
		return false;
	}

	@Override
	public String userImageUpload(String id, String imageData) throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, USER_IMAGE_UPLOAD);
		// 设置参数
		rpc.addProperty("code", "1573b98b-dc0d-497d-b5bc-a90233c63735");
		rpc.addProperty("mobile", id);
		rpc.addProperty("strbyte", imageData);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return rootJsonObject.getString("data");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return null;

			}

		}
		return null;
	}

	public int likeScenicRecord(String UserMobile, String SID, int IsLike)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, SCENIC_LIKE);
		// 设置参数
		rpc.addProperty("code", "ACD66693-2C54-44EE-AF6A-4F6D29A3678A");
		rpc.addProperty("UserMobile", UserMobile);
		rpc.addProperty("SID", SID);
		rpc.addProperty("IsLike", IsLike);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				return Integer.parseInt(rootJsonObject.getString("result"));
			} catch (JSONException e) {
				return 1;
			}
		}
		return 1;
	}

	public int likeAgrRecord(String UserMobile, String SID, int IsLike)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, AGR_LIKE);
		// 设置参数
		rpc.addProperty("code", "1771030E-9B40-48D4-B160-469997C6567A");
		rpc.addProperty("UserMobile", UserMobile);
		rpc.addProperty("AID", SID);
		rpc.addProperty("IsLike", IsLike);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				return Integer.parseInt(rootJsonObject.getString("result"));
			} catch (JSONException e) {
				return 1;
			}
		}
		return 1;
	}

	public int likeSpeRecord(String UserMobile, String SID, int IsLike)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, SPE_LIKE);
		// 设置参数
		rpc.addProperty("code", "40303B85-B6CA-4523-A8FA-DD9D0A51773E");
		rpc.addProperty("UserMobile", UserMobile);
		rpc.addProperty("SpeID", SID);
		rpc.addProperty("IsLike", IsLike);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				return Integer.parseInt(rootJsonObject.getString("result"));
			} catch (JSONException e) {
				return 1;
			}
		}
		return 1;
	}

	@Override
	public ArrayList<NaviMapObject> getScencParentByMobile(String mobile)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, SCENIC_ISFLAG);
		// 设置参数
		rpc.addProperty("code", "71076739-eae5-4a69-8356-328bf72c7fb3");
		rpc.addProperty("Mobile", mobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, SCENIC_URL);
		Log.i("rzh", response);
		ArrayList<NaviMapObject> naviMapObjects = null;
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					JSONArray dataArray = rootJsonObject.getJSONArray("data");
					if (dataArray != null) {
						JSONObject dataObject;
						NaviMapObject naviMapObject;
						naviMapObjects = new ArrayList<NaviMapObject>();
						for (int i = 0; i < dataArray.length(); i++) {
							dataObject = dataArray.getJSONObject(i);
							if (dataObject.getInt("IsFlag") == 1)
								naviMapObject = new NaviMapObject(i,
										dataObject.getInt("MapX"),
										dataObject.getInt("MapY"),
										dataObject.getString("SC_ID"),
										dataObject.getString("Name"), true);
							else
								naviMapObject = new NaviMapObject(i,
										dataObject.getInt("MapX"),
										dataObject.getInt("MapY"),
										dataObject.getString("SC_ID"),
										dataObject.getString("Name"), false);

							naviMapObjects.add(naviMapObject);
						}
					}
					return naviMapObjects;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return naviMapObjects;

			}

		}
		return naviMapObjects;
	}

	public Boolean shareScenicRecord(String mobile, String SID) throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, SCENIC_SHARE);
		// 设置参数
		rpc.addProperty("code", "46e755c9-7e33-4c54-8743-912c313332b5");
		rpc.addProperty("SID", SID);
		rpc.addProperty("mobile", mobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}

	public Boolean shareSpeRecord(String mobile, String SpeID) throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, SPR_SHARE);
		// 设置参数
		rpc.addProperty("code", "BBEE558F-554F-4259-97CF-F3759C6228F8");
		rpc.addProperty("SpeID", SpeID);
		rpc.addProperty("UserMobile", mobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}

	public Boolean ForgetPwd(String mobile, String newPwd) throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, FINDPWD);
		// 设置参数
		rpc.addProperty("code", "b8a78c6a-7ea5-e040-1aa0-960b410b421f");
		rpc.addProperty("NewPass", newPwd);
		rpc.addProperty("mobile", mobile);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}

	public VersionInfo getCurrentVersionInfo(String currentVersion,
			String packageName) throws WSError {
		String arg0 = XMLUtil.BuildBOXXml(Constants.CMD_CURRENT_VERSION,
				"<RS_DATA><RS_ROW PROD_CODE=\"" + packageName
						+ "\" PROD_VERSION=\"" + currentVersion
						+ "\" /></RS_DATA>");
		String result = Caller.doPostDefault(POST_VERSION_API, arg0);

		if (TextUtils.isEmpty(result)) {
			return null;
		}

		Document xmldoc = XMLUtil.StringToDocument(result);
		if (xmldoc.selectSingleNode("//@PROD_CODE") == null
				|| TextUtils.isEmpty(xmldoc.selectSingleNode("//@PROD_CODE")
						.getText()))
			return null;

		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setPROD_CODE(xmldoc.selectSingleNode("//@PROD_CODE")
				.getText());
		versionInfo.setPROD_VERSION(xmldoc.selectSingleNode("//@PROD_VERSION")
				.getText());
		versionInfo.setPROD_SIZE(xmldoc.selectSingleNode("//@PROD_SIZE")
				.getText());
		versionInfo.setIS_DB_UPDATE(xmldoc.selectSingleNode("//@IS_DB_UPDATE")
				.getText());
		versionInfo.setPROD_RELEASE_DATE(xmldoc.selectSingleNode(
				"//@PROD_RELEASE_DATE").getText());
		versionInfo.setPROD_DOWNLOAD_URL(xmldoc.selectSingleNode(
				"//@PROD_DOWNLOAD_URL").getText());
		return versionInfo;
	}

	public Boolean addSuggestion(String mobile, String text) throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, ADD_SUGGEST);
		// 设置参数
		rpc.addProperty("code", "dc4e52a3-0bb1-44bb-9e85-10efb92baf01");
		rpc.addProperty("UserMobile", mobile);
		rpc.addProperty("Text", text);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}

	public Boolean changePwd(String mobile, String newPwd , String nowPwd)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, CHANGEPWD);
		// 设置参数
		rpc.addProperty("code", "670c268a-3078-423b-8d94-b2fedb8f44dc");
		rpc.addProperty("Newpassword", newPwd);
		rpc.addProperty("UserMobile", mobile);
		rpc.addProperty("Oldpassword", nowPwd);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}
	
	public Boolean changeUsersName(String mobile, String newName)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, CHANGE_USER_NAME);
		// 设置参数
		rpc.addProperty("code", "114a9e5d-92a7-4828-80e2-a0e96c9bc5cf");
		rpc.addProperty("Mobile", mobile);
		rpc.addProperty("UserName", newName);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}
	
	public Boolean BindMobile(String UID, String Mobile, String codeNum)
			throws WSError {
		// TODO Auto-generated method stub
		SoapObject rpc = new SoapObject(NAMESPACE, BIND_MOBILE);
		// 设置参数
		rpc.addProperty("code", "501a76d6-151e-406e-b26c-ecf14a068b7b");
		rpc.addProperty("UID", UID);
		rpc.addProperty("Mobile", Mobile);
		rpc.addProperty("codeNum", codeNum);
		// 获取响应值
		String response = WebServicesUtil.doWebServicesRequest(rpc, USER_URL);
		if (!TextUtils.isEmpty(response)) {
			JSONObject rootJsonObject;
			try {
				rootJsonObject = new JSONObject(response);
				if (rootJsonObject.getString("result").equals("0")) {
					return true;
				}
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}

}