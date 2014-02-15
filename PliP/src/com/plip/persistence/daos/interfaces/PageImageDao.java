package com.plip.persistence.daos.interfaces;

import com.plip.exceptions.persistence.NullModelAttributesException;
import com.plip.exceptions.persistence.PageImageNotFoundException;
import com.plip.persistence.model.PageImage;

public interface PageImageDao{
	
	public Long addPageImage(PageImage pageImage) throws NullModelAttributesException ;
	
	public PageImage getPageImage(long idPageImage) throws PageImageNotFoundException;
	
	public void updatePageImage(PageImage pageImage) throws PageImageNotFoundException;
	
	public void deletePageImage(long pageImageId);

}
