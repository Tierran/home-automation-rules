package com.ninetailsoftware.ha.home_automation_rules;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.time.SessionPseudoClock;

import com.ninetailsoftware.ha.support.TrackingAgendaEventListener;

public class MyTestHelper extends CommonTestMethodBase {
	protected int rulesFired;
	protected TrackingAgendaEventListener agendaEventListener;
	protected SessionPseudoClock clock;
	
	protected KieSession testHelper(String... rules) {
		KieSessionConfiguration ksconfig = KieServices.Factory.get().newKieSessionConfiguration();
		ksconfig.setOption(ClockTypeOption.get("pseudo"));
		KieBaseConfiguration kbconfig = KieServices.Factory.get().newKieBaseConfiguration();
		kbconfig.setOption(EventProcessingOption.STREAM);
		final KieBase kbase = loadKnowledgeBase(kbconfig, rules);
		final KieSession ksession = kbase.newKieSession(ksconfig, null);
		this.clock = ksession.getSessionClock();
		this.agendaEventListener = new TrackingAgendaEventListener();
		ksession.addEventListener(agendaEventListener);
		
		return ksession;
	}	
}
