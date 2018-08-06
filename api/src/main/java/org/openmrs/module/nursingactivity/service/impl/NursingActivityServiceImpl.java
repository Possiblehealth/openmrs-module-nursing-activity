package org.openmrs.module.nursingactivity.service.impl;

import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.module.nursingactivity.contract.MedicineScheduleRequest;
import org.openmrs.module.nursingactivity.dao.NursingActivityScheduleDao;
import org.openmrs.module.nursingactivity.model.MedicationAdministrationSchedule;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.model.NursingActivityType;
import org.openmrs.module.nursingactivity.service.NursingActivityService;
import org.openmrs.module.nursingactivity.utils.DateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.nursingactivity.constants.MedicineScheduleConstants.START_OF_WEEK;

@Service
@Transactional
public class NursingActivityServiceImpl implements NursingActivityService {

  private NursingActivityScheduleDao nursingActivityScheduleDao;

  private PatientService patientService;
  private OrderService orderService;

  @Autowired
  public NursingActivityServiceImpl(PatientService patientService, NursingActivityScheduleDao nursingActivityScheduleDao, OrderService orderService) {
    this.patientService = patientService;
    this.nursingActivityScheduleDao = nursingActivityScheduleDao;
    this.orderService = orderService;
  }

  @Override
  public List<NursingActivitySchedule> getScheduleEntriesForPatient(String patientUuid, Date startDate, Date endDate) {
    Patient patient = patientService.getPatientByUuid(patientUuid);
    if (patient == null) {
      throw new IllegalArgumentException("Patient is required when fetching active orders");
    }
    if (startDate == null && endDate == null){
      startDate = DateUtility.getWeekStart(new Date(),START_OF_WEEK);
    }
    if (startDate == null){
      startDate = DateUtility.addDays(endDate,-6);
    }
    if (endDate == null) {
      endDate = DateUtility.addDays(startDate,6);
    }
    if (DateUtility.compare(startDate, endDate) > 0) {
      throw new IllegalArgumentException("Given start date is after end date");
    }
    Date endOfDay = DateUtility.getEndOfDay(endDate);
    return nursingActivityScheduleDao.getScheduleEntriesForPatient(patient, startDate, endOfDay);
  }

  @Override
  public String createSchedules(MedicineScheduleRequest medicineScheduleRequest) {
    String patientUuid = medicineScheduleRequest.getPatientUuid();
    String orderUuid = medicineScheduleRequest.getOrderUuid();
    Patient patient = patientService.getPatientByUuid(patientUuid);
    Order order = orderService.getOrderByUuid(orderUuid);
    ArrayList<String> timings = medicineScheduleRequest.getTimings();
    if (!DateUtility.areAllValidTimeStrings(timings)){
      throw new IllegalArgumentException("Given time strings are not valid");
    }
    if (patient == null){
      throw new IllegalArgumentException("Patient is required when fetching active orders");
    }
    //TODO:Remove this when you are dealing with order as optional
    if (order == null){
      throw new IllegalArgumentException("Currently order is required to create schedules");
    }
    Date startDate = order.getScheduledDate();
    Date endDate = order.getAutoExpireDate();
    saveMedicationSchedules(patient,timings,order,startDate,endDate);
    return "{\"stats\":\"added\"}";
  }

  private void saveMedicationSchedules(Patient patient, ArrayList<String> timings, Order order, Date startDate, Date endDate) {
    for (Date scheduledDate = startDate; DateUtility.isBetween(startDate, endDate,scheduledDate);scheduledDate=DateUtility.addDays(scheduledDate,1)){
      for (int j=0;j<timings.size();j++){
        String timeString = timings.get(j);

        Date scheduleTime = createScheduleTime(scheduledDate, timeString);
        NursingActivityType activityType = new NursingActivityType();
        activityType.setTypeId(1);
        activityType.setActivityName("Medication");

        DrugOrder drugOrder = (DrugOrder) order;

        MedicationAdministrationSchedule schedule = new MedicationAdministrationSchedule();
        schedule.setPatient(patient);
        schedule.setOrder(drugOrder);
        schedule.setDose(drugOrder.getDose());
        schedule.setDoseUnits(drugOrder.getDoseUnits());
        schedule.setDrug(drugOrder.getDrug());
        schedule.setRoute(drugOrder.getRoute());

        schedule.setActivityType(activityType);
        schedule.setScheduleTime(scheduleTime);
        schedule.setStatus("scheduled");

        this.nursingActivityScheduleDao.saveSchedule(schedule);

        System.out.println(schedule);
      }
    }
  }

  private Date createScheduleTime(Date scheduledDate, String timeString) {
    Date newDate = (Date) scheduledDate.clone();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime time = LocalTime.parse(timeString, formatter);
    newDate.setHours(time.getHour());
    newDate.setMinutes(time.getMinute());
    return  newDate;
  }
}
