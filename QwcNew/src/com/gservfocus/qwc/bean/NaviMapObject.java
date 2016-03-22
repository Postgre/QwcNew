package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class NaviMapObject {
	private static ArrayList<NaviMapObject> naviMapObjects;
	private int id; // ������ͼ����id
	private int x; // ������ͼ���������
	private int y; // ������ͼ����������
	private boolean isScan; // �þ����Ƿ�ɨ���
	private String scenicID; // ������ͼ����ID
	private String name; // ������ͼ��������

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
	 * ��ȡ���е�ͼ���� getAll
	 * 
	 * @return ArrayList<NaviMapObject>
	 * */
	public static ArrayList<NaviMapObject> getAll() {
		if (naviMapObjects == null) {
			naviMapObjects = new ArrayList<NaviMapObject>();
			// �ũ�ŷ�԰
			naviMapObjects.add(new NaviMapObject(0, 638, 1150,
					"20140524150630760", "�ũ�ŷ�԰", false));
			// ����ľ��ʯ��
			naviMapObjects.add(new NaviMapObject(1, 1049, 726,
					"20140513194127271", "����ľ��ʯ��", false));
			// �й���ʯ��
			naviMapObjects.add(new NaviMapObject(2, 1121, 674,
					"20140524154948779", "�й���ʯ��", false));
			// �л������
			naviMapObjects.add(new NaviMapObject(3, 1161, 617,
					"20140524144657929", "�л������", false));
			// �׷�����
			naviMapObjects.add(new NaviMapObject(4, 1076, 810,
					"20140513193744629", "�׷�����", false));
			// ��������
			naviMapObjects.add(new NaviMapObject(5, 1340, 794,
					"20140804144640072", "��������", false));
			// ��ݻ�԰
			naviMapObjects.add(new NaviMapObject(6, 1321, 1012,
					"20140519083207880", "��ݻ�԰", false));
			// ��Ʒ�߲˲�ժ
			naviMapObjects.add(new NaviMapObject(7, 1483, 1119,
					"20140804121251622", "�߹���ժ", false));

		}
		return naviMapObjects;
	}

	public static void setAll(ArrayList<NaviMapObject> naviMapObjectsT) {

		naviMapObjects = naviMapObjectsT;
	}

	/**
	 * ��ѯ��ͼ������Ϣ findNaviMapObject
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
