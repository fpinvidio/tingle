package com.plip.persistence.managers;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public abstract class DataTypeManager {
 
	public static Mat convertBlobToMat(byte[] image){
		  Mat result = new Mat();
		  if(image.length > 0){
			  MatOfByte aux = new MatOfByte();
			  aux.fromArray(image);
			  result = Highgui.imdecode(aux, Highgui.CV_LOAD_IMAGE_ANYDEPTH);  
		  }
		  return result;
	}
	
	public static byte[] convertMatToBlob(Mat image){
		MatOfByte buffer = new MatOfByte(); 
		if(!image.empty()){ 
		   String extension = "bmp";
		   Highgui.imencode(extension, image, buffer);
		 }
		return buffer.toArray();
		 
	}
	
	
}
