package com.plip.eventhandlers.events;

import java.util.EventObject;

public class FinishCounterEvent extends EventObject {

	private static final long serialVersionUID = 272855349758957367L;

	public FinishCounterEvent(Object source) {
		super(source);
	}

}
