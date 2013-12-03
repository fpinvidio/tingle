package imageprocessing.tray;

import imageprocessing.counter.ObjectCounter;

import java.util.EventObject;

import org.opencv.core.Mat;

import eventhandling.events.TrayArrivalEvent;
import eventhandling.listeners.GenericEventListener;

public class ObjectProcessor implements GenericEventListener {

	@Override
	public void handleEvent(EventObject event) {
		if (event instanceof TrayArrivalEvent) {
			TrayArrivalEvent tae = (TrayArrivalEvent) event;
			Mat[] images = tae.getTray_images();
			Mat countImage = images[images.length / 2];
			ObjectCounter oc = new ObjectCounter();
			oc.count(countImage);
		}

	}
}
