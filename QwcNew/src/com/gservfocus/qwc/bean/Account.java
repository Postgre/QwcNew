/*
 * @(#)Account.java 2011-2-25
 *
 * Copyright 2006-2011 YiMing Wang, All Rights reserved.
 */
package com.gservfocus.qwc.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 */
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String EMPTYFLAG = "";
	
	private String account = EMPTYFLAG;		//�˺�
	private String password = EMPTYFLAG;	//����
	private String mobile = EMPTYFLAG;		//�绰
	private String rankName = EMPTYFLAG;		//�ȼ�����
	private String integral = EMPTYFLAG;		//���û���
	private String imageUrl = EMPTYFLAG;
	private String UserId = EMPTYFLAG;      //�û�ID
	
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Account(){
		
		
	}
	public Account(String userName,String password,String mobile){
		this.account = userName;
		this.password = password;
		this.mobile = mobile;
	}
	/**
	 * ������л�����
	 * 
	 * @param account 
	 * 			�˺Ŷ���
	 * @return
	 */
	public static byte[] serializable2Data(Account account) {
		byte[] data = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(account);
			data = baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * ������л�����
	 * 
	 * @param lbAccount
	 * @return
	 */
	public static Account serializable4Data(byte[] data) {
		Account account = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			account = (Account) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return account;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}

}
