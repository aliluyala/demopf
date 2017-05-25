package com.ydy258.ydy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 根据时间字符串返回Date对象
	 * @param dateStr,可以接受3种格式分别是:yyyy-MM-dd,yyyy-MM-dd HH:mm,yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getDateByStr(String dateStr) {
		SimpleDateFormat formatter = null;
		if (dateStr.length() == 10)
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		else if (dateStr.length() == 7)
			formatter = new SimpleDateFormat("yyyy-MM");
		else if (dateStr.length() == 13)
			formatter = new SimpleDateFormat("yyyy-MM-dd:HH");
		else if (dateStr.length() == 16)
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		else if (dateStr.length() == 19)
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else {
			System.out.println("日期字符串格式错�?!");
			return null;
		}
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回日期的字符串
	 * @param date Date对象
	 * @param format 例如:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getStrByDate(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return date!=null?formatter.format(date):"";
	}

	/**
	 * 返回日期的字符串,
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getStrYMDByDate(Date date) {
		return getStrByDate(date, "yyyy-MM-dd");
	}

	public static String getStrYMByDate(Date date) {
		return getStrByDate(date, "yyyy-MM");
	}
	
	public static String getStrByDate(Date date) {
		return getStrByDate(date, "yyyyMMddHHmmss");
	}
	/**
	 * 返回日期的字符串,�?:�?:�?
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getStrHMSByDate(Date date) {
		return getStrByDate(date, "HH:mm:ss");
	}

	/**
	 * 返回日期的字符串,
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getStrYMDHMSByDate(Date date) {
		return getStrByDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回日期的字符串,年月
	 * @param date
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getStrYMDHMByDate(Date date) {
		return getStrByDate(date, "yyyy-MM-dd HH:mm");
	}
	
	
	/**
	 * 返回日期的字符串,小时
	 * @param date
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getStrHHByDate(Date date) {
		return getStrByDate(date, "HH");
	}

	/**
	 * 对天数进行加减运�?
	 * @param date 原来的时�?
	 * @param days 正数为加,负数为减
	 * @return 返回运算后的时间
	 */
	public static Date addDay(Date date, Integer days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 对月数进行加减运�?
	 * @param date 原来的时�?
	 * @param days 正数为加,负数为减
	 * @return 返回运算后的时间
	 */
	public static Date addMonth(Date date, Integer months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	
	public static String dateToShortCode() {
		Date date = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat(
				"yyyyMMdd");
		String s = simpledateformat.format(date);
		return s;
	}

	/**
	 * 返回中文时间格式
	 * @param object 可以为Date对象�?2007-06-12格式的字符串
	 * @return
	 */
	public static String toChinese(Object object) {
		String dateStr = null;
		if (object instanceof Date)
			dateStr = getStrYMDByDate((Date) object);
		else if (object instanceof String)
			dateStr = (String) object;
		else
			return dateStr;
		String[] cnArray = { "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?" };
		String year = dateStr.split("-")[0];
		String month = dateStr.split("-")[1];
		String date = dateStr.split("-")[2];
		dateStr = "";
		for (int i = 0; i < year.length(); i++)
			dateStr += cnArray[Integer.valueOf(String.valueOf(year.charAt(i)))];
		dateStr += "�?";
		if ("10".equals(month))
			dateStr += "�?";
		else {
			int num = Integer.valueOf(String.valueOf(month.charAt(1)));
			if ("0".equals(String.valueOf(month.charAt(0))))
				dateStr += cnArray[num];
			else
				dateStr += "�?" + cnArray[num];
		}
		dateStr += "�?";
		if ("10".equals(date))
			dateStr += "�?";
		else {
			String temp = String.valueOf(date.charAt(0));
			if ("1".equals(temp))
				dateStr += "�?";
			else if ("2".equals(temp))
				dateStr += "二十";
			else if ("3".equals(temp))
				dateStr += "三十";
			if (!"0".equals(String.valueOf(date.charAt(1))))
				dateStr += cnArray[Integer.valueOf(String.valueOf(date.charAt(1)))];
		}
		dateStr += "�?";
		return dateStr;
	}

	/**
	 * 返回星期�?
	 * @param object Date对象或�?�字符串,yyyy-MM-dd
	 * @return 星期�?
	 */
	@SuppressWarnings("deprecation")
	public static String getWeek(Object object) {
		Date date = null;
		if (object instanceof Date)
			date = (Date) object;
		else if (object instanceof String)
			date = getDateByStr((String) object);
		else
			return "";
		String[] cnWeek = { "�?", "�?", "�?", "�?", "�?", "�?", "�?" };
		return "星期" + cnWeek[date.getDay()];
	}

	public static Date get00_00_00Date(Date date) {
		return getDateByStr(getStrYMDByDate(date));
	}

	public static Date get23_59_59Date(Date date) {
		return getDateByStr(getStrYMDHMSByDate(date).substring(0, 10) + " 23:59:59");
	}

	public static Integer changeSecond(String hms) {
		if (hms == null || "".equals(hms)) {
			return null;
		}

		String[] t = hms.split(":");
		int hour = Integer.valueOf("0" + t[0]);
		int min = Integer.valueOf("0" + t[1]);
		int sec = Integer.valueOf("0" + t[2]);

		return hour * 3600 + min * 60 + sec;
	}

	/**
	 * 返回多少时间�?
	 * @param date
	 * @return
	 */
	public static String getPreTime(Date date) {
		long time = date.getTime();
		Long result = 0L;
		result = (new Date().getTime() - time);
		result = result / 1000L;
		if (result < 60L) {
			return result + "秒前";
		}
		if (result >= 60L && result < 3600L) {
			result = result / 60;
			return result + "分钟�?";
		}
		if (result >= 3600L && result < 86400L) {
			result = result / 3600;
			return result + "小时�?";
		} else {
			result = result / 3600 / 24;
			return result + "天前";
		}
	}

	/**
	 * 取传入日期后的第�?个星期一(包括传入的日�?)
	 * @param startTime
	 * @return
	 */
	public static Date getFirstMondayAfter(Date startTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTime();
	}

	/**
	 * 当前�?
	 * @return
	 */
	public static int getCurrYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 当前�?
	 * @return
	 */
	public static int getCurrMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 当前�?
	 * @return
	 */
	public static int getCurrDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * �?
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * �?
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * �?
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取传入时间的当月第一�?
	 * @param date
	 * @return
	 */
	public static Date getFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	/**
	 * 取传入时间的当月�?后一�?
	 * @param date
	 * @return
	 */
	public static Date getLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}
	
	public static String getFristTime(String yearStr,String monthstr){
		if(StringUtil.isNotEmpty(yearStr) && StringUtil.isNotEmpty(monthstr)){
			return yearStr+"-"+monthstr+"-"+"01 00:00:00";
		}
		return null;
	}
	
	public static String getLastTime(String yearStr,String monthstr){
		if(StringUtil.isEmpty(yearStr) || StringUtil.isEmpty(monthstr)){
			return null;
		}
		int month=Integer.parseInt(monthstr);
		String LastTime=null;
		if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
			LastTime=yearStr+"-"+monthstr+"-"+"31 59:59:59";
		}else if(month==2){
			int year=Integer.parseInt(yearStr);
			if(year%4==0){
				LastTime=yearStr+"-"+monthstr+"-"+"29 59:59:59";
			}else{
				LastTime=yearStr+"-"+monthstr+"-"+"28 59:59:59";
			}
		}else{
			LastTime=yearStr+"-"+monthstr+"-"+"30 59:59:59";
		}
		return LastTime;
	}
}