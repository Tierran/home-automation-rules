package com.ninetailsoftware.ha.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.ninetailsoftware.ha.home_automation_rules.MyTestHelper;
import com.ninetailsoftware.model.events.HaEvent;
import com.ninetailsoftware.model.facts.ElectricOutlet;

public class HolidayRulesTest extends MyTestHelper {

	@Test
	public void testHolidayLightsActivateAtNight() throws Exception {
		KieSession ksession = testHelper("Update Device Status.drl", "Holiday Rules.drl");

		DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
		Date date = df.parse( "2019-01-01T14:16:00.000-0000" );
		clock.advanceTime(date.getTime(), TimeUnit.MILLISECONDS);
		/**
		 * Check that front flood light activates at 16:30.  This should only happen the moment
		 * of the event to allow for manual control of the light otherwise
		 */
		
		ElectricOutlet eo = new ElectricOutlet();
		eo.setId("71");
		eo.setSource("homeseer");
		eo.setStatus("0");
		
		ksession.insert(eo);
		
		ksession.fireAllRules();
		clock.advanceTime(27, TimeUnit.HOURS);		
		rulesFired = ksession.fireAllRules();

		Assert.assertEquals("Check that holiday lights were turned on", "255", eo.getStatus());
		
		clock.advanceTime(6, TimeUnit.HOURS);		
		rulesFired = ksession.fireAllRules();
		
		//Assert.assertEquals("Holiday light deactivated at 10 P.M.", "0", eo.getStatus());

		ksession.dispose();
	}

	@Test
	public void testHolidayLightsTurnOffAtTen() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Holiday Rules.drl");

		/**
		 * First we will confirm that the flood light turns off at midnight.  Keep an eye on the 
		 * time offset as this may cause a problem during daylight savings.  If this test starts breaking
		 * then I may need to introduce some logic to adjust between CST and CDT. 
		 */
		
		DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
		Date date = df.parse( "2019-01-01T21:22:00.000-0000" );
		clock.advanceTime(date.getTime(), TimeUnit.MILLISECONDS);
		
		
		ElectricOutlet eo = new ElectricOutlet();
		eo.setId("71");
		eo.setSource("homeseer");
		eo.setStatus("0");
		
		ksession.insert(eo);
		
		ksession.fireAllRules();
		clock.advanceTime(25, TimeUnit.HOURS);		
		rulesFired = ksession.fireAllRules();

		//Assert.assertEquals("Holiday light deactivated at 10 P.M.", "0", eo.getStatus());

		ksession.dispose();
	}
}
