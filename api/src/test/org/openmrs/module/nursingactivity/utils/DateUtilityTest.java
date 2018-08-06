package org.openmrs.module.nursingactivity.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.naming.Reference;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DateUtilityTest {
  @Test
  public void shouldParseDate() throws ParseException {
    Date date = DateUtility.parseDate("2018-12-12");
    Assert.assertNotNull(date);
  }

  @Test(expected = ParseException.class)
  public void shouldNotParseInvalidDate() throws ParseException {
    DateUtility.parseDate("2018-24-12");
  }

  @Test
  public void shouldGiveNullWhenGivenDateStringIsNull() throws ParseException {
    Assert.assertNull(DateUtility.parseDate(null));
  }

  @Test
  public void shouldGiveAddNoOfDaysAskedToAdd() throws ParseException {
    Date date = DateUtility.parseDate("2018-12-10");
    Date expectedDate = DateUtility.parseDate("2018-12-17");
    Assert.assertEquals(DateUtility.addDays(date, 7),expectedDate);
  }

  @Test
  public void shouldVerifyEndDateLaterThanStartDate() throws ParseException {
    int result = DateUtility.compare(DateUtility.parseDate("2018-01-12"), DateUtility.parseDate("2018-02-12"));
    Assert.assertTrue("startDate should be less than endDate. Actual: " + result, result < 0);

    result = DateUtility.compare(DateUtility.parseDate("2018-02-12"), DateUtility.parseDate("2018-02-11"));
    Assert.assertTrue("startDate should be greater than endDate. Actual: " + result, result > 0);

    result = DateUtility.compare(DateUtility.parseDate("2018-02-12"), DateUtility.parseDate("2018-02-12"));
    Assert.assertTrue("startDate should be equal to endDate. Actual: " + result, result == 0);
  }

  @Test
  public void shouldGetStartOfWeekWhenDateIsGiven() throws ParseException {
    Date result = DateUtility.getWeekStart( DateUtility.parseDate("2018-07-25"),3);
    Assert.assertEquals(DateUtility.parseDate("2018-07-24"),result);
  }

  @Test
  public void shouldGiveFalseWhenGivenListConsistsOneInvalidTimeString() {
    ArrayList<String> timings = new ArrayList<>();
    timings.add("12:30");
    timings.add("29:30");
    Assert.assertFalse(DateUtility.areAllValidTimeStrings(timings));
  }

  @Test
  public void shouldGiveTrueWhenGivenListConsistsAllValidTimeString() {
    ArrayList<String> timings = new ArrayList<>();
    timings.add("10:30");
    timings.add("12:30");
    timings.add("16:30");
    Assert.assertTrue(DateUtility.areAllValidTimeStrings(timings));
  }

  @Test
  public void shouldGiveTrueWhenGivenEmptyList() {
    ArrayList<String> timings = new ArrayList<>();
    Assert.assertTrue(DateUtility.areAllValidTimeStrings(timings));
  }

  @Test
  public void shouldGiveTrueWhenGivenDateIsSameAsStartDate() throws ParseException {
    Date startDate = DateUtility.parseDate("2018-12-10");
    Date endDate = DateUtility.parseDate("2018-12-13");
    Assert.assertTrue(DateUtility.isBetween(startDate,endDate,startDate));
  }

  @Test
  public void shouldGiveFalseWhenGivenDateIsBeforeStartDate() throws ParseException {
    Date givenDate = DateUtility.parseDate("2018-12-9");
    Date startDate = DateUtility.parseDate("2018-12-10");
    Date endDate = DateUtility.parseDate("2018-12-13");
    Assert.assertFalse(DateUtility.isBetween(startDate,endDate,givenDate));
  }

  @Test
  public void shouldGiveTrueWhenGivenDateIsAfterStartDateAndBeforeEndDate() throws ParseException {
    Date givenDate = DateUtility.parseDate("2018-12-12");
    Date startDate = DateUtility.parseDate("2018-12-10");
    Date endDate = DateUtility.parseDate("2018-12-13");
    Assert.assertTrue(DateUtility.isBetween(startDate,endDate,givenDate));
  }

  @Test
  public void shouldGiveTrueWhenGivenDateIsSameAsEndDate() throws ParseException {
    Date givenDate = DateUtility.parseDate("2018-12-13");
    Date startDate = DateUtility.parseDate("2018-12-10");
    Date endDate = DateUtility.parseDate("2018-12-13");
    Assert.assertTrue(DateUtility.isBetween(startDate,endDate,givenDate));
  }

  @Test
  public void shouldGiveFalseWhenGivenDateAfterEndDate() throws ParseException {
    Date givenDate = DateUtility.parseDate("2018-12-14");
    Date startDate = DateUtility.parseDate("2018-12-10");
    Date endDate = DateUtility.parseDate("2018-12-13");
    Assert.assertFalse(DateUtility.isBetween(startDate,endDate,givenDate));
  }

  @Test
  public void shouldGiveTimeInLongInteger() {
    long time = DateUtility.parseTime("1:30");
    int sec = 1000;
    int secInMin = 60;
    Assert.assertEquals(time,90 * secInMin * sec);
  }

  @Test
  public void shouldGiveTimeInLongIntegerForAnyTime() {
    long time = DateUtility.parseTime("14:30");
    int sec = 1000;
    int secInMin = 60;
    Assert.assertEquals(time,(14*60+30)* secInMin * sec);
  }

  @Test
  public void name() {

    ArrayList<String> timings = new ArrayList<>();
    timings.add("11:30");
    int j=0;
    String timeString = timings.get(j);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime time = LocalTime.parse(timeString, formatter);
    System.out.println(time.getHour());
    System.out.println(time.getMinute());
  }
}
