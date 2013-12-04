package com.plip.imageprocessing.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class RectangleProcessor {

	public double angle(Point pt1, Point pt2, Point pt0) {
		double dx1 = pt1.x - pt0.x;
		double dy1 = pt1.y - pt0.y;
		double dx2 = pt2.x - pt0.x;
		double dy2 = pt2.y - pt0.y;
		return (dx1 * dx2 + dy1 * dy2)
				/ Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
						+ 1e-10);
	}

	public Mat findRectanglesInImage(Mat inputFrame) {

		Mat result = inputFrame;
		Mat blurred = new Mat();
		// apply median blur to input image
		Imgproc.medianBlur(inputFrame, blurred, 9);

		Mat gray0 = new Mat(blurred.size(), blurred.type());
		Imgproc.cvtColor(gray0, gray0, Imgproc.COLOR_RGB2GRAY);
		Mat gray = new Mat();

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> squares = new ArrayList<MatOfPoint>();

		// find squares in every color plane of the image
		for (int c = 0; c < 3; c++) {

			int ch[] = { c, 0 };
			MatOfInt fromto = new MatOfInt(ch);
			List<Mat> blurredlist = new ArrayList<Mat>();
			List<Mat> graylist = new ArrayList<Mat>();
			blurredlist.add(blurred);
			graylist.add(gray0);
			Core.mixChannels(blurredlist, graylist, fromto);
			// try several threshold levels
			int tlevel = 2;
			for (int l = 0; l < tlevel; l++) {
				// Use Canny instead of zero threshold level!
				// Canny helps to catch squares with gradient shading
				if (l == 0) {
					Imgproc.Canny(gray0, gray, 20, 30); //
					// Dilate helps to remove potential holes between edge
					// segments
					Imgproc.dilate(gray, gray, Mat.ones(new Size(3, 3), 0));
				} else {
					int thresh = (l + 1) * 255 / tlevel;
					Imgproc.threshold(gray0, gray, thresh, 255,
							Imgproc.THRESH_TOZERO);
				}
				// Find contours and store them in a list
				// Imgproc.findContours(gray, contours, new Mat(), 1, 2);
				Imgproc.findContours(gray, contours, new Mat(), 2, 2);

				MatOfPoint2f approx = new MatOfPoint2f();
				MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
				MatOfPoint mMOP = new MatOfPoint();

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
							double cosine = Math.abs(angle(list[j % 4],
									list[j - 2], list[j - 1]));
							maxcosine = Math.max(maxcosine, cosine);
						}

						if (maxcosine < 0.3) {
							MatOfPoint temp = new MatOfPoint();
							approx.convertTo(temp, CvType.CV_32S);
							squares.add(temp);
						}
					}

				}
			}
		}
		Core.polylines(result, squares, true, new Scalar(0, 255, 0), 1);
		return result;
	}

}
