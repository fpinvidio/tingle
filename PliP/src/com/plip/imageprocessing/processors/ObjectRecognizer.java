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

import com.plip.imageprocessing.matchers.ImageMatcher;
import com.plip.imageprocessing.matchers.MinDistanceMatcher;
import com.plip.imageprocessing.matchers.exceptions.NoMatchException;
import com.plip.persistence.dao.impls.ImageDaoImpl;
import com.plip.persistence.exceptions.ImageNotFoundException;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.PageProduct;
import com.plip.persistence.model.Product;

public class ObjectRecognizer {
	
	private List<Mat> foundImagesDescriptors = null; 
	private List<Mat> productImagesDescriptors = new ArrayList<Mat>();
	private List<String> foundImageNames = new ArrayList<String>();
	private List<String> productImagesNames = new ArrayList<String>();
	private ImageDescriptorExtractor extractor;
	
	private ImageMatcher matcher;
	
	public ObjectRecognizer() {
		super();
		extractor =  new ImageDescriptorExtractor(FeatureDetector.ORB, DescriptorExtractor.BRISK);
		matcher = new MinDistanceMatcher(DescriptorMatcher.BRUTEFORCE_HAMMING);
	}
	
	public ObjectRecognizer(int featureDetector, int descriptorExtractor, int descriptorMatcher) {
		super();
		extractor =  new ImageDescriptorExtractor(featureDetector, descriptorExtractor);
		matcher = new MinDistanceMatcher(descriptorMatcher);
	}

	public List<Mat> getFoundImagesDescriptors() {
		return foundImagesDescriptors;
	}

	public void setFoundImagesDescriptors(List<Mat> foundImagesDescriptors) {
		this.foundImagesDescriptors = foundImagesDescriptors;
	}

	public List<Mat> getProductImagesDescriptors() {
		return productImagesDescriptors;
	}

	public void setProductImagesDescriptors(List<Mat> productImagesDescriptors) {
		this.productImagesDescriptors = productImagesDescriptors;
	}

	public List<String> getFoundImageNames() {
		return foundImageNames;
	}

	public void setFoundImageNames(List<String> foundImageNames) {
		this.foundImageNames = foundImageNames;
	}

	public List<String> getProductImagesNames() {
		return productImagesNames;
	}

	public void setProductImagesNames(List<String> productImagesNames) {
		this.productImagesNames = productImagesNames;
	}

	public ImageDescriptorExtractor getExtractor() {
		return extractor;
	}

	public void setExtractor(ImageDescriptorExtractor extractor) {
		this.extractor = extractor;
	}

	public ImageMatcher getMatcher() {
		return matcher;
	}

	public void setMatcher(ImageMatcher matcher) {
		this.matcher = matcher;
	}
	
	/**
	 * 
	 * @param page
	 * @param foundImageDescriptor
	 * @return
	 * @throws NoMatchException
	 */
	public Product recognize(Page page , Mat foundImageDescriptor) throws NoMatchException {
		
		if(page !=null && foundImagesDescriptors.size() > 0){
		
		//computeDescriptors(foundObjects,page);
		
		//Product product = new Product();
		//Set pageProducts = page.getPageProducts();
		//Iterator pageProductsIterator = pageProducts.iterator();
		
		//for (int i = 0; i < foundImagesDescriptors.size(); i++) {
		
		Product	productMatch = new Product();
		//try{
		//productMatch =	matcher.match(extractor, foundImagesDescriptors.get(i), page);
		productMatch = matcher.match(foundImageDescriptor, page)	;
		
		//}catch(NoMatchException e){
		//	e.printStackTrace();
		//	generateNoMatchStatus();
		
		/*if(productMatch !=null && productMatch.getName() != null){
			System.out.println (productMatch.getName() + '-' + foundImageNames.get(i));
		}*/
		
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
		return productMatch;
		}
		return null;
	}
	
	/**
	 * 
	 * @param foundObjects
	 * @param page
	 * @return
	 */
	public List<Mat> computeDescriptors(ArrayList<Mat> foundObjects, Page page) {
		
		foundImagesDescriptors = new ArrayList<>(); 
//		File foundFolder = new File(getClass().getResource("/FoundObjects")
//				.getPath());
//		File descriptorsFolder = new File(getClass().getResource(
//				"/Descriptors").getPath());
//		File[] foundListOfFiles = foundFolder.listFiles();
//		File[] descriptorsListOfFiles = descriptorsFolder.listFiles();
//		ImageDescriptorExtractor extractor = new ImageDescriptorExtractor(FeatureDetector.ORB,DescriptorExtractor.BRISK);
		
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
				e.printStackTrace();
			}
			
			product.setImages(new HashSet<Image>(productImages));
		}
		return foundImagesDescriptors;
	}
	
	public void generateNoMatchStatus(){
		
	}
}
