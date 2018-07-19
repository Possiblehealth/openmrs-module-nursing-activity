package org.openmrs.module.nursingactivity.model;

public class NursingActivityType {

  private Integer typeId;
  private String activityName;

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }
}
