package com.gservfocus.qwc.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class RouteStepContent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int time;
	private int distance;
	private ArrayList<String> routeSteps = new ArrayList<String>();
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public ArrayList<String> getRouteSteps() {
		return routeSteps;
	}
	
}
