package com.plip.eventhandlers.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;

import org.opencv.core.Mat;

import com.plip.eventhandlers.listeners.GenericEventListener;

public class CounterEventHandler extends GenericEventHandler {

	private ArrayList<Mat> countedObjects;

	@Override
	protected synchronized void fireEvent(String type) {
		Mat[] tempArray = (Mat[]) countedObjects.toArray();
		EventObject event = EventFactory.generateEvent(type, this, new Date(),
				tempArray);
		Iterator<GenericEventListener> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			((GenericEventListener) iterator.next()).handleEvent(event);
		}
	}

	public void addCountedObjects(ArrayList<Mat> countedObjects) {
		this.countedObjects = countedObjects;
		this.fireEvent(EventFactory.FINISHED_COUNT_EVENT);
	}
}
