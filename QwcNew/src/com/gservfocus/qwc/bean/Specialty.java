package com.gservfocus.qwc.bean;

import java.io.Serializable;

public class Specialty implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;  //ũ�ز����
	private String imageUrl;  //ũ�ز�ͼƬimageUrl
	private String name;  //ũ�ز�����
	private String intro;  //ũ�ز����
	private String shareNum;	// �������
	private String likeNum;	    // �ղش���
	
	public String getShareNum() {
		return shareNum;
	}
	public void setShareNum(String shareNum) {
		this.shareNum = shareNum;
	}
	public String getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	
}
