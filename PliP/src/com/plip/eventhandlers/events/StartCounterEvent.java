package com.plip.eventhandlers.events;

import java.util.EventObject;

public class StartCounterEvent extends EventObject {

	private static final long serialVersionUID = 3615741944257423287L;

	public StartCounterEvent(Object source) {
		super(source);
	}

}
