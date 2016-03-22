package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class RecommendRoute {
	private String routeIconUrl; //推荐线路图标
	private String routeID;		 //推荐线路ID
	private String routeName;	 //推荐线路名称
	private ArrayList<Path> paths;	//从A景点到B景点的路过景点
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
