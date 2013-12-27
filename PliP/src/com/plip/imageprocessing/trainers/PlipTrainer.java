package com.plip.imageprocessing.trainers;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

import com.plip.imageprocessing.processors.ImageDescriptorExtractor;
import com.plip.persistence.dao.impls.ImageDaoImpl;
import com.plip.persistence.dao.impls.PositionDaoImpl;
import com.plip.persistence.dao.impls.ProductDaoImpl;
import com.plip.persistence.dao.interfaces.ProductDao;
import com.plip.persistence.exceptions.ProductNotFoundException;
import com.plip.persistence.managers.DaoManager;
import com.plip.persistence.managers.DataTypeManager;
import com.plip.persistence.managers.FileSystemManager;
import com.plip.persistence.model.Image;
import com.plip.persistence.model.Position;
import com.plip.persistence.model.Product;

public class PlipTrainer {

	public void processProductImages(){
		File productImageFolder = new File(getClass().getResource(
				"/ProductImages").getPath());
		ImageDescriptorExtractor extractor = new ImageDescriptorExtractor(FeatureDetector.ORB, DescriptorExtractor.BRISK);
		
		File[] productImageListOfFiles = productImageFolder.listFiles();
		ProductDaoImpl pDao = new ProductDaoImpl();
		ImageDaoImpl iDao = new ImageDaoImpl();
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
				if (split.length > 1){
					pos = getCodePosition(split[1]);
				}else{
					pos = getCodePosition(null);
				}
				Product product = new Product();
				try{
				 product =  pDao.getProductByName(productName);
				}catch(ProductNotFoundException e){
				 product.setName(productName);
			     product.setEnabled(true);
				 product.setLaboratory("");
				 product.setDescription(productName);
				 pDao.addProduct(product);	
				}
				Image image = new Image();
				image.setPosition(pos);
				image.setProduct(product);
				image.setDescriptor(DataTypeManager.convertMatToBlob(descriptors));
				image.setPath(getClass().getResource(
				"/ProductImages").getPath() +"/"+ productImageListOfFiles[i].getName());
				iDao.addImage(image);
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
