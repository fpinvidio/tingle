package com.plip.imageprocessing.processors;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import com.plip.persistence.dao.impls.StatusDaoImpl;
import com.plip.persistence.dao.impls.TrayStatusDaoImpl;
import com.plip.persistence.exceptions.StatusNotFoundException;
import com.plip.persistence.model.Status;
import com.plip.persistence.model.TrayStatus;

public class ObjectCounter {

	private int quantity;
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private Mat image = new Mat();
	private static int minAreaThreshold = 0;
	private static int maxAreaThreshold = 1000000;
	
	public ObjectCounter(){
		loadParams();
	}

	public ArrayList<Mat> count(Mat image, int pageQuantity) throws NoImageException {
		if (image != null) {
			this.image=image;
			ArrayList<Mat> resultImages = new ArrayList<Mat>();

			/* Pre process image to enhance contours */
			Mat imageWithEdges = edgeDetector(this.image);

			/* Removes detected tray contour */
			Mat noTray = removeTray(imageWithEdges);

			/* Find contours and validates quadrilateral forms */
			Mat detectedObjects = findContours(noTray);
			
			if(quantity != pageQuantity){
				/* Applies distanceTransform to separate products */
				imageDistanceTransformer(detectedObjects);
			}
			
			System.out.println("Detected Products:" + quantity);
			System.out.println("Page quantity:" + pageQuantity);
			
			/*
			 * Reports to TrayStatus to determinate the quantity of found
			 * products
			 */
			TrayStatusDaoImpl trayStatusDao = new TrayStatusDaoImpl();
			TrayStatus trayStatus = new TrayStatus();
			StatusDaoImpl statusDao = new StatusDaoImpl();
			Status status = new Status();
			try {
				status = statusDao.getStatus(1);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
			}
			trayStatus.setDate(new Date());
			trayStatus.setQuantity(quantity);
			trayStatus.setStatus(status);
			trayStatusDao.addTrayStatus(trayStatus);
			for (int i = 0; i < this.contours.size(); i++){
				Mat productImage = cropContour(this.image, contours.get(i), i, false);
//				Highgui.imwrite("bound"+i+".jpg",productImage);
				resultImages.add(productImage);
			}

			return resultImages;
		} else {
			throw new NoImageException();
		}
	}

	/**
	 * This method improves the original image for contours detection. It is in
	 * charge of smoothing, filtering and enhancing edges.
	 * 
	 * @param Mat
	 *            image - Detected Tray Screenshot
	 * @return Mat - Pre processed filtered image with enhanced edges.
	 * @throws NoImageException
	 *             - if Screenshot is null
	 */
	public Mat edgeDetector(Mat image) throws NoImageException {
		if (image != null) {
			Mat medianBlur = new Mat();
			Mat bilateral = new Mat();
			Mat cannyBilateral = new Mat();
			Mat binary = new Mat();

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

	/**
	 * This method removes the detected tray contour from the pre processed
	 * image.
	 * 
	 * @param image
	 *            - Pre processed image.
	 * @return Mat image - Pre processed image without tray.
	 */
	public Mat removeTray(Mat image) {
		if (image != null) {

			Highgui.imwrite("noCrop.jpg", image);

			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			MatOfPoint trayFloorContour = new MatOfPoint();
			boolean initialized = false;
			MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
			Scalar s1 = new Scalar(255, 0, 0);

			Imgproc.findContours(image, contours, new Mat(),
					Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);

			for (int i = 0; i < contours.size(); i++) {

				contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
				RotatedRect rotated = Imgproc.minAreaRect(mMOP2f1);
				Point[] vertices = new Point[4];
				rotated.points(vertices);
				MatOfPoint2f verticesMat = new MatOfPoint2f();
				verticesMat.fromArray(vertices);

				if (Imgproc.contourArea(verticesMat) > maxAreaThreshold) {

					Imgproc.drawContours(image, contours, i, new Scalar(0), 2);

					if (!initialized) {
						trayFloorContour = contours.get(i);
						initialized = true;
					} else {
						Rect boundRect = Imgproc.boundingRect(contours.get(i));
						Rect floor = Imgproc.boundingRect(trayFloorContour);
						if (boundRect.x > floor.x && boundRect.y <= floor.y) {
							trayFloorContour = contours.get(i);
						}
					}
				} else {
					Imgproc.drawContours(image, contours, i, s1, 2);
				}
			}
			if (initialized) {
				image = cropContour(image, trayFloorContour, 1, true);
				if(this.image!=null){
				this.image = cropContour(this.image, trayFloorContour, 1,true);
				}
			}
			Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY);
			return image;
		}
		return new Mat();
	}

	/**
	 * This method search's for contours and validates their quadrilateral
	 * shape.
	 * 
	 * @param filteredImage
	 *            - Image without tra.y
	 * @return Mat - Returns detected quadrilateral products.
	 * @throws NoImageException
	 *             - If filteredImage is null.
	 */

	public Mat findContours(Mat filteredImage) throws NoImageException {
		if (filteredImage != null) {
			quantity = 0;
			this.contours.clear();
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
			Scalar s1 = new Scalar(255, 0, 0);

			Highgui.imwrite("noTray.jpg", filteredImage);

			Imgproc.Canny(filteredImage, filteredImage, 35, 85);
			// Highgui.imwrite("CannybilateralFilter.jpg", cannyBilateral);

			Imgproc.dilate(filteredImage, filteredImage, new Mat(), new Point(
					-1, -1), 3);

			Highgui.imwrite("contours.jpg", filteredImage);

			/*
			 * Dilate image to complete contours and highlight lines for a + *
			 * better performance.
			 */
			Imgproc.dilate(filteredImage, filteredImage, new Mat(), new Point(
					-1, -1), 2);

			Highgui.imwrite("contourse.jpg", filteredImage);

			contours = new ArrayList<MatOfPoint>();

			Imgproc.findContours(filteredImage, contours, new Mat(),
					Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

			for (int i = 0; i < contours.size(); i++) {

				contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);

				RotatedRect rotated = Imgproc.minAreaRect(mMOP2f1);
				Point[] vertices = new Point[4];
				rotated.points(vertices);
				MatOfPoint2f verticesMat = new MatOfPoint2f();
				verticesMat.fromArray(vertices);

				/*
				 * Draw contours between an estimated area, to avoid to big or
				 * to small objects
				 */
				if (Imgproc.contourArea(verticesMat) > minAreaThreshold
						&& Imgproc.contourArea(verticesMat) < maxAreaThreshold) {

					/* System.out.println(Imgproc.contourArea(verticesMat)); */

					Imgproc.drawContours(filteredImage, contours, i, s1, -1);

					this.contours.add(contours.get(i));

					Highgui.imwrite("contour" + i + ".jpg", filteredImage);

					quantity++;
				}
			}
			Highgui.imwrite("contoursfilled.jpg", filteredImage);
			return filteredImage;
		} else {
			throw new NoImageException();
		}
	}

	/**
	 * This method applies a distance transform function which separates the
	 * detected objects.
	 * 
	 * @param detectedObjects
	 *            - The detected objects.
	 * @throws NoImageException
	 *             - If the detected object image is null.
	 */
	public void imageDistanceTransformer(Mat detectedObjects)
			throws NoImageException {

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
			Imgproc.threshold(normalize, thres, 64, 255, Imgproc.THRESH_BINARY);
			// Highgui.imwrite("thres.jpg", thres);

			Imgproc.dilate(thres, thres, new Mat(), new Point(-1, -1), 10);

			Highgui.imwrite("thresd.jpg", thres);

			thres.convertTo(thres, CvType.CV_8UC1);

			/* Called to recount the objects */
			findContoursAfterDistance(thres);

		} else {
			throw new NoImageException();
		}
	}

