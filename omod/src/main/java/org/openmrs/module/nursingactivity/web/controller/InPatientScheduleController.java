package org.openmrs.module.nursingactivity.web.controller;


import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/nursing-activity")
public class InPatientScheduleController extends BaseRestController {

  @RequestMapping(method = RequestMethod.GET, value = "/{uuid}",produces = "application/json")
  @ResponseBody
  public String getSchedule(@PathVariable("uuid") String patientUuid) {
    return "{\n" +
        "  medicineName: \"Metformin 850mg\",\n" +
        "  dose: 1,\n" +
        "  unit: TAB,\n" +
        "  frequency: BID,\n" +
        "  schedules: [{\n" +
        "    scheduledTime: new Date('June 2, 2018 02:30:00'),\n" +
        "    status: TOBEADMINISTRATED\n" +
        "  }, {\n" +
        "    scheduledTime: new Date('June 3, 2018 9:30:00'),\n" +
        "    status: NOTADMINISTRATED\n" +
        "  }, {\n" +
        "    scheduledTime: new Date('June 4, 2018 02:30:00'),\n" +
        "    status: NOTADMINISTRATED\n" +
        "  }, {\n" +
        "    scheduledTime: new Date('June 5, 2018 07:30:00'),\n" +
        "    status: NOTADMINISTRATED\n" +
        "  }, {\n" +
        "    scheduledTime: new Date('June 6, 2018 02:30:00'),\n" +
        "    status: ADMINISTRATED\n" +
        "  }]\n" +
        "}";
  }

}
