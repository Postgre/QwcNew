package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class RecommendRoute {
	private String routeIconUrl; //�Ƽ���·ͼ��
	private String routeID;		 //�Ƽ���·ID
	private String routeName;	 //�Ƽ���·����
	private ArrayList<Path> paths;	//��A���㵽B�����·������
	public String getRouteIconUrl() {
		return routeIconUrl;
	}
	public void setRouteIconUrl(String routeIconUrl) {
		this.routeIconUrl = routeIconUrl;
	}

	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public ArrayList<Path> getPaths() {
		return paths;
	}
	public void setPaths(ArrayList<Path> paths) {
		this.paths = paths;
	}
}
