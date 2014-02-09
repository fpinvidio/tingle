package com.plip.eventhandlers.listeners;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.handlers.CounterEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.systemconfig.SystemUtils;
import com.plip.systemconfig.exceptions.AdministratorPanelConnectionException;

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

		if (event instanceof FinishCounterEvent) 
			try {
				urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("page_id", tehandler
						.getPage().getIdPage().toString()));
				urlParameters.add(new BasicNameValuePair("tray_status_id", String
						.valueOf(cehandler.getTrayStatusId())));
				SystemUtils.connectPlipAdministratiorPanel(urlParameters);
			} catch (AdministratorPanelConnectionException e) {
				e.printStackTrace();
			} catch (PageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

