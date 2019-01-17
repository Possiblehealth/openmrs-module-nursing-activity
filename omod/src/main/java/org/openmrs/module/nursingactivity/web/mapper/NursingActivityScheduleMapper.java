package org.openmrs.module.nursingactivity.web.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.proxy.HibernateProxy;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.nursingactivity.model.MedicationAdministrationSchedule;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

public class NursingActivityScheduleMapper {
	public NursingActivityScheduleMapper() {
	}

	public List<Object> createResponse(List<NursingActivitySchedule> scheduleEntriesForPatient) {
		// System.out.println(scheduleEntriesForPatient);
		return scheduleEntriesForPatient.stream().map(schedule -> this.mapToDefaultResponse(schedule))
				.collect(Collectors.toList());
	}

	private Object mapToDefaultResponse(NursingActivitySchedule schedule) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleId", schedule.getScheduleId());
		map.put("patient", mapPatient(schedule.getPatient()));
		if (schedule.getAdministeredBy() != null) {
			map.put("provider", mapProvider(schedule.getAdministeredBy()));
		}
		map.put("scheduledTime", schedule.getScheduleTime());
		map.put("actualTime", schedule.getActualTime());
		map.put("status", schedule.getActivityStatus().getStatus());
		Order order = schedule.getOrder();
		if (order != null) {
			map.put("order", mapOrder(order));
		}
		if (isMedicationAdministrationSchedule(schedule)) {
			map.put("drug", mapDrug(schedule));
		}
		return map;
	}

	private Map<String, Object> mapDrug(NursingActivitySchedule schedule) {
		Locale locale = Context.getUserContext().getLocale();
		MedicationAdministrationSchedule medicationSchedule = (MedicationAdministrationSchedule) schedule;
		Map<String, Object> drugMap = new HashMap<String, Object>();
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

	private Map<String, Object> mapOrder(Order order) {
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("uuid", order.getUuid());
		orderMap.put("orderNumber", order.getOrderNumber());
		orderMap.put("date", order.getDateCreated());
		Map<String, String> providerMap = new HashMap<String, String>();
		providerMap.put("uuid", order.getOrderer().getUuid());
		providerMap.put("name", order.getOrderer().getName());
		orderMap.put("orderer", providerMap);
		return orderMap;
	}

	private Object mapProvider(User provider) {
		PersonName personName = provider.getPersonName();
		Map<String, String> personNameMap = new HashMap<String, String>();
		personNameMap.put("givenName", personName.getGivenName());
		personNameMap.put("middleName", personName.getMiddleName());
		personNameMap.put("familyName", personName.getFamilyName());
		personNameMap.put("fullName", personName.getFullName());

		Map<String, Object> patientMap = new HashMap<String, Object>();
		patientMap.put("providerName", personNameMap);
		patientMap.put("uuid", provider.getUuid());
		return patientMap;
	}

	private Object mapPatient(Patient patient) {
		PersonName personName = patient.getPersonName();
		Map<String, String> personNameMap = new HashMap<String, String>();
		personNameMap.put("givenName", personName.getGivenName());
		personNameMap.put("middleName", personName.getMiddleName());
		personNameMap.put("familyName", personName.getFamilyName());
		personNameMap.put("fullName", personName.getFullName());

		Map<String, Object> patientMap = new HashMap<String, Object>();
		patientMap.put("personName", personNameMap);
		patientMap.put("uuid", patient.getUuid());
		patientMap.put("identifier", patient.getPatientIdentifier().getIdentifier());
		return patientMap;
	}
}
