package com.plip.imageprocessing.matchers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorMatcher;

import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.dao.impls.ImageDaoImpl;
import com.plip.persistence.exceptions.ImageNotFoundException;
import com.plip.persistence.managers.DataTypeManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

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

	public Product match(ImageDescriptorExtractor extractor, Mat descriptor,
			Page page) {
		Product product = new Product();
		Set pageProducts = page.getPageProducts();
		ImageDaoImpl imageDao = new ImageDaoImpl();
		double minDist = 100;
		Iterator<PageProduct> pageProductIterator =  pageProducts.iterator(); 
		while(pageProductIterator.hasNext()){
			PageProduct next = pageProductIterator.next();
			Product productToCompare = next.getProduct();
			train(productToCompare);
			double dist = minDist(descriptor);
			if(dist < minDist && dist < 50){
				minDist = dist;
				product = productToCompare;
			}
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
	
	public void train(Product product){
	       if(product != null){
	    	   Set images = product.getImages();
	    	   if (images != null){
	    	   Iterator imagesIterator = images.iterator();
	    	   ArrayList<Mat> descriptors = new ArrayList<Mat>();
	    	   matcher.clear();
	    	   while(imagesIterator.hasNext()){
	    		   Image next = (Image) imagesIterator.next();
	    		   byte[] descriptorBytes = next.getDescriptor();
	    		   Mat descriptor = DataTypeManager.convertBlobToMat(descriptorBytes);
	    		   descriptors.add(descriptor);
	    	   }
	    	   matcher.add(descriptors);
	    	   matcher.train();
	    	   }
	       }
	}
	
	public double minDist(Mat objectDescriptor){
		double min_dist = -100;
		if (objectDescriptor != null) {

			DescriptorMatcher matcher = this.matcher;
			MatOfDMatch matches = new MatOfDMatch();
			matcher.match(objectDescriptor, matches);
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
	
	public DescriptorMatcher getMatcher() {
		return matcher;
	}

	public void setMatcher(DescriptorMatcher matcher) {
		this.matcher = matcher;
	}
}
