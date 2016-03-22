package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class NaviMapObject {
	private static ArrayList<NaviMapObject> naviMapObjects;
	private int id; // 导航地图景点id
	private int x; // 导航地图景点横坐标
	private int y; // 导航地图景点纵坐标
	private boolean isScan; // 该景点是否被扫描过
	private String scenicID; // 导航地图景点ID
	private String name; // 导航地图景点名称

	public NaviMapObject(int id, int x, int y, String scenicID, String name,
			boolean isScan) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.scenicID = scenicID;
		this.name = name;
		this.isScan = isScan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getScenicID() {
		return scenicID;
	}

	public void setScenicID(String scenicID) {
		this.scenicID = scenicID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取所有地图对象 getAll
	 * 
	 * @return ArrayList<NaviMapObject>
	 * */
	public static ArrayList<NaviMapObject> getAll() {
		if (naviMapObjects == null) {
			naviMapObjects = new ArrayList<NaviMapObject>();
			// 瀛农古风园
			naviMapObjects.add(new NaviMapObject(0, 638, 1150,
					"20140524150630760", "瀛农古风园", false));
			// 世界木化石馆
			naviMapObjects.add(new NaviMapObject(1, 1049, 726,
					"20140513194127271", "世界木化石馆", false));
			// 中国奇石馆
			naviMapObjects.add(new NaviMapObject(2, 1121, 674,
					"20140524154948779", "中国奇石馆", false));
			// 中华根雕馆
			naviMapObjects.add(new NaviMapObject(3, 1161, 617,
					"20140524144657929", "中华根雕馆", false));
			// 雷锋纪念馆
			naviMapObjects.add(new NaviMapObject(4, 1076, 810,
					"20140513193744629", "雷锋纪念馆", false));
			// 红土网球场
			naviMapObjects.add(new NaviMapObject(5, 1340, 794,
					"20140804144640072", "红土网球场", false));
			// 香草花园
			naviMapObjects.add(new NaviMapObject(6, 1321, 1012,
					"20140519083207880", "香草花园", false));
			// 精品蔬菜采摘
			naviMapObjects.add(new NaviMapObject(7, 1483, 1119,
					"20140804121251622", "蔬果采摘", false));

		}
		return naviMapObjects;
	}

	public static void setAll(ArrayList<NaviMapObject> naviMapObjectsT) {

		naviMapObjects = naviMapObjectsT;
	}

	/**
	 * 查询地图对象信息 findNaviMapObject
	 * 
	 * @return NaviMapObject
	 * */
	public static NaviMapObject findNaviMapObject(int id) {
		for (NaviMapObject naviMapObject : naviMapObjects) {
			if (naviMapObject.getId() == id)
				return naviMapObject;
		}

		return naviMapObjects.get(0);
	}

	public boolean isScan() {
		return isScan;
	}

	public void setScan(boolean isScan) {
		this.isScan = isScan;
	}

}
