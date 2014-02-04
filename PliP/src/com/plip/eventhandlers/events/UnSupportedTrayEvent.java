package com.plip.eventhandlers.events;

import java.util.EventObject;

public class UnSupportedTrayEvent extends EventObject {

	private static final long serialVersionUID = -1006212515016007096L;
	
	public UnSupportedTrayEvent(Object source) {
		super(source);
	}
}
