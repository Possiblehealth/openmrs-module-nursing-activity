/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.nursingactivity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.ModuleActivator;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class NursingActivityModuleActivator implements ModuleActivator {

  private Log log = LogFactory.getLog(this.getClass());

  @Override
  public void willRefreshContext() {
    log.warn("Will refresh Nursing Activity Module");
  }

  @Override
  public void contextRefreshed() {
    log.warn("Refreshed Nursing Activity Module");
  }

  @Override
  public void willStart() {
    log.warn("Will start Nursing Activity Module");

  }

  @Override
  public void started() {
    log.warn("Started Nursing Activity Module");
  }

  @Override
  public void willStop() {
    log.warn("Will stop Nursing Activity Module");
  }

  @Override
  public void stopped() {
    log.warn("Stopped Nursing Activity Module");
  }
}
