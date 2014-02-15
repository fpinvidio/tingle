package com.plip.imageprocessing.matchers;

import org.opencv.core.Mat;

import com.plip.exceptions.imageprocessing.NoMatchException;
import com.plip.persistence.model.Page;
import com.plip.persistence.model.Product;


public interface ImageMatcher {

	public Product match(Mat image, Page page) throws NoMatchException;
		
}
