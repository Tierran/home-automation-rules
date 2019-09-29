package com.ninetailsoftware.ha.tests;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.time.SessionPseudoClock;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.ha.support.TrackingAgendaEventListener;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.AlarmPanel;
import com.ninetailsoftware.model.facts.SimpleSwitch;
import com.ninetailsoftware.model.facts.TheSun;

public class WelcomeHomeRulesTest extends MyTestHelper {
	
	@Test
	public void testTurn2ndFloorHallLightsOnWithMotion() throws Exception {
		
		KieSession ksession = testHelper("Update Device Status.drl", "Welcome Home.drl");
	
		TrackingAgendaEventListener agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);

		/**
		 * Check to see that hall light is set to 33% brightness when there is motion on the front porch,
		 * the sun is set, and the alarm is armed away.
		 */
		
		SimpleSwitch hallSwitch = new SimpleSwitch();
		hallSwitch.setId("26");
		hallSwitch.setSource("homeseer");
		hallSwitch.setStatus("0");
		hallSwitch.setDisableWhenAway(false);
		
		TheSun sun = new TheSun();
		sun.setStatus("0");
		
		AlarmPanel alarmPanel = new AlarmPanel();
		alarmPanel.setStatus("2");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("8");
	    ha.setDeviceId("27");

		ksession.insert(hallSwitch);
		ksession.insert(sun);
		ksession.insert(alarmPanel);
		ksession.fireAllRules();
		
		ksession.insert(ha);
		
		agendaEventListener.reset();
		clock.advanceTime(500, TimeUnit.MILLISECONDS);	
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that hall light was set to 33% illumination", "33", hallSwitch.getStatus());
		Assert.assertTrue(agendaEventListener.isRuleFired("Turn 2nd floor hall lights on with motion"));
		Assert.assertTrue(agendaEventListener.isRuleFired("Update Device Status"));
		
