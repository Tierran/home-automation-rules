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

public class PergolaRulesTest extends MyTestHelper {

	@Test
	public void testPergolaLightsActivateAtDusk() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Pergola Rules.drl");

		/**
		 * Check that front flood light activates at dusk.  This should only happen the moment
		 * of the event to allow for manual control of the light otherwise
		 */
		
		ElectricOutlet eo = new ElectricOutlet();
		eo.setId("296");
		eo.setSource("homeseer");
		eo.setStatus("0");

		HaEvent event = new HaEvent();
		event.setDeviceId("23");
		event.setValue("2");

		clock.advanceTime(500, TimeUnit.MILLISECONDS);
		System.out.println(clock.getCurrentTime());
		ksession.insert(eo);
		ksession.insert(event);
		agendaEventListener.reset();
		rulesFired = ksession.fireAllRules();

		Assert.assertEquals("Check that pergola lights were turned on", "255", eo.getStatus());

		ksession.dispose();
	}

	@Test
	public void testPergolaLightsTurnOffAtNine() throws Exception {

		KieSession ksession = testHelper("Update Device Status.drl", "Pergola Rules.drl");

		/**
		 * First we will confirm that the flood light turns off at midnight.  Keep an eye on the 
		 * time offset as this may cause a problem during daylight savings.  If this test starts breaking
		 * then I may need to introduce some logic to adjust between CST and CDT. 
		 */
		
		DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
		Date date = df.parse( "2019-01-01T01:21:00.000-0000" );
		clock.advanceTime(date.getTime(), TimeUnit.MILLISECONDS);
		
		
		ElectricOutlet eo = new ElectricOutlet();
		eo.setId("296");
		eo.setSource("homeseer");
		eo.setStatus("255");
		
		ksession.insert(eo);
		
		ksession.fireAllRules();
		clock.advanceTime(29, TimeUnit.HOURS);		
		rulesFired = ksession.fireAllRules();

		Assert.assertEquals("Pergola light deactivated at 9 P.M.", "0", eo.getStatus());

		ksession.dispose();
	}
}
