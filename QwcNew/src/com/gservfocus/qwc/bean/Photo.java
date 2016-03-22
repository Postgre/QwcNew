package com.gservfocus.qwc.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Photo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public static byte[] serializable2Data(Photo photo) {
		byte[] data = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(photo);
			data = baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static Photo serializable4Data(byte[] data) {
		Photo photo = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			photo = (Photo) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return photo;
	}
	public Boolean getIsChoose() {
		return isChoose;
	}
	public void setIsChoose(Boolean isChoose) {
		this.isChoose = isChoose;
	}
	private String path;
	private String date;
	private Boolean isChoose = false;
}
