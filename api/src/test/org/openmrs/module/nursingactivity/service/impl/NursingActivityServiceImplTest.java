package org.openmrs.module.nursingactivity.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequest;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.utils.DateUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openmrs.module.nursingactivity.FrequencyType.WEEKLY;
import static org.openmrs.module.nursingactivity.constants.MedicineScheduleConstants.START_OF_WEEK;


@RunWith(MockitoJUnitRunner.class)
public class NursingActivityServiceImplTest {
  @Mock
  private PatientService patientService;
  @Mock
  private NursingActivityScheduleDao nursingActivityScheduleDao;
  @Mock
  private OrderService orderService;

  private Patient patient;
  private NursingActivityServiceImpl nursingActivityService;

  @Before
  public void setUp() {
    patient = new Patient();
    nursingActivityService = new NursingActivityServiceImpl(patientService, nursingActivityScheduleDao, orderService);
  }


  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorWhenProvidedStartDateIsAfterEndDate() throws ParseException {
    Date startDate = DateUtility.parseDate("2018-01-10");
    Date endDate = DateUtility.parseDate("2018-01-09");
    when(patientService.getPatientByUuid("XYZ")).thenReturn(patient);
    nursingActivityService.getScheduleEntriesForPatient("XYZ", startDate, endDate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorWhenIllegalPatientUuidIsProvided() throws ParseException {
    Date startDate = DateUtility.parseDate("2018-01-09");
    Date endDate = DateUtility.parseDate("2018-01-10");
    when(patientService.getPatientByUuid("XYZ")).thenReturn(null);
    nursingActivityService.getScheduleEntriesForPatient("XYZ", startDate, endDate);
  }

  @Test
  public void shouldConsiderBeginAndStartOfDatesWhileFetchingSchedules() throws ParseException {
    Date startDate = DateUtility.parseDate("2018-01-09");
    Date endDate = DateUtility.parseDate("2018-01-10");
    when(patientService.getPatientByUuid("XYZ")).thenReturn(patient);
    nursingActivityService.getScheduleEntriesForPatient("XYZ", startDate, endDate);
    verify(nursingActivityScheduleDao, Mockito.atLeastOnce())
        .getScheduleEntriesForPatient(Mockito.eq(patient),
            Mockito.eq(parseDateTime("2018-01-09 00:00:00")),
            Mockito.eq(parseDateTime("2018-01-10 23:59:59")));

  }

  @Test
  public void shouldConsiderEndDateAs7DaysFromStartDateWhenEndDateIsNull() throws ParseException {
    Date startDate = DateUtility.parseDate("2018-01-09");
    when(patientService.getPatientByUuid("XYZ")).thenReturn(patient);
    nursingActivityService.getScheduleEntriesForPatient("XYZ", startDate, null);
    verify(nursingActivityScheduleDao, Mockito.atLeastOnce())
        .getScheduleEntriesForPatient(Mockito.eq(patient),
            Mockito.eq(parseDateTime("2018-01-09 00:00:00")),
            Mockito.eq(parseDateTime("2018-01-15 23:59:59")));

  }

  @Test
  public void shouldConsiderStartDateAs7DaysPastFromEndDateWhenStartDateIsNull() throws ParseException {
    Date endDate = DateUtility.parseDate("2018-01-15");
    when(patientService.getPatientByUuid("XYZ")).thenReturn(patient);
    nursingActivityService.getScheduleEntriesForPatient("XYZ", null, endDate);
    verify(nursingActivityScheduleDao, Mockito.atLeastOnce())
        .getScheduleEntriesForPatient(Mockito.eq(patient),
            Mockito.eq(parseDateTime("2018-01-09 00:00:00")),
            Mockito.eq(parseDateTime("2018-01-15 23:59:59")));

  }

  @Test
  public void shouldCalculateStartAndEndFromTodayWhenBothEndAndStartDateAreNull() throws ParseException {
    Date date = new Date();
    Date startDate = DateUtility.getWeekStart(date, START_OF_WEEK);
    Date endDate = DateUtility.addDays(startDate, 6);
    endDate = DateUtility.getEndOfDay(endDate);
    when(patientService.getPatientByUuid("XYZ")).thenReturn(patient);
    nursingActivityService.getScheduleEntriesForPatient("XYZ", null, null);
    verify(nursingActivityScheduleDao, Mockito.atLeastOnce())
        .getScheduleEntriesForPatient(Mockito.eq(patient),
            Mockito.eq(startDate),
            Mockito.eq(endDate));

  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorWhenIllegalPatientUuidIsProvidedWhileCreatingSchedules() {
    when(patientService.getPatientByUuid("XYZ")).thenReturn(null);
    ArrayList<DayOfWeek> days = new ArrayList<>();
    MedicineScheduleRequest scheduleRequest = new MedicineScheduleRequest("XYZ", "order123", new ArrayList<>(), WEEKLY, days);
    nursingActivityService.createSchedules(scheduleRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorWhenTimingsConsistsArrayWhichDoesNotFollowTimeFormat() {
    when(patientService.getPatientByUuid("XYZ")).thenReturn(null);
    ArrayList<String> timings = new ArrayList<>();
    timings.add("11:23");
    timings.add("111:23");
    ArrayList<DayOfWeek> days = new ArrayList<>();
    MedicineScheduleRequest scheduleRequest = new MedicineScheduleRequest("XYZ", "order123", timings, WEEKLY, days);
    nursingActivityService.createSchedules(scheduleRequest);
  }

  private Date parseDateTime(String dateString) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return format.parse(dateString);
  }


}
