package com.plip.persistence.dao.interfaces;

import com.plip.persistence.exceptions.NullModelAttributesException;
import com.plip.persistence.exceptions.PageImageNotFoundException;
import com.plip.persistence.model.PageImage;

public interface PageImageDao{
	
	public Long addPageImage(PageImage pageImage) throws NullModelAttributesException ;
	
	public PageImage getPageImage(long idPageImage) throws PageImageNotFoundException;
	
	public void updatePageImage(PageImage pageImage) throws PageImageNotFoundException;
	
	public void deletePageImage(long pageImageId);

}
