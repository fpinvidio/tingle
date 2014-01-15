package com.plip.imageprocessing.processors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import com.plip.imageprocessing.matchers.MinDistanceMatcher;
import com.plip.persistence.dao.impls.ImageDaoImpl;
import com.plip.persistence.exceptions.ImageNotFoundException;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public class OrbBriskDetector {
	
	public List<Mat> foundImagesDescriptors = new ArrayList<Mat>();
	public List<Mat> productImagesDescriptors = new ArrayList<Mat>();
	public List<String> foundImageNames = new ArrayList<String>();
	public List<String> productImagesNames = new ArrayList<String>();
	public ImageDescriptorExtractor extractor;
	
	public OrbBriskDetector() {
		super();
		extractor =  new ImageDescriptorExtractor(FeatureDetector.ORB, DescriptorExtractor.BRISK);
	}

//	public OrbBriskDetector(int matcher) {
//		super();
//		this.matcher = DescriptorMatcher.create(matcher);
//	}
	
	
	public void recognize(Page page, ArrayList<Mat> foundObjects) {
		computeDescriptors(foundObjects,page);
		MinDistanceMatcher matcher = new MinDistanceMatcher(DescriptorMatcher.BRUTEFORCE_HAMMING);
		Product product = new Product();
		Set pageProducts = page.getPageProducts();
		Iterator pageProductsIterator = pageProducts.iterator();
		
		for (int i = 0; i < foundImagesDescriptors.size(); i++) {
			
		Product productMatch =	matcher.match(extractor, foundImagesDescriptors.get(i), page);
		if(productMatch !=null && productMatch.getName() != null){
			System.out.println (productMatch.getName() + '-' + foundImageNames.get(i));
		}
//			for (int j = 0; j < pageProducts.size(); j++) {
//				PageProduct pageProduct = pageProducts.next();
//				double dist = matcher.minDist(foundImagesDescriptors.get(i),
//						);
//				if (dist < 50) {
//					System.out.println(foundImageNames.get(i) + "-"
//							+ productImagesNames.get(j) + "- Dist:" + dist);
//				}
//			}
//
		}
	}
	
	
	public void computeDescriptors(ArrayList<Mat> foundObjects, Page page) {
//		File foundFolder = new File(getClass().getResource("/FoundObjects")
//				.getPath());
//		File descriptorsFolder = new File(getClass().getResource(
//				"/Descriptors").getPath());
//		File[] foundListOfFiles = foundFolder.listFiles();
//		File[] descriptorsListOfFiles = descriptorsFolder.listFiles();
		ImageDescriptorExtractor extractor = new ImageDescriptorExtractor(FeatureDetector.ORB,DescriptorExtractor.BRISK);
		for (int i = 0; i < foundObjects.size(); i++) {
			
				
				Mat objectImage = foundObjects.get(i);
				Mat descriptors = extractor.extractDescriptor(objectImage);
				foundImagesDescriptors.add(descriptors);
				foundImageNames.add("image-"+i);
			
		}
		Set pageProducts = page.getPageProducts();
		Iterator pageProductsIterator = pageProducts.iterator();
		ImageDaoImpl imageDao = new ImageDaoImpl();
		while(pageProductsIterator.hasNext()){
			PageProduct next = (PageProduct) pageProductsIterator.next();
			Product product  = next.getProduct();
			ArrayList<Image> productImages =  new ArrayList<Image>();
			try {
				productImages = imageDao.getImagesByProductId(product.getIdProduct());
			} catch (ImageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			product.setImages(new HashSet<Image>(productImages));
		}
	}

}
