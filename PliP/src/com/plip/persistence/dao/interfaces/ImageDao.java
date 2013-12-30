package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.ImageNotFoundException;
import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.model.Image;

public interface ImageDao {
	
	public Long addImage(Image image) throws NullModelAttributesException;
	
	public Image getImage(long idImage) throws ImageNotFoundException;
	
	public void updateImage(Image image) throws ImageNotFoundException;
	
	public void deleteImage(long imageId);

}
