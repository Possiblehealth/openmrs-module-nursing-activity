package org.openmrs.module.nursingactivity.web.mapper;

import org.hibernate.proxy.HibernateProxy;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.nursingactivity.model.MedicationAdministrationSchedule;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class NursingActivityScheduleMapper {
  public NursingActivityScheduleMapper() {
  }

  public List<Object> createResponse(List<NursingActivitySchedule> scheduleEntriesForPatient) {
	// System.out.println(scheduleEntriesForPatient);
    return scheduleEntriesForPatient.stream().map(schedule -> this.mapToDefaultResponse(schedule)).collect(Collectors.toList());
  }

  private Object mapToDefaultResponse(NursingActivitySchedule schedule) {
    Map map = new HashMap();
    map.put("scheduleId", schedule.getScheduleId());
    map.put("patient", mapPatient(schedule.getPatient()));
    map.put("scheduledTime", schedule.getScheduleTime());
    map.put("status", schedule.getStatus());
    Order order = schedule.getOrder();
    if (order != null) {
      map.put("order", mapOrder(order));
    }
    if (isMedicationAdministrationSchedule(schedule)) {
      map.put("drug", mapDrug(schedule));
    }
    return map;
  }

  private Map mapDrug(NursingActivitySchedule schedule) {
    Locale locale = Context.getUserContext().getLocale();
    MedicationAdministrationSchedule medicationSchedule = (MedicationAdministrationSchedule) schedule;
    Map drugMap = new HashMap();
    drugMap.put("id", medicationSchedule.getDrug().getDrugId());
    drugMap.put("dose", medicationSchedule.getDose());
    drugMap.put("doseUnits", medicationSchedule.getDoseUnits().getName(locale).getName());
    drugMap.put("route", medicationSchedule.getRoute().getName(locale).getName());
    drugMap.put("drugName", medicationSchedule.getDrug().getFullName(locale));
    return drugMap;
  }

  private boolean isMedicationAdministrationSchedule(NursingActivitySchedule schedule) {
    return MedicationAdministrationSchedule.class.isAssignableFrom(getActualType(schedule));
  }

  private Class<?> getActualType(Object persistentObject) {
    Class<?> type = persistentObject.getClass();
    if (persistentObject instanceof HibernateProxy) {
      type = ((HibernateProxy) persistentObject).getHibernateLazyInitializer().getPersistentClass();
    }
    return type;
  }

  private Map mapOrder(Order order) {
    Map orderMap = new HashMap();
    orderMap.put("uuid", order.getUuid());
    orderMap.put("orderNumber", order.getOrderNumber());
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
    personNameMap.put("givenName", personName.getGivenName());
    personNameMap.put("middleName", personName.getMiddleName());
    personNameMap.put("familyName", personName.getFamilyName());
    personNameMap.put("fullName", personName.getFullName());

    Map patientMap = new HashMap();
    patientMap.put("personName", personNameMap);
    patientMap.put("uuid", patient.getUuid());
    patientMap.put("identifier", patient.getPatientIdentifier().getIdentifier());
    return patientMap;
  }
}
