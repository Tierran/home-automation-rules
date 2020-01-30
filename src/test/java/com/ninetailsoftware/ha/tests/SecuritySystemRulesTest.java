package com.ninetailsoftware.ha.tests;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.AlarmPanel;

public class SecuritySystemRulesTest extends MyTestHelper {

	@Test
	public void testAlarmArmedAway() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Security System Rules.drl");
		
		HaEvent ha = new HaEvent();
		ha.setValue("2");
		ha.setDeviceId("216");
		
		AlarmPanel panel = new AlarmPanel();
		panel.setId("216");
		panel.setStatus("0");
		panel.setSource("homeseer");
		
		ksession.insert(ha);
		ksession.insert(panel);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that alarm panel was armed away (Status 2)", "2", panel.getStatus());
	}
	
	@Test
	public void testAlarmArmedStay() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Security System Rules.drl");
		
		HaEvent ha = new HaEvent();
		ha.setValue("1");
		ha.setDeviceId("216");
		
		AlarmPanel panel = new AlarmPanel();
		panel.setId("216");
		panel.setStatus("0");
		panel.setSource("homeseer");
		
		ksession.insert(ha);
		ksession.insert(panel);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that alarm panel was armed away (Status 1)", "1", panel.getStatus());
	}
	
	@Test
	public void testAlarmDisarmed() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Security System Rules.drl");
		
		HaEvent ha = new HaEvent();
		ha.setValue("0");
		ha.setDeviceId("216");
		
		AlarmPanel panel = new AlarmPanel();
		panel.setId("216");
		panel.setStatus("2");
		panel.setSource("homeseer");
		
		ksession.insert(ha);
		ksession.insert(panel);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that alarm panel was armed away (Status 0)", "0", panel.getStatus());
	}
}
