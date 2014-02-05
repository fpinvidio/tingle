package com.plip.eventhandlers.listeners;

import java.io.IOException;
import java.util.EventObject;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

public class CounterEventListener implements GenericEventListener {
	
	@Override
	public void handleEvent(EventObject event) {
	
		if (event instanceof FinishCounterEvent) {
			String urlParameters="";
			System.out.println("Entro en CounterEventListener");
			/*try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}*/
		}
	}
}
