package com.tmind.kite.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * <p>
 * Title: Framework
 * </p>
 * <p>
 * Description: Framework
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: tmind.com
 * </p>
 * @version 1.0 日期公用类
 * 
 */
public class DateUtils {
	public static String getChar8() {
		return DateFormatUtils.format(new Date(), "yyyyMMdd");
	}

	public static String getCha6() {
		return DateFormatUtils.format(new Date(), "yyyyMM");
	}

	public static String getChar14() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 取当前时间的字符串，精确到分钟
	 * 
	 * 
	 * @return 取当前时间字符串,格式为“yyyyMMddHHmm”
	 * 
	 * 
	 */
	public static String getChar12() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmm");
	}

	public static String getChar6() {
		return DateFormatUtils.format(new Date(), "HHmmss");
	}

	public static String formatChar6(String char6) {
		if (char6 == null || char6.length() == 0)
			return char6;
		return char6.substring(0, 4) + "-" + char6.substring(4, 6);

	}

	public static String formatChar8(String char8) {
		if (char8 == null || char8.length() == 0)
			return "";
		return char8.substring(0, 4) + "-" + char8.substring(4, 6) + "-" + char8.substring(6, 8);

	}

	public static String formatChar10(String char10) {
		if (char10 == null || char10.length() == 0)
			return "";
		return char10.substring(0, 4) + "-" + char10.substring(4, 6) + "-" + char10.substring(6, 8) + " "
				+ char10.substring(8, 10);

	}

	public static String formatChar12(String char12) {
		if (char12 == null || char12.length() == 0)
			return "";
		return char12.substring(0, 4) + "-" + char12.substring(4, 6) + "-" + char12.substring(6, 8) + " "
				+ char12.substring(8, 10) + ":" + char12.substring(10, 12);

	}

	public static String formatChar14(String char14) {
		if (char14 == null || char14.length() == 0)
			return char14;
		return char14.substring(0, 4) + "-" + char14.substring(4, 6) + "-" + char14.substring(6, 8) + " "
				+ char14.substring(8, 10) + ":" + char14.substring(10, 12) + ":" + char14.substring(12, 14);
	}

	/**
	 * 判断是否超过指定的结束时间
	 * 
	 * @param srcBeginDate
	 *            String 开始时间
	 * @param srcEndDate
	 *            String 结束时间（有两种可能性：1：相对开始时间后的天数或者月数，2：绝对的结束时间）
	 * @param relatetivelyFlag
	 *            int 相对标志（1：天数，2：月数）
	 * @return boolean
	 */
	public static boolean judgeIfExceedEndDate(String srcBeginDate, String srcEndDate, int relatetivelyFlag) {
		if (srcEndDate.trim().length() != 8) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(srcBeginDate.substring(0, 4)), Integer.parseInt(srcBeginDate.substring(4, 6)),
					Integer.parseInt(srcBeginDate.substring(6, 8)));
			if (relatetivelyFlag == 1) {
				cal.roll(Calendar.DAY_OF_YEAR, Integer.parseInt(srcEndDate));
				cal.roll(Calendar.YEAR, Integer.parseInt(srcEndDate) / 365);
			} else if (relatetivelyFlag == 2) {
				cal.roll(Calendar.MONTH, Integer.parseInt(srcEndDate));
				cal.roll(Calendar.YEAR, Integer.parseInt(srcEndDate) / 12);
			}
			srcEndDate = formatToChar8(cal);
		}
		if (Long.parseLong(getChar8()) >= Long.parseLong(srcEndDate))
			return true;
		return false;
	}

	/**
	 * 将当前日期向后滚动n个月
	 * 
	 * @param srcDate
	 *            String 当前日期
	 * @param rollMonth
	 *            String 待滚动的月数
	 * @return String eg:20081013,2->20081213; 20081013,3->20090113;
	 *         20080213,-1->20080113; 20080213,-2->20071213;
	 */
	public static String rollMonth(String srcDate, String rollMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(srcDate.substring(0, 4)), Integer.parseInt(srcDate.substring(4, 6)) - 1, Integer
				.parseInt(srcDate.substring(6, 8)));
		cal.roll(Calendar.MONTH, Integer.parseInt(rollMonth));
		formatToChar8(cal);
		// 当当前日期与待滚动的月数之和超过12（不包括12）时，需要进行年份的滚动
		int yearRollNum = 0;// 年份滚动数目
		if (Integer.parseInt(rollMonth) > 0) {// 月份正向滚动
			yearRollNum = (Integer.parseInt(srcDate.substring(4, 6)) + Integer.parseInt(rollMonth) - 1) / 12;
		} else if (Integer.parseInt(rollMonth) < 0) {// 月份反向滚动
			// 滚动后的月份值
			int rolledMonthNum = Integer.parseInt(srcDate.substring(4, 6)) + Integer.parseInt(rollMonth);
			// 若滚动后的月份值大于0，说明仍在当前月，不需要年份转换，反之，需要年份转换，不满12个月的，需要减去一年
			if (rolledMonthNum <= 0) {
				yearRollNum = (rolledMonthNum / 12) - 1;
			}
		}

		cal.roll(Calendar.YEAR, yearRollNum);

		return formatToChar8(cal);
	}

	public static String formatToChar8(Calendar tmpcal) {
		String tmpYear = Integer.toString(tmpcal.get(Calendar.YEAR));
		String tmpMonth = Integer.toString(tmpcal.get(Calendar.MONTH) + 1);
		String tmpDay = Integer.toString(tmpcal.get(Calendar.DAY_OF_MONTH));
		String tmpDate = tmpYear + (tmpMonth.length() == 1 ? "0" + tmpMonth : tmpMonth)
				+ (tmpDay.length() == 1 ? "0" + tmpDay : tmpDay);
		return tmpDate;
	}

	public static String formatCha6(String char6) {
		if (char6 == null || char6.length() == 0)
			return char6;
		return char6.substring(0, 2) + ":" + char6.substring(2, 4) + ":" + char6.substring(4, 6);
	}

	/**
	 * 获取指定日期 向前或向后滚动特定天数后的日期
	 * @param dateNow String 当前日期
	 * @param rollDate String 待滚动的月数
	 * @return String 指定日期 +/- 特定天数 后的日期（格式 CCYYMMDD）
	 */
	public static String rollDate(String dateNow, int rollDate) {
		String dateReturn = "";
		if (dateNow == null || dateNow.trim().length() < 8)
			return dateReturn;

		dateNow = dateNow.trim();
		Calendar cal = Calendar.getInstance();
		int nYear = Integer.parseInt(dateNow.substring(0, 4));
		int nMonth = Integer.parseInt(dateNow.substring(4, 6));
		int nDate = Integer.parseInt(dateNow.substring(6, 8));
		cal.set(nYear, nMonth - 1, nDate);
		cal.add(Calendar.DATE, rollDate);
		String strYear = String.valueOf(cal.get(Calendar.YEAR));
		String strMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String strDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		strMonth = (strMonth.length() == 1) ? "0" + strMonth : strMonth;
		strDay = (strDay.length() == 1) ? "0" + strDay : strDay;
		dateReturn = strYear + strMonth + strDay;
		return dateReturn;
	}

	/***************************************************************************
	 * 返回当天所在的年份
	 * 
	 * @return yyyy
	 */
	public static String getYearByCurrentDate() {
		return getChar8().substring(0, 4);
	}

	public static String getYearByInputDate(String year) {
		if (year == null || year.length() < 4) {
			return getYearByCurrentDate();
		}
		return year.substring(0, 4);
	}

	/***************************************************************************
	 * 返回当天所在的日期
	 * 
	 * @return mm
	 */
	public static String getDateByCurrentDate() {
		return getChar8().substring(6, 8);
	}

	/***************************************************************************
	 * 返回当天所在的月份
	 * 
	 * @return mm
	 */
	public static String getMonthByCurrentDate() {
		return getChar8().substring(4, 6);
	}

	public static String getMonthByInputDate(String month) {
		if (month == null || month.length() < 6) {
			return getChar8().substring(4, 6);
		}
		return month.substring(5, 7);
	}

	public static String formatDate(String date, String formatter) {
		SimpleDateFormat myFormatter = null;
		Date da = null;
		if (date.length() < 15)
			myFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		else
			myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			da = myFormatter.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DateFormatUtils.format(da, formatter);
	}

	/**
	 * 将字符串格式的时间转换成日期对象
	 * @param date
	 * @param formatter
	 * @return
	 */
	public static Date convertString2Date(String date) {
		SimpleDateFormat myFormatter = null;
		Date da = null;
		if (date.length() < 15)
			myFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		else
			myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			da = myFormatter.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return da;
	}

	/**
	 * 转化日期时间（yyyyMMddHHmmss）的格式
	 * 
	 * @param date
	 *            String 日期时间
	 * @param informatter
	 *            String 输入的格式
	 * 
	 * @param outformatter
	 *            String 输出的格式
	 * 
	 * @return String
	 */
	public static String formatDate(String date, String informatter, String outformatter) {
		Date da = null;
		try {
			SimpleDateFormat myFormatter = new SimpleDateFormat(informatter);
			da = myFormatter.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DateFormatUtils.format(da, outformatter);
	}

	/**
	 * 取得指定日期n个工作日之后的日期，工作日（周一到周五）
	 * 
	 * @param date
	 *            指定起始日期
	 * @param n
	 *            n个工作日
	 * @return
	 */
	public static String getDateNWorkDayAfter(String date, int n) {
		int m = 0;
		Calendar cal = Calendar.getInstance();

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date2 = format.parse(date);
			cal.setTime(date2);
			if (cal.get(Calendar.DAY_OF_WEEK) == 5 || cal.get(Calendar.DAY_OF_WEEK) == 6) {
				m = (n / 7) * 7 + 4;
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
				m = (n / 7) * 7 + 3;
			} else {
				m = (n / 7) * 7 + 2;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rollDate(date, m);
	}

	/**
	 * 取得当前日期n个工作日之后的日期，工作日（周一到周五）
	 * 
	 * @param n
	 *            n个工作日
	 * @return
	 */
	public static String getDateNWorkDayAfterNow(int n) {
		return getDateNWorkDayAfter(getChar8(), n);
	}

	/**
	 * 获取指定年和月中的天数
	 * @param year
	 * @param mon
	 * @return
	 */
	public static int getDaysInMonth(int year,int mon)   {   
		GregorianCalendar date = new GregorianCalendar(year,mon,1);   
		date.add(Calendar.DATE,-1);   
		return (date.get(Calendar.DAY_OF_MONTH));   
	}
	
	public static void main(String[] args) {
		
		Date currentTime = DateUtils.convertString2Date(DateUtils.getChar14());
		
		Date dateTime = DateUtils.convertString2Date("2015-01-10 22:20:10");
		
		long diff = currentTime.getTime() - dateTime.getTime();
		
		long minutes = (diff/(60*1000));
		
		if(minutes>20){
			System.out.println(String.valueOf(minutes)+":已经解锁，可以重新登陆");
		}
	}
}
