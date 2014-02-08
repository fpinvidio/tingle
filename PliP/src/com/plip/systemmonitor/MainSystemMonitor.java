package com.plip.systemmonitor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
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
import com.plip.eventhandlers.listeners.CounterEventListener;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.eventhandlers.listeners.RecognizerEventListener;
import com.plip.eventhandlers.listeners.TrayEventListener;
import com.plip.imageprocessing.matchers.exceptions.NoMatchException;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.ObjectRecognizer;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.imageprocessing.processors.Exceptions.NoImageException;
import com.plip.persistence.dao.impls.PlipRoleDaoImpl;
import com.plip.persistence.dao.impls.TrayDaoImpl;
import com.plip.persistence.dao.interfaces.PlipRoleDao;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.managers.LocalPageManager;
import com.plip.persistence.managers.PageManager;
import com.plip.persistence.managers.exceptions.NoPageRecievedException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;
import com.plip.persistence.model.Tray;
import com.plip.system.validators.TrayValidator;
import com.plip.systemconfig.SystemUtils;
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

	public void initialize() {

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
		
		celistener = new CounterEventListener();
		relistener = new RecognizerEventListener();
		telistener = new TrayEventListener();
		
		/*event listeners*/
		tehandler.addEventListener(mmf);
		tehandler.addEventListener(this);
		//tehandler.addEventListener(telistener);
		
		cehandler.addEventListener(this);
		//cehandler.addEventListener(celistener); //Sends request to Administration Panel
		
		rehandler.addEventListener(this);	
		//rehandler.addEventListener(relistener);
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
			Rect trayBounds = null;
			if(this.trayBounds != null){
				trayBounds = this.trayBounds;
			
				double widthScale = ((double) imageResolutionWidth)/captureResolutionWidth;
			
				double heightScale =((double)  imageResolutionHeight)/captureResolutionHeight;
			
				trayBounds.height =(int) Math.floor(trayBounds.height*heightScale);
				trayBounds.y = (int) Math.floor(trayBounds.y*heightScale);
				trayBounds.width = (int) Math.floor(trayBounds.width*widthScale);
				trayBounds.x = (int) Math.floor(trayBounds.x*widthScale);
			}
			tehandler.setTray(tray);
			tehandler.saveTrayArraivalStatus();
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, imageResolutionWidth);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, imageResolutionHeight);

			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			Mat screenshot = new Mat();
			vcapture.read(screenshot);
			
			if(trayBounds != null){
			
			screenshot = screenshot.submat(trayBounds);
			}
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
			cehandler.saveFinishedCounterStatus(tehandler.getTray());
			rehandler.startRecognitionEvent(cehandler.getCountedObjects());
			
		} else if (event instanceof StartRecognitionEvent) {

			System.out.println("Start Recognition Event");
			
			orecognizer.computeDescriptors(cehandler.getCountedObjects(),
					tehandler.getPage());
			
			List<Mat> foundImagesDescriptors = orecognizer
					.getFoundImagesDescriptors();
			
			if(foundImagesDescriptors.size() > 0){
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
					rehandler.saveProductRecognizedStatus(tehandler.getTray(), productMatch);
					rehandler.validRecognitionEvent();
					System.out.println("True Matcher Event");
				} catch (NoMatchException e) {
					rehandler.saveProductNotRecognizedStatus(tehandler.getTray());
					rehandler.falseRecognitionEvent();
					System.out.println("False Matcher Event");
				} 
			}
			}
				rehandler.finishRecognitionEvent();
		} else if (event instanceof FinishRecognitionEvent) {
			System.out.println("Finish Recognition Event");
			TrayValidator trayValidator = new TrayValidator();
			trayValidator.validateTray(tehandler.getTray());
		}
	}

	
}
