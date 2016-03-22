package com.gservfocus.qwc.bean;

import java.io.Serializable;

public class Specialty implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;  //农特产编号
	private String imageUrl;  //农特产图片imageUrl
	private String name;  //农特产名词
	private String intro;  //农特产简介
	private String shareNum;	// 分享次数
	private String likeNum;	    // 收藏次数
	
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
