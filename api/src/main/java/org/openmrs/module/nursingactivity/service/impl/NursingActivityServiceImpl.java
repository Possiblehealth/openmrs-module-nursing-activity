package org.openmrs.module.nursingactivity.service.impl;

import org.openmrs.Patient;
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

  @Autowired
  public NursingActivityServiceImpl(NursingActivityScheduleDao nursingActivityScheduleDao) {
    this.nursingActivityScheduleDao = nursingActivityScheduleDao;
  }

  @Override
  public List<NursingActivitySchedule> getScheduleEntriesForPatient(Patient patient) {
    return nursingActivityScheduleDao.getScheduleEntriesForPatient(patient);
  }
}
