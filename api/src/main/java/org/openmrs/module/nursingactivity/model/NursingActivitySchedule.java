package org.openmrs.module.nursingactivity.model;

import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.User;

import java.util.Date;

public class NursingActivitySchedule {
	private Patient patient;
	private Order order;
	private Date scheduleTime;
	private Date actualTime;
	private Long scheduleId;
	private NursingActivityType activityType;
	private NursingActivityStatus activityStatus;
	private User administeredBy;

	public NursingActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(NursingActivityType activityType) {
		this.activityType = activityType;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
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

	public NursingActivityStatus getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(NursingActivityStatus activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public User getAdministeredBy() {
		return administeredBy;
	}

	public void setAdministeredBy(User administeredBy) {
		this.administeredBy = administeredBy;
	}

	@Override
	public String toString() {
		return "NursingActivitySchedule [patient=" + patient + ", order=" + order + ", scheduleTime=" + scheduleTime
				+ ", actualTime=" + actualTime + ", scheduleId=" + scheduleId + ", activityType=" + activityType
				+ ", activityStatus=" + activityStatus + ", administeredBy=" + administeredBy + "]";
	}

}
