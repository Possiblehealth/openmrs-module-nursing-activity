package org.openmrs.module.nursingactivity.service;

import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

import java.util.List;

public interface NursingActivityService {
  List<NursingActivitySchedule> getScheduleEntriesForPatient(String patient);
}
