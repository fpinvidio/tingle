package com.plip.systemmonitor;

import java.util.ArrayList;
import java.util.EventObject;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.plip.eventhandlers.events.FalseMatcherEvent;
import com.plip.eventhandlers.events.FinishCounterEvent;
import com.plip.eventhandlers.events.FinishRecognitionEvent;
import com.plip.eventhandlers.events.StartRecognitionEvent;
import com.plip.eventhandlers.events.TrayArrivalEvent;
import com.plip.eventhandlers.events.TrayDepartureEvent;
import com.plip.eventhandlers.events.TrueMatcherEvent;
import com.plip.eventhandlers.handlers.CounterEventHandler;
import com.plip.eventhandlers.handlers.RecognizerEventHandler;
import com.plip.eventhandlers.handlers.TrayEventHandler;
import com.plip.eventhandlers.listeners.GenericEventListener;
import com.plip.imageprocessing.processors.ObjectCounter;
import com.plip.imageprocessing.processors.ObjectRecognizer;
import com.plip.imageprocessing.processors.TrayProcessor;
import com.plip.imageprocessing.processors.Exceptions.NoImageException;
import com.plip.imageprocessing.trainers.PlipTrainer;
import com.plip.persistence.dao.impls.PageDaoImpl;
import com.plip.persistence.exceptions.PageNotFoundException;
import com.plip.persistence.model.Page;
import com.plip.uinterfaces.MainMenuFrame;

// Queda pendiente organizar handlers para todos los eventos 

public class TestMainSystemMonitor implements GenericEventListener {

	private VideoCapture vcapture;

	private TrayProcessor tprocessor;
	private ObjectCounter ocounter;
	private ObjectRecognizer orecognizer;

	private TrayEventHandler tehandler;
	private CounterEventHandler cehandler;
	private RecognizerEventHandler rehandler;

	private static MainMenuFrame mmf;

	public TestMainSystemMonitor() {
		super();
		// mmf = new MainMenuFrame();
		// mmf.setVisible(true);
	}
	
	public VideoCapture getVcapture() {
		return vcapture;
	}

	public void setVcapture(VideoCapture vcapture) {
		this.vcapture = vcapture;
	}

	public TrayProcessor getTprocessor() {
		return tprocessor;
	}

	public void setTprocessor(TrayProcessor tprocessor) {
		this.tprocessor = tprocessor;
	}

	public ObjectCounter getOcounter() {
		return ocounter;
	}

	public void setOcounter(ObjectCounter ocounter) {
		this.ocounter = ocounter;
	}

	public ObjectRecognizer getOrecognizer() {
		return orecognizer;
	}

	public void setOrecognizer(ObjectRecognizer orecognizer) {
		this.orecognizer = orecognizer;
	}

	public TrayEventHandler getTehandler() {
		return tehandler;
	}

	public void setTehandler(TrayEventHandler tehandler) {
		this.tehandler = tehandler;
	}

	public CounterEventHandler getCehandler() {
		return cehandler;
	}

	public void setCehandler(CounterEventHandler cehandler) {
		this.cehandler = cehandler;
	}

	public RecognizerEventHandler getRehandler() {
		return rehandler;
	}

	public void setRehandler(RecognizerEventHandler rehandler) {
		this.rehandler = rehandler;
	}
	
	public static MainMenuFrame getMmf() {
		return mmf;
	}

	public static void setMmf(MainMenuFrame mmf) {
		TestMainSystemMonitor.mmf = mmf;
	}

	public static void main(String arg[]) {
		System.loadLibrary("opencv_java246");

		 PlipTrainer trainer = new PlipTrainer();
		 trainer.processProductImages();
		 
		/*TestMainSystemMonitor tmsm = new TestMainSystemMonitor();
		tmsm.initialize();
		
		ObjectCounter ocounter = new ObjectCounter();	
		Mat image = Highgui.imread(TestMainSystemMonitor.class.getResource("/tray0.jpg").getPath());
		ArrayList<Mat> foundObjects = null;
		
		try {
			foundObjects = ocounter.count(image , );
			tmsm.getCehandler().addCountedObjects(foundObjects);
		} catch (NoImageException e) {
			e.printStackTrace();
		}
		
		PageDao pageDao = new PageDaoImpl();
		Page page = new Page();
		
		try {
		page = pageDao.getPage(Long.valueOf(2));
		} catch (PageNotFoundException e) {
		e.printStackTrace();
		}		
		tmsm.getRehandler().startRecognitionEvent(tmsm.getCehandler().getCountedObjects());
		TestObjectRecognizer orbBriskDetector = new TestObjectRecognizer();
		ArrayList<Product> recognizedObjects = orbBriskDetector.recognize(page, foundObjects, tmsm.getRehandler());
		
		System.out.println("Quantity Recognized:" + recognizedObjects.size());
		
		tmsm.getRehandler().finishRecognitionEvent();*/
		
	}
	
	
	public void initialize() {

		/*event handlers*/
		//tehandler = new TrayEventHandler();
		cehandler = new CounterEventHandler();
		rehandler = new RecognizerEventHandler();
		
		/*event listeners*/
		//tehandler.addEventListener(mmf);
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
				e1.printStackTrace();
			}
			//tehandler.setPage(page);

			// Pensar como vamos a saber la page correspondiente a la tray en
			// ese momento!

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1620);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 1080);

			try {
				Thread.sleep(400);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			Mat screenshot = new Mat();
			vcapture.read(screenshot);

			Highgui.imwrite("Tray.jpg", screenshot);

			vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
			vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);

			ArrayList<Mat> images = new ArrayList<Mat>();

		//	try {
				//images = ocounter.count(screenshot);
			//} catch (NoImageException e) {
			//	e.printStackTrace();
		//	}
			cehandler.addCountedObjects(images);
		} else if (event instanceof TrayDepartureEvent) {
			System.out.println("Tray Departure");
		} else if (event instanceof FinishCounterEvent) {
			System.out.println("Finish Counter");
		} else if (event instanceof StartRecognitionEvent) {
			System.out.println("Start Recognition Event");
		} else if (event instanceof TrueMatcherEvent){
			System.out.println("True Matcher Event");
		} else if (event instanceof FalseMatcherEvent){
			System.out.println("False Matcher Event");
		}else if (event instanceof FinishRecognitionEvent) {
			System.out.println("Finish Recognition Event");
		}
	}
}
