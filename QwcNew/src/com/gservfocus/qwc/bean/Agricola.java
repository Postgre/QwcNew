package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class Agricola {
	private String id;
	private String name;
	private String abstracts;	//简介
	private String address;
	private String phone;
	private String phone1;
	private String phone2;
	private String image_url;
	private String level;
	private String sort;		//分类
	private String content;		//内容
	private String user_id;		//评论人名称
	private String c_date;		//评论时间
	private String service;		//服务
	private String taste;		//口味
	private String condition;		//环境
	private String shareNum;	// 分享次数
	private String likeNum;	    // 收藏次数
	private ArrayList<Comment> commentsArrayList;
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getC_date() {
		return c_date;
	}
	public void setC_date(String c_date) {
		this.c_date = c_date;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public ArrayList<Comment> getCommentsArrayList() {
		return commentsArrayList;
	}
	public void setCommentsArrayList(ArrayList<Comment> commentsArrayList) {
		this.commentsArrayList = commentsArrayList;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
