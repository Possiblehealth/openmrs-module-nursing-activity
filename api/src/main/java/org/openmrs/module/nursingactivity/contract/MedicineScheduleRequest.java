package org.openmrs.module.nursingactivity.contract;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineScheduleRequest {
  private String patientUuid;
  private String orderUuid;
  private ArrayList<String> timings;

  public MedicineScheduleRequest() {
  }

  public MedicineScheduleRequest(String patientUuid, String orderUuid, ArrayList<String> timings) {
    this.patientUuid = patientUuid;
    this.orderUuid = orderUuid;
    this.timings = timings;
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
}
