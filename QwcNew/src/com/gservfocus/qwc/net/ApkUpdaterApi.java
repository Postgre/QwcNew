package com.gservfocus.qwc.net;

import com.gservfocus.qwc.bean.VersionInfo;


public interface ApkUpdaterApi {
	/**
	 * download apk��
	 * @param uriҪ��װ��apk����·��
	 * @param localPathҪ����ڱ��ص�λ��
	 * */
	void downloadApk(String url,String localPath);
	/** 
	 * ��װapk�ļ�
	 * 	@param apkNameҪ��װ��apk����
	 * */
	void installApk(String apkName) ;

	/**
	 *  ж��apk�ļ�
	 *  @param apkNameҪж�ص�apk����
	 *  */
	void uninstallApk(String apkName);

	/**
	 *  ��ȡ��ǰ�İ汾��
	 *  @param apkNameҪж�ص�apk����
	 *  */
	int getVersionCode();
	/**
	 *  ��ȡ��ǰ����
	 *  @param apkNameҪж�ص�apk����
	 *  */
	public String getPackName() ;
	/**
	 *  ж��apk�ļ�
	 *  @param apkNameҪж�ص�apk����
	 *  */
	void isUpdatePrompt(VersionInfo versionInfo);
}
