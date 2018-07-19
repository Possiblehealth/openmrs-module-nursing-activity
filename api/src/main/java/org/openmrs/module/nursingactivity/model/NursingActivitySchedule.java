package org.openmrs.module.nursingactivity.model;

import org.openmrs.Order;
import org.openmrs.Patient;

import java.util.Date;

public class NursingActivitySchedule {
  private Patient patient;
  private Order order;
  private Date scheduleTime;
  private Date actualTime;
  private Integer scheduleId;
  private NursingActivityType activityType;
  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public NursingActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(NursingActivityType activityType) {
    this.activityType = activityType;
  }

  public Integer getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  public Date getActualTime() {
    return actualTime;
  }

  public void setActualTime(Date actualTime) {
    this.actualTime = actualTime;
  }

  public Date getScheduleTime() {
    return scheduleTime;
  }

  public void setScheduleTime(Date scheduleTime) {
    this.scheduleTime = scheduleTime;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

//  @Override
//  public String toString() {
//    return "NursingActivitySchedule{" +
//        "patient=" + patient +
//        ", order=" + order +
//        ", scheduleTime=" + scheduleTime +
//        ", actualTime=" + actualTime +
//        ", scheduleId=" + scheduleId +
//        ", activityType=" + activityType +
//        ", status='" + status + '\'' +
//        '}';
//  }
}
