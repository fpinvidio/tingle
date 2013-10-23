package systemmonitor;

import imageprocessing.TrayProcessor;
import imageprocessing.ObjectProcessor;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import uinterfaces.MainMenuFrame;
import eventhandling.TrayEventHandler;

public class MainSystemMonitor{
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
		vcapture = new VideoCapture(0);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 200);
		vcapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 250);
		tprocessor = new TrayProcessor();
		tehandler = new TrayEventHandler();
		tehandler.addEventListener(mmf);
		tehandler.addEventListener(new ObjectProcessor());
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
}
