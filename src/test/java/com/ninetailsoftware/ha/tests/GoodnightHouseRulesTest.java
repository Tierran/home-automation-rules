package com.ninetailsoftware.ha.tests;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.SimpleSwitch;

public class GoodnightHouseRulesTest extends MyTestHelper{

	@Test
	public void testGoodnightHouse() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("26");
		ss1.setSource("homeseer");
		ss1.setStatus("255");
		ss1.setFloor("First Floor");
		ss1.setRoom("Dining Room");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("34");
		ss2.setSource("homeseer");
		ss2.setStatus("255");
		ss2.setFloor("Second Floor");
		ss2.setRoom("Nursery");
		ss2.setDisableWhenAway(false);
		
		SimpleSwitch ss3 = new SimpleSwitch();
		ss3.setId("62");
		ss3.setSource("homeseer");
		ss3.setStatus("255");
		ss3.setFloor("Outdoors");
		ss3.setRoom("Outdoors");
		ss3.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("57");
		event.setValue("0");

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(ss3);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("Hallway lights turned off", "0", ss1.getStatus());
		Assert.assertEquals("Nursery lights turned off", "0", ss2.getStatus());
		Assert.assertEquals("Outdoor lights not turned off", "255", ss3.getStatus());
	}
	
	@Test
	public void enableGoodnightHouseButton() throws Exception{
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("26");
		ss1.setSource("homeseer");
		ss1.setStatus("0");
		ss1.setFloor("First Floor");
		ss1.setRoom("Dining Room");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("57");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		SimpleSwitch ss3 = new SimpleSwitch();
		ss2.setId("73");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("26");
		event.setValue("255");
		event.setSource("homeseer");
		
		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(ss3);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("Goodnight House Button has been enabled", "100", ss2.getStatus());
	}
	
	@Test
	public void enableFirstFloorButton() throws Exception{
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("26");
		ss1.setSource("homeseer");
		ss1.setStatus("0");
		ss1.setFloor("First Floor");
		ss1.setRoom("Dining Room");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("73");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("26");
		event.setValue("255");
		event.setSource("homeseer");
		
		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("First Floor Button has been enabled", "100", ss2.getStatus());
	}
	
	@Test
	public void enableSecondFloorButton() throws Exception{
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("24");
		ss1.setSource("homeseer");
		ss1.setStatus("0");
		ss1.setFloor("Second Floor");
		ss1.setRoom("Office");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("74");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("24");
		event.setValue("255");
		event.setSource("homeseer");
		
		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("Second Floor Button has been enabled", "100", ss2.getStatus());
	}
	
	@Test
	public void testTurnOffFirstFLoorLights() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("26");
		ss1.setSource("homeseer");
		ss1.setStatus("255");
		ss1.setFloor("First Floor");
		ss1.setRoom("Dining Room");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("34");
		ss2.setSource("homeseer");
		ss2.setStatus("255");
		ss2.setFloor("Second Floor");
		ss2.setRoom("Nursery");
		ss2.setDisableWhenAway(false);
		
		SimpleSwitch ss3 = new SimpleSwitch();
		ss3.setId("62");
		ss3.setSource("homeseer");
		ss3.setStatus("255");
		ss3.setFloor("Outdoors");
		ss3.setRoom("Outdoors");
		ss3.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("73");
		event.setValue("0");

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(ss3);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("Hallway lights turned off", "0", ss1.getStatus());
		Assert.assertEquals("Nursery lights not turned off", "255", ss2.getStatus());
		Assert.assertEquals("Outdoor lights not turned off", "255", ss3.getStatus());
	}
	
	@Test
	public void testTurnOffSecondFLoorLights() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("26");
		ss1.setSource("homeseer");
		ss1.setStatus("255");
		ss1.setFloor("First Floor");
		ss1.setRoom("Dining Room");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("34");
		ss2.setSource("homeseer");
		ss2.setStatus("255");
		ss2.setFloor("Second Floor");
		ss2.setRoom("Nursery");
		ss2.setDisableWhenAway(false);
		
		SimpleSwitch ss3 = new SimpleSwitch();
		ss3.setId("62");
		ss3.setSource("homeseer");
		ss3.setStatus("255");
		ss3.setFloor("Outdoors");
		ss3.setRoom("Outdoors");
		ss3.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("74");
		event.setSource("homeseer");
		event.setValue("0");

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(ss3);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("Hallway lights not turned off", "255", ss1.getStatus());
		Assert.assertEquals("Nursery lights turned off", "0", ss2.getStatus());
		Assert.assertEquals("Outdoor lights not turned off", "255", ss3.getStatus());
	}
	
	@Test
	public void testDisableFirstFloorSwitch() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("26");
		ss1.setSource("homeseer");
		ss1.setStatus("255");
		ss1.setFloor("First Floor");
		ss1.setRoom("Dining Room");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("73");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("26");
		event.setSource("homeseer");
		event.setValue("0");

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("First floor switch disabled", "0", ss2.getStatus());
	}
	
	@Test
	public void testDisableGoodnightHouse() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("57");
		ss1.setSource("homeseer");
		ss1.setStatus("100");
		ss1.setFloor("Hidden");
		ss1.setRoom("Hidden");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("73");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		SimpleSwitch ss3 = new SimpleSwitch();
		ss3.setId("74");
		ss3.setSource("homeseer");
		ss3.setStatus("100");
		ss3.setFloor("Hidden");
		ss3.setRoom("Hidden");
		ss3.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("74");
		event.setSource("homeseer");
		event.setValue("0");

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(ss3);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("Master power button disabled", "0", ss1.getStatus());
	}
	
	@Test
	public void testDisableSecondFloorSwitch() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Goodnight House.drl");
		
		SimpleSwitch ss1 = new SimpleSwitch();
		ss1.setId("24");
		ss1.setSource("homeseer");
		ss1.setStatus("255");
		ss1.setFloor("Second Floor");
		ss1.setRoom("Office");
		ss1.setDisableWhenAway(false);
		
		SimpleSwitch ss2 = new SimpleSwitch();
		ss2.setId("73");
		ss2.setSource("homeseer");
		ss2.setStatus("0");
		ss2.setFloor("Hidden");
		ss2.setRoom("Hidden");
		ss2.setDisableWhenAway(false);
		
		HaEvent event = new HaEvent();
		event.setDeviceId("24");
		event.setSource("homeseer");
		event.setValue("0");

		ksession.insert(ss1);
		ksession.insert(ss2);
		ksession.insert(event);
		ksession.fireAllRules();
		
		Assert.assertEquals("First floor switch disabled", "0", ss2.getStatus());
	}
}
