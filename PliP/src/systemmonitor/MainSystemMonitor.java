package systemmonitor;

import imageprocessing.tray.ObjectProcessor;
import imageprocessing.tray.TrayProcessor;

import java.util.EventObject;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import uinterfaces.MainMenuFrame;
import eventhandling.TrayEventHandler;
import eventhandling.events.FinishedCountEvent;
import eventhandling.events.StartedCountEvent;
import eventhandling.events.TrayArrivalEvent;
import eventhandling.events.TrayDepartureEvent;
import eventhandling.listeners.GenericEventListener;

// Queda pendiente organizar handlers para todos los eventos 

public class MainSystemMonitor implements GenericEventListener {
	private VideoCapture vcapture;
	private TrayProcessor tprocessor;
	private TrayEventHandler tehandler;
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
	}

	public void initializeCapture() {
		vcapture = new VideoCapture(1);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 200);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 250);
		tprocessor = new TrayProcessor();
		tehandler = new TrayEventHandler();
		tehandler.addEventListener(mmf);
		tehandler.addEventListener(new ObjectProcessor());
		tehandler.addEventListener(this);
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
		} else if (event instanceof TrayDepartureEvent) {
			System.out.println("Departure");
		} else if (event instanceof StartedCountEvent) {
			System.out.println("StartedCount");
		} else if (event instanceof FinishedCountEvent) {
			System.out.println("FinishedCount");
		}
	}
}
