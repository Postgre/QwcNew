package com.gservfocus.qwc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.bean.Teacher;
import com.gservfocus.qwc.bean.Weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class Constants {
	public static ArrayList<Weather> weathers;
	public static Account mAccount = null;
	public static int isWxLogin = 0;
	public static Teacher mTeacher = null;
	public static int ScreenWidth = 0;
	public static int ScreenHeight = 0;
	public static float ScreenDensity = 0;
	public static String isShareWhat;
	public static String isShareWhatID;
	public static String SceUrl = "http://www.qwc.org.cn/ScenicDetail.aspx?ID=";
	public static String AgrUrl = "http://www.qwc.org.cn/AgricolaDetail.aspx?ID=";
	public static String SpeUrl = "http://www.qwc.org.cn/SpecialtyDetail.aspx?ID=";
	public static String About_Qwc = "http://m.qwc.org.cn";
	public static String WXappID = "wx8ef4eb6be6b68675";
	public static String WXSecret = "bd4d1d98d62868623ed4c164b00b6b98";
	public static int ScienicShareNum = -1;
	public static String ScenicName = null;
	public static String OpenId;
	public static String NickName;
	public static String UserImage;
	/** PREFERENCE 数据库所在文件夹名  */
	public static final String PREFERENCE_DBNAME = "QingZhou";
	public static final String APK_UPDATER_DIR = "/update";
	/**获取当前的apk版本*/
	public static final String CMD_CURRENT_VERSION = "GET_CURRENT_VERSION";
	//判断是否有网络连接
	public static boolean isNetworkConnected(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null) {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }  
	    return false;  
	}
	// weather url
	public static final String GET_WEATHER_URL = "http://wthrcdn.etouch.cn/weather_mini?citykey=101021100";
	// 数据库路径
	public static final String DB_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/qwstc";

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获得指定分享内容
	 * 
	 * @return
	 */
	public static String getShareContent(String content) {
		return "#崇明前卫村# " + content + "下载前卫村APP http://www.qwc.org.cn ";
	}
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] getLocalImage2ByteArray(String filePath) {
		byte[] data = null;
		File file = null;
		FileInputStream fis = null;
		try {
			file = new File(filePath);
			if (file != null) {
				fis = new FileInputStream(file);
				data = new byte[fis.available()];
				fis.read(data);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return data;
	}
	
	/**
	 * 获得指定SD卡目录
	 * 
	 * @return
	 */
	public static String getDatabasePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+PREFERENCE_DBNAME;
	}
}
