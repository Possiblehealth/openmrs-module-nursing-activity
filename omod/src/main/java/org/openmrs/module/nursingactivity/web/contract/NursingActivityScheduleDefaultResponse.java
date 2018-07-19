package org.openmrs.module.nursingactivity.web.contract;

import java.util.Date;
import java.util.Map;

public class NursingActivityScheduleDefaultResponse {
  private Integer scheduleId;
  private Map patient;
  private Date startDateTime;
  private String status;

  public void setScheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  public Integer getScheduleId() {
    return scheduleId;
  }

  public void setPatient(Map patient) {
    this.patient = patient;
  }

  public Map getPatient() {
    return patient;
  }

  public void setStartDateTime(Date startDateTime) {
    this.startDateTime = startDateTime;
  }

  public Date getStartDateTime() {
    return startDateTime;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
