package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class Path {
	private String id;							//·�����
	private Scenic scenic = null;				//·��������Ϣ
	private String sort;						//����
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
