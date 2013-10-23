package imageprocessing;

import java.util.EventObject;

import org.opencv.core.Mat;

import eventhandling.EventFactory;
import eventhandling.GenericTrayEvent;
import eventhandling.TrayArrivalEvent;
import eventhandling.TrayEventListener;

public class ObjectProcessor implements TrayEventListener {

	@Override
	public void handleTrayEvent(EventObject event) {
		GenericTrayEvent gte = (GenericTrayEvent) event;
		if (gte.isOfType(EventFactory.TRAY_ARRIVAL_EVENT)) {
			TrayArrivalEvent tae = (TrayArrivalEvent) event;
			Mat[] images = tae.getTray_images();
			for (Mat image : images) {
				if (image != null) {
					System.out.println("Contar Objetos");
				}
			}
		}

	}

}
