package com.gservfocus.qwc.net;

import java.util.ArrayList;

import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.AirQuality;
import com.gservfocus.qwc.bean.Message;
import com.gservfocus.qwc.bean.NaviMapObject;
import com.gservfocus.qwc.bean.RecommendRoute;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.bean.Specialty;

public interface QwcApi {
	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws WSError
	 */
	public Account verify(String mobile, String password) throws WSError;

	/**
	 * 用户第三方登录
	 * 
	 * @param Uid
	 * @param UserName
	 * @return
	 * @throws WSError
	 */
	public Account verifyForOther(String Uid, String UserName) throws WSError;

	/**
	 * 用户注册
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws WSError
	 */
	public Integer register(String account, String mobile, String email,
			String password) throws WSError;

	/**
	 * 提交调查问卷
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws WSError
	 */
	public Boolean surveyAnswer(String info) throws WSError;

	/**
	 * 获取景点列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Scenic> getAllScenicList() throws WSError;

	/**
	 * 获取指定景点信息
	 * 
	 * @param id
	 * @return
	 * @throws WSError
	 */
	public Scenic getScenicById(String id) throws WSError;

	/**
	 * getScenicByQR 获取指定景点信息
	 * 
	 * @param id
	 * @return
	 * @throws WSError
	 */
	public Scenic getScenicByQR(String qr, String mobile) throws WSError;

	/**
	 * 获取农家乐列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Agricola> getAllAgricolaList() throws WSError;

	/**
	 * 获取指定农家乐列表
	 * 
	 * @param id
	 *            //农家乐编号
	 * @return
	 * @throws WSError
	 */
	public Agricola getAgricolaById(String id) throws WSError;

	/**
	 * 获取农特产列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Specialty> getAllSpecialtyList() throws WSError;

	/**
	 * 获取指定农特产列表
	 * 
	 * @param id
	 *            //农特产编号
	 * @return
	 * @throws WSError
	 */
	public Specialty getSpecialtyById(String id) throws WSError;

	/**
	 * 获取信息列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Message> getMessageList() throws WSError;

	/**
	 * 获取信息详情
	 * 
	 * @return
	 * @throws WSError
	 */
	public Message getMessageById(String id) throws WSError;

	/**
	 * 用户评论 包含农家乐评论和信息评论
	 * 
	 * @return
	 * @throws WSError
	 */
	public Boolean userComment(String id, String content, String mobile,
			String type, String service, String taste, String condition)
			throws WSError;

	/**
	 * getAirqualityInfo 获取空气指数
	 * 
	 * @return
	 * @throws WSError
	 */
	public AirQuality getAirqualityInfo() throws WSError;

	/**
	 * getRecommendInfo 获取推荐线路列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<RecommendRoute> getRecommendList() throws WSError;

	/**
	 * getRecommendInfo 获取推荐线路
	 * 
	 * @return
	 * @throws WSError
	 */
	public RecommendRoute getRecommend(String id) throws WSError;

	/**
	 * getRecommendInfo 用户头像上传
	 * 
	 * @return
	 * @throws WSError
	 */
	public String userImageUpload(String id, String imageData) throws WSError;

	/**
	 * 获取热门列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Scenic> getHotScenicList() throws WSError;

	/**
	 * 获取收藏景点列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Scenic> getUserScenicList(String UserMobile)
			throws WSError;

	/**
	 * 获取收藏农家乐列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Agricola> getUserAgricolaList(String UserMobile)
			throws WSError;

	/**
	 * 获取收藏农特产列表
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Specialty> getUserSpecialtyList(String UserMobile)
			throws WSError;

	public ArrayList<NaviMapObject> getScencParentByMobile(String mobile)
			throws WSError;

	public Boolean ForgetPwd(String mobile, String newPwd) throws WSError;

	public Boolean addSuggestion(String mobile, String text) throws WSError;

	// 获取验证码
	public Integer CreateCheckCode(String mobile) throws WSError;

	// 检测验证码
	public Integer CheckIsTrue(String mobile, String codeNum) throws WSError;

	public Integer RegisterNew(String mobile, String NewPass) throws WSError;
	//绑定手机
	public Boolean BindMobile(String UID, String Mobile,String codeNum) throws WSError;
	public Boolean changePwd(String mobile, String newPwd , String nowPwd)
			throws WSError;
	public Boolean changeUsersName(String mobile, String newName)
			throws WSError;
}