	/**
	 * This method recounts objects after distance transform.
	 * 
	 * @param distanceTransformedImage
	 *            - Image with separate objects.
	 * @return detectedObjectsAfterDistance - detected distance transformed
	 *         objects.
	 */
	public Mat findContoursAfterDistance(Mat distanceTransformedImage) {
		quantity = 0;
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Scalar s1 = new Scalar(255, 0, 0);

		/* Finds contours in the image and saves contours to contours object */
		Imgproc.findContours(distanceTransformedImage, contours, new Mat(),
				Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

		quantity = contours.size();

		this.contours = contours;

		// Highgui.imwrite("contoursT.jpg", filteredImage);

		Imgproc.drawContours(distanceTransformedImage, contours, -1, s1, -1);

		Highgui.imwrite("contoursfilledT.jpg", distanceTransformedImage);

		return distanceTransformedImage;
	}

	/**
	 * This method crops objects from the detected Objects Image.
	 * 
	 * @param detectedObjects
	 *            - Detected objects image.
	 * @param contour
	 *            - Detected object contour
	 * @param i
	 *            - Detected object index
	 * @return subImage - Object image.
	 */
	public Mat cropContour(Mat detectedObjects, MatOfPoint contour, int i, boolean tray) {
		if (detectedObjects != null && contour != null) {

			Rect boundRect = Imgproc.boundingRect(contour);
			if(!tray){
				boundRect.x +=0;
				boundRect.y -=15;
				boundRect.height += 30;
				boundRect.width += 30;
			}
			if (boundRect.x < 0) {
				boundRect.x = 0;
			}
			if (boundRect.y < 0) {
				boundRect.y = 0;
			}
			if (boundRect.width + boundRect.x > detectedObjects.cols()) {
				boundRect.width = Math
						.abs(detectedObjects.cols() - boundRect.x);
			}
			if (boundRect.height + boundRect.y > detectedObjects.rows()) {
				boundRect.height = Math.abs(detectedObjects.rows()
						- boundRect.y);
			}

			Mat subImage = detectedObjects.submat(boundRect);
			
			String filename = getClass().getResource("/FoundObjects").getPath()
					+ "/bound" + i + ".jpg";
			Highgui.imwrite(filename, subImage);
			return subImage;
		} else {
			return new Mat();
		}
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
		minAreaThreshold = new Integer(props.getProperty("minAreaThreshold"));
		maxAreaThreshold = new Integer(props.getProperty("maxAreaThreshold"));
		}
}