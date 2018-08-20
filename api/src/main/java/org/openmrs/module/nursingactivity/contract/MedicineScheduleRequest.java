package org.openmrs.module.nursingactivity.contract;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.time.DayOfWeek;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineScheduleRequest {
  public String scheduleType;
  private String patientUuid;
  private String orderUuid;
  private ArrayList<String> timings;
  private ArrayList<DayOfWeek> days;

  public MedicineScheduleRequest() {
  }

  public MedicineScheduleRequest(String patientUuid, String orderUuid, ArrayList<String> timings, String scheduleType, ArrayList<DayOfWeek> days) {
    this.patientUuid = patientUuid;
    this.orderUuid = orderUuid;
    this.timings = timings;
    this.scheduleType = scheduleType;
    this.days = days;
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
}
