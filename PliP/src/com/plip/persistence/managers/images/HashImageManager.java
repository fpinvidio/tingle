package com.plip.persistence.managers.images;

import java.io.File;

import org.opencv.highgui.Highgui;

public class HashImageManager implements ImageManager {
	public static String imagesRootPath = "";
	@Override
	public String getImagesPath(String filename) {
		int hashcode = filename.hashCode();
		int mask = 255;
		int firstDir = hashcode & mask;
		int secondDir = (hashcode >> 8) & mask;
		StringBuilder path = new StringBuilder(imagesRootPath
				+ File.separator + "public/uploads/");
		path.append(String.format("%03d", firstDir));
		path.append(File.separator);
		path.append(String.format("%03d", secondDir));
		path.append(File.separator);
		path.append(filename);	
		path.append(".jpg");
		return path.toString();
	}

}
