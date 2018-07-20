package org.openmrs.module.nursingactivity.web.mapper;

import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NursingActivityScheduleMapper {
  public NursingActivityScheduleMapper() {
  }

  public List<Object> createResponse(List<NursingActivitySchedule> scheduleEntriesForPatient) {
    return scheduleEntriesForPatient.stream().map(schedule -> this.mapToDefaultResponse(schedule)).collect(Collectors.toList());
  }

  private Object mapToDefaultResponse(NursingActivitySchedule schedule) {
    Map map = new HashMap();
    map.put("scheduledId", schedule.getScheduleId());
    map.put("patient", mapPatient(schedule.getPatient()));
    map.put("scheduledTime", schedule.getScheduleTime());
    map.put("order", mapOrder(schedule.getOrder()));
    map.put("status", schedule.getStatus());
    return map;
  }

  private Map mapOrder(Order order) {
    Map orderMap = new HashMap();
    orderMap.put("uuid", order.getUuid());
    orderMap.put("date", order.getDateCreated());
    Map providerMap = new HashMap();
    providerMap.put("uuid", order.getOrderer().getUuid());
    providerMap.put("name", order.getOrderer().getName());
    orderMap.put("orderer", providerMap);
    return orderMap;
  }

  private Object mapPatient(Patient patient) {
    PersonName personName = patient.getPersonName();
    Map personNameMap = new HashMap();
    personNameMap.put("givenName",personName.getGivenName());
    personNameMap.put("middleName",personName.getMiddleName());
    personNameMap.put("familyName",personName.getFamilyName());
    personNameMap.put("fullName",personName.getFullName());

    Map patientMap = new HashMap();
    patientMap.put("personName", personNameMap);
    patientMap.put("uuid", patient.getUuid());
    patientMap.put("identifier", patient.getPatientIdentifier().getIdentifier());
    return patientMap;
  }
}
