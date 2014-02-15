package com.plip.eventhandlers.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;

import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.exceptions.persistence.NoPageRecievedException;
import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.exceptions.persistence.StatusNotFoundException;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.persistence.daos.impls.StatusDaoImpl;
import com.plip.persistence.daos.impls.TrayDaoImpl;
import com.plip.persistence.daos.impls.TrayStatusDaoImpl;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;
import com.plip.system.monitors.MainSystemMonitor;
import com.plip.system.providers.LocalPageProvider;
import com.plip.system.providers.PageProvider;
import com.plip.system.providers.RemotePageProvider;

public class TrayEventHandler extends GenericEventHandler {
	private final int BUFFER_SIZE = 2;
	private final float tol = 0.99f;
	private List<Mat> tray_buffer = new ArrayList<Mat>();
	private Tray tray;
	private long trayStatusId;
	
	public long getTrayStatusId() {
		return trayStatusId;
	}

	public void setTrayStatusId(long trayStatusId) {
		this.trayStatusId = trayStatusId;
	}

	public Tray getTray() {
		return tray;
	}

	public void setTray(Tray tray) {
		this.tray = tray;
	}

	@Override
	protected synchronized void fireEvent(String type) {
		Mat[] tempArray = trayBufferArray();
		EventObject event = EventFactory.generateEvent(type, this, new Date(),
				tempArray);
		Iterator<GenericEventListener> iterator = listeners.iterator();
		setLastEvent(event);
		while (iterator.hasNext()) {
			((GenericEventListener) iterator.next()).handleEvent(event);
		}
	}

	private void setLastEvent(EventObject event) {
		this.lastEvent = event;
	}

	 public Page getPage() throws PageNotFoundException {
	 if(tray.getPage()!=null) {
	 return this.tray.getPage();
	 }else throw new PageNotFoundException();	 
	 }

	private boolean isBufferFull() {
		return tray_buffer.size() == BUFFER_SIZE;
	}

	
	public Tray createTray() throws NoPageRecievedException, PageNotFoundException{
		/* Get Tray Page from Database */
		Tray trayModel = new Tray();
		//PageProvider pageManager = new LocalPageProvider();
		PageProvider pageProvider = new RemotePageProvider();
		
		TrayDaoImpl trayDao = new TrayDaoImpl();
		try {
			Page page = pageProvider.getLastPage();
			trayModel.setPage(page);
			trayModel.setCode(page.getOrder().getCode());
			trayDao.addTray(trayModel);
		} catch ( NullModelAttributesException e) {
			e.printStackTrace();
		}
		return trayModel;
	}
	
	public void addTray(Mat tray) throws PageNotFoundException {
		tray_buffer.add(tray);
		if (isBufferFull()) {
			if (isBufferFullOfTrays()) {
				if (lastEvent == null
						|| !(lastEvent instanceof TrayArrivalEvent)) {
					MainSystemMonitor.trayBounds = TrayProcessor.getTrayBound();
					
					Tray trayModel;
					try {
						trayModel = createTray();
					} catch (NoPageRecievedException e) {
						return;
					}
					setTray(trayModel);
					setTrayStatusId(saveTrayArraivalStatus());					
					fireEvent(EventFactory.TRAY_ARRIVAL_EVENT);
				}
			} else if (isBufferFullOfVoid()) {
				if (lastEvent != null && lastEvent instanceof TrayArrivalEvent) {
					fireEvent(EventFactory.TRAY_DEPARTURE_EVENT);
				}
			}
			tray_buffer.clear();
		}
	}
	
	public void unSupportedTrayEvent() throws NoPageRecievedException, PageNotFoundException{
		Tray trayModel = createTray();
		setTray(trayModel);
		setTrayStatusId(saveTrayArraivalStatus());
		fireEvent(EventFactory.UNSUPPORTED_TRAY_EVENT);
	}

	private boolean isBufferFullOfTrays() {
		int count = 0;
		Iterator<Mat> iterator = tray_buffer.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() != null) {
				count++;
			}
		}
		return count > BUFFER_SIZE * tol;
	}

	private boolean isBufferFullOfVoid() {
		int count = 0;
		Iterator<Mat> iterator = tray_buffer.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == null) {
				count++;
			}
		}
		return count > BUFFER_SIZE * tol;
	}

	private Mat[] trayBufferArray() {
		Mat[] array = new Mat[BUFFER_SIZE];
		for (int i = 0; i < BUFFER_SIZE; i++) {
			Mat mat = tray_buffer.get(i);
			array[i] = mat;
		}
		return array;
	}
	
	public long saveTrayArraivalStatus(){
		/*Save Status when tray arrives*/
		long idStatus = 0;
		TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
		TrayStatus trayDetected = new TrayStatus();
		StatusDaoImpl statusDao = new StatusDaoImpl();
		if(this.tray != null){
		try {
			Status trayDetectedStatus = statusDao.getStatus(Status.STATUS_TRAY_ARRAIVAL);
			trayDetected.setDate(new Date());
			trayDetected.setStatus(trayDetectedStatus);
			trayDetected.setTray(this.tray);
			idStatus = trayStatusDao.addTrayStatus(trayDetected);
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
		}
		}
		return idStatus;
	}
}
