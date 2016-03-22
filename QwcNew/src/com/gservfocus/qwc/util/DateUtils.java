package com.gservfocus.qwc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	/**
	 * get date 
	 * @param format
	 *    yyyyMMddHHmmss
	 * */
	public static String getDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());
		// 获取当前时间 
		return formatter.format(curDate);
	}
}
