package org.openmrs.module.nursingactivity.service;

import java.util.Date;
import java.util.List;

import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequest;
import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequestList;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

public interface NursingActivityService {
  List<NursingActivitySchedule> getScheduleEntriesForPatient(String patientUuid, Date startDate, Date endDate);

  String createSchedules(MedicineScheduleRequestList medicineScheduleRequest);
  
  String createSchedules(List<MedicineScheduleRequest> medicineScheduleRequest);
}
