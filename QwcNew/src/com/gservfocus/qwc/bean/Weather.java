package com.gservfocus.qwc.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Weather implements Parcelable{

	String tempnow;
	String temp;
	String type;
	String date;
	String week;
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Weather() {
		super();
	}
	
	public Weather(String type,String temp,  String date,
			String week) {
		super();
		
		this.type = type;
		this.temp = temp;
		this.date = date;
		this.week = week;
	}
	public String getTempnow() {
		return tempnow;
	}
	public void setTempnow(String tempnow) {
		this.tempnow = tempnow;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int describeContents() {
		
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {

		dest.writeString(type);  
		dest.writeString(temp);
		dest.writeString(date);
		dest.writeString(week);
		
	}
	
	public static final Creator<Weather> CREATOR = new Creator<Weather>() {  
	        public Weather createFromParcel(Parcel source) {  
           //先构造位置，后构造名称(与构造方法的顺序关联)  
            return new Weather(source.readString(),source.readString(),source.readString(),source.readString());  
	        }  
	  
	        public Weather[] newArray(int size) {  
		           return new Weather[size];  
	        }  
	    };  

}
