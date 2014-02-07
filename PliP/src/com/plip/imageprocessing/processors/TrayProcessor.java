package com.plip.imageprocessing.processors;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.plip.imageprocessing.validators.SquareValidator;

public class TrayProcessor {

	private static Mat possibleTray = null;
	private static Rect trayBound = null;
	

	public static int threshold = 2;
	public static double thr1;
	public static double thr2;
	public static double thr3;
	public static double thr4;
	public static double thr5;
	public static double thr6;
	public static Mat background = null;
	public static boolean hasRun = false;

	public TrayProcessor() {
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
		Core.inRange(mHSV, new Scalar( thr1,  thr2,
				thr3), new Scalar( thr4,  thr5,
				thr6), gray);

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
				Rect boundRect = Imgproc.boundingRect(squares.get(idx));
				trayBound = boundRect;
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
	public static Rect getTrayBound() {
		return trayBound;
	}
}
