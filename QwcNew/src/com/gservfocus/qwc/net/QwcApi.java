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
	 * �û���¼
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws WSError
	 */
	public Account verify(String mobile, String password) throws WSError;

	/**
	 * �û���������¼
	 * 
	 * @param Uid
	 * @param UserName
	 * @return
	 * @throws WSError
	 */
	public Account verifyForOther(String Uid, String UserName) throws WSError;

	/**
	 * �û�ע��
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws WSError
	 */
	public Integer register(String account, String mobile, String email,
			String password) throws WSError;

	/**
	 * �ύ�����ʾ�
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws WSError
	 */
	public Boolean surveyAnswer(String info) throws WSError;

	/**
	 * ��ȡ�����б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Scenic> getAllScenicList() throws WSError;

	/**
	 * ��ȡָ��������Ϣ
	 * 
	 * @param id
	 * @return
	 * @throws WSError
	 */
	public Scenic getScenicById(String id) throws WSError;

	/**
	 * getScenicByQR ��ȡָ��������Ϣ
	 * 
	 * @param id
	 * @return
	 * @throws WSError
	 */
	public Scenic getScenicByQR(String qr, String mobile) throws WSError;

	/**
	 * ��ȡũ�����б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Agricola> getAllAgricolaList() throws WSError;

	/**
	 * ��ȡָ��ũ�����б�
	 * 
	 * @param id
	 *            //ũ���ֱ��
	 * @return
	 * @throws WSError
	 */
	public Agricola getAgricolaById(String id) throws WSError;

	/**
	 * ��ȡũ�ز��б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Specialty> getAllSpecialtyList() throws WSError;

	/**
	 * ��ȡָ��ũ�ز��б�
	 * 
	 * @param id
	 *            //ũ�ز����
	 * @return
	 * @throws WSError
	 */
	public Specialty getSpecialtyById(String id) throws WSError;

	/**
	 * ��ȡ��Ϣ�б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Message> getMessageList() throws WSError;

	/**
	 * ��ȡ��Ϣ����
	 * 
	 * @return
	 * @throws WSError
	 */
	public Message getMessageById(String id) throws WSError;

	/**
	 * �û����� ����ũ�������ۺ���Ϣ����
	 * 
	 * @return
	 * @throws WSError
	 */
	public Boolean userComment(String id, String content, String mobile,
			String type, String service, String taste, String condition)
			throws WSError;

	/**
	 * getAirqualityInfo ��ȡ����ָ��
	 * 
	 * @return
	 * @throws WSError
	 */
	public AirQuality getAirqualityInfo() throws WSError;

	/**
	 * getRecommendInfo ��ȡ�Ƽ���·�б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<RecommendRoute> getRecommendList() throws WSError;

	/**
	 * getRecommendInfo ��ȡ�Ƽ���·
	 * 
	 * @return
	 * @throws WSError
	 */
	public RecommendRoute getRecommend(String id) throws WSError;

	/**
	 * getRecommendInfo �û�ͷ���ϴ�
	 * 
	 * @return
	 * @throws WSError
	 */
	public String userImageUpload(String id, String imageData) throws WSError;

	/**
	 * ��ȡ�����б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Scenic> getHotScenicList() throws WSError;

	/**
	 * ��ȡ�ղؾ����б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Scenic> getUserScenicList(String UserMobile)
			throws WSError;

	/**
	 * ��ȡ�ղ�ũ�����б�
	 * 
	 * @return
	 * @throws WSError
	 */
	public ArrayList<Agricola> getUserAgricolaList(String UserMobile)
			throws WSError;

	/**
	 * ��ȡ�ղ�ũ�ز��б�
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

	// ��ȡ��֤��
	public Integer CreateCheckCode(String mobile) throws WSError;

	// �����֤��
	public Integer CheckIsTrue(String mobile, String codeNum) throws WSError;

	public Integer RegisterNew(String mobile, String NewPass) throws WSError;
	//���ֻ�
	public Boolean BindMobile(String UID, String Mobile,String codeNum) throws WSError;
	public Boolean changePwd(String mobile, String newPwd , String nowPwd)
			throws WSError;
	public Boolean changeUsersName(String mobile, String newName)
			throws WSError;
}
