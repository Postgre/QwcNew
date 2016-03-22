package com.gservfocus.qwc.bean;

public class VersionInfo {
	private String PROD_CODE;	//apk编号
	private String PROD_VERSION;//apk版本号
	private String PROD_SIZE;	//apk大小
	private String IS_DB_UPDATE;	//apk是否进行数据更新
	private String PROD_RELEASE_DATE;	//apk发布日期
	private String PROD_DOWNLOAD_URL;	//apk下载地址
	public String getPROD_CODE() {
		return this.PROD_CODE;
	}
	public void setPROD_CODE(String pROD_CODE) {
		PROD_CODE = pROD_CODE;
	}
	public String getPROD_VERSION() {
		return PROD_VERSION;
	}
	public void setPROD_VERSION(String pROD_VERSION) {
		PROD_VERSION = pROD_VERSION;
	}
	public String getPROD_SIZE() {
		return PROD_SIZE;
	}
	public void setPROD_SIZE(String pROD_SIZE) {
		PROD_SIZE = pROD_SIZE;
	}
	public String getIS_DB_UPDATE() {
		return IS_DB_UPDATE;
	}
	public void setIS_DB_UPDATE(String iS_DB_UPDATE) {
		IS_DB_UPDATE = iS_DB_UPDATE;
	}
	public String getPROD_RELEASE_DATE() {
		return PROD_RELEASE_DATE;
	}
	public void setPROD_RELEASE_DATE(String pROD_RELEASE_DATE) {
		PROD_RELEASE_DATE = pROD_RELEASE_DATE;
	}
	public String getPROD_DOWNLOAD_URL() {
		return PROD_DOWNLOAD_URL;
	}
	public void setPROD_DOWNLOAD_URL(String pROD_DOWNLOAD_URL) {
		PROD_DOWNLOAD_URL = pROD_DOWNLOAD_URL;
	}
	
}
