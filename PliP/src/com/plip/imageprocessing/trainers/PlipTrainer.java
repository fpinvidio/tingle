package com.plip.imageprocessing.trainers;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.dao.impls.PositionDaoImpl;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.managers.FileSystemManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Position;

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
				String productName = "";
				Position pos = new Position();
				if (split.length > 0){
					productName = split[0]; 
				}
				if (split.length < 1){
					pos = getCodePosition(split[1]);
				}
				
				Image image = new Image();
				
			}
		}
	}
	
	public Position getCodePosition(String code){
		PositionDaoImpl positionDao = new PositionDaoImpl();
		
		Position position = positionDao.getPosition(1);;
		if (code != null){
			switch (code){
			case "U" : position = positionDao.getPosition(1);
					   break;
			case "R" : position = positionDao.getPosition(2);
					   break;	
			case "D" : position = positionDao.getPosition(3);
					   break;
			case "L" : position = positionDao.getPosition(4);
					   break;
			default : break;		   
			}
		}
		return position;
	}
}
