package com.ninetailsoftware.ha.tests;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.AlarmPanel;

public class ADTEmailRulesTest extends MyTestHelper {

	@Test
	public void testAlarmArmedAway() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "ADT Email Rules.drl");
		
		HaEvent ha = new HaEvent();
		ha.setValue("Security System Armed Away");
		
		AlarmPanel panel = new AlarmPanel();
		panel.setId("18");
		panel.setStatus("0");
		panel.setSource("homeseer");
		
		ksession.insert(ha);
		ksession.insert(panel);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that alarm panel was armed away (Status 2)", "2", panel.getStatus());
	}
	
	@Test
	public void testAlarmArmedStay() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "ADT Email Rules.drl");
		
		HaEvent ha = new HaEvent();
		ha.setValue("Security System Armed Stay");
		
		AlarmPanel panel = new AlarmPanel();
		panel.setId("18");
		panel.setStatus("0");
		panel.setSource("homeseer");
		
		ksession.insert(ha);
		ksession.insert(panel);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that alarm panel was armed away (Status 1)", "1", panel.getStatus());
	}
	
	@Test
	public void testAlarmDisarmed() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "ADT Email Rules.drl");
		
		HaEvent ha = new HaEvent();
		ha.setValue("Security System Disarmed");
		
		AlarmPanel panel = new AlarmPanel();
		panel.setId("18");
		panel.setStatus("2");
		panel.setSource("homeseer");
		
		ksession.insert(ha);
		ksession.insert(panel);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that alarm panel was armed away (Status 0)", "0", panel.getStatus());
	}
}
