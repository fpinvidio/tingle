package com.plip.systemmonitor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.events.FinishRecognitionEvent;
import com.plip.eventhandlers.events.StartRecognitionEvent;
import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.TrayDepartureEvent;
import com.plip.eventhandlers.handlers.CounterEventHandler;
import com.plip.eventhandlers.handlers.RecognizerEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.imageprocessing.matchers.exceptions.NoMatchException;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.ObjectRecognizer;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.imageprocessing.processors.Exceptions.NoImageException;
import com.plip.persistence.dao.impls.PageDaoImpl;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;
import com.plip.uinterfaces.MainMenuFrame;

// Queda pendiente organizar handlers para todos los eventos 

public class MainSystemMonitor implements GenericEventListener {

	private VideoCapture vcapture;

	private TrayProcessor tprocessor;
	private ObjectCounter ocounter;
	private ObjectRecognizer orecognizer;

	private TrayEventHandler tehandler;
	private CounterEventHandler cehandler;
	private RecognizerEventHandler rehandler;

	private MainMenuFrame mmf;

	public MainSystemMonitor() {
		super();
		 mmf = new MainMenuFrame(this);
		 mmf.setVisible(true);
	}

	public static void main(String arg[]) {
		System.loadLibrary("opencv_java246");
		MainSystemMonitor msm = new MainSystemMonitor();
		msm.initializeCapture();

		// PlipTrainer trainer = new PlipTrainer();
		// trainer.processProductImages();
		// PlipRoleDao roleDao = new PlipRoleDaoImpl();
	}

	/*
	 * CvCapture *cap; int n = 0; while(1) { cap = cvCreateCameraCapture(n++);
	 * if (cap == NULL) break; cvReleaseCapture(&cap); }
	 * 
	 * cvReleaseCapture(&cap); return n-1;
	 */

	public void initializeCapture() {

		vcapture = new VideoCapture(0);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 850);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 650);
		
		/*PLiP processors*/
		tprocessor = new TrayProcessor();
		ocounter = new ObjectCounter();
		orecognizer = new ObjectRecognizer();

		/*event handlers*/
		tehandler = new TrayEventHandler();
		cehandler = new CounterEventHandler();
		rehandler = new RecognizerEventHandler();

		/*event listeners*/
		tehandler.addEventListener(mmf);
		tehandler.addEventListener(this);
		cehandler.addEventListener(this);
		rehandler.addEventListener(this);
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

			System.out.println("Tray Arrival");

			/* Get Tray Page from Database */
			PageDaoImpl pageDao = new PageDaoImpl();
			Page page = null;
			try {
				page = pageDao.getPage(1);
			} catch (PageNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("se fue todo al joraca");
			}
			tehandler.setPage(page);

			// Pensar como vamos a saber la page correspondiente a la tray en
			// ese momento!

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 2048);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1366);

			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			Mat screenshot = new Mat();
			vcapture.read(screenshot);

			Highgui.imwrite("Tray.jpg", screenshot);

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 850);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 650);

			ArrayList<Mat> images = new ArrayList<Mat>();

			try {
				images = ocounter.count(screenshot);
			} catch (NoImageException e) {
				e.printStackTrace();
			}

			cehandler.addCountedObjects(images);

		} else if (event instanceof TrayDepartureEvent) {

			System.out.println("Tray Departure");

		} else if (event instanceof FinishCounterEvent) {

			System.out.println("Finish Counter");
			rehandler.startRecognitionEvent(cehandler.getCountedObjects());

		} else if (event instanceof StartRecognitionEvent) {

			System.out.println("Start Recognition Event");
			orecognizer.computeDescriptors(cehandler.getCountedObjects(),
					tehandler.getPage());
			List<Mat> foundImagesDescriptors = orecognizer
					.getFoundImagesDescriptors();

			for (Mat image : foundImagesDescriptors) {
				try {
					Product productMatch = orecognizer.recognize(
							tehandler.getPage(), image);

					if (productMatch != null && productMatch.getName() != null) {
						System.out.println(productMatch.getName()
								+ '-'
								+ orecognizer.getFoundImageNames().get(
										foundImagesDescriptors.indexOf(image)));
					}

					rehandler.validRecognitionEvent();
					System.out.println("True Matcher Event");

				} catch (NoMatchException e) {
					rehandler.falseRecognitionEvent();
					System.out.println("False Matcher Event");
				} finally {
					rehandler.finishRecognitionEvent();
				}
			}
		} else if (event instanceof FinishRecognitionEvent) {
			System.out.println("Finish Recognition Event");
		}
	}
}
