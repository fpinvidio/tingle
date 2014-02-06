package com.plip.eventhandlers.listeners;

import java.util.EventObject;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

public class CounterEventListener implements GenericEventListener {
	
		@Override
		public void handleEvent(EventObject event) {
		if (event instanceof FinishCounterEvent) {
			String urlParameters="sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}
		}
	}
}
