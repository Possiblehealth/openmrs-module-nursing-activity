package org.openmrs.module.nursingactivity.web.mapper;

import org.openmrs.Patient;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.web.contract.NursingActivityScheduleDefaultResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NursingActivityScheduleMapper {

  public NursingActivityScheduleDefaultResponse constructResponse(NursingActivitySchedule nursingActivitySchedule) {
    return this.mapToDefaultResponse(nursingActivitySchedule, new NursingActivityScheduleDefaultResponse());
  }

  private NursingActivityScheduleDefaultResponse mapToDefaultResponse(NursingActivitySchedule nursingActivitySchedule, NursingActivityScheduleDefaultResponse response) {
    response.setScheduleId(nursingActivitySchedule.getScheduleId());
    response.setPatient(createPatientMap(nursingActivitySchedule.getPatient()));
    response.setStartDateTime(nursingActivitySchedule.getScheduleTime());
    response.setStatus(nursingActivitySchedule.getStatus());
    return response;
  }

  private Map createPatientMap(Patient p) {
    Map map = new HashMap();
    map.put("name", p.getPersonName().getFullName());
    map.put("uuid", p.getUuid());
    map.put("identifier", p.getPatientIdentifier().getIdentifier());
    return map;
  }

  public List<NursingActivityScheduleDefaultResponse> constructResponse(List<NursingActivitySchedule> scheduleEntriesForPatient) {
    return scheduleEntriesForPatient.stream().map(as -> this.mapToDefaultResponse(as, new NursingActivityScheduleDefaultResponse())).collect(Collectors.toList());
  }
}
