package com.plip.systemmonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Properties;

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
import com.plip.imageprocessing.matchers.MinDistanceMatcher;
import com.plip.imageprocessing.matchers.exceptions.NoMatchException;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.ObjectRecognizer;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.imageprocessing.processors.Exceptions.NoImageException;
import com.plip.imageprocessing.trainers.PlipTrainer;
import com.plip.persistence.dao.impls.PlipRoleDaoImpl;
import com.plip.persistence.dao.impls.StatusDaoImpl;
import com.plip.persistence.dao.impls.TrayDaoImpl;
import com.plip.persistence.dao.impls.TrayStatusDaoImpl;
import com.plip.persistence.dao.interfaces.PlipRoleDao;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.StatusNotFoundException;
import com.plip.persistence.managers.LocalPageManager;
import com.plip.persistence.managers.PageManager;
import com.plip.persistence.managers.exceptions.NoPageRecievedException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.Tray;
import com.plip.persistence.model.TrayStatus;
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
	
	private static int imageResolutionWidth = 800;
	private static int imageResolutionHeight = 600;
	private static int captureResolutionWidth = 800;
	private static int captureResolutionHeight = 600;
    
	public final static int STATUS_TRAY_ARRAIVAL = 1;
	public final static int STATUS_TRAY_QUANTITY_EXCEEDED = 2;
	public final static int STATUS_TRAY_COUNTED = 3;
	public final static int STATUS_VALID_QUANTITY = 4;
	public final static int STATUS_INVALID_QUANTITY = 5;
	public final static int STATUS_PRODUCT_RECOGNIZED = 6;
	public final static int STATUS_PRODUCT_NOT_RECOGNIZED = 7;
	public final static int STATUS_VALID_TRAY = 8;
	public final static int STATUS_INVALID_TRAY = 9;
	
	
	public static int cameraInput;

	public MainSystemMonitor() {
		super();
		 loadParams();
		 mmf = new MainMenuFrame(this);
		 mmf.setVisible(true);
	}

	public static void main(String arg[]) {
		System.loadLibrary("opencv_java246");
		MainSystemMonitor msm = new MainSystemMonitor();
		msm.initializeCapture();

//		 PlipTrainer trainer = new PlipTrainer();
//		 trainer.processProductImages();
//		 PlipRoleDao roleDao = new PlipRoleDaoImpl();
	}

	/*
	 * CvCapture *cap; int n = 0; while(1) { cap = cvCreateCameraCapture(n++);
	 * if (cap == NULL) break; cvReleaseCapture(&cap); }
	 * 
	 * cvReleaseCapture(&cap); return n-1;
	 */

	public void initializeCapture() {

		vcapture = new VideoCapture(cameraInput);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, captureResolutionWidth);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, captureResolutionHeight);
		
		setCamara();
		
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
	
	public void setCamara(){
		vcapture.set(Highgui.CV_CAP_PROP_FOCUS, 28);
		vcapture.set(Highgui.CV_CAP_PROP_BACKLIGHT,1000);
		vcapture.set(Highgui.CV_CAP_PROP_AUTOGRAB, 1024);
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
			Tray tray = new Tray();
			PageManager pageManager = new LocalPageManager();
			TrayDaoImpl trayDao = new TrayDaoImpl();
			try {
				Page page = pageManager.getLastPage();
				tray.setPage(page);
				tray.setCode(page.getOrder().getCode());
				trayDao.addTray(tray);
			} catch (NoPageRecievedException | NullModelAttributesException e) {
				System.out.println("Tray could not be identified");
				return;
			}
			
			tehandler.setTray(tray);
			
			// Pensar como vamos a saber la page correspondiente a la tray en
			// ese momento!

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, imageResolutionWidth);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, imageResolutionHeight);

			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			Mat screenshot = new Mat();
			vcapture.read(screenshot);

			Highgui.imwrite("tray.jpg", screenshot);

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, captureResolutionWidth);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, captureResolutionHeight);
			
			int pageQuantity = tehandler.getPage().getProductQuantity();
			
			if( pageQuantity < 10){
		
				ArrayList<Mat> images = new ArrayList<Mat>();

				try {
					images = ocounter.count(screenshot, tehandler.getPage().getProductQuantity());
				} catch (NoImageException e) {
					e.printStackTrace();
				}
				cehandler.addCountedObjects(images);
			}else{
				tehandler.unSupportedTrayEvent();
				System.out.println("The quantity of products exceeds the system capacity");
				return;
			}
			
		} else if (event instanceof TrayDepartureEvent) {

			System.out.println("Tray Departure");

		} else if (event instanceof FinishCounterEvent) {

			System.out.println("Finish Counter");
			/*
			 * Reports to TrayStatus to determinate the quantity of found
			 * products
			 */
			TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
			TrayStatus countedTrayStatus = new TrayStatus();
			TrayStatus quantityResultTrayStatus = new TrayStatus();
			StatusDaoImpl statusDao = new StatusDaoImpl();
			try {
				Status countedStatus = statusDao.getStatus(STATUS_TRAY_COUNTED);
				Status quantityStatus;
				if(tehandler.getPage().getProductQuantity() == cehandler.getCountedObjects().size()){
				 quantityStatus = statusDao.getStatus(STATUS_VALID_QUANTITY);	
				 System.out.println("STATUS_VALID_QUANTITY");
				}else{
				 quantityStatus = statusDao.getStatus(STATUS_INVALID_QUANTITY);
				 System.out.println("STATUS_INVALID_QUANTITY");
				}
				countedTrayStatus.setDate(new Date());
				countedTrayStatus.setQuantity(cehandler.getCountedObjects().size());
				countedTrayStatus.setStatus(countedStatus);
				countedTrayStatus.setTray(tehandler.getTray());
				trayStatusDao.addTrayStatus(countedTrayStatus);
				quantityResultTrayStatus.setDate(new Date());
				quantityResultTrayStatus.setQuantity(tehandler.getPage().getProductQuantity() - cehandler.getCountedObjects().size());
				quantityResultTrayStatus.setStatus(quantityStatus);
				quantityResultTrayStatus.setTray(tehandler.getTray());
				trayStatusDao.addTrayStatus(quantityResultTrayStatus);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
			
			
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
	
	public void loadParams() {
		Properties props = new Properties();
		InputStream is = null;
		try {
			File f = new File("./res/config.properties");
			is = new FileInputStream(f);
		} catch (Exception e) {
			is = null;
		}

		try {
			if (is == null) {
				is = getClass().getResourceAsStream("./res/config.properties");
			}

			props.load(is);
		} catch (Exception e) {
		}
		imageResolutionWidth = new Integer(props.getProperty("imageResolutionWidth"));
		imageResolutionHeight = new Integer(props.getProperty("imageResolutionHeight"));
		captureResolutionWidth = new Integer(props.getProperty("captureResolutionWidth"));
		captureResolutionHeight = new Integer(props.getProperty("captureResolutionHeight"));
		
		
		cameraInput = new Integer(props.getProperty("cameraInput"));
		
		ObjectCounter.minAreaThreshold = new Integer(props.getProperty("minAreaThreshold"));
		ObjectCounter.maxAreaThreshold = new Integer(props.getProperty("maxAreaThreshold"));
		
		MinDistanceMatcher.minDistanceThreshold = new Double(props.getProperty("minMatchingDistance"));
		
		TrayProcessor.thr1 = new Double(props.getProperty("minHueThreshold"));
		TrayProcessor.thr2 = new Double(props.getProperty("minSatThreshold"));
		TrayProcessor.thr3 = new Double(props.getProperty("minValueThreshold"));
		TrayProcessor.thr4 = new Double(props.getProperty("maxHueThreshold"));
		TrayProcessor.thr5 = new Double(props.getProperty("maxSatThreshold"));
		TrayProcessor.thr6 = new Double(props.getProperty("maxValueThreshold"));
		

		}
}
