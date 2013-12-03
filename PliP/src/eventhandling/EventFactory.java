package eventhandling;

import java.util.Date;
import java.util.EventObject;

import org.opencv.core.Mat;

import eventhandling.events.FinishedCountEvent;
import eventhandling.events.StartedCountEvent;
import eventhandling.events.TrayArrivalEvent;
import eventhandling.events.TrayDepartureEvent;

public class EventFactory {
	public static final String TRAY_ARRIVAL_EVENT = "TrayArrivalEvent";
	public static final String TRAY_DEPARTURE_EVENT = "TrayDepartureEvent";
	public static final String STARTED_COUNT_EVENT = "StartedCountEvent";
	public static final String FINISHED_COUNT_EVENT = "FinishedCountEvent";

	public static EventObject generateEvent(String type, Object source,
			Date date, Mat[] images) {
		EventObject event = null;
		switch (type) {
		case TRAY_ARRIVAL_EVENT:
			event = new TrayArrivalEvent(source, date, images);
			break;
		case TRAY_DEPARTURE_EVENT:
			event = new TrayDepartureEvent(source, date);
			break;
		case STARTED_COUNT_EVENT:
			event = new StartedCountEvent(source);
			break;
		case FINISHED_COUNT_EVENT:
			event = new FinishedCountEvent(source);
			break;
		}
		return event;
	}
}
