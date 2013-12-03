package imageprocessing.tray;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class TrayProcessor {

	private static Mat possibleTray = null;
	public static int threshold = 2;
	public static int thr1 = 0;
	public static int thr2 = 102;
	public static int thr3 = 0;
	public static int thr4 = 90;
	public static int thr5 = 500;
	public static int thr6 = 500;
	public static Mat background = null;
	public static boolean hasRun = false;

	public TrayProcessor() {
		super();
		loadParams();
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
		thr1 = new Integer(props.getProperty("minHueThreshold", "0"));
		thr2 = new Integer(props.getProperty("minSatThreshold", "108"));
		thr3 = new Integer(props.getProperty("minValueThreshold", "0"));
		thr4 = new Integer(props.getProperty("maxHueThreshold", "90"));
		thr5 = new Integer(props.getProperty("maxSatThreshold", "500"));
		thr6 = new Integer(props.getProperty("maxValueThreshold", "500"));
	}

	public double angle(Point pt1, Point pt2, Point pt0) {
		double dx1 = pt1.x - pt0.x;
		double dy1 = pt1.y - pt0.y;
		double dx2 = pt2.x - pt0.x;
		double dy2 = pt2.y - pt0.y;
		return (dx1 * dx2 + dy1 * dy2)
				/ Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
						+ 1e-10);
	}

	public Mat findRectangleInImage(Mat inputFrame) {
		Mat mRGB = inputFrame.clone();
		Mat mHSV = new Mat();
		Mat gray = new Mat();
		Mat resultRGB = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> squares = new ArrayList<MatOfPoint>();
		MatOfPoint2f approx = new MatOfPoint2f();
		MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
		MatOfPoint mMOP = new MatOfPoint();

		Imgproc.cvtColor(mRGB, mHSV, Imgproc.COLOR_RGB2HSV);
		Core.inRange(mHSV, new Scalar((double) thr1, (double) thr2,
				(double) thr3), new Scalar((double) thr4, (double) thr5,
				(double) thr6), gray);

		Imgproc.Canny(gray, gray, 20, 30);
		Imgproc.dilate(gray, gray, Mat.ones(new Size(3, 3), 0));
		Imgproc.findContours(gray, contours, new Mat(), 2, 2);

		for (int i = 0; i < contours.size(); i++) {
			contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
			Imgproc.approxPolyDP(mMOP2f1, approx,
					Imgproc.arcLength(mMOP2f1, true) * 0.02, true);
			approx.convertTo(mMOP, CvType.CV_32S);

			if (approx.rows() == 4
					&& Math.abs(Imgproc.contourArea(approx)) > 1000
					&& Imgproc.isContourConvex(mMOP)) {

				double maxcosine = 0;
				Point[] list = approx.toArray();

				for (int j = 2; j < 5; j++) {
					double cosine = Math.abs(angle(list[j % 4], list[j - 2],
							list[j - 1]));
					maxcosine = Math.max(maxcosine, cosine);
				}

				if (maxcosine < 0.3) {
					MatOfPoint temp = new MatOfPoint();
					approx.convertTo(temp, CvType.CV_32S);
					squares.add(temp);
				}
			}

		}
		for (int idx = 0; idx < squares.size(); ++idx) {
			Mat contour = squares.get(idx);
			if (SquareValidator.validateSquare(contour)) {
				Mat mask = Mat.zeros(inputFrame.size(), inputFrame.type());
				Scalar s = new Scalar(255, 255, 255);
				Imgproc.drawContours(mask, squares, idx, s, -1);

				Mat canvas = new Mat(inputFrame.size(), inputFrame.type());
				Scalar s2 = new Scalar(0, 0, 0);
				canvas.setTo(s2);
				inputFrame.copyTo(canvas, mask);

				Scalar s1 = new Scalar(255);
				Imgproc.drawContours(canvas, squares, idx, s1, 1);

				possibleTray = canvas;
				// possibleTray = inputFrame;
			}
		}
		Imgproc.cvtColor(gray, resultRGB, Imgproc.COLOR_GRAY2RGB);
		Core.polylines(resultRGB, squares, true, new Scalar(0, 255, 0), 1);
		// return possibleTray;
		return resultRGB;
	}

	public Mat getPossibleTray() {
		return possibleTray;
	}

	public void clearPossibleTray() {
		possibleTray = null;
	}
}
