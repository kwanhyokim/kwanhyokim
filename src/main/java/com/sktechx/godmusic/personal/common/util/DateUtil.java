package com.sktechx.godmusic.personal.common.util;

import org.apache.commons.lang.time.DateUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Kobe.
 *
 * 기존 fe api 에서 이관
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time PM 4:36
 */
public class DateUtil {
	private static TimeZone timeZone;

	static {
		try {
			timeZone = TimeZone.getTimeZone("GMT+09:00");

		} catch (Exception e) {}
	}

	/**
	 * 현재 날짜를 반환한다(Date type).
	 *
	 * @return 현재 날짜
	 */
	public static Date getDate() {
		Calendar cal = Calendar.getInstance(timeZone, Locale.KOREAN);

		return cal.getTime();
	}

	/**
	 * 현재 날짜에서 offset(sec)만큼 더한 날짜를 반환한다. offset은 초(sec)단위이며 이전 날짜를 계산할 경우 음수 값도
	 * 가능하다.
	 *
	 * @param offset 현재 날짜에서의 offset
	 * @return 현재 날짜
	 */
	public static Date getDate(long offset) {
		Calendar cal = Calendar.getInstance(timeZone, Locale.KOREAN);

		cal.setTime(new Date(cal.getTime().getTime() + (offset * 1000)));
		return cal.getTime();
	}

	public static Date getDateOffsetDay(int offsetDay) {
		Calendar cal = Calendar.getInstance(timeZone, Locale.KOREAN);

		cal.add(cal.DATE, offsetDay);
		return cal.getTime();
	}

	/**
	 * 특정 날짜에서 offset(sec)만큼 더한 날짜를 반환한다. offset은 초(sec)단위이며 이전 날짜를 계산할 경우 음수 값도
	 * 가능하다.
	 *
	 * @param date   특정 날짜
	 * @param offset 현재 날짜에서의 offset
	 * @return 현재 날짜
	 */
	public static Date getDate(Date date, long offset) {
		return new Date(date.getTime() + (offset * 1000));
	}

