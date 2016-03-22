package com.gservfocus.qwc.bean;

import com.gservfocus.qwc.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class Teacher {

	private boolean isFirstLogin = true;
	private boolean isBoy = true;
	public boolean isFirstLogin() {
		return isFirstLogin;
	}
	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public boolean isBoy() {
		return isBoy;
	}
	public void setBoy(boolean isBoy) {
		this.isBoy = isBoy;
	}

	public static void saveTeacher(Context context,Teacher teacher) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = context.getSharedPreferences("teacher_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstLogin", teacher.isFirstLogin);
		editor.putBoolean("isBoy", teacher.isBoy);
		editor.commit();
	}

	public static Teacher getTeacher(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("teacher_info",
				Context.MODE_PRIVATE);
		Teacher teacher = new Teacher();
		teacher.setFirstLogin(preferences.getBoolean("isFirstLogin", true));
		teacher.setBoy(preferences.getBoolean("isBoy", true));
		return teacher;
	}
}
