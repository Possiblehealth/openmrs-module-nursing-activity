package org.openmrs.module.nursingactivity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
  public static Date parseDate(String dateString) throws ParseException {
    if (dateString == null){
      return null;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
    return format.parse(dateString);
  }

  public static Date getEndOfDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR, 23);
    calendar.add(Calendar.MINUTE, 59);
    calendar.add(Calendar.SECOND, 59);
    return calendar.getTime();
  }

  public static int compare(Date startDate, Date endDate) {
    return startDate.compareTo(endDate);
  }

  public static Date addDays(Date date, int noOfDaysToAdd) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE,noOfDaysToAdd);
    return calendar.getTime();
  }

  public static Date getWeekStart(Date date, int startOfWeek) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.clear(Calendar.MINUTE);
    calendar.clear(Calendar.SECOND);
    calendar.clear(Calendar.MILLISECOND);
    calendar.set(Calendar.DAY_OF_WEEK, startOfWeek);
    return calendar.getTime();
  }
}
