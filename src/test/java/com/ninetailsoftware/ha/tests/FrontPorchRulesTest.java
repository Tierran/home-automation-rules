package com.ninetailsoftware.ha.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.AlarmPanel;
import com.ninetailsoftware.model.facts.SimpleSwitch;
import com.ninetailsoftware.model.facts.TheSun;

public class FrontPorchRulesTest extends MyTestHelper {

	@Test
	public void testFrontPorchLightWhenAwayAfterDusk() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Front Porch Rules.drl");

		TheSun sun = new TheSun();
		AlarmPanel panel = new AlarmPanel();
		
		SimpleSwitch ss = new SimpleSwitch();
		ss.setId("55");
		ss.setSource("homeseer");
		ss.setStatus("0");

		sun.setId("23");
		sun.setStatus("2");
		panel.setId("18");
		panel.setStatus("2");

		HaEvent event2 = new HaEvent();
		event2.setDeviceId("23");
		event2.setValue("2");
		ksession.insert(ss);
		ksession.insert(sun);
		ksession.insert(panel);

		ksession.fireAllRules();

		Assert.assertEquals("Check that flood light was turned on", "255", ss.getStatus());
		Assert.assertTrue(agendaEventListener.isRuleFired("Turn on front porch at Dusk when away"));
		Assert.assertTrue(agendaEventListener.isRuleFired("Update Device Status"));

		ksession.dispose();
	}

	@Test
	public void testFrontFloodActivatesAtDusk() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Front Porch Rules.drl");

		/**
		 * Check that front flood light activates at dusk.  This should only happen the moment
		 * of the event to allow for manual control of the light otherwise
		 */
		
		SimpleSwitch ss = new SimpleSwitch();
		ss.setId("54");
		ss.setSource("homeseer");
		ss.setStatus("0");

		HaEvent event = new HaEvent();
		event.setDeviceId("23");
		event.setValue("2");

		FactHandle fh = ksession.insert(ss);
		clock.advanceTime(500, TimeUnit.MILLISECONDS);
		System.out.println(clock.getCurrentTime());
		ksession.insert(event);
		agendaEventListener.reset();
		rulesFired = ksession.fireAllRules();

		Assert.assertEquals("Check that flood light was turned on", "255", ss.getStatus());
		Assert.assertTrue(agendaEventListener.isRuleFired("Turn on front flood at Dusk"));
		Assert.assertTrue(agendaEventListener.isRuleFired("Update Device Status"));
		
		/**
		 * Check that front flood light does not activate after dusk once the 1 second window has passed
		 */
		
		clock.advanceTime(5, TimeUnit.MINUTES);
		ksession.fireAllRules();
		ss.setStatus("0");
		ksession.update(fh, ss);
		rulesFired = ksession.fireAllRules();
		
		Assert.assertEquals("Check that flood light was not turned on", "0", ss.getStatus());
		Assert.assertEquals("Check that no rules fired",0,rulesFired);
		

		ksession.dispose();
	}

	@Test
	public void testFrontFloodTurnsOffAtMidnight() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Front Porch Rules.drl");

		/**
		 * First we will confirm that the flood light turns off at midnight.  Keep an eye on the 
		 * time offset as this may cause a problem during daylight savings.  If this test starts breaking
		 * then I may need to introduce some logic to adjust between CST and CDT. 
		 */
		
		DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
		Date date = df.parse( "2019-01-01T01:00:00.000-0000" );
		clock.advanceTime(date.getTime(), TimeUnit.MILLISECONDS);
		
		
		SimpleSwitch ss = new SimpleSwitch();
		ss.setId("54");
		ss.setSource("homeseer");
		ss.setStatus("255");
		FactHandle fh = ksession.insert(ss);
		
		ksession.fireAllRules();
		clock.advanceTime(29, TimeUnit.HOURS);		
		rulesFired = ksession.fireAllRules();

		Assert.assertEquals("Front port flood deactivated at Midnight", "0", ss.getStatus());
		Assert.assertEquals("Check that only 2 rules fired",2,rulesFired);
		
		/**
		 * Time to test and make sure the light won't turn off randomly.  Pretty sure it won't
		 * since it was such a pain to get the test to work correctly from the start 8) 
		 */
		
		agendaEventListener.reset();
		clock.advanceTime(10, TimeUnit.HOURS);
		ksession.fireAllRules();
		
		ss.setStatus("255");
		ksession.update(fh, ss);
		
		clock.advanceTime(2, TimeUnit.HOURS);
		rulesFired = ksession.fireAllRules();
		
		Assert.assertEquals("Front port flood did not deactivate at Noon", "255", ss.getStatus());
		Assert.assertEquals("Check that no rules fired",0,rulesFired);

		ksession.dispose();
	}
}
