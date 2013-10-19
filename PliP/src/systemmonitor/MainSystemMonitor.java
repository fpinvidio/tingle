package systemmonitor;

import imageprocessing.ImageProcessor;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import uinterfaces.MainMenuFrame;
import eventhandling.TrayProcessor;

public class MainSystemMonitor{
	private VideoCapture vcapture;
	private ImageProcessor iprocessor;
	private TrayProcessor tprocessor;
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
		iprocessor = new ImageProcessor();
		tprocessor = new TrayProcessor();
		tprocessor.addEventListener(mmf);
	}

	public Mat captureVideoFrame() {
		Mat capturedFrame = new Mat();
		if (vcapture.isOpened()) {
			vcapture.read(capturedFrame);
			if (!capturedFrame.empty()) {
				capturedFrame = iprocessor.findRectangleInImage(capturedFrame);
				tprocessor.addTray(iprocessor.getPossibleTray());
				iprocessor.clearPossibleTray();
			} else {
				capturedFrame = null;
			}
		}
		return capturedFrame;
	}
}
