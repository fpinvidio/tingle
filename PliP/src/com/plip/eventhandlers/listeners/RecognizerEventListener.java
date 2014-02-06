package com.plip.eventhandlers.listeners;

import java.util.EventObject;

import com.plip.eventhandlers.events.FinishRecognitionEvent;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

public class RecognizerEventListener implements GenericEventListener {

	@Override
	public void handleEvent(EventObject event) {
		
		if (event instanceof FinishRecognitionEvent) {
			String urlParameters ="sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			System.out.println("Entro en RecognizerEventListener");
			try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}
		}
	}
}
