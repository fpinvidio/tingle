package eventhandling;

import java.util.Date;
import java.util.EventObject;

public class TrayDepartureEvent extends EventObject implements GenericTrayEvent {

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

	@Override
	public boolean isOfType(String type) {
		return EventFactory.TRAY_DEPARTURE_EVENT.equals(type);
	}
	
	@Override
	public String getEventType() {
		return EventFactory.TRAY_DEPARTURE_EVENT;
	}
}
