package imageprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {

	private static Mat possibleTray = null;
	public static int threshold = 2;
	// public static int thr1 = 20;
	// public static int thr2 = 30;
	public static int thr1 = 0;
	public static int thr2 = 154;
	public static int thr3 = 90;
	public static int thr4 = 500;
	public static int thr5 = 500;
	public static int thr6 = 500;
	public static Mat background = null;
	public static boolean hasRun = false;

	public ImageProcessor() {
		super();
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
				possibleTray = contour;
			}
		}
		Imgproc.cvtColor(gray, resultRGB, Imgproc.COLOR_GRAY2RGB);
		Core.polylines(resultRGB, squares, true, new Scalar(0, 255, 0), 1);

		return resultRGB;
	}

	public Mat getPossibleTray() {
		return possibleTray;
	}

	public void clearPossibleTray() {
		possibleTray = null;
	}
}
