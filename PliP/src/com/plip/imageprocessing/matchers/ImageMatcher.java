package com.plip.imageprocessing.matchers;

import org.opencv.core.Mat;

import com.plip.imageprocessing.matchers.exceptions.NoMatchException;
import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;


public interface ImageMatcher {

	public Product match(Mat image, Page page) throws NoMatchException;
		
}
