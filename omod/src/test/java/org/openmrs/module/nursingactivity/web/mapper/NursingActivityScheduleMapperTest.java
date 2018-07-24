package org.openmrs.module.nursingactivity.web.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.nursingactivity.model.NursingActivitySchedule;

import java.util.ArrayList;
import java.util.List;

public class NursingActivityScheduleMapperTest {
  @Test
  public void shouldGetEmptyListWhenGivenEmptyListOfNursingActivitySchedules() {
    List<NursingActivitySchedule> nursingActivitySchedules = new ArrayList<>();
    NursingActivityScheduleMapper nursingActivityScheduleMapper = new NursingActivityScheduleMapper();
    List<Object> response = nursingActivityScheduleMapper.createResponse(nursingActivitySchedules);

    ArrayList<Object> expected = new ArrayList<>();
    Assert.assertEquals(expected, response);
  }
}
