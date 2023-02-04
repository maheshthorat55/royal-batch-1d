package com.skillup.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	
	public static Date getDateFromYYYYMMDD(String date) throws ParseException{
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateformatter.parse(date);
	}
	
	public static Date getDateToday() throws ParseException{
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateformatter.parse(dateformatter.format(new Date()));
	}
	
	public static String getDrawDateTime(Date date, String time) throws ParseException{
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateformatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateformatter2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		String dateTime = dateformatter.format(date) + " " + time;
		return dateformatter2.format(dateformatter1.parse(dateTime));
	}

	public static String getStringDateTime() throws ParseException{
		SimpleDateFormat dateformatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		return dateformatter.format(new Date());
	}
	public static String getTimeTwelveHour(String resultTime) {
		SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
	    SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
	    try {
			Date date = parseFormat.parse(resultTime);
			return displayFormat.format(date);
		} catch (ParseException e) {
		}
		return resultTime;
	}
	
	public static String getNextGameTime() {
		Date whateverDateYouWant = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(whateverDateYouWant);
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		int unroundedMinutes = calendar.get(Calendar.MINUTE);
		int mod = unroundedMinutes % 15;
		calendar.add(Calendar.MINUTE, mod < 0 ? -mod : (15-mod));
		return df.format(calendar.getTime());
	}

}
