package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class Path {
	private String id;							//路径编号
	private Scenic scenic = null;				//路径景点信息
	private String sort;						//排序
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Scenic getScenic() {
		return scenic;
	}
	public void setScenic(Scenic scenic) {
		this.scenic = scenic;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
