package com.plip.imageprocessing.processors;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.plip.persistence.managers.DataTypeManager;
import com.plip.persistence.managers.FileSystemManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Product;

public class ImageDescriptorExtractor {

		private int featureDetector = 0;
		private int descriptorExtractor = 0;
		private DescriptorExtractor extractor;
		private FeatureDetector detector;

		/*
		 * Create ImageDescriptorExtractor using a featureDetector and a
		 * descriptorExtractor
		 */

		public ImageDescriptorExtractor(int featureDetector, int descriptorExtractor) {
			super();
			this.featureDetector = featureDetector;
			this.descriptorExtractor = descriptorExtractor;
			detector = FeatureDetector.create(this.featureDetector);
			extractor = org.opencv.features2d.DescriptorExtractor
					.create(this.descriptorExtractor);
		}

		/*
		 * Extract Descriptors From all ProductImages and save them
		 * /bin/res/descriptors
		 */
		public void processProductImages() {
			File productImageFolder = new File(getClass().getResource(
					"/ProductImages").getPath());

			File[] productImageListOfFiles = productImageFolder.listFiles();
			for (int i = 0; i < productImageListOfFiles.length; i++) {
				if (productImageListOfFiles[i].isFile()
						&& !((productImageListOfFiles[i].getName())
								.equals(".DS_Store"))) {
					String imagePath = productImageListOfFiles[i].getPath();
					Mat productImage = Highgui.imread(imagePath);
					Mat descriptors = extractDescriptor(productImage);
					String filename = getClass().getResource("/Descriptors/")
							.getPath()
							+ FileSystemManager.stripExtension(productImageListOfFiles[i].getName())
							+ ".bmp";
					Highgui.imwrite(filename, descriptors);
				}
			}
		}

		/* Extract Descriptors From an Image */

		public Mat extractDescriptor(Mat image) {
			Mat descriptors = new Mat();
			if (image != null) {
				Mat greyscale_image = new Mat();
				Imgproc.cvtColor(image, greyscale_image, Imgproc.COLOR_BGR2GRAY);
				Imgproc.GaussianBlur(greyscale_image, greyscale_image, new Size(5,
						5), 2.2, 2);
				MatOfKeyPoint keypoints = new MatOfKeyPoint();
				detector.detect(greyscale_image, keypoints);
				extractor.compute(greyscale_image, keypoints, descriptors);
			}
			return descriptors;
		}
		
		public ArrayList<Mat> extractProductImageDescriptors(Product product){
			ArrayList<Mat> descriptors = new ArrayList<Mat>();
			if(product != null){
			   HashSet<Image> images = (HashSet) product.getImages();
			   Iterator<Image> imageIterator = images.iterator();
			   while(imageIterator.hasNext()){
				   Image image = imageIterator.next();
				   Mat descriptor = new Mat();
				   if(image.isTrained()){
					  descriptor = DataTypeManager.convertBlobToMat( image.getDescriptor());
				   }else{
					   
				   }
			   }
			}
			return descriptors;   
		}
	}



