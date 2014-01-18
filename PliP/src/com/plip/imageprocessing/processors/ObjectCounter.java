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
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.plip.imageprocessing.processors.Exceptions.NoImageException;

public class ObjectCounter {

	private int quantity;
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

	public ArrayList<Mat> count(Mat image) throws NoImageException {
		if (image != null) {
			ArrayList<Mat> resultImages = new ArrayList<Mat>();
			Mat imageWithEdges = edgeDetector(image);
			Mat detectedObjects = findContours(imageWithEdges);
			System.out.println("Quantity:" + quantity);
			System.out.println("Contours size:" + this.contours.size());
			for (int i = 0; i < this.contours.size(); i++) {
				Mat productImage = cropContour(image, contours.get(i), i);
				resultImages.add(productImage);
			}

			/*
			 * Falta Eliminar el contorno de la cubeta y aplicar distance
			 * transform si los productos estan pegados
			 * imageDistanceTransformer(detectedObjects);
			 * System.out.println(quantity);
			 */
			return resultImages;
		} else {
			throw new NoImageException();
		}
	}

	public Mat findContours(Mat filteredImage) throws NoImageException {
		if (filteredImage != null) {
			quantity = 0;
			this.contours.clear();
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			MatOfPoint2f approx = new MatOfPoint2f();
			MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
			MatOfPoint mMOP = new MatOfPoint();
			Scalar s1 = new Scalar(255, 0, 0);
			int count = 0;

			/* Finds contours in the image and saves contours to contours object */
			Imgproc.findContours(filteredImage, contours, new Mat(),
					Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
			Imgproc.drawContours(filteredImage, contours, -1, s1, -1);

			// Highgui.imwrite("contours.jpg", filteredImage);
			Imgproc.erode(filteredImage, filteredImage, new Mat(), new Point(
					-1, -1), 5);
			// Highgui.imwrite("contourse.jpg", filteredImage);
			contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(filteredImage, contours, new Mat(),
					Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
			for (int i = 0; i < contours.size(); i++) {
				contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
				/*
				 * Imgproc.approxPolyDP(mMOP2f1, approx,
				 * Imgproc.arcLength(mMOP2f1, true) * 0.02, true);
				 * approx.convertTo(mMOP, CvType.CV_32S); Point[] pointArray =
				 * approx.toArray();
				 */
				RotatedRect rotated = Imgproc.minAreaRect(mMOP2f1);
				Point[] vertices = new Point[4];
				rotated.points(vertices);
				MatOfPoint2f verticesMat = new MatOfPoint2f();
				verticesMat.fromArray(vertices);

				/*
				 * Draw contours between an estimated area, to avoid to big or
				 * to small objects
				 */
				if (Imgproc.contourArea(verticesMat) > 40000
						&& Imgproc.contourArea(verticesMat) < 6000000) {

					// System.out.println(Imgproc.contourArea(verticesMat));
					Imgproc.drawContours(filteredImage, contours, i, s1, -1);
					this.contours.add(contours.get(i));
					// Highgui.imwrite("contour"+i+".jpg", filteredImage);
					quantity++;
				}
			}
			Highgui.imwrite("contoursfilled.jpg", filteredImage);
			return filteredImage;
		} else {
			throw new NoImageException();
		}
	}

	public Mat findContoursAfterDistance(Mat filteredImage) {
		quantity = 0;
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfPoint2f approx = new MatOfPoint2f();
		MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
		MatOfPoint mMOP = new MatOfPoint();
		Scalar s1 = new Scalar(255, 0, 0);
		int count = 0;
		/* Finds contours in the image and saves contours to contours object */
		Imgproc.findContours(filteredImage, contours, new Mat(),
				Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
		quantity = contours.size();
		// Highgui.imwrite("contoursT.jpg", filteredImage);
		// Imgproc.drawContours(filteredImage, contours, -1, s1, -1);
		// Highgui.imwrite("contoursfilledT.jpg", filteredImage);
		return filteredImage;
	}

	public Mat edgeDetector(Mat image) throws NoImageException {
		if (image != null) {
			Mat medianBlur = new Mat();
			Mat bilateral = new Mat();
			Mat cannyBilateral = new Mat();
			Mat binary = new Mat();

			// Filter Create binary image from source image
			/* Transform Image from BGR To RGB */
			// Imgproc.cvtColor(image, binary, Imgproc.COLOR_BGR2RGB);
			binary = image;
			/* Apply median blur to smooth image */
			Imgproc.medianBlur(binary, medianBlur, 7);
			/* Apply bilateral filter to smooth image but maintaining edges */
			Imgproc.bilateralFilter(medianBlur, bilateral, 5, 230, 5, 1);
			// Highgui.imwrite("bilateral.jpg", bilateral);
			/* Apply edge detector detect medicine edges and contours */
			Imgproc.Canny(bilateral, cannyBilateral, 35, 85);
			// Highgui.imwrite("CannybilateralFilter.jpg", cannyBilateral);
			/*
			 * Dilate image to complete contours and highlight lines for a
			 * better performance of findCountours
			 */
			Imgproc.dilate(cannyBilateral, cannyBilateral, new Mat(),
					new Point(-1, -1), 3);
			// Highgui.imwrite("Median.jpg", medianBlur);
			// Highgui.imwrite("CannybilateralFilterDilated.jpg",
			// cannyBilateral);

			return cannyBilateral;
		} else {
			throw new NoImageException();
		}
	}

	public void imageDistanceTransformer(Mat detectedObjects)
			throws NoImageException {
		/* Distance transform to separate objects */
		if (detectedObjects != null) {
			Mat thres = new Mat();
			Mat normalize = new Mat();
			Mat transform = new Mat();

			Imgproc.distanceTransform(detectedObjects, transform,
					Imgproc.CV_DIST_L1, 0);
			// Highgui.imwrite("transform.jpg", transform);

			Core.normalize(transform, normalize, 0, 255, Core.NORM_MINMAX);
			// Highgui.imwrite("normalize.jpg", normalize);

			/* Threshold to recount the objects */
			Imgproc.threshold(normalize, thres, 64, 128, Imgproc.THRESH_BINARY);
			// Highgui.imwrite("thres.jpg", thres);
			Imgproc.dilate(thres, thres, new Mat(), new Point(-1, -1), 10);
			// Highgui.imwrite("thresd.jpg", thres);

			thres.convertTo(thres, CvType.CV_8UC1);

			findContoursAfterDistance(thres);
		} else {
			throw new NoImageException();
		}
	}

	public Mat cropContour(Mat image, MatOfPoint contour, int i) {
		if (image != null && contour != null) {
			Rect boundRect = Imgproc.boundingRect(contour);
			boundRect.height += 30;
			boundRect.width += 30;
			boundRect.x -= 15;
			boundRect.y -= 15;
			Mat subImage = image.submat(boundRect);
			String filename = getClass().getResource("/FoundObjects").getPath()
					+ "/bound" + i + ".jpg";
			Highgui.imwrite(filename, subImage);

			return subImage;
		} else {
			return new Mat();
		}
	}
}