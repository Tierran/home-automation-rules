package com.ninetailsoftware.ha.tests;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.SimpleSwitch;

public class AwayModeRulesTest extends MyTestHelper {

	@Test
	public void testLightsTurnedOffWhenAway() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Away Mode Rule.drl");
		/**
		 * Check that lights are disabled when the alarm event 2 is inserted.  This does not check that the ADT rule
		 * to insert the event works as that as a separate rule
		 */
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("54");
		ss1.setSource("homeseer");
		ss1.setStatus("255");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("10");
		ss2.setSource("homeseer");
		ss2.setStatus("255");
		ss2.setDisableWhenAway(true);

	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("2");
	    ha.setDeviceId("216");
	    ha.setSendUpdate(true);

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(ha);
		
		ksession.fireAllRules();

		Assert.assertEquals("Check that one light was disabled when marked Disable When Away", "0", ss2.getStatus());
		Assert.assertEquals("Check that one light was not disabled when not marked Disable When Away", "255", ss1.getStatus());
		
		ksession.dispose();
	}
}
