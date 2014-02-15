package com.plip.eventhandlers.listeners;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.plip.eventhandlers.events.FinishRecognitionEvent;
import com.plip.eventhandlers.events.TrueMatcherEvent;
import com.plip.eventhandlers.handlers.RecognizerEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.exceptions.system.AdministratorPanelConnectionException;
import com.plip.system.communication.WebServiceManager;
import com.plip.system.config.SystemUtils;

public class RecognizerEventListener implements GenericEventListener {
	
	private RecognizerEventHandler rehandler;
	private TrayEventHandler tehandler;

	public RecognizerEventListener(TrayEventHandler tehandler,RecognizerEventHandler rehandler) {
		this.rehandler = rehandler;
		this.tehandler = tehandler;
	}

	@Override
	public void handleEvent(EventObject event) {
		List<NameValuePair> urlParameters = null;
		
		if (event instanceof FinishRecognitionEvent) {
			try {
				urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("page_id", tehandler.getPage().getIdPage().toString()));
				urlParameters.add(new BasicNameValuePair("tray_status_id", String.valueOf(rehandler.getTrayStatusId())));
		
				WebServiceManager wsManager = new WebServiceManager(urlParameters); 
				wsManager.setUrl(new SystemUtils().getParam("plipAdministratorPanelUrl"));
				Thread myThread = new Thread(wsManager);
				myThread.start(); 
							
			} catch (PageNotFoundException e) {
				return;
				//e.printStackTrace();
			}
		}
	}
	
	
	
}
