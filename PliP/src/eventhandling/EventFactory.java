package eventhandling;

import java.util.Date;
import java.util.EventObject;

import org.opencv.core.Mat;

public class EventFactory {
	public static final String TRAY_ARRIVAL_EVENT = "TrayArrivalEvent";
	public static final String TRAY_DEPARTURE_EVENT = "TrayDepartureEvent";

	public static EventObject generateTrayEvent(String type, Object source,
			Date date, Mat[] tray_images) {
		EventObject event = null;
		switch (type) {
		case TRAY_ARRIVAL_EVENT:
			event = new TrayArrivalEvent(source, date, tray_images);
			break;
		case TRAY_DEPARTURE_EVENT:
			event = new TrayDepartureEvent(source, date);
		}
		return event;
	}
}
