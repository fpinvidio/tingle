package com.plip.eventhandlers.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import com.plip.eventhandlers.listeners.GenericEventListener;



public abstract class GenericEventHandler {

	protected List<GenericEventListener> listeners = new ArrayList<GenericEventListener>();
	protected EventObject lastEvent = null;

	public synchronized void addEventListener(GenericEventListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeEventListener(GenericEventListener listener) {
		listeners.remove(listener);
	}

	protected synchronized void fireEvent(String type) {
		EventObject event = EventFactory.generateEvent(type, this, new Date(),
				null);
		Iterator<GenericEventListener> iterator = listeners.iterator();
		setLastEvent(event);
		while (iterator.hasNext()) {
			((GenericEventListener) iterator.next()).handleEvent(event);
		}
	}

	private void setLastEvent(EventObject event) {
		this.lastEvent = event;
	}

}
