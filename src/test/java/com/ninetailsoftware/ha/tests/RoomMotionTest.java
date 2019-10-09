package com.ninetailsoftware.ha.tests;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.ha.support.TrackingAgendaEventListener;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.MotionSensor;
import com.ninetailsoftware.model.facts.SimpleSwitch;

public class RoomMotionTest extends MyTestHelper{
	
	@Test
	public void testLightsOffWithNoMotion() throws Exception {
		
		KieSession ksession = testHelper("Update Device Status.drl", "Room Motion.drl");
	
		TrackingAgendaEventListener agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
	
		SimpleSwitch officeLight = new SimpleSwitch();
		officeLight.setId("25");
		officeLight.setSource("homeseer");
		officeLight.setStatus("255");
		officeLight.setDisableWhenAway(false);
		officeLight.setFloor("Second Floor");
		officeLight.setRoom("Office");
		
		MotionSensor ms = new MotionSensor();
		ms.setId("32");
		ms.setFloor("Second Floor");
		ms.setRoom("Office");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("0");
	    ha.setDeviceId("32");
	    ha.setFloor("Second Floor");
	    ha.setRoom("Office");
	    
	    HaEvent ha2 = new HaEvent();
	    ha2.setSource("homeseer");
	    ha2.setValue("8");
	    ha2.setDeviceId("32");
	    ha2.setFloor("Second Floor");
	    ha2.setRoom("Office");
	    
	    ksession.insert(officeLight);
	    ksession.insert(ms);
	    ksession.insert(ha2);
	    ksession.fireAllRules();
	    
	    clock.advanceTime(61, TimeUnit.SECONDS);
		
		ksession.insert(ha);
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that office light is still on", "255", officeLight.getStatus());
		
		clock.advanceTime(61, TimeUnit.HOURS);
		agendaEventListener.reset();
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that office light was turned off", "0", officeLight.getStatus());

		ksession.dispose();
	}
}
