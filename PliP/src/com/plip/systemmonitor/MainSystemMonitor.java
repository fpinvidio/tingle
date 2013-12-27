package com.plip.systemmonitor;


import java.util.ArrayList;
import java.util.EventObject;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.events.StartCounterEvent;
import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.TrayDepartureEvent;
import com.plip.eventhandlers.handlers.CounterEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.imageprocessing.trainers.PlipTrainer;
import com.plip.persistence.dao.impls.PositionDaoImpl;
import com.plip.persistence.model.Position;
import com.plip.uinterfaces.MainMenuFrame;


// Queda pendiente organizar handlers para todos los eventos 

public class MainSystemMonitor implements GenericEventListener {
	private VideoCapture vcapture;
	private TrayProcessor tprocessor;
	private ObjectCounter ocounter;
	private TrayEventHandler tehandler;
	private CounterEventHandler cehandler;
	private MainMenuFrame mmf;
	

	public MainSystemMonitor() {
		super();
		mmf = new MainMenuFrame(this);
		mmf.setVisible(true);
	}

	public static void main(String arg[]) {
		System.loadLibrary("opencv_java246");
//		MainSystemMonitor msm = new MainSystemMonitor();
//		msm.initializeCapture();
		
		PlipTrainer trainer = new PlipTrainer();
		trainer.processProductImages();
	}

	/*CvCapture *cap;
	int n = 0;
	while(1)
	{
	   cap = cvCreateCameraCapture(n++);
	   if (cap == NULL) break;
	   cvReleaseCapture(&cap);
	}

	cvReleaseCapture(&cap);
	return n-1;
	*/
	public void initializeCapture() {

		vcapture = new VideoCapture(1);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 200);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 250);
		tprocessor = new TrayProcessor();
		ocounter = new ObjectCounter();
		tehandler = new TrayEventHandler();
		cehandler = new CounterEventHandler();
		tehandler.addEventListener(mmf);
		tehandler.addEventListener(this);
		cehandler.addEventListener(this);
	}

	public Mat captureVideoFrame() {
		Mat capturedFrame = new Mat();
		if (vcapture.isOpened()) {
			vcapture.read(capturedFrame);
			if (!capturedFrame.empty()) {
				capturedFrame = tprocessor.findRectangleInImage(capturedFrame);
				Mat possibleTray = tprocessor.getPossibleTray();
				tehandler.addTray(possibleTray);
				tprocessor.clearPossibleTray();
			} else {
				capturedFrame = null;
			}
		}
		return capturedFrame;
	}

	@Override
	public void handleEvent(EventObject event) {
		if (event instanceof TrayArrivalEvent) {
			System.out.println("Arrival");
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 2592);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1936);
			Mat screenshot = new Mat(); 
			vcapture.read(screenshot);
			while(screenshot.empty()) {
				vcapture.read(screenshot);
			}
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 200);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 250);
			Highgui.imwrite("Tray.jpg", screenshot);
			TrayArrivalEvent temp = (TrayArrivalEvent) event;
			ArrayList<Mat> images = ocounter.count(screenshot);
			cehandler.addCountedObjects(images);
		} else if (event instanceof TrayDepartureEvent) {
			System.out.println("Departure");
		} else if (event instanceof StartCounterEvent) {
			System.out.println("StartedCount");
		} else if (event instanceof FinishCounterEvent) {
			System.out.println("FinishedCount");
		}
	}
}
