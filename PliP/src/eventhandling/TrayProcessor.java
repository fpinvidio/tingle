package eventhandling;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;

public class TrayProcessor {
	private final int BUFFER_SIZE = 5;
	private final float tol = 0.7f;
	private List<Mat> tray_buffer = new ArrayList<Mat>();
	private List<TrayEventListener> listeners = new ArrayList<TrayEventListener>();
	private GenericTrayEvent lastEvent = null;

	public synchronized void addEventListener(TrayEventListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeEventListener(TrayEventListener listener) {
		listeners.remove(listener);
	}

	private synchronized void fireEvent(String type) {
		Mat[] tempArray = trayBufferArray();
		EventObject event = EventFactory.generateTrayEvent(type, this,
				new Date(), tempArray);
		Iterator<TrayEventListener> iterator = listeners.iterator();
		setLastEvent((GenericTrayEvent) event);
		while (iterator.hasNext()) {
			((TrayEventListener) iterator.next()).handleTrayEvent(event);
		}
	}

	private void setLastEvent(GenericTrayEvent event) {
		this.lastEvent = event;
	}

	private boolean isBufferFull() {
		return tray_buffer.size() == BUFFER_SIZE;
	}

	public void addTray(Mat tray) {
		tray_buffer.add(tray);
		if (isBufferFull()) {
			if (isBufferFullOfTrays()) {
				if (lastEvent == null
						|| !lastEvent.isOfType(EventFactory.TRAY_ARRIVAL_EVENT)) {
					fireEvent(EventFactory.TRAY_ARRIVAL_EVENT);
				}
			} else if (isBufferFullOfVoid()) {
				if (lastEvent != null
						&& lastEvent.isOfType(EventFactory.TRAY_ARRIVAL_EVENT)) {
					fireEvent(EventFactory.TRAY_DEPARTURE_EVENT);
				}
			}
			tray_buffer.clear();
		}
	}

	private boolean isBufferFullOfTrays() {
		int count = 0;
		Iterator<Mat> iterator = tray_buffer.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() != null) {
				count++;
			}
		}
		return count > BUFFER_SIZE*tol;
	}

	private boolean isBufferFullOfVoid() {
		int count = 0;
		Iterator<Mat> iterator = tray_buffer.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == null) {
				count++;
			}
		}
		return count > BUFFER_SIZE*tol;
	}

	private Mat[] trayBufferArray() {
		Mat[] array = new Mat[BUFFER_SIZE];
		for (int i = 0; i < BUFFER_SIZE; i++) {
			Mat mat = tray_buffer.get(i);
			array[i] = mat;
		}
		return array;
	}
}
