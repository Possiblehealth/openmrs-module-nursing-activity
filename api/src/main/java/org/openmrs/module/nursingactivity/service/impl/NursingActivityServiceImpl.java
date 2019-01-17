package org.openmrs.module.nursingactivity.service.impl;

import static org.openmrs.module.nursingactivity.constants.MedicineScheduleConstants.START_OF_WEEK;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.time.DateUtils;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.nursingactivity.FrequencyType;
import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequest;
import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequestList;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.model.MedicationAdministrationSchedule;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.model.NursingActivityStatus;
import org.openmrs.module.nursingactivity.model.NursingActivityType;
import org.openmrs.module.nursingactivity.model.Schedule;
import org.openmrs.module.nursingactivity.service.NursingActivityService;
import org.openmrs.module.nursingactivity.utils.DateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NursingActivityServiceImpl implements NursingActivityService {

	private NursingActivityScheduleDao nursingActivityScheduleDao;

	private PatientService patientService;
	private OrderService orderService;
	private ConceptService conceptService;
	private UserService userService;
	private String NURSING_ACTIVITY_STATUS_SCHEDULED = "SCHEDULED";
	private String NURSING_ACTIVITY_STATUS_ADMINISTERED = "ADMINISTERED";

	@Autowired
	public NursingActivityServiceImpl(PatientService patientService,
			NursingActivityScheduleDao nursingActivityScheduleDao, OrderService orderService,
			ConceptService conceptService, UserService userService) {
		this.patientService = patientService;
		this.nursingActivityScheduleDao = nursingActivityScheduleDao;
		this.orderService = orderService;
		this.conceptService = conceptService;
		this.userService = userService;
	}

	@Override
	public List<NursingActivitySchedule> getScheduleEntriesForPatient(String patientUuid, Date startDate,
			Date endDate) {
		Patient patient = patientService.getPatientByUuid(patientUuid);
		if (patient == null) {
			throw new IllegalArgumentException("Patient is required when fetching active orders");
		}
		if (startDate == null && endDate == null) {
			startDate = DateUtility.getWeekStart(new Date(), START_OF_WEEK);
		}
		if (startDate == null) {
			startDate = DateUtility.addDays(endDate, -6);
		}
		if (endDate == null) {
			endDate = DateUtility.addDays(startDate, 6);
		}
		if (DateUtility.compare(startDate, endDate) > 0) {
			throw new IllegalArgumentException("Given start date is after end date");
		}
		Date endOfDay = DateUtility.getEndOfDay(endDate);
		return nursingActivityScheduleDao.getScheduleEntriesForPatient(patient, startDate, endOfDay);
	}

	@Override
	public String createSchedules(MedicineScheduleRequestList medicineScheduleRequests) {
		for (MedicineScheduleRequest medicineScheduleRequest : medicineScheduleRequests) {
			String patientUuid = medicineScheduleRequest.getPatientUuid();
			Patient patient = patientService.getPatientByUuid(patientUuid);
			ArrayList<String> timings = medicineScheduleRequest.getTimings();
			if (null == medicineScheduleRequest.getSchedules() && !DateUtility.areAllValidTimeStrings(timings)) {
				throw new IllegalArgumentException("Given time strings are not valid");
			}
			if (patient == null) {
				throw new IllegalArgumentException("Patient is required when fetching active orders");
			}
			// TODO:Remove this when you are dealing with order as optional
			// if (order == null) {
			// throw new IllegalArgumentException("Currently order is required to create
			// schedules");
			// }
			if (medicineScheduleRequest.getScheduleType().equals(FrequencyType.WEEKLY)) {
				ArrayList<DayOfWeek> dayOfWeeks = medicineScheduleRequest.getDays();
				if (dayOfWeeks == null || dayOfWeeks.size() == 0) {
					throw new IllegalArgumentException("Days are required for week type schedules");
				}
				saveWeeklyMedicineSchedules(medicineScheduleRequest);
			} else {
				saveDailyMedicationSchedules(medicineScheduleRequest);
			}
		}
		return "{\"stats\":\"added\"}";
	}

	@Override
	public String createSchedules(List<MedicineScheduleRequest> medicineScheduleRequests) {
		for (MedicineScheduleRequest medicineScheduleRequest : medicineScheduleRequests) {
			String patientUuid = medicineScheduleRequest.getPatientUuid();
			Patient patient = patientService.getPatientByUuid(patientUuid);
			ArrayList<String> timings = medicineScheduleRequest.getTimings();
			if (!DateUtility.areAllValidTimeStrings(timings)) {
				throw new IllegalArgumentException("Given time strings are not valid");
			}
			if (patient == null) {
				throw new IllegalArgumentException("Patient is required when fetching active orders");
			}
			// TODO:Remove this when you are dealing with order as optional
			// if (order == null) {
			// throw new IllegalArgumentException("Currently order is required to create
			// schedules");
			// }
			if (medicineScheduleRequest.getScheduleType().equals(FrequencyType.WEEKLY)) {
				ArrayList<DayOfWeek> dayOfWeeks = medicineScheduleRequest.getDays();
				if (dayOfWeeks == null || dayOfWeeks.size() == 0) {
					throw new IllegalArgumentException("Days are required for week type schedules");
				}
				saveWeeklyMedicineSchedules(medicineScheduleRequest);
			} else {
				saveDailyMedicationSchedules(medicineScheduleRequest);
			}
		}
		return "{\"stats\":\"added\"}";
	}

	private void saveDailyMedicationSchedules(MedicineScheduleRequest medicineScheduleRequest) {
		Date startDate = medicineScheduleRequest.getStartingDate();
		Date endDate = medicineScheduleRequest.getEndingDate();
		for (Date scheduledDate = startDate; DateUtility.isBetween(startDate, endDate,
				scheduledDate); scheduledDate = DateUtility.addDays(scheduledDate, 1)) {
			saveSchedulesForADay(medicineScheduleRequest, scheduledDate);
		}
	}

	private void saveSchedulesForADay(MedicineScheduleRequest medicineScheduleRequest, Date scheduledDate) {
		ArrayList<String> timings = medicineScheduleRequest.getTimings();
		Order order = orderService.getOrderByUuid(medicineScheduleRequest.getOrderUuid());
		Patient patient = patientService.getPatientByUuid(medicineScheduleRequest.getPatientUuid());
		Concept doseUnit = conceptService.getConcept(medicineScheduleRequest.getDoseUnits());
		Drug drug = conceptService.getDrugByUuid(medicineScheduleRequest.getDrugUuid());
		Concept route = conceptService.getConcept(medicineScheduleRequest.getRoute());
		User provider = userService.getUserByUuid(Context.getAuthenticatedUser().getUuid());
		NursingActivityType medicationActivity = getNursingActivityType("Medication");
		NursingActivityStatus nursingActivityStatus = getNursingActivityStatus(NURSING_ACTIVITY_STATUS_SCHEDULED);
		List<Schedule> schedules = medicineScheduleRequest.getSchedules();
		ListIterator<Schedule> scheduleIterator = null;
		if (null != schedules) {
			scheduleIterator = schedules.listIterator();
		}

		if (medicationActivity == null) {
			throw new RuntimeException("Can not find Medication Activity");
		}
		
		//To change the nursing status
		if (null != schedules && null == timings) {
			while (scheduleIterator.hasNext()) {
				Schedule scheduleObj = scheduleIterator.next();
				Date scheduleTime = DateUtility.ConvertMilliSecondsToFormattedDate(scheduleObj.getScheduledTime(), Boolean.TRUE);
				Date actualTime = new Date();
				
				//Allowing update status for today only
				if (!DateUtils.isSameDay(scheduleTime, actualTime)) 
					continue;
				
				MedicationAdministrationSchedule schedule = new MedicationAdministrationSchedule();
				schedule = prepareSchedule(schedule, patient, order, medicineScheduleRequest, doseUnit, route, drug,
						medicationActivity, scheduleTime, nursingActivityStatus);
				schedule.setScheduleId(scheduleObj.getId());
				nursingActivityStatus = scheduleObj.getStatus() != null && !scheduleObj.getStatus().isEmpty()
						? getNursingActivityStatus(scheduleObj.getStatus())
						: nursingActivityStatus;
				schedule.setActivityStatus(nursingActivityStatus);
				if (NURSING_ACTIVITY_STATUS_ADMINISTERED.equals(nursingActivityStatus.getStatus())) {
					schedule.setActualTime(actualTime);
					schedule.setAdministeredBy(provider);
				} else {
					continue;
				}

				nursingActivityScheduleDao.saveSchedule(schedule);
			}
		} else {
			for (int j = 0; j < timings.size(); j++) {
				String timeString = timings.get(j);
				Date scheduleTime = createScheduleTime(scheduledDate, timeString);

				MedicationAdministrationSchedule schedule = new MedicationAdministrationSchedule();
				schedule = prepareSchedule(schedule, patient, order, medicineScheduleRequest, doseUnit, route, drug,
						medicationActivity, scheduleTime, nursingActivityStatus);

				if (null != schedules) { // update case
					while (scheduleIterator.hasNext()) {
						Schedule scheduleObj = scheduleIterator.next();
						if (DateUtility.ConvertMilliSecondsToFormattedDate(scheduleObj.getScheduledTime())
								.compareTo(DateUtility.formattedDateOnly(scheduledDate)) == 0) {
							schedule.setScheduleId(scheduleObj.getId());
							scheduleIterator.remove();
							nursingActivityStatus = scheduleObj.getStatus() != null
									&& !scheduleObj.getStatus().isEmpty()
											? getNursingActivityStatus(scheduleObj.getStatus())
											: nursingActivityStatus;
							schedule.setActivityStatus(nursingActivityStatus);
							if (NURSING_ACTIVITY_STATUS_ADMINISTERED.equals(nursingActivityStatus.getStatus())) {
								schedule.setActualTime(new Date());
								schedule.setAdministeredBy(provider);
							}

							nursingActivityScheduleDao.saveSchedule(schedule);
							break;

						}
					}
				} else { // insert case
					nursingActivityScheduleDao.saveSchedule(schedule);
				}
			}

		}
	}

	private MedicationAdministrationSchedule prepareSchedule(MedicationAdministrationSchedule schedule, Patient patient,
			Order order, MedicineScheduleRequest medicineScheduleRequest, Concept doseUnit, Concept route, Drug drug,
			NursingActivityType medicationActivity, Date scheduleTime, NursingActivityStatus nursingActivityStatus) {
		schedule.setPatient(patient);
		schedule.setOrder(order);
		schedule.setDose(medicineScheduleRequest.getDose());
		schedule.setDoseUnits(doseUnit);
		schedule.setRoute(route);
		schedule.setDrug(drug);
		schedule.setActivityType(medicationActivity);
		schedule.setScheduleTime(scheduleTime);
		schedule.setActivityStatus(nursingActivityStatus);
		return schedule;
	}

	private NursingActivityType getNursingActivityType(String activityTypeName) {
		List<NursingActivityType> nursingActivityTypes = nursingActivityScheduleDao.getNursingActivityTypes();
		for (NursingActivityType activityType : nursingActivityTypes) {
			if (activityType.getActivityName().equals(activityTypeName)) {
				return activityType;
			}
		}
		return null;
	}

	private NursingActivityStatus getNursingActivityStatus(String activityStatusValue) {
		List<NursingActivityStatus> nursingActivityStatus = nursingActivityScheduleDao.getNursingActivityStatus();
		for (NursingActivityStatus activityStatus : nursingActivityStatus) {
			if (activityStatus.getStatus().equals(activityStatusValue)) {
				return activityStatus;
			}
		}
		return null;
	}

	private void saveWeeklyMedicineSchedules(MedicineScheduleRequest medicineScheduleRequest) {
		ArrayList<DayOfWeek> dayOfWeeks = medicineScheduleRequest.getDays();
		for (int i = 0; i < dayOfWeeks.size(); i++) {
			Date startDate = medicineScheduleRequest.getStartingDate();
			Date endDate = medicineScheduleRequest.getEndingDate();
			ArrayList<Date> allDatesBetweenDuration = DateUtility.getAllDatesBetweenOf(dayOfWeeks.get(i), startDate,
					endDate);
			saveMedicationSchedulesForDuration(allDatesBetweenDuration, medicineScheduleRequest);
		}
	}

	private void saveMedicationSchedulesForDuration(ArrayList<Date> allDatesBetweenDuration,
			MedicineScheduleRequest medicineScheduleRequest) {
		for (int j = 0; j < allDatesBetweenDuration.size(); j++) {
			Date scheduledDate = allDatesBetweenDuration.get(j);
			saveSchedulesForADay(medicineScheduleRequest, scheduledDate);
		}
	}

	private Date createScheduleTime(Date scheduledDate, String timeString) {
		Date newDate = (Date) scheduledDate.clone();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeString, formatter);
		newDate.setHours(time.getHour());
		newDate.setMinutes(time.getMinute());
		return newDate;
	}
}
