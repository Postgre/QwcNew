package com.gservfocus.qwc;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.annotation.SuppressLint;

import com.gservfocus.qwc.bean.Agricola;
import com.gservfocus.qwc.bean.Specialty;

public class SearchModel {

	@SuppressLint("DefaultLocale")
	public static <T> ArrayList<T> getSearchList(ArrayList<T> array, String[] methodNames,String key) {
		ArrayList<T> resultArray = null;
		Class<?> currentClass = null;
		if (array != null && array.size() > 0) {
			currentClass = array.get(0).getClass();
			resultArray = new ArrayList<T>();
			for (int i = 0; i < array.size(); i++) {
				try {
					
					for (String methodString : methodNames) {

						Method method = currentClass.getMethod(
								"get" + methodString.substring(0, 1).toUpperCase()
										+ methodString.substring(1));
						String info = (String) method.invoke(array.get(i));
						if(info != null && info.contains(key)){
							resultArray.add(array.get(i));
							break;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} 
		}
		return resultArray;
	}
}
