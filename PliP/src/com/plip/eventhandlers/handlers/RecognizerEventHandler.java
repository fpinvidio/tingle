package com.plip.eventhandlers.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;

import org.opencv.core.Mat;

import com.plip.eventhandlers.listeners.GenericEventListener;

public class RecognizerEventHandler extends GenericEventHandler {

	@Override
	protected synchronized void fireEvent(String type) {
		Mat[] tempArray = null;
		EventObject event = EventFactory.generateEvent(type, this, new Date(),
				tempArray);
		Iterator<GenericEventListener> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			((GenericEventListener) iterator.next()).handleEvent(event);
		}
	}

	public void startRecognitionEvent(ArrayList<Mat> countedObjects) {
		if (countedObjects != null && countedObjects.size() > 0) {
			this.fireEvent(EventFactory.START_RECOGNITION_EVENT);
		}
	}

	public void finishRecognitionEvent() {
		this.fireEvent(EventFactory.FINISHED_RECOGNITION_EVENT);
	}

	public void validRecognitionEvent() {
		this.fireEvent(EventFactory.TRUE_MATCHER_EVENT);
	}

	public void falseRecognitionEvent() {
		this.fireEvent(EventFactory.FALSE_MATCHER_EVENT);
	}
}
