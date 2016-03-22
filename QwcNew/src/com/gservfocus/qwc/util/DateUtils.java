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
		// ��ȡ��ǰʱ�� 
		return formatter.format(curDate);
	}
}
