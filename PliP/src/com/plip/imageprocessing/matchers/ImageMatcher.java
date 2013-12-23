package com.plip.imageprocessing.matchers;

import org.opencv.core.Mat;

import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;


public interface ImageMatcher {

	public Product match(ImageDescriptorExtractor extractor, Mat image, Page page);
		
}
