package com.plip.eventhandlers.events;

import java.util.Date;
import java.util.EventObject;

import org.opencv.core.Mat;

public class TrayArrivalEvent extends EventObject {

	private static final long serialVersionUID = -5279179661488190208L;
	private Date arrival_date;
	private Mat[] tray_images;

	public TrayArrivalEvent(Object source) {
		super(source);
	}

	public TrayArrivalEvent(Object source, Date arrival_date, Mat[] tray_images) {
		super(source);
		this.arrival_date = arrival_date;
		this.tray_images = tray_images;
	}

	public Date getArrival_date() {
		return arrival_date;
	}

	public Mat[] getTray_images() {
		return tray_images;
	}
	
}
