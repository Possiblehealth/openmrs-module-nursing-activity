package org.openmrs.module.nursingactivity.service.impl;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.service.NursingActivityService;
import org.openmrs.module.nursingactivity.utils.DateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.openmrs.module.nursingactivity.constants.MedicineScheduleConstants.START_OF_WEEK;

@Service
@Transactional
public class NursingActivityServiceImpl implements NursingActivityService {

  private NursingActivityScheduleDao nursingActivityScheduleDao;

  private PatientService patientService;

  @Autowired
  public NursingActivityServiceImpl(PatientService patientService, NursingActivityScheduleDao nursingActivityScheduleDao) {
    this.patientService = patientService;
    this.nursingActivityScheduleDao = nursingActivityScheduleDao;
  }

  @Override
  public List<NursingActivitySchedule> getScheduleEntriesForPatient(String patientUuid, Date startDate, Date endDate) {
    Patient patient = patientService.getPatientByUuid(patientUuid);
    if (patient == null) {
      throw new IllegalArgumentException("Patient is required when fetching active orders");
    }
    if (startDate == null && endDate == null){
      startDate = DateUtility.getWeekStart(new Date(),START_OF_WEEK);
    }
    if (startDate == null){
      startDate = DateUtility.addDays(endDate,-6);
    }
    if (endDate == null) {
      endDate = DateUtility.addDays(startDate,6);
    }
    if (DateUtility.compare(startDate, endDate) > 0) {
      throw new IllegalArgumentException("Given start date is after end date");
    }
    Date endOfDay = DateUtility.getEndOfDay(endDate);
    return nursingActivityScheduleDao.getScheduleEntriesForPatient(patient, startDate, endOfDay);
  }

}
