package org.openmrs.module.nursingactivity.dao;

import org.openmrs.Patient;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

import java.util.Date;
import java.util.List;

public interface NursingActivityScheduleDao {
  List<NursingActivitySchedule> getScheduleEntriesForPatient(Patient patient, Date startDate, Date endDate);
}
