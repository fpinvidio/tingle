package com.plip.eventhandlers.listeners;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.handlers.CounterEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.exceptions.system.AdministratorPanelConnectionException;
import com.plip.system.communication.WebServiceManager;
import com.plip.system.config.SystemUtils;

public class CounterEventListener implements GenericEventListener {
	
	private TrayEventHandler tehandler;
	private CounterEventHandler cehandler;

	public CounterEventListener(TrayEventHandler tehandler,
			CounterEventHandler cehandler) {
		this.tehandler = tehandler;
		this.cehandler = cehandler;
	}

	@Override
	public void handleEvent(EventObject event) {
		List<NameValuePair> urlParameters = null;

		if (event instanceof FinishCounterEvent){ 
			try {
				urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("page_id", tehandler
						.getPage().getIdPage().toString()));
				urlParameters.add(new BasicNameValuePair("tray_status_id", String
						.valueOf(cehandler.getTrayStatusId())));
				
				WebServiceManager wsManager = new WebServiceManager(urlParameters); 
				wsManager.setUrl(new SystemUtils().getParam("plipAdministratorPanelUrl"));
				Thread myThread = new Thread(wsManager);
				myThread.start(); 
				
			} catch (PageNotFoundException e) {
				e.printStackTrace();
			}
		}
	}	
}

