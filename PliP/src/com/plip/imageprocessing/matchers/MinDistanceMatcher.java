package com.plip.imageprocessing.matchers;

import java.util.HashSet;
import java.util.Iterator;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorMatcher;

import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.models.Page;
import com.plip.persistence.models.PageProduct;
import com.plip.persistence.models.Product;

public class MinDistanceMatcher implements ImageMatcher {

	private DescriptorMatcher matcher;

	public MinDistanceMatcher(int descriptorMatcher) {
		super();
		this.matcher = DescriptorMatcher.create(descriptorMatcher);
	}

	public double minDist(Mat objectDescriptor, Mat productDescriptor) {

		double min_dist = -100;
		if (objectDescriptor != null && productDescriptor != null) {

			DescriptorMatcher matcher = this.matcher;
			MatOfDMatch matches = new MatOfDMatch();
			matcher.match(objectDescriptor, productDescriptor, matches);
			double max_dist = 0;
			min_dist = 100;
			// matches = matchesList.get(0);
			// -- Quick calculation of max and min distances between keypoints

			double[] distances = new double[objectDescriptor.rows()];
			for (int i = 0; i < objectDescriptor.rows(); i++) {
				DMatch[] dmatches = matches.toArray();
				double dist = dmatches[i].distance;
				distances[i] = dist;
				if (dist < min_dist) {
					min_dist = dist;
				}
				if (dist > max_dist) {
					max_dist = dist;
				}
			}
		}
		return min_dist;
	}

	public Product match(ImageDescriptorExtractor extractor, Mat image,
			Page page) {
		Product product = new Product();
		HashSet<PageProduct> pageProducts = (HashSet) page.getPageProducts();
		Mat imageDescriptors = extractor.extractDescriptor(image);
		double dist = 100;
		Iterator<PageProduct> pageProductIterator =  pageProducts.iterator(); 
		while(pageProductIterator.hasNext()){
			PageProduct next = pageProductIterator.next();
			Product productToCompare = next.getProduct();
			
		}
//		for (int i = 0; i < orderProducts.size(); i++) {
//			
//			Product productToCompare = orderProducts.get(i);
//			ArrayList<Mat> productToCompareDescriptors = productToCompare
//					.getProductImageDescriptors();
//			for (int j = 0; j < productToCompareDescriptors.size(); j++) {
//				double temp = minDist(imageDescriptors,
//						productToCompareDescriptors.get(j));
//				/*
//				 * 60 is the threshold selected to ensure that an image
//				 * corresponds to a product
//				 */
//				if (temp < dist && temp < 60) {
//					dist = temp;
//					product = productToCompare;
//				}
//			}
//
//		}
		return product;
	}
}
