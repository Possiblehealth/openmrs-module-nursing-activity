package org.openmrs.module.nursingactivity.dao.impl;

import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class NursingActivityScheduleDaoImpl implements NursingActivityScheduleDao {

  private SessionFactory sessionFactory;

  @Autowired
  public NursingActivityScheduleDaoImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public List<NursingActivitySchedule> getScheduleEntriesForPatient(Patient patient) {
    NursingActivitySchedule schedule = new NursingActivitySchedule();
    schedule.setPatient(patient);
    return Collections.singletonList(schedule);
  }
}
