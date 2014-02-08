package com.plip.persistence.dao.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.plip.persistence.exceptions.ImageNotFoundException;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.model.Image;

public interface ImageDao {
	
	public Long addImage(Image image) throws NullModelAttributesException;
	
	public Image getImage(long idImage) throws ImageNotFoundException;
	
	public void updateImage(Image image) throws ImageNotFoundException;
	
	public void deleteImage(long imageId);

	ArrayList<Image> getImagesByProductId(long idProduct) throws ImageNotFoundException;
	
	public Image getImageByProductIdAndPositio(long idProduct, long idPosition)  throws ImageNotFoundException;
	
	public List<Image> getNotTrainedImages() throws ImageNotFoundException;
}
