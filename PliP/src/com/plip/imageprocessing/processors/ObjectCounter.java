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

import com.plip.exceptions.imageprocessing.NoImageException;

public class ObjectCounter {

	private int quantity;
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private Mat image = new Mat();
	public static int minAreaThreshold = 0;
	public static int maxAreaThreshold = 1000000;
	public static int trayFloorX = 0;
	public static int trayFloorY = 0;
	public static int trayFloorHeight=1000;
	public static int trayFloorWidth=1700;
	
	public ObjectCounter(){
		super();
	}

	public ArrayList<Mat> count(Mat image, int pageQuantity) throws NoImageException {
		if (image != null) {
			this.image=image;
			ArrayList<Mat> resultImages = new ArrayList<Mat>();

			/* Pre process image to enhance contours */
			imagenesInforme(this.image);
			Mat imageWithEdges = edgeDetector(this.image);

			/* Removes detected tray contour */
			Mat noTray = removeTrayAlternative(imageWithEdges);

			/* Find contours and validates quadrilateral forms */
			Mat detectedObjects = findContours(noTray);
//			Highgui.imwrite("noTrayColor.jpg", this.image);
			
			if(quantity != pageQuantity){
				/* Applies distanceTransform to separate products */
				imageDistanceTransformer(detectedObjects);
			}
			
			System.out.println("Detected Products:" + quantity);
			System.out.println("Page quantity:" + pageQuantity);
			
			
			for (int i = 0; i < this.contours.size(); i++){
				Mat productImage = cropProduct(this.image, contours.get(i), i);

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
			//Highgui.imwrite("bilateral.jpg", bilateral);
			
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
//	public Mat removeTray(Mat image) {
//		if (image != null) {
//
//			Highgui.imwrite("noCrop.jpg", image);
//
//			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//			MatOfPoint trayFloorContour = new MatOfPoint();
//			boolean initialized = false;
//			MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
//			Scalar s1 = new Scalar(255, 0, 0);
//
//			Imgproc.findContours(image, contours, new Mat(),
//					Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
//
//			for (int i = 0; i < contours.size(); i++) {
//
//				contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
//				RotatedRect rotated = Imgproc.minAreaRect(mMOP2f1);
//				Point[] vertices = new Point[4];
//				rotated.points(vertices);
//				MatOfPoint2f verticesMat = new MatOfPoint2f();
//				verticesMat.fromArray(vertices);
//
//				if (Imgproc.contourArea(verticesMat) > maxAreaThreshold) {
//
//					Imgproc.drawContours(image, contours, i, new Scalar(0), 2);
//
//					if (!initialized) {
//						trayFloorContour = contours.get(i);
//						initialized = true;
//					} else {
//						Rect boundRect = Imgproc.boundingRect(contours.get(i));
//						Rect floor = Imgproc.boundingRect(trayFloorContour);
//						if (boundRect.x > floor.x && boundRect.y <= floor.y) {
//							trayFloorContour = contours.get(i);
//						}
//					}
//				} else {
//					Imgproc.drawContours(image, contours, i, s1, 2);
//				}
//			}
//			if (initialized) {
//				image = cropContour(image, trayFloorContour, 1, true);
//				if(this.image!=null){
//				this.image = cropContour(this.image, trayFloorContour, 1,true);
//				}
//			}
//			Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY);
//			return image;
//		}
//		return new Mat();
//	}
	
	public Mat removeTrayAlternative(Mat image){
		if (image != null) {

//			Highgui.imwrite("noCrop.jpg", image);
//
//			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//			MatOfPoint trayFloorContour = new MatOfPoint();
//			boolean initialized = false;
//			MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
//			Scalar s1 = new Scalar(255, 0, 0);
//			Mat originalImage = image.clone();
//			Imgproc.findContours(image, contours, new Mat(),
//					Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
//
//			for (int i = 0; i < contours.size(); i++) {
//
//				contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
//				RotatedRect rotated = Imgproc.minAreaRect(mMOP2f1);
//				Point[] vertices = new Point[4];
//				rotated.points(vertices);
//				MatOfPoint2f verticesMat = new MatOfPoint2f();
//				verticesMat.fromArray(vertices);
//
//				if (Imgproc.contourArea(verticesMat) > maxAreaThreshold) {
//
//					Imgproc.drawContours(originalImage, contours, i, new Scalar(0), 2);
//
//					if (!initialized) {
//						trayFloorContour = contours.get(i);
//						initialized = true;
//					} else {
//						Rect boundRect = Imgproc.boundingRect(contours.get(i));
//						Rect floor = Imgproc.boundingRect(trayFloorContour);
//						if (boundRect.x < floor.x && boundRect.y >= floor.y) {
//							trayFloorContour = contours.get(i);
//						}
//					}
//				} else {
//					Imgproc.drawContours(image, contours, i, s1, 2);
//				}
//			}
//			if (initialized) {
				image = cropTray(image);
				if(this.image!=null){
				this.image = cropTray(this.image);
			
				}
				return image;
//			}
//			Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY);
//			return originalImage;
			
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
	
	public Mat cropTray(Mat image){
		Mat subMat = new Mat();
		
		if (image != null ) {
		Rect boundRect = new Rect(trayFloorX,trayFloorY,trayFloorWidth,trayFloorHeight);
		
		
//		Core.rectangle(image,boundRect.br(),boundRect.tl(), new Scalar(0,255,0),3);
//		Highgui.imwrite("brect.jpg", image);
//		boundRect.height=1000;
//		boundRect.width=1600;
//		Core.rectangle(image,boundRect.br(),boundRect.tl(), new Scalar(0,255,0),3);
//		Highgui.imwrite("cubetaAntes.jpg", image);
//		System.out.println(boundRect.x +" "+image.cols()+" "+((image.cols()-boundRect.x)/2));
//		System.out.println(boundRect.y +" "+image.rows()+" "+((image.rows()-boundRect.y)/2));
//		if(boundRect.x + boundRect.width > ((image.cols()-boundRect.x)/2)){
//			boundRect.x -=270;
//		}else{
//			boundRect.x +=270;
//		}
//		if(boundRect.y + boundRect.height > ((image.rows()-boundRect.y)/2)){
//			boundRect.y +=190;
//		}else{
//			boundRect.y -=190;
//		}
//		
		subMat = ImageCropper.cropContour(image, boundRect);
//		Core.rectangle(image,boundRect.br(),boundRect.tl(), new Scalar(0,255,0),3);
		Highgui.imwrite("cubeta.jpg", image);
		}
		return subMat;
	}
	public Mat cropProduct(Mat image, MatOfPoint contour, int i){
		Mat subMat = new Mat();
		if (image != null && contour != null) {
		Rect boundRect = Imgproc.boundingRect(contour);
		boundRect.x +=0;
		boundRect.y -=15;
		boundRect.height += 30;
		boundRect.width += 30;
		subMat = ImageCropper.cropContour(image, boundRect);
		
		String filename = getClass().getResource("/FoundObjects").getPath()
				+ "/bound" + i + ".jpg";
		Highgui.imwrite(filename, subMat);
		}
		return subMat;
	}
	
	public void imagenesInforme(Mat image){
		Mat binary = image.clone();
		Mat canny = new Mat();
		Mat bilateralInforme = new Mat();
		Mat bilateralInformeDilated = new Mat();
		Imgproc.bilateralFilter(binary, bilateralInforme, 5, 230, 5, 1);
		Imgproc.Canny(bilateralInforme, canny, 35, 85);
		Imgproc.dilate(canny, bilateralInformeDilated, new Mat(),
				new Point(-1, -1), 3);
		//Highgui.imwrite("bilateralDilated.jpg",bilateralInformeDilated);
		Mat medianBlurInforme = new Mat();
		Mat medianBlurInformeDilated = new Mat();
		Imgproc.medianBlur(binary, medianBlurInforme, 7);
		Imgproc.Canny(medianBlurInforme, canny, 35, 85);
		Imgproc.dilate(canny, medianBlurInformeDilated, new Mat(),
				new Point(-1, -1), 3);
		//Highgui.imwrite("medianBlurInformeDilated.jpg",medianBlurInformeDilated);
		Mat mixed = new Mat();
		Mat mixedDilated = new Mat();
		Imgproc.bilateralFilter(medianBlurInforme, mixed, 5, 230, 5, 1);
		Imgproc.Canny(mixed, canny, 35, 85);
		Imgproc.dilate(canny, mixedDilated, new Mat(),
				new Point(-1, -1), 3);
		Highgui.imwrite("mixedInformeDilated.jpg",mixedDilated);
	}

}