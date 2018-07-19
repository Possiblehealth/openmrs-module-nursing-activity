package org.openmrs.module.nursingactivity.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.service.NursingActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
  public List<NursingActivitySchedule> getScheduleEntriesForPatient(String patientUuid) {
    Patient patient = patientService.getPatientByUuid(patientUuid);
    if (patient == null) {
      throw new IllegalArgumentException("Patient is required when fetching active orders");
    }
    return nursingActivityScheduleDao.getScheduleEntriesForPatient(patient);
  }

}
