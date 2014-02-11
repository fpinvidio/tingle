package com.plip.imageprocessing.matchers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorMatcher;

import com.plip.imageprocessing.matchers.exceptions.NoMatchException;
import com.plip.imageprocessing.processors.Exceptions.NoImageException;
import com.plip.persistence.managers.DataTypeManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public class MinDistanceMatcher implements ImageMatcher {

	private DescriptorMatcher matcher;
	public static double minDistanceThreshold = 70;
	
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

	public Product match(Mat descriptor,
			Page page) throws NoMatchException{
		Product product = null;
		Set pageProducts = page.getPageProducts();
		
		double minDist = 300;
		Iterator<PageProduct> pageProductIterator = pageProducts.iterator(); 
		while(pageProductIterator.hasNext()){
			PageProduct next = pageProductIterator.next();
			Product productToCompare = next.getProduct();
			for(int i = 0; i<Product.faces; i++){
			try {
				train(productToCompare,i+1);
				double dist = minDist(descriptor);
				if(dist < minDist && dist < minDistanceThreshold){
					minDist = dist;
					product = productToCompare;
				}
			} catch (NoImageException e) {
				break;
			}
			}
		}
		System.out.println(minDist);
		if(product == null){
			throw new NoMatchException();
		}
		return product;
	}
	
	public void train(Product product, int face) throws NoImageException{
	       if(product != null){
	    	   Set images = product.getImages();
	    	   if (images != null && images.size() > 0){
	    	   Iterator imagesIterator = images.iterator();
	    	   ArrayList<Mat> descriptors = new ArrayList<Mat>();
	    	   matcher.clear();
	    	   while(imagesIterator.hasNext()){
	    		   Image next = (Image) imagesIterator.next();
	    		   if(next.isTrained() && next.getPosition().getFace() == face){
	    		   byte[] descriptorBytes = next.getDescriptor();
	    		   Mat descriptor = DataTypeManager.convertBlobToMat(descriptorBytes);
	    		   descriptors.add(descriptor);
	    		   }
	    	   }	   
	    	   matcher.add(descriptors);
	    	   matcher.train();
	    	   }else{
	    		   throw new NoImageException();
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
			min_dist = 300;
			// matches = matchesList.get(0);
			// -- Quick calculation of max and min distances between keypoints
			DMatch[] dmatches = matches.toArray();
			if(dmatches.length>0){
			double[] distances = new double[objectDescriptor.rows()];
			for (int i = 0; i < objectDescriptor.rows(); i++) {
				
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
