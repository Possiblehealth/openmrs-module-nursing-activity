package org.openmrs.module.nursingactivity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

  public static boolean areAllValidTimeStrings(ArrayList<String> timings) {
    for (int i=0;i<timings.size();i++){
      try{
        String timeString = timings.get(i);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(timeString, formatter);
      }
      catch (DateTimeParseException dateTimeParseException){
        return false;
      }
    }
    return true;
  }

  public static boolean isBetween(Date startDate, Date endDate, Date date) {
    boolean isBetween = startDate.before(date) && endDate.after(date);
    boolean isSameAsStartDate = startDate.equals(date);
    boolean isSameAsEndDate = endDate.equals(date);
    return isSameAsStartDate || isBetween || isSameAsEndDate;
  }

  public static long parseTime(String timeString) {

    return 0;
  }
}
