package com.plip.eventhandlers.events;

import java.util.EventObject;

public class StartRecognitionEvent extends EventObject {

	private static final long serialVersionUID = 1590794217565043198L;

	public StartRecognitionEvent(Object source) {
		super(source);
	}
}
