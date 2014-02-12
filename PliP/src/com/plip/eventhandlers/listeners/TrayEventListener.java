package com.plip.eventhandlers.listeners;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.UnSupportedTrayEvent;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.WebServiceManager;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

public class TrayEventListener implements GenericEventListener {

	private TrayEventHandler tehandler;

	public TrayEventListener(TrayEventHandler tehandler) {
		this.tehandler = tehandler;
	}

	@Override
	public void handleEvent(EventObject event) {
		List<NameValuePair> urlParameters = null;
		if (event instanceof TrayArrivalEvent ) {
			try {
				urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("page_id", tehandler.getPage().getIdPage().toString()));
				urlParameters.add(new BasicNameValuePair("tray_status_id", String
						.valueOf(tehandler.getTrayStatusId())));
				
				WebServiceManager wsManager = new WebServiceManager(urlParameters); 
				Thread myThread = new Thread(wsManager);
				myThread.start(); 
				
			} catch (PageNotFoundException e) {
				return;
			}
			
		} else if (event instanceof UnSupportedTrayEvent) {
			try {
				urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("page_id", tehandler.getPage().getIdPage().toString()));
				urlParameters.add(new BasicNameValuePair("tray_status_id", String
						.valueOf(tehandler.getTrayStatusId())));
				
				WebServiceManager wsManager = new WebServiceManager(urlParameters); 
				Thread myThread = new Thread(wsManager);
				myThread.start(); 
	
			} catch (PageNotFoundException e) {
			    return;
			}
		}
	}
}
