package com.plip.eventhandlers.handlers;

import java.util.Date;
import java.util.EventObject;

import org.opencv.core.Mat;

import com.plip.eventhandlers.events.FalseMatcherEvent;
import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.events.FinishRecognitionEvent;
import com.plip.eventhandlers.events.StartRecognitionEvent;
import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.TrayDepartureEvent;
import com.plip.eventhandlers.events.TrueMatcherEvent;

public class EventFactory {
	public static final String TRAY_ARRIVAL_EVENT = "TrayArrivalEvent";
	public static final String TRAY_DEPARTURE_EVENT = "TrayDepartureEvent";
	public static final String FINISHED_COUNT_EVENT = "FinishedCountEvent";
	public static final String TRUE_MATCHER_EVENT = "TrueMatcherEvent";
	public static final String FALSE_MATCHER_EVENT = "FalseMatcherEvent";
	public static final String START_RECOGNITION_EVENT = "StartRecognitionEvent";
	public static final String FINISHED_RECOGNITION_EVENT = "FinishedRecognitionEvent";

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
		case FINISHED_COUNT_EVENT:
			event = new FinishCounterEvent(source);
			break;
		case TRUE_MATCHER_EVENT:
			event = new TrueMatcherEvent(source);
			break;
		case FALSE_MATCHER_EVENT:
			event = new FalseMatcherEvent(source);
			break;
		case START_RECOGNITION_EVENT:
			event = new StartRecognitionEvent(source);
			break;
		case FINISHED_RECOGNITION_EVENT:
			event = new FinishRecognitionEvent(source);
			break;
		}
		return event;
	}
}
