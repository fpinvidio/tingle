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
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;
import com.plip.system.validators.TrayValidator;

public class RecognizerEventHandler extends GenericEventHandler {
	private long trayStatusId;

	public long getTrayStatusId() {
		return trayStatusId;
	}

	private void setTrayStatusId(long trayStatusId) {
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

	public void startRecognitionEvent(ArrayList<Mat> countedObjects) {
		if (countedObjects != null && countedObjects.size() > 0) {
			this.fireEvent(EventFactory.START_RECOGNITION_EVENT);
		}
	}

	public void finishRecognitionEvent(Tray tray) {
		TrayValidator trayValidator = new TrayValidator();
		trayValidator.validateTray(tray);	
		this.fireEvent(EventFactory.FINISHED_RECOGNITION_EVENT);
	}

	public void validRecognitionEvent(Tray tray, Product productMatch) {
		saveProductRecognizedStatus(tray, productMatch);
		this.fireEvent(EventFactory.TRUE_MATCHER_EVENT);
	}

	public void falseRecognitionEvent(Tray tray) {
		saveProductNotRecognizedStatus(tray);
		this.fireEvent(EventFactory.FALSE_MATCHER_EVENT);
	}

	public void saveProductRecognizedStatus(Tray tray, Product product) {
		/* Save trayStatus when a product is recognized */
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		if (tray != null) {
			try {
				Status productRecognizedStatus = statusDao
						.getStatus(Status.STATUS_PRODUCT_RECOGNIZED);
				TrayStatus productRecognized = new TrayStatus(product, tray,
						productRecognizedStatus, 1, new Date());
				trayStatusDao.addTrayStatus(productRecognized);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveProductNotRecognizedStatus(Tray tray) {
		/* Save trayStatus when no product is recognized */
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		if (tray != null) {
			try {
				Status productNotRecognizedStatus = statusDao
						.getStatus(Status.STATUS_PRODUCT_NOT_RECOGNIZED);
				TrayStatus productRecognized = new TrayStatus(tray,
						productNotRecognizedStatus, new Date());
				setTrayStatusId(trayStatusDao.addTrayStatus(productRecognized));
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
