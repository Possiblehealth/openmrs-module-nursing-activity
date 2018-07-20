package org.openmrs.module.nursingactivity.web.controller;


import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;
import org.openmrs.module.nursingactivity.service.NursingActivityService;
import org.openmrs.module.nursingactivity.web.mapper.NursingActivityScheduleMapper;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/ipd/schedules")
public class IpdScheduleController extends BaseRestController {
  @Autowired
  NursingActivityService nursingActivityService;

  @RequestMapping(method = RequestMethod.GET, value = "/{uuid}", produces = "application/json")
  @ResponseBody
  public String getSchedule(@PathVariable("uuid") String patientUuid) {
    return "{\"medicineName\":\"Metformin 850mg\",\"dose\":1,\"unit\":\"TAB\",\"frequency\":\"BID\",\"schedules\":[{\"scheduledTime\":\"2018-06-01T21:00:00.000Z\",\"status\":\"TOBEADMINISTRATED\"},{\"scheduledTime\":\"2018-06-03T04:00:00.000Z\",\"status\":\"NOTADMINISTRATED\"},{\"scheduledTime\":\"2018-06-03T21:00:00.000Z\",\"status\":\"NOTADMINISTRATED\"},{\"scheduledTime\":\"2018-06-05T02:00:00.000Z\",\"status\":\"NOTADMINISTRATED\"},{\"scheduledTime\":\"2018-06-05T21:00:00.000Z\",\"status\":\"ADMINISTRATED\"}]}";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/patient/{uuid}", produces = "application/json")
  @ResponseBody
  public List<Object> getPatientSchedules(@PathVariable("uuid") String patientUuid) {
    List<NursingActivitySchedule> scheduleEntriesForPatient = nursingActivityService.getScheduleEntriesForPatient(patientUuid);
    NursingActivityScheduleMapper nursingActivityScheduleMapper = new NursingActivityScheduleMapper();
    return nursingActivityScheduleMapper.createResponse(scheduleEntriesForPatient);
  }
}
