package com.plip.eventhandlers.events;

import java.util.EventObject;

public class FinishCounterEvent extends EventObject {

	private static final long serialVersionUID = 1137289429612506218L;

	public FinishCounterEvent(Object source) {
		super(source);
	}
}
