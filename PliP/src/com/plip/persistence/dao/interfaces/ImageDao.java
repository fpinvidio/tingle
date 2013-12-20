package com.plip.persistence.dao.interfaces;

import com.plip.persistence.model.Image;

public interface ImageDao {
	
	public Integer addImage(Image image) ;
	
	public Image getImage(int idImage);
	
	public void updateImage(Image image);
	
	public void deleteImage(Integer imageId);

}
