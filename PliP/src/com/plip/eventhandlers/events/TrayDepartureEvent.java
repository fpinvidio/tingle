package com.plip.eventhandlers.events;

import java.util.Date;
import java.util.EventObject;

public class TrayDepartureEvent extends EventObject {

	private static final long serialVersionUID = 5506707119943266359L;
	private Date departure_date;

	public TrayDepartureEvent(Object source) {
		super(source);
	}

	public TrayDepartureEvent(Object source, Date departure_date) {
		super(source);
		this.departure_date = departure_date;
	}

	public Date getDeparture_date() {
		return departure_date;
	}

}
