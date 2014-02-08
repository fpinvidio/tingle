package com.plip.eventhandlers.listeners;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.UnSupportedTrayEvent;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

public class TrayEventListener implements GenericEventListener {

	TrayEventHandler tehandler;
	
	public TrayEventListener(TrayEventHandler tehandler) {
		this.tehandler = tehandler;
	}

	@Override
	public void handleEvent(EventObject event) {
		List<NameValuePair> urlParameters = null;
		if (event instanceof TrayArrivalEvent) {
			
			urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("page_id", tehandler.getPage().getIdPage().toString()));
			
			try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}
			
		} else if (event instanceof UnSupportedTrayEvent) {
			urlParameters = new ArrayList<NameValuePair>();
			try {
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			}
		}
	}
	
}
