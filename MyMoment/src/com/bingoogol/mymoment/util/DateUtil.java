package com.bingoogol.mymoment.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期操作工具类
 * 
 * @author 王浩 bingoogol@sina.com
 */
public class DateUtil {
	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat publishSDf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private DateUtil() {
	}

	public static String getDateString() {
		String result = dateSdf.format(new Date());
		if (result.charAt(8) == '0') {
			result = result.replace("月0", "月");
		}
		if (result.charAt(5) == '0') {
			result = result.replace("年0", "年");
		}
		return result;
	}

	public static String getTimeString() {
		return timeSdf.format(new Date());
	}

	public static String getPublishTime() {
		return publishSDf.format(new Date());
	}
}
