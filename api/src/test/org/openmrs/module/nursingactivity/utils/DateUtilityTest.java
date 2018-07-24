package org.openmrs.module.nursingactivity.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
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
}
