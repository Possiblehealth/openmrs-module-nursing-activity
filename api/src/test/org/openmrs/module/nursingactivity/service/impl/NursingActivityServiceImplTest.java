package org.openmrs.module.nursingactivity.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.utils.DateUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NursingActivityServiceImplTest {
  @Mock
  private PatientService patientService;
  @Mock
  private NursingActivityScheduleDao nursingActivityScheduleDao;

  private Patient patient;
  private NursingActivityServiceImpl nursingActivityService;

  @Before
  public void setUp() {
    patient = new Patient();
    nursingActivityService = new NursingActivityServiceImpl(patientService, nursingActivityScheduleDao);
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


  private Date parseDateTime(String dateString) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return format.parse(dateString);
  }


}
