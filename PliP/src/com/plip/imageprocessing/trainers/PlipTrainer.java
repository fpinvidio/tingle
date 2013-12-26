package com.plip.imageprocessing.trainers;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.managers.FileSystemManager;
import com.plip.persistence.model.Image;

public class PlipTrainer {

	public void processProductImages(){
		File productImageFolder = new File(getClass().getResource(
				"/ProductImages").getPath());
		ImageDescriptorExtractor extractor = new ImageDescriptorExtractor(FeatureDetector.ORB, DescriptorExtractor.BRISK);
		
		File[] productImageListOfFiles = productImageFolder.listFiles();
		for (int i = 0; i < productImageListOfFiles.length; i++) {
			if (productImageListOfFiles[i].isFile()
					&& !((productImageListOfFiles[i].getName())
							.equals(".DS_Store"))) {
				String imagePath = productImageListOfFiles[i].getPath();
				Mat productImage = Highgui.imread(imagePath);
				Mat descriptors = extractor.extractDescriptor(productImage);
				String filename = FileSystemManager.stripExtension(productImageListOfFiles[i].getName());
				String[] split = filename.split("_");
				Image image = new Image();
				
			}
		}
	}
}
