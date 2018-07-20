package org.openmrs.module.nursingactivity.model;

import org.openmrs.Concept;
import org.openmrs.Drug;

public class MedicationAdministrationSchedule extends NursingActivitySchedule {
  private Double dose;
  private Concept doseUnits;
  private Concept route;
  private Drug drugId;

  public Drug getDrugId() {
    return drugId;
  }

  public void setDrugId(Drug drugId) {
    this.drugId = drugId;
  }

  public Concept getRoute() {
    return route;
  }

  public void setRoute(Concept route) {
    this.route = route;
  }

  public Concept getDoseUnits() {
    return doseUnits;
  }

  public void setDoseUnits(Concept doseUnits) {
    this.doseUnits = doseUnits;
  }

  public Double getDose() {
    return dose;
  }

  public void setDose(Double dose) {
    this.dose = dose;
  }
}
