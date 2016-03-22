package com.gservfocus.qwc.net;

import com.gservfocus.qwc.bean.VersionInfo;


public interface ApkUpdaterApi {
	/**
	 * download apk包
	 * @param uri要安装的apk网络路径
	 * @param localPath要存放在本地的位置
	 * */
	void downloadApk(String url,String localPath);
	/** 
	 * 安装apk文件
	 * 	@param apkName要安装的apk名称
	 * */
	void installApk(String apkName) ;

	/**
	 *  卸载apk文件
	 *  @param apkName要卸载的apk名称
	 *  */
	void uninstallApk(String apkName);

	/**
	 *  获取当前的版本号
	 *  @param apkName要卸载的apk名称
	 *  */
	int getVersionCode();
	/**
	 *  获取当前包名
	 *  @param apkName要卸载的apk名称
	 *  */
	public String getPackName() ;
	/**
	 *  卸载apk文件
	 *  @param apkName要卸载的apk名称
	 *  */
	void isUpdatePrompt(VersionInfo versionInfo);
}
