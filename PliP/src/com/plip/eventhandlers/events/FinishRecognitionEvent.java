package com.plip.eventhandlers.events;

import java.util.EventObject;

public class FinishRecognitionEvent extends EventObject{

	private static final long serialVersionUID = -9086393041187929040L;

	public FinishRecognitionEvent(Object source) {
		super(source);
	}
}
