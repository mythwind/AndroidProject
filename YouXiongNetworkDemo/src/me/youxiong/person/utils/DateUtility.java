package me.youxiong.person.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtility {
	private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String getFirstDayOfMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date()) + "-01";
	}
	
	/**
	 *  根据年月，获取月末的日期
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, 0);
		return mSimpleDateFormat.format(cal.getTime());
	}
	
	/** 
	 *  取得某天所在周的第一天 
	 *   @param date 
	 *   @return 
	 */
	public static String getFirstDayOfWeek(Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		c.setTime(calendar.getTime());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return sdf.format(c.getTime());
	}
	
	/** 
	 *  取得某天所在周的最后一天 
	 *   @param date 
	 *   @return 
	 */
	public static String getLastDayOfWeek(Calendar calendar) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(calendar.getTime());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
		return mSimpleDateFormat.format(c.getTime());
	}
	
	/**
	 *  将时间格式补为标准格式
	 * @param date
	 * @return
	 */
	public static String completeDate(int date) {
		if(date < 0) {
			return "";
		} else {
			return (date < 10) ? ("0" + date) : ("" + date);
		}
	}
	
	/**
	 * 把 Date 类型格式化为字符串类型
	 * @param date 日期
	 * @param formatStr 格式化之后显示的日期格式的字符串
	 * @return
	 */
	public static String formatStrDate(Date date, String formatStr) {
		if(date == null) 
			return "";
		SimpleDateFormat formater = new SimpleDateFormat(formatStr);
		return formater.format(date);
	}
	/**
	 * 把 字符串类型格式化为Date 类型
	 * @param strDate  字符串类型的日期
	 * @param formatStr 格式化之后的日期格式
	 * @return
	 */
	public static Date formatDate(String strDate, String formatStr) {
		if(strDate == null||"".equals(strDate)) 
			return null;
		SimpleDateFormat formater = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = formater.parse(strDate);
		} catch (ParseException e) {
		}
		return date;
	}
	public static Date formatDate(String strDate) {
		return formatDate(strDate, "yyyy-MM-dd");
	}
	
}