		ksession.dispose();
	}
	
	@Test
	public void testTurn2ndFloorHallLightsOffWithoutEntry() throws Exception {
		
		KieSession ksession = testHelper("Update Device Status.drl", "Welcome Home.drl");
	
		TrackingAgendaEventListener agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
	
		SimpleSwitch hallSwitch = new SimpleSwitch();
		hallSwitch.setId("26");
		hallSwitch.setSource("homeseer");
		hallSwitch.setStatus("33");
		hallSwitch.setDisableWhenAway(false);
		
		TheSun sun = new TheSun();
		sun.setStatus("0");
		
		AlarmPanel alarmPanel = new AlarmPanel();
		alarmPanel.setStatus("2");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("8");
	    ha.setDeviceId("27");

		ksession.insert(hallSwitch);
		ksession.insert(sun);
		ksession.insert(alarmPanel);
		ksession.insert(ha);
		
		ksession.fireAllRules();
		clock.advanceTime(61, TimeUnit.SECONDS);
		agendaEventListener.reset();
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that hall light was turned off", "0", hallSwitch.getStatus());

		ksession.dispose();
	}
	
	@Test
	public void testTurnDiningRoomLightsOnWithMotionNoEntry() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Welcome Home.drl");

		/**
		 * Check to see that hall light is set to 33% brightness when there is motion on the front porch,
		 * the sun is set, and the alarm is armed away.
		 */
		
		SimpleSwitch hallSwitch = new SimpleSwitch();
		hallSwitch.setId("25");
		hallSwitch.setSource("homeseer");
		hallSwitch.setStatus("0");
		hallSwitch.setDisableWhenAway(false);
		
		TheSun sun = new TheSun();
		sun.setStatus("0");
		
		AlarmPanel alarmPanel = new AlarmPanel();
		alarmPanel.setStatus("2");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("8");
	    ha.setDeviceId("27");

		ksession.insert(hallSwitch);
		ksession.insert(sun);
		ksession.insert(alarmPanel);
		ksession.fireAllRules();
		
		ksession.insert(ha);
		
		agendaEventListener.reset();
		clock.advanceTime(500, TimeUnit.MILLISECONDS);	
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that hall light was set to 33% illumination", "33", hallSwitch.getStatus());
		Assert.assertTrue(agendaEventListener.isRuleFired("Turn dining room lights on with motion"));
		Assert.assertTrue(agendaEventListener.isRuleFired("Update Device Status"));
		
		/**
		 * Light should turn off after a minute
		 */
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		
		agendaEventListener.reset();
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that hall light was turned off", "0", hallSwitch.getStatus());

		ksession.dispose();
	}
	
	@Test
	public void testTurnDiningRoomLightsOffWithoutEntry() throws Exception {
		
		KieSession ksession = testHelper("Update Device Status.drl", "Welcome Home.drl");
	
		TrackingAgendaEventListener agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
	
		SimpleSwitch hallSwitch = new SimpleSwitch();
		hallSwitch.setId("25");
		hallSwitch.setSource("homeseer");
		hallSwitch.setStatus("33");
		hallSwitch.setDisableWhenAway(false);
		
		TheSun sun = new TheSun();
		sun.setStatus("0");
		
		AlarmPanel alarmPanel = new AlarmPanel();
		alarmPanel.setStatus("2");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("8");
	    ha.setDeviceId("27");

		ksession.insert(hallSwitch);
		ksession.insert(sun);
		ksession.insert(alarmPanel);
		ksession.insert(ha);
		
		ksession.fireAllRules();
		clock.advanceTime(61, TimeUnit.SECONDS);
		agendaEventListener.reset();
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that dining room light was turned off", "0", hallSwitch.getStatus());

		ksession.dispose();
	}
	
	@Test
	public void testDontTurnDiningRoomLightsOffWhenAlarmDisarmed() throws Exception {
		
		KieSession ksession = testHelper();
	
		TrackingAgendaEventListener agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
	
		SimpleSwitch hallSwitch = new SimpleSwitch();
		hallSwitch.setId("25");
		hallSwitch.setSource("homeseer");
		hallSwitch.setStatus("33");
		hallSwitch.setDisableWhenAway(false);
		
		TheSun sun = new TheSun();
		sun.setStatus("0");
		
		AlarmPanel alarmPanel = new AlarmPanel();
		alarmPanel.setStatus("2");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("8");
	    ha.setDeviceId("27");

		ksession.insert(hallSwitch);
		ksession.insert(sun);
		FactHandle fh = ksession.insert(alarmPanel);
		ksession.insert(ha);
		ksession.fireAllRules();
		alarmPanel.setStatus("0");
		ksession.update(fh, alarmPanel);
		ksession.fireAllRules();
		clock.advanceTime(61, TimeUnit.SECONDS);
		agendaEventListener.reset();
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that dining room light is still on", "33", hallSwitch.getStatus());

		ksession.dispose();
	}
	
	@Test
	public void testDontTurn2ndFloorHallLightsOffWhenAlarmDisarmed() throws Exception {
		
		KieSession ksession = testHelper("Update Device Status.drl", "Welcome Home.drl");
	
		TrackingAgendaEventListener agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
	
		SimpleSwitch hallSwitch = new SimpleSwitch();
		hallSwitch.setId("26");
		hallSwitch.setSource("homeseer");
		hallSwitch.setStatus("33");
		hallSwitch.setDisableWhenAway(false);
		
		TheSun sun = new TheSun();
		sun.setStatus("0");
		
		AlarmPanel alarmPanel = new AlarmPanel();
		alarmPanel.setStatus("2");
		
	    HaEvent ha = new HaEvent();
	    ha.setSource("homeseer");
	    ha.setValue("8");
	    ha.setDeviceId("27");

		ksession.insert(hallSwitch);
		ksession.insert(sun);
		FactHandle fh = ksession.insert(alarmPanel);
		ksession.insert(ha);
		ksession.fireAllRules();
		alarmPanel.setStatus("0");
		ksession.update(fh, alarmPanel);
		ksession.fireAllRules();
		clock.advanceTime(61, TimeUnit.SECONDS);
		agendaEventListener.reset();
		ksession.fireAllRules();
		
		Assert.assertEquals("Check that 2nd floor light is still on", "33", hallSwitch.getStatus());

		ksession.dispose();
	}
	
	private KieSession testHelper() {
		KieSessionConfiguration ksconfig = KieServices.Factory.get().newKieSessionConfiguration();
		ksconfig.setOption(ClockTypeOption.get("pseudo"));
		KieBaseConfiguration kbconfig = KieServices.Factory.get().newKieBaseConfiguration();
		kbconfig.setOption(EventProcessingOption.STREAM);
		final KieBase kbase = loadKnowledgeBase(kbconfig, "Update Device Status.drl", "Welcome Home.drl");
		final KieSession ksession = kbase.newKieSession(ksconfig, null);
		this.clock = ksession.getSessionClock();
		this.agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
		
		return ksession;
	}
}
