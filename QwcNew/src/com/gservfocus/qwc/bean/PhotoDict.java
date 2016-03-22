package com.gservfocus.qwc.bean;

import java.util.ArrayList;

public class PhotoDict {
	private String date;
	private ArrayList<Photo> photos;
	public PhotoDict(){
		
	}
	public PhotoDict(String date,ArrayList<Photo> photos){
		this.date = date;
		this.photos = photos;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

}
