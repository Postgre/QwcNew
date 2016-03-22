package com.gservfocus.qwc.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;			//id
	private String title;		//title
	private String content;		//detail content
	private String date;		//public date
	private String imageUrl;		//message picture url
	private String type;		//message type
	private String commentCount;		//comment count
	private ArrayList<Comment> commentsArrayList;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<Comment> getCommentsArrayList() {
		return commentsArrayList;
	}
	public void setCommentsArrayList(ArrayList<Comment> commentsArrayList) {
		this.commentsArrayList = commentsArrayList;
	}
	
}
