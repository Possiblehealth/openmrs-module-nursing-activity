package org.openmrs.module.nursingactivity.contract;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineScheduleRequestList extends ArrayList<MedicineScheduleRequest> {

	private static final long serialVersionUID = -2354251818585172938L;

}
