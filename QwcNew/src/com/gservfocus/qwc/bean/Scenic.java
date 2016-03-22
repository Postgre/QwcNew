package com.gservfocus.qwc.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Scenic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; // ������
	private String name; // ��������
	private String imageUrl; // ����ͼƬ
	private String abstracts; // ������
	private String ShareNum; // ������
	private String LikeNum; // �ղ���
	private String ParentName;
	private String ParentID;
	private String isHot; // �Ƿ������Ƽ�
	private String ParentImageUrl;
	private String QrScore = "0";
	private ArrayList<ChildScenic> Child;
	

	public String getQrScore() {
		return QrScore;
	}

	public void setQrScore(String qrScore) {
		QrScore = qrScore;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public ArrayList<ChildScenic> getChild() {
		return Child;
	}

	public void setChild(ArrayList<ChildScenic> child) {
		Child = child;
	}

	public String getParentName() {
		return ParentName;
	}

	public void setParentName(String parentName) {
		ParentName = parentName;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public String getParentImageUrl() {
		return ParentImageUrl;
	}

	public void setParentImageUrl(String parentImageUrl) {
		ParentImageUrl = parentImageUrl;
	}

	public String getShareNum() {
		return ShareNum;
	}

	public void setShareNum(String shareNum) {
		ShareNum = shareNum;
	}

	public String getLikeNum() {
		return LikeNum;
	}

	public void setLikeNum(String likeNum) {
		LikeNum = likeNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	/**
	 * ������л�����
	 * 
	 * @param account
	 *            �˺Ŷ���
	 * @return
	 */
	public static byte[] serializable2Data(Scenic scenic) {
		byte[] data = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(scenic);
			data = baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * ������л�����
	 * 
	 * @param lbAccount
	 * @return
	 */
	public static Scenic serializable4Data(byte[] data) {
		Scenic scenic = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			scenic = (Scenic) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return scenic;
	}
}
