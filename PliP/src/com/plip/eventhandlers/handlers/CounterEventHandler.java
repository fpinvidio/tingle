package com.plip.eventhandlers.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;

import org.opencv.core.Mat;

import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.persistence.dao.impls.StatusDaoImpl;
import com.plip.persistence.dao.impls.TrayStatusDaoImpl;
import com.plip.persistence.exceptions.StatusNotFoundException;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;

public class CounterEventHandler extends GenericEventHandler {

	private ArrayList<Mat> countedObjects;
	private long trayStatusId;

	public long getTrayStatusId() {
		return trayStatusId;
	}

	public void setTrayStatusId(long trayStatusId) {
		this.trayStatusId = trayStatusId;
	}

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

	public void addCountedObjects(ArrayList<Mat> countedObjects, Tray tray) {
		this.countedObjects = countedObjects;
		saveFinishedCounterStatus(tray);
		this.fireEvent(EventFactory.FINISHED_COUNT_EVENT);
	}

	public ArrayList<Mat> getCountedObjects() {
		return countedObjects;
	}

	public void setCountedObjects(ArrayList<Mat> countedObjects) {
		this.countedObjects = countedObjects;
	}

	public void saveFinishedCounterStatus(Tray tray) {
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		TrayStatus countedTrayStatus = new TrayStatus();
		TrayStatus quantityResultTrayStatus = new TrayStatus();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		if (tray.getPage() != null) {
			try {
				Status countedStatus = statusDao
						.getStatus(Status.STATUS_TRAY_COUNTED);
				Status quantityStatus;
				if (tray.getPage().getProductQuantity() == this.countedObjects
						.size()) {
					quantityStatus = statusDao
							.getStatus(Status.STATUS_VALID_QUANTITY);
					System.out.println("STATUS_VALID_QUANTITY");
				} else {
					quantityStatus = statusDao
							.getStatus(Status.STATUS_INVALID_QUANTITY);
					System.out.println("STATUS_INVALID_QUANTITY");
				}
				countedTrayStatus.setDate(new Date());
				countedTrayStatus.setQuantity(this.getCountedObjects().size());
				countedTrayStatus.setStatus(countedStatus);
				countedTrayStatus.setTray(tray);
				trayStatusDao.addTrayStatus(countedTrayStatus);
				quantityResultTrayStatus.setDate(new Date());
				quantityResultTrayStatus.setQuantity(this.countedObjects.size()
						- tray.getPage().getProductQuantity());
				quantityResultTrayStatus.setStatus(quantityStatus);
				quantityResultTrayStatus.setTray(tray);

				setTrayStatusId(trayStatusDao
						.addTrayStatus(quantityResultTrayStatus));

			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
