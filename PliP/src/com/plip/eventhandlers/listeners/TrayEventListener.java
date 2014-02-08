package com.plip.eventhandlers.listeners;

import java.util.EventObject;

import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.UnSupportedTrayEvent;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

public class TrayEventListener implements GenericEventListener {

	@Override
	public void handleEvent(EventObject event) {

		if (event instanceof TrayArrivalEvent) {
			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}
		} else if (event instanceof UnSupportedTrayEvent) {
			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}
		}
	}
	
}
