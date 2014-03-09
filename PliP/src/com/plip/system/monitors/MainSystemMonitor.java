package com.plip.system.monitors;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.events.StartRecognitionEvent;
import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.TrayDepartureEvent;
import com.plip.eventhandlers.handlers.CounterEventHandler;
import com.plip.eventhandlers.handlers.RecognizerEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.eventhandlers.listeners.CounterEventListener;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.eventhandlers.listeners.RecognizerEventListener;
import com.plip.eventhandlers.listeners.TrayEventListener;
import com.plip.exceptions.imageprocessing.NoImageException;
import com.plip.exceptions.imageprocessing.NoMatchException;
import com.plip.exceptions.persistence.NoPageRecievedException;
import com.plip.exceptions.persistence.PageNotFoundException;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.ObjectRecognizer;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.persistence.model.Product;
import com.plip.system.config.SystemUtils;
import com.plip.uinterfaces.MainMenuFrame;

public class MainSystemMonitor implements GenericEventListener {

	private VideoCapture vcapture;

	private TrayProcessor tprocessor;
	private ObjectCounter ocounter;
	private ObjectRecognizer orecognizer;

	private TrayEventHandler tehandler;
	private CounterEventHandler cehandler;
	private RecognizerEventHandler rehandler;

	private CounterEventListener celistener;
	private RecognizerEventListener relistener;
	private TrayEventListener telistener;

	private MainMenuFrame mmf;

	public static Rect trayBounds = null;
	public static int imageResolutionWidth = 800;
	public static int imageResolutionHeight = 600;
	public static int captureResolutionWidth = 800;
	public static int captureResolutionHeight = 600;

	public static int cameraInput;

	public MainSystemMonitor() {
		super();
		SystemUtils utils = new SystemUtils();
		mmf = new MainMenuFrame(this);
		mmf.setVisible(true);
	}

	public static void main(String arg[]) {
		System.loadLibrary("opencv_java246");
		MainSystemMonitor msm = new MainSystemMonitor();
		msm.initialize();
	}

	public void initialize() {

		vcapture = new VideoCapture(cameraInput);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, captureResolutionWidth);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, captureResolutionHeight);

		setCamara();

		/* PLiP processors */
		tprocessor = new TrayProcessor();
		ocounter = new ObjectCounter();
		orecognizer = new ObjectRecognizer();

		/* event handlers */
		tehandler = new TrayEventHandler();
		cehandler = new CounterEventHandler();
		rehandler = new RecognizerEventHandler();

		telistener = new TrayEventListener(tehandler);
		celistener = new CounterEventListener(tehandler, cehandler);
		relistener = new RecognizerEventListener(tehandler, rehandler);

		/* event listeners */
		tehandler.addEventListener(mmf);
		tehandler.addEventListener(telistener);
		tehandler.addEventListener(this);

		cehandler.addEventListener(celistener);
		cehandler.addEventListener(this);
		// Sends request to Administration Panel

		rehandler.addEventListener(relistener);
		rehandler.addEventListener(this);

	}

	public void setCamara() {
		vcapture.set(Highgui.CV_CAP_PROP_FOCUS, 28);
		vcapture.set(Highgui.CV_CAP_PROP_BACKLIGHT, 1000);
		vcapture.set(Highgui.CV_CAP_PROP_AUTOGRAB, 1024);
	}

	public Mat captureVideoFrame() {
		Mat capturedFrame = new Mat();
		if (vcapture.isOpened()) {
			vcapture.read(capturedFrame);
			if (!capturedFrame.empty()) {
				capturedFrame = tprocessor.findRectangleInImage(capturedFrame);
				Mat possibleTray = tprocessor.getPossibleTray();
				try {
					tehandler.addTray(possibleTray);
				} catch (PageNotFoundException e) {
					System.out.println("Page not found");
				}
				tprocessor.clearPossibleTray();
			} else {
				capturedFrame = null;
			}
		}
		return capturedFrame;
	}
	
	public void stop(){
		try {
		
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleEvent(EventObject event) {

		if (event instanceof TrayArrivalEvent) {

			System.out.println("Tray Arrival");

			Rect trayBounds = null;
			if (this.trayBounds != null) {
				trayBounds = this.trayBounds;

				double widthScale = ((double) imageResolutionWidth)
						/ captureResolutionWidth;

				double heightScale = ((double) imageResolutionHeight)
						/ captureResolutionHeight;

				trayBounds.height = (int) Math.floor(trayBounds.height
						* heightScale);
				trayBounds.y = (int) Math.floor(trayBounds.y * heightScale);
				trayBounds.width = (int) Math.floor(trayBounds.width
						* widthScale);
				trayBounds.x = (int) Math.floor(trayBounds.x * widthScale);
			}

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, imageResolutionWidth);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,
					imageResolutionHeight);

			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			Mat screenshot = new Mat();
			vcapture.read(screenshot);

			if (trayBounds != null) {

				screenshot = screenshot.submat(trayBounds);
			}
			try {
				tehandler.getPage().addTrayImage(screenshot);
			} catch (PageNotFoundException e) {
				e.printStackTrace();
			}
			Highgui.imwrite("tray.jpg", screenshot);

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH,
					captureResolutionWidth);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,
					captureResolutionHeight);

			int pageQuantity = 0;
			try {
				pageQuantity = tehandler.getPage().getProductQuantity();
			} catch (PageNotFoundException e1) {
				return;
			}

			if (pageQuantity <= 10) {

				ArrayList<Mat> images = new ArrayList<Mat>();

				try {
					images = ocounter.count(screenshot, tehandler.getPage()
							.getProductQuantity());
				} catch (NoImageException e) {
					e.printStackTrace();
				} catch (PageNotFoundException e) {
					return;
				}
				cehandler.addCountedObjects(images, tehandler.getTray());
			} else {
				try {
					tehandler.unSupportedTrayEvent();
				} catch (NoPageRecievedException e) {
					return;
				} catch (PageNotFoundException e) {
					System.out.println("Page not found");
				}
				System.out
						.println("The quantity of products exceeds the system capacity");
				return;
			}

		} else if (event instanceof TrayDepartureEvent) {

			System.out.println("Tray Departure");

		} else if (event instanceof FinishCounterEvent) {

			System.out.println("Finish Counter");

			rehandler.startRecognitionEvent(cehandler.getCountedObjects());

		} else if (event instanceof StartRecognitionEvent) {

			System.out.println("Start Recognition Event");

			try {
				orecognizer.computeDescriptors(cehandler.getCountedObjects(),
						tehandler.getPage());
			} catch (PageNotFoundException e1) {
				return;
			}

			List<Mat> foundImagesDescriptors = orecognizer
					.getFoundImagesDescriptors();

			if (foundImagesDescriptors.size() > 0) {
				for (Mat image : foundImagesDescriptors) {
					try {
						Product productMatch = orecognizer.recognize(
								tehandler.getPage(), image);

						if (productMatch != null
								&& productMatch.getName() != null) {
							System.out.println(productMatch.getName()
									+ '-'
									+ orecognizer.getFoundImageNames().get(
											foundImagesDescriptors
													.indexOf(image)));
						}
						rehandler.validRecognitionEvent(tehandler.getTray(),
								productMatch);

					} catch (NoMatchException e) {
						rehandler.falseRecognitionEvent(tehandler.getTray());
					} catch (PageNotFoundException e) {
						return;
					}
				}
			}
			rehandler.finishRecognitionEvent(tehandler.getTray());
		}
	}

}