	/**
	 * format에 맞추어 현재 날짜를 String으로 반환한다. (예 : "yyyy-MM-dd HH:mm:ss:SSS")
	 *
	 * @param format 날짜의 format
	 * @return 현재 날짜
	 */
	public static String getDateString(String format) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);

		simpleFormat.setTimeZone(timeZone);
		return simpleFormat.format(getDate());
	}

	/**
	 * "yyyy-MM-dd HH:mm:ss"형식의 현재 날짜를 String으로 반환한다.
	 *
	 * @return 현재 날짜
	 */
	public static String getDateString() {
		return getDateString("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * format에 맞추어 현재 날짜를 long 타입 숫자로 반환한다.
	 *
	 * @param format 날짜 형식
	 * @return long long형태의 현재 날짜
	 */
	public static  long getDateLong(String format) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);

		simpleFormat.setTimeZone(timeZone);
		return Long.parseLong(simpleFormat.format(getDate()));
	}

	/**
	 * "yyyyMMddHHmmss"형식의 현재 날짜를 long으로 반환한다.
	 *
	 * @return long long형태의 현재 날짜
	 */
	public static long getDateLong() {
		return getDateLong("yyyyMMddHHmmss");
	}

	/**
	 * 특정 날짜를 반환한다(Date type).
	 *
	 * @param year   년
	 * @param month  월
	 * @param day    일
	 * @param hour   시
	 * @param minute 분
	 * @param second 초
	 * @return 현재 날짜 object
	 */
	public static Date getDate(int year, int month, int day, int hour,
	                           int minute, int second) {
		GregorianCalendar cal = new GregorianCalendar(timeZone, Locale.KOREAN);

		cal.set(year, month - 1, day, hour, minute, second);
		return cal.getTime();
	}

	/**
	 * format에 맞추어 주어진 날짜를 String으로 반환한다.(예 : "yyyy-MM-dd HH:mm:ss:SSS")
	 *
	 * @param date   String으로 표현할 날짜
	 * @param format 날짜의 format
	 * @return String으로 변환된 날짜
	 */
	public static String dateToString(Date date, String format) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);

		simpleFormat.setTimeZone(timeZone);
		return simpleFormat.format(date);
	}

	/**
	 * format에 맞추어 주어진 String을 Date로 변환한다.
	 *
	 * @param dateString String으로 표현된 날짜
	 * @param format     날짜의 format
	 * @return Date로 변환된 날짜
	 * @throws java.text.ParseException Sring parsing 에러 발생시
	 */
	public static Date stringToDate(String dateString, String format)
			throws ParseException {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);

		simpleFormat.setTimeZone(timeZone);
		return simpleFormat.parse(dateString);
	}

	/**
	 * format에 맞추어 주어진 날짜를 long으로 반환한다.(예 : "yyyy-MM-dd HH:mm:ss:SSS")
	 *
	 * @param date   String으로 표현할 날짜
	 * @param format 날짜의 format
	 * @return long으로 변환된 날짜
	 */
	public static long dateToLong(Date date, String format) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);

		simpleFormat.setTimeZone(timeZone);
		return Long.valueOf(simpleFormat.format(date));
	}

	/**
	 * format에 맞추어 주어진 long 타입 숫자을 Date로 변환한다.
	 *
	 * @param dateLong Long으로 표현된 날짜
	 * @param format   날짜의 format
	 * @return Date Date로 변환된 날짜
	 * @throws java.text.ParseException String parsing 에러 발생시
	 */
	public static Date longToDate(long dateLong, String format)
			throws ParseException {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);

		simpleFormat.setTimeZone(timeZone);
		return simpleFormat.parse(Long.toString(dateLong));
	}

	/**
	 * format에 맞추어 주어진 long 타입 날짜(yyyyMMddHHmmss)를 String으로 변환한다.
	 *
	 * @param dateLong Long으로 표현된 날짜
	 * @param format   날짜의 format
	 * @return String String로 변환된 날짜
	 * @throws java.text.ParseException
	 */
	public static String longToString(long dateLong, String format)
			throws ParseException {
		return dateToString(longToDate(dateLong, "yyyyMMddHHmmss"), format);
	}

	/**
	 * 두 Date의 날짜 차이를 date1 - date2 방식으로 계산한다.
	 *
	 * @param date1 operand date 1
	 * @param date2 operand date 2
	 * @return 날짜 차이
	 */
	public static int getAfterDays(Date date1, Date date2) {
		return (int) ((date1.getTime() - date2.getTime()) / 86400000);
	}

	/**
	 * java.util.Date 를 java.util.GregorianCalendar 로 변환한다.
	 *
	 * @param date
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar dateToGregorianCalendar(Date date) {
		if (date == null) return null;
		GregorianCalendar cal = new GregorianCalendar(timeZone, Locale.KOREAN);

		cal.setTime(date);

		return cal;
	}

	/**
	 * java.util.Date 를 javax.xml.datatype.XMLGregorianCalendar 로 변환한다.
	 *
	 * @param date
	 * @return XMLGregorianCalendar
	 * @throws javax.xml.datatype.DatatypeConfigurationException
	 *
	 */
	public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date)
			throws DatatypeConfigurationException {
		if (date == null) return null;

		return DatatypeFactory.newInstance().newXMLGregorianCalendar(
				dateToGregorianCalendar(date));
	}

	/**
	 * javax.xml.datatype.XMLGregorianCalendar 를  java.util.Date 로 변환한다.
	 *
	 * @param calendar
	 * @return java.util.Date
	 */
	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar calendar) {
		if (calendar == null) return null;

		return calendar.toGregorianCalendar().getTime();
	}

	/**
	 * XML스키마의 xs:dateTime 타입 날짜 스트링을 java.util.Date 로 변환한다.
	 *
	 * @param date xs:dateTime 타입 날짜 스트링
	 * @return java.util.Date
	 */
	public static Date parseXmlDateTime(String date) {
		return javax.xml.bind.DatatypeConverter.parseDateTime(date).getTime();
	}

	/**
	 * java.util.Date 를 XML스키마의 xs:dateTime 타입 날짜 스트링으로 변환한다.
	 *
	 * @param date java.util.Date
	 * @return xs:dateTime 타입 날짜 스트링
	 */
	public static String printXmlDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = new GregorianCalendar();

		cal.setTime(date);
		return javax.xml.bind.DatatypeConverter.printDateTime(cal);
	}

	/**
	 * 날짜여부 체크
	 * @param year : 년
	 * @param month : 월
	 * @param day : 일
	 * @return boolean : 날짜여부
	 */
	public static  boolean isDate(int year, int month, int day) {
		return (toDate(year, month, day) != null);
	}

	/**
	 * 날짜여부 체크
	 * @param dateStr : 년월일
	 * @return boolean : 날짜여부
	 */
	public static boolean isDate(String dateStr) {
		return (toDate(dateStr) != null);
	}

	/**
	 * 날짜여부 체크
	 * @param dateStr : 년월일
	 * @param format : 날짜형식(ex : yyyyMMdd, yyyy-MM-dd...)
	 * @return boolean : 날짜여부
	 */
	public static boolean isDate(String dateStr, String format) {
		return (toDate(dateStr, format) != null);
	}

	/**
	 * 원하는 날짜 Date 생성
	 * @param year : 년
	 * @param month : 월
	 * @param day : 일
	 * @return Date : 원하는 날짜의 Date객체
	 */
	public static Date toDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setLenient(false);
		cal.set(year, month - 1, day);
		return cal.getTime();
	}

	/**
	 * 원하는 날짜 Date 생성
	 * @param dateStr : 년월일(yyyy-MM-dd)
	 * @return Date : 원하는 날짜의 Date객체
	 */
	public static Date toDate(String dateStr) {
		return toDate(dateStr.replaceAll("[-|/|.]", ""), "yyyyMMdd");
	}

	/**
	 * 원하는 날짜 Date 생성
	 * @param dateStr : 년월일
	 * @param format : 날짜형식(ex : yyyyMMdd, yyyy-MM-dd...)
	 * @return Date : 원하는 날짜의 Date객체
	 */
	public static Date toDate(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException pe) {
			return null;
		}
	}

	/**
	 * Date 객체를 String으로 변환(yyyy-MM-dd)
	 * @param date : 날짜객체
	 * @return String : 날짜의 String 객체(yyyy-MM-dd)
	 */
	public static String toString(Date date) {
		return toString(date, "yyyy-MM-dd");
	}

	/**
	 * Date 객체를 String으로 변환
	 * @param date : 날짜객체
	 * @param format : 원하는 날짜형식(ex : yyyyMMdd, yyyy-MM-dd...)
	 * @return String : 날짜의 String 객체
	 */
	public static String toString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 두 날짜 사이의 차
	 * @param startday : 시작일
	 * @param endday : 종료일
	 * @return 두 날짜 사이의 차
	 */
	public static long getDateDiff(Date startday, Date endday) {
		long diff = endday.getTime() - startday.getTime();
		return (diff / (1000 * 60 * 60 * 24));
	}

	/**
	 * 시간의 차이(분) 구하기
	 * @param start : 시작일시
	 * @return 시간의 차이(분) 계산
	 */
	public static long getDtimeMinDiff(String start, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date startday = sdf.parse(start, new ParsePosition(0));
		long startTime = startday.getTime();

		//현재의 시간 설정
		Calendar cal= Calendar.getInstance();
		Date endDate = cal.getTime();
		long endTime = endDate.getTime();

		long mills = endTime-startTime;

		//분으로 변환
		long min = mills/60000;
		return min;
	}

	/**
	 * yyyymmdd 형식의 데이터를 cnt 만큼 더한 날짜를 반환
	 *
	 * @param	String yyyymmdd
	 * @param	int cnt
	 * @return		String
	 */
	public static String getPlusDate(String yyyymmdd, int cnt){
		if (yyyymmdd.length() != 8) return "";

		String s_yy = yyyymmdd.substring(0, 4);
		String s_mm = yyyymmdd.substring(4, 6);
		String s_dd = yyyymmdd.substring(6, 8);

		int yy =  Integer.parseInt(s_yy) ; // - 1900;
		int mm =  Integer.parseInt(s_mm) - 1;
		int dd =  Integer.parseInt(s_dd);

		//---- 날짜를 계산한다. ----------
		Calendar cal  = Calendar.getInstance();
		cal.set(Calendar.YEAR  ,yy      );
		cal.set(Calendar.MONTH ,mm      ); //월은 0부터 시작함
		cal.set(Calendar.DATE  ,dd + cnt);

		int year = cal.get(Calendar.YEAR  );
		int month= cal.get(Calendar.MONTH ) +1; //월은 0부터 시작함
		int day  = cal.get(Calendar.DATE  );

		String appy_year  = Integer.toString(year );
		String appy_month = Integer.toString(month);
		String appy_day   = Integer.toString(day);

		if (month < 10) appy_month = "0" + appy_month;
		if (day   < 10) appy_day   = "0" + appy_day;

		return appy_year + appy_month + appy_day;
	}

	/**
	 * yyyymmdd 형식의 데이터를 cnt 만큼 뺀 날짜를 반환
	 *
	 * @param	String yyyymmdd
	 * @param	int cnt
	 * @return	String
	 */
	public static String getMinusDate(String yyyymmdd, int cnt){
		if (yyyymmdd.length() != 8) return "";

		String s_yy = yyyymmdd.substring(0, 4); //년 4자리
		String s_mm = yyyymmdd.substring(4, 6); //월 2자리
		String s_dd = yyyymmdd.substring(6, 8); //일 2자리

		int yy =  Integer.parseInt(s_yy) ; // - 1900;
		int mm =  Integer.parseInt(s_mm) - 1;
		int dd =  Integer.parseInt(s_dd);

		//---- 날짜를 계산한다. ----------
		Calendar cal  = Calendar.getInstance();
		cal.set(Calendar.YEAR  ,yy      );
		cal.set(Calendar.MONTH ,mm      ); //월은 0부터 시작함
		cal.set(Calendar.DATE  ,dd - cnt);

		int year = cal.get(Calendar.YEAR  );
		int month= cal.get(Calendar.MONTH ) +1; //월은 0부터 시작함
		int day  = cal.get(Calendar.DATE  );

		String appy_year  = Integer.toString(year );
		String appy_month = Integer.toString(month);
		String appy_day   = Integer.toString(day);

		if (month < 10) appy_month = "0" + appy_month;
		if (day   < 10) appy_day   = "0" + appy_day;

		return appy_year + appy_month + appy_day;
	}

	/**
	 * @Param String fomat : 날짜포멧
	 *
	 * @return 파라미터로 받은 날짜포멧을 적용하여 현재 날짜를 구하고 리턴한다.
	 * @throws Exception
	 */
	public static String getCurDate(final String fomat) {
		java.text.SimpleDateFormat dateFormatter = null;
		String result = null;

		try {
			dateFormatter = new java.text.SimpleDateFormat(fomat);
			result = dateFormatter.format(new java.util.Date());

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
		return  result;
	}

	/**
	 * @ 오늘의 요일 이름을 구한다.
	 */
	public static String getCurDayName() {
		// 1     2     3     4     5     6     7
		final String[] week = { "일", "월", "화", "수", "목", "금", "토" };
		return week[Calendar.getInstance( ).get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 해당월에 마지막 일자 구하기
	 * yyyymm
	 * @param yearMonth
	 * @return
	 */
	public static String getLastDayOfMonth( String yearMonth)  {
		Calendar cal = Calendar.getInstance() ;
		int year = Integer.parseInt( yearMonth.substring(0,4) ) ;
		int month = Integer.parseInt( yearMonth.substring(4,6) ) ;

		cal.set(year, month-1,1);

		StringBuilder strBuild = new StringBuilder();
		strBuild.append(year);
		strBuild.append(month);
		strBuild.append(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return strBuild.toString();
	}

	/**
	 * "YYYYMMDDHHMISS"형식의 현재 날짜를 String으로 반환한다.
	 *
	 * @return 현재 날짜
	 */
	public static String getDateOrderString() {
		return getDateString("yyyyMMddHHmmss");
	}

	/**
	 * 오늘 날짜 시간 이후 분초는 절삭
	 * 오늘 날짜 한시간 이후 분초는 절삭
	 *
	 * @return 현재 날짜
	 */
	public static Date getDateStartEnd() {
		Date startDate = getDate();


		String strStartDate = dateToString(startDate, "yyyyMMddHHmmss");

		Date startDateResult = null;
		try {
			startDateResult = stringToDate(strStartDate.substring(0, 10) + "0000", "yyyyMMddHHmmss");
		} catch (ParseException e) {
		}

		return startDateResult;
	}

	public static Date getFirstDateOfMonth() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getLastDateTimeSecondOfMonth() {
		return DateUtils.addSeconds(DateUtils.addMonths(getFirstDateOfMonth(), 1), -1);
	}

	public static String getCurrentDateTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
