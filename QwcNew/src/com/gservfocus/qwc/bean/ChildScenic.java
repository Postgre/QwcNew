package com.gservfocus.qwc.bean;

public class ChildScenic {
	private String ChildName;
	private String ChildID;
	private String ChildImageUrl;
	
	public ChildScenic(String childName, String childID, String childImageUrl) {
		super();
		ChildName = childName;
		ChildID = childID;
		ChildImageUrl = childImageUrl;
	}
	
	public ChildScenic() {
		super();
	}

	public String getChildName() {
		return ChildName;
	}
	public void setChildName(String childName) {
		ChildName = childName;
	}
	public String getChildID() {
		return ChildID;
	}
	public void setChildID(String childID) {
		ChildID = childID;
	}
	public String getChildImageUrl() {
		return ChildImageUrl;
	}
	public void setChildImageUrl(String childImageUrl) {
		ChildImageUrl = childImageUrl;
	}
	
}
