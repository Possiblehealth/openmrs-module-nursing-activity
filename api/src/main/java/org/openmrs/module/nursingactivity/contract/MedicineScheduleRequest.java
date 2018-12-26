package org.openmrs.module.nursingactivity.contract;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.module.nursingactivity.model.Schedule;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineScheduleRequest {
  public String scheduleType;
  private String patientUuid;
  private String orderUuid;
  private ArrayList<String> timings;
  private ArrayList<DayOfWeek> days;
  private Date startingDate;
  private Date endingDate;
  private Double dose;
  private String doseUnits;
  private String drugUuid;
  private String route;
  private List<Schedule> schedules;

  public MedicineScheduleRequest() {
  }

  public MedicineScheduleRequest(String patientUuid, String orderUuid, ArrayList<String> timings, String scheduleType, ArrayList<DayOfWeek> days, Date startingDate, Date endingDate, Double dose, String doseUnits, String drugUuid, String route) {
    this.patientUuid = patientUuid;
    this.orderUuid = orderUuid;
    this.timings = timings;
    this.scheduleType = scheduleType;
    this.days = days;
    this.startingDate = startingDate;
    this.endingDate = endingDate;
    this.dose = dose;
    this.doseUnits = doseUnits;
    this.drugUuid = drugUuid;
    this.route = route;
  }

  public String getPatientUuid() {
    return this.patientUuid;
  }

  public String getOrderUuid() {
    return this.orderUuid;
  }

  public ArrayList<String> getTimings() {
    return this.timings;
  }

  public String getScheduleType() {
    return this.scheduleType;
  }

  public ArrayList<DayOfWeek> getDays() {
    return this.days;
  }

  public Date getStartingDate() {
    return this.startingDate;
  }

  public Date getEndingDate() {
    return this.endingDate;
  }

  public Double getDose() {
    return this.dose;
  }

  public String getDoseUnits() {
    return this.doseUnits;
  }

  public String getDrugUuid() {
    return this.drugUuid;
  }

  public String getRoute() {
    return this.route;
  }

public List<Schedule> getSchedules() {
	return schedules;
}

public void setSchedules(List<Schedule> schedules) {
	this.schedules = schedules;
}
}
