package com.gservfocus.qwc.bean;

public class Comment {
	private String content;
	private String UserID;
	private String C_Date;
	public Comment(){
		
	}
	public Comment(String content,String userid,String cDate){
		this.content = content;
		this.UserID = userid;
		this.C_Date = cDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getC_Date() {
		return C_Date;
	}
	public void setC_Date(String c_Date) {
		C_Date = c_Date;
	}
	
}
