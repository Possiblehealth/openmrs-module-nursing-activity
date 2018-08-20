package org.openmrs.module.nursingactivity.service;

import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequest;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

import java.util.Date;
import java.util.List;

public interface NursingActivityService {
  List<NursingActivitySchedule> getScheduleEntriesForPatient(String patientUuid, Date startDate, Date endDate);

  String createSchedules(MedicineScheduleRequest medicineScheduleRequest);
}
